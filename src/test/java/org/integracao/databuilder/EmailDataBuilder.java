package org.integracao.databuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.integracao.databuilder.utils.Reader;
import org.integracao.entity.Cliente;
import org.integracao.entity.EmailEntity;

import com.fasterxml.jackson.databind.ObjectMapper;



public class EmailDataBuilder {
  private List<EmailEntity> emails;

  private EmailDataBuilder(List<EmailEntity> emails){
    this.emails = emails;
  } 
  
  public static EmailDataBuilder umGroupoEmails(){
    return new EmailDataBuilder(readEmailsFileJSON("content/emails.json"));
  }

  public List<EmailEntity> construirGrupo() {
    return emails;
  }

  private static List<EmailEntity> readEmailsFileJSON(String filename) {
    var objectMapper = new ObjectMapper();
    try {
      var emails = objectMapper.readValue(Reader.loadFile(filename), EmailEntity[].class);

      return Arrays.asList(emails)
          .stream()
          .collect(Collectors.toList());
            
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }
}
