package org.integracao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.integracao.contracts.email.EmailPayload;
import org.integracao.contracts.email.EmailSender;
import org.integracao.contracts.email.EmailStatus;
import org.integracao.contracts.email.exceptions.EmailPayloadInvalidException;
import org.integracao.contracts.email.exceptions.EmailRejectException;
import org.integracao.databuilder.EmailDataBuilder;
import org.integracao.repository.EmailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
  
  private EmailService emailService;

  @Mock
  private EmailRepository emailRepository;
  
  @Mock
  private EmailSender emailSender;

  @BeforeEach
  void setup() {
    emailService = EmailService.aInstance(emailSender, emailRepository, 0);
  }
  
  @AfterEach
  void restore() {
    EmailService.destroy();
  }

  @Test
  void deveJogarUmExececaoParaEmailPayloadInvalido() {
    assertThrows(EmailPayloadInvalidException.class, () -> {
      new EmailPayload("", "", "");
    });

    assertThrows(EmailPayloadInvalidException.class, () -> {
      new EmailPayload(null, null, null);
    });
  }

  @Test
  void deveEnviarUmEmail() {
    var emailPayload = new EmailPayload("cenoraroxa@email.com", "manga@email.com", "Sua Fruta ...");
    when(emailSender.send(emailPayload)).thenReturn(EmailStatus.SENT);
    var status = emailService.enviarEmail(emailPayload);

    assertEquals(EmailStatus.SENT, status);
  }

  @Test
  void deveTentarEnviarUmEmailEntaoDeveJogarUmaExcecao() {
    var emailPayload = new EmailPayload("cenoraroxa@email.com", "manga@email.com", "Sua Fruta ...");
    when(emailSender.send(emailPayload)).thenReturn(EmailStatus.REJECT);

    assertThrows(EmailRejectException.class, () -> {
      emailService.enviarEmail(emailPayload);
    });
  }

  @Test
  void deveEnviarUmEmailComRetentativa() throws InterruptedException {

    var emails = EmailDataBuilder.umGroupoEmails().construirGrupo();  

    var emailPayload = new EmailPayload("cenoraroxa@email.com", "manga@email.com", "Suco de Fruta ...");
 
    when(emailRepository.save(any())).thenReturn(any());
    when(emailRepository.buscarEmailPorStatus(EmailStatus.REJECT)).thenReturn(emails);

    emailService.enviarEmailRetry(emailPayload);

    verify(emailRepository, atLeast(1)).buscarEmailPorStatus(EmailStatus.REJECT);
    verify(emailRepository, atLeast(1)).save(any());
  }
}
