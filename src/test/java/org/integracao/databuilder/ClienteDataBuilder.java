package org.integracao.databuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.integracao.databuilder.utils.Reader;
import org.integracao.entity.Cliente;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClienteDataBuilder {
  private List<Cliente> clientes;

  private ClienteDataBuilder(List<Cliente> clientes) {
    this.clientes = clientes;
  }

  public static ClienteDataBuilder umGroupoClientes() {
    return new ClienteDataBuilder(readClientesFileJSON("content/clientes.json"));
  }

  public List<Cliente> construirClientes() {
    return clientes;
  }

  private static List<Cliente> readClientesFileJSON(String filename) {
    var objectMapper = new ObjectMapper();
    try {
      var clientes = objectMapper.readValue(Reader.loadFile(filename), Cliente[].class);

      return Arrays.asList(clientes)
          .stream()
          .collect(Collectors.toList());
            
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }


}
