package org.integracao.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

import org.integracao.contracts.email.EmailPayload;
import org.integracao.contracts.email.EmailSender;
import org.integracao.contracts.email.EmailStatus;
import org.integracao.contracts.email.exceptions.EmailRejectException;
import org.integracao.entity.EmailEntity;
import org.integracao.repository.EmailRepository;

public class EmailService {
  private EmailSender emailSender;
  private EmailRepository emailRepository;
  private static Boolean schaduleOn = false;
  private static EmailService instance;
  private Integer retryTime = 5;


  private EmailService() {}

  public static EmailService aInstance(EmailSender emailSender, EmailRepository emailRepository) {
    if(instance == null)
      instance = new EmailService(emailSender, emailRepository);

    return instance;
  }

  public static void destroy() {
    instance = null;
  }

  public static EmailService aInstance(EmailSender emailSender, EmailRepository emailRepository, Integer retryTime) {
    if(instance == null)
      instance = new EmailService(emailSender, emailRepository, retryTime);

    return instance;
  }


  private EmailService(EmailSender emailSender, EmailRepository emailRepository) {
    this.emailSender = emailSender;
    this.emailRepository = emailRepository;
  }

  private EmailService(EmailSender emailSender, EmailRepository emailRepository, Integer retryTime) {
    this.emailSender = emailSender;
    this.emailRepository = emailRepository;
    this.retryTime = retryTime;
  }


  public EmailStatus enviarEmail(String from, String to, String body) {
    var email = new EmailPayload(body, from, to);
    return emailSender.send(email);
  }

  public EmailStatus enviarEmail(EmailPayload email) {
    var status = emailSender.send(email);

    if(status.equals(EmailStatus.REJECT))
      throw new EmailRejectException("email rejeitado");

    return status;
  }

  public void enviarEmailRetry(EmailPayload email) {
    enviarEmailRetry(email.getFrom(), email.getTo(), email.getBody());
  }

  public void enviarEmailRetry(String from, String to, String body) {

    var emailSave = EmailEntity.builder()
      .from(from)
      .to(to)
      .body(body)
      .status(EmailStatus.REJECT.getName())
      .updateAt(LocalDateTime.now().toString())
      .build();

    emailRepository.save(emailSave);

    if(schaduleOn)
      return;

    Timer timer = new Timer();
    TimerTask  retentativaEnvioEmail = new TimerTask() {
      @Override
      public void run() {
        var emailRejeitados = emailRepository.buscarEmailPorStatus(EmailStatus.REJECT);

        emailRejeitados
          .stream()
          .forEach(emailRetry -> {
            try {
              var email = new EmailPayload(emailRetry.getBody(), emailRetry.getFrom(), emailRetry.getTo());
              enviarEmail(email);
              emailRetry.setStatus(EmailStatus.SENT.getName());
              emailRepository.save(emailRetry);
            }
            catch(Exception ignore) {
              try {
                var diffDays = ChronoUnit.DAYS.between( LocalDateTime.parse(emailRetry.getUpdateAt()),  LocalDateTime.now());
                if(diffDays > 5)
                  emailRetry.setStatus(EmailStatus.FAILED.getName());
                else
                  emailRetry.setStatus(EmailStatus.REJECT.getName());
    
                emailRepository.save(emailRetry);
              }
              catch(Exception e) {

                e.printStackTrace();
              }
            }
          });
      }
    }; 

    timer.schedule(retentativaEnvioEmail, retryTime);
  }

}
