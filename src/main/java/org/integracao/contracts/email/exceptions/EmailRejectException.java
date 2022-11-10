package org.integracao.contracts.email.exceptions;

public class EmailRejectException extends RuntimeException {
  public EmailRejectException(String message) {
    super(message);
  }
}
