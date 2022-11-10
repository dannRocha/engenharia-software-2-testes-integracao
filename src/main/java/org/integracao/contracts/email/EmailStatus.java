package org.integracao.contracts.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailStatus {
  SENT("sent", 0),
  REJECT("reject", 1),
  FAILED("failed", 2);

  private String name;
  private Integer code;
}
