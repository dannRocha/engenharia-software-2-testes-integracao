package org.integracao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.integracao.databuilder.ImovelDataBuilder;
import org.integracao.entity.Imovel;
import org.integracao.exceptions.ResourceNotFoundException;
import org.integracao.repository.ImovelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class ImovelServiceTest {

  private EntityManager manager;
  private ImovelService imovelService;

  @BeforeEach
  void setup() {

    var factory = Persistence
					.createEntityManagerFactory("business_h2");

			manager = factory.createEntityManager();
			var imovelRepository = new ImovelRepository(manager);
			imovelService = new ImovelService(imovelRepository);
			
  }

  @AfterEach
  void restore() {
    manager.close();
  }

  @Test
  void deveSalvarUmNovoImovel() {
    var imovel = new Imovel();
			imovel.setBairro("Recanto");
			imovel.setCep("00000-000");
			imovel.setEndereco("Rua Sabiá");
      imovel.setDormitorios(Short.valueOf("10"));

      var expectedID = 1;

      var imovelSalvo = imovelService.cadastrarImovel(imovel);

      assertEquals(expectedID, imovelSalvo.getId());
  }

  @Test
  void deveAtualizarUmNovoImovelPorID() {
    var imovel = new Imovel();
    imovel.setBairro("Recanto");
    imovel.setCep("00000-000");
    imovel.setEndereco("Rua Sabiá");
    imovel.setDormitorios(Short.valueOf("10"));

    var imovelSalvo = imovelService.cadastrarImovel(imovel);

    var imovelAtualizar = new Imovel();
    imovelAtualizar.setBairro("Recanto dos Pássaros");
    imovelAtualizar.setCep("65058-777");
    imovelAtualizar.setEndereco("Rua Sabiá");
    imovelAtualizar.setDormitorios(Short.valueOf("12"));

    imovelService.atualizarDetalhesImovel(imovelSalvo.getId(), imovelAtualizar);

    var imovelAtualizado = imovelService.buscarImovel(imovelSalvo.getId());
  
    assertEquals(imovelSalvo.getId(), imovelAtualizado.getId());
    assertEquals("Recanto dos Pássaros", imovelAtualizado.getBairro());
    assertEquals("65058-777", imovelAtualizado.getCep());
    assertEquals(Short.valueOf("12"), imovelAtualizado.getDormitorios());
  }

  @Test
  void deveBuscarImovelPorId(){
    var imovel = new Imovel();
    imovel.setBairro("Recanto");
    imovel.setCep("00000-000");
    imovel.setEndereco("Rua Sabiá");
    imovel.setDormitorios(Short.valueOf("10"));

    var imovelSalvo = imovelService.cadastrarImovel(imovel);

    var imovelEncontrado = imovelService.buscarImovel(imovelSalvo.getId());

    assertEquals(imovelSalvo, imovelEncontrado);
  }

  @Test
  void deveRemoverUmImovelPorId(){
    var imovel = new Imovel();
    imovel.setBairro("Recanto");
    imovel.setCep("00000-000");
    imovel.setEndereco("Rua Sabiá");
    imovel.setDormitorios(Short.valueOf("10"));

    var imovelSalvo = imovelService.cadastrarImovel(imovel);
    var imovelRemovido = imovelService.removerImovel(imovelSalvo.getId());
    

    var mensagem = assertThrows(ResourceNotFoundException.class, () -> {
      imovelService.buscarImovel(imovelSalvo.getId());
    });

    assertEquals(imovelSalvo, imovelRemovido.get());
    assertEquals(String.format("imovel com ID %s nao encontrado", imovelSalvo.getId()), mensagem.getMessage());
  }

  @Test
  public void dadoUmBairroEntaoDeveRetornaTodosOsImoveisDisponiveisParaAlugarDoTipoApartamento() {
    var imoveis = ImovelDataBuilder
      .umGrupoDeImoveis()
      .construirGrupo();

    for(var imovel : imoveis) {
      imovel.setId(null);
      imovelService.cadastrarImovel(imovel);
    }

    var tipoImovel = "Apartamento";
    var bairro = "Bairro 10";

    var expected = 2;
    var imoveisEncontrados = imovelService.buscarImovelDisponiveisPorTipoAndBairro(tipoImovel, bairro);
    
    assertEquals(expected, imoveisEncontrados.size());
    
  }

  @Test
  public void dadoDadoUmValorDeAluguelEntaoDeveEncontrarImoveisDisponiveisComBaseNoValorOuMenor() {
    var imoveis = ImovelDataBuilder
      .umGrupoDeImoveis()
      .construirGrupo();

    for(var imovel : imoveis) {
      imovel.setId(null);
      imovelService.cadastrarImovel(imovel);
    }

    var valorNaximo = 200D;

    var imoveisEncontradosInMemory = imoveis
      .stream()
      .filter(i -> i.getValorAluguel() <= valorNaximo)
      .collect(Collectors.toList());

    var imoveisEncontrados = imovelService
      .buscarImovelDisponiveisPorValorMaximo(valorNaximo);
    
    assertEquals(imoveisEncontradosInMemory.size(), imoveisEncontrados.size());
  }


  
}
