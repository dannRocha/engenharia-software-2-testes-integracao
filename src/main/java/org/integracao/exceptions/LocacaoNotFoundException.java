package org.integracao.exceptions;

public class LocacaoNotFoundException extends RuntimeException {
  public LocacaoNotFoundException(String message) {
    super(message);
  }
}
