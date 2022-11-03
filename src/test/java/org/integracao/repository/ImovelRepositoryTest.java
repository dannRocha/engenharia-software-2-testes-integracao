package org.integracao.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.integracao.entity.Imovel;
import org.integracao.exceptions.ResourceNotFoundException;
import org.integracao.service.ImovelService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class ImovelRepositoryTest {

  private EntityManager manager;
  private ImovelRepository imovelRepository;

  @BeforeEach
  void setup() {

    var factory = Persistence
					.createEntityManagerFactory("business_h2");

			manager = factory.createEntityManager();
			imovelRepository = new ImovelRepository(manager);
  }

  @AfterEach
  void restore() {
    manager.close();
  }

  @Test
  void deveSalvarUmNovoImovelNoBancoDeDados() {
    var imovel = new Imovel();
			imovel.setBairro("Recanto");
			imovel.setCep("00000-000");
			imovel.setEndereco("Rua Sabiá");
      imovel.setDormitorios(Short.valueOf("10"));

      var expectedID = 1;

      var imovelSalvo = imovelRepository.save(imovel);

      assertEquals(expectedID, imovelSalvo.getId());
  }

  @Test
  void deveAtualizarUmNovoImovelNoBancoDeDadosPorID() {
    var imovel = new Imovel();
    imovel.setBairro("Recanto");
    imovel.setCep("00000-000");
    imovel.setEndereco("Rua Sabiá");
    imovel.setDormitorios(Short.valueOf("10"));

    var imovelSalvo = imovelRepository.save(imovel);

    var imovelAtualizar = new Imovel();
    imovelAtualizar.setId(imovelSalvo.getId());
    imovelAtualizar.setBairro("Recanto dos Pássaros");
    imovelAtualizar.setCep("65058-777");
    imovelAtualizar.setEndereco("Rua Sabiá");
    imovelAtualizar.setDormitorios(Short.valueOf("12"));

    imovelRepository.save(imovelAtualizar);

    var imovelAtualizadoOptional = imovelRepository.findById(imovelSalvo.getId());
    
    assertTrue(imovelAtualizadoOptional.isPresent());
    var imovelAtualizado = imovelAtualizadoOptional.get();

    assertEquals(imovelSalvo.getId(), imovelAtualizado.getId());
    assertEquals("Recanto dos Pássaros", imovelAtualizado.getBairro());
    assertEquals("65058-777", imovelAtualizado.getCep());
    assertEquals(Short.valueOf("12"), imovelAtualizado.getDormitorios());
  }

  @Test
  void deveBuscarImovelNoBancoDeDadosPorID(){
    var imovel = new Imovel();
    imovel.setBairro("Recanto");
    imovel.setCep("00000-000");
    imovel.setEndereco("Rua Sabiá");
    imovel.setDormitorios(Short.valueOf("10"));

    var imovelSalvo = imovelRepository.save(imovel);

    var imovelEncontradoOptional = imovelRepository.findById(imovelSalvo.getId());

    assertTrue(imovelEncontradoOptional.isPresent());

    var imovelEncontrado = imovelEncontradoOptional.get();

    assertEquals(imovelSalvo, imovelEncontrado);
  }

  @Test
  void deveRemoverUmImovelNoBancoDeDadosPorId(){
    var imovel = new Imovel();
    imovel.setBairro("Recanto");
    imovel.setCep("00000-000");
    imovel.setEndereco("Rua Sabiá");
    imovel.setDormitorios(Short.valueOf("10"));

    var imovelSalvo = imovelRepository.save(imovel);
    var imovelRemovido = imovelRepository.delete(imovelSalvo.getId());

    assertTrue(imovelRemovido.isPresent());
    assertEquals(imovelSalvo, imovelRemovido.get());
  }
}
