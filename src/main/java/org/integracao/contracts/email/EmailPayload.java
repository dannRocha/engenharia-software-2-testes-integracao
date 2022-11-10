package org.integracao.contracts.email;

import java.util.Arrays;
import java.util.Objects;

import org.integracao.contracts.email.exceptions.EmailPayloadInvalidException;

import lombok.Data;


@Data
public class EmailPayload {
  
  
  public EmailPayload(String from, String to, String body) {
    this.from = from;
    this.to = to;
    this.body = body;

    verify();
  }

  private String from;
  private String to;
  private String body;

  private void verify() {
    if(isBlank(from, to, body))
      throw new EmailPayloadInvalidException(String.format("email '%s' invalido", this));

  }

  private boolean isBlank(String ...args) {
    return Arrays.asList(args).stream().anyMatch(arg -> Objects.isNull(arg) || arg.length() == 0);
  }  

}
