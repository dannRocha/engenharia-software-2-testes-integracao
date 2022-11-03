package org.integracao.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.integracao.entity.Imovel;
import org.integracao.exceptions.ResourceNotFoundException;
import org.integracao.repository.ImovelRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ImovelService {
  private final ImovelRepository imovelRepository;

  public Imovel cadastrarImovel(Imovel imovel) {
    return imovelRepository.save(imovel);
  }

  public Imovel buscarImovel(Integer imovelID) {
    var imovelSalvo = imovelRepository.findById(imovelID);

    if(imovelSalvo.isEmpty()) 
      throw new ResourceNotFoundException(String.format("imovel com ID %s nao encontrado", imovelID));
    
    return imovelSalvo.get();
  }

  public Imovel atualizarDetalhesImovel(Integer imovelID, Imovel imovel) {
    var imovelSalvo = buscarImovel(imovelID);
    imovel.setId(imovelSalvo.getId());
    imovel.setLocacoes(imovelSalvo.getLocacoes());

    imovelRepository.save(imovel);

    return imovel;
  }

  public Optional<Imovel> removerImovel(Integer imovelID) {
    return imovelRepository.delete(imovelID);
  }

  public List<Imovel> buscarImovelDisponiveisPorTipoAndBairro(String tipoImovel, String bairro) {
    return imovelRepository.buscarImoveisPor(tipoImovel, bairro, true);
  }

  public List<Imovel> buscarImovelDisponiveisPorValorMaximo(Double valorAluguel) {
    return imovelRepository.buscarImoveisPor(valorAluguel, true);
  }

  public List<Imovel> buscarTodosImoveis() {
    return imovelRepository.findAll();
  }

}

