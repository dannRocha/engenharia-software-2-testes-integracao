package org.integracao.contracts.email.exceptions;

public class EmailPayloadInvalidException extends RuntimeException {
  public EmailPayloadInvalidException(String message) {
    super(message);
  }
}
