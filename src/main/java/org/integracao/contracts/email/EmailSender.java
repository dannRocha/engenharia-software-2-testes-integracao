package org.integracao.contracts.email;

public interface EmailSender {
  EmailStatus send(EmailPayload email);
}
