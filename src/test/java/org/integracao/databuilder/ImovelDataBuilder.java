package org.integracao.databuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.integracao.databuilder.utils.Reader;
import org.integracao.entity.Imovel;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ImovelDataBuilder {
  
  private List<Imovel> imoveis;

  private ImovelDataBuilder(List<Imovel> imoveis) {
    this.imoveis = imoveis;
  }

  public static ImovelDataBuilder umGrupoDeImoveis() {
    return new ImovelDataBuilder(readImoveisFileJSON("content/imoveis.json"));
  }

  public List<Imovel> construirGrupo() {
    return imoveis;
  }

  private static List<Imovel> readImoveisFileJSON(String filename) {
    var objectMapper = new ObjectMapper();
    try {
      var imoveis = objectMapper.readValue(Reader.loadFile(filename), Imovel[].class);

      return Arrays.asList(imoveis)
          .stream()
          .collect(Collectors.toList());
            
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }
}
