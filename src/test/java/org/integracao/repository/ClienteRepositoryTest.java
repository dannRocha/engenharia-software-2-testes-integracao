package org.integracao.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.integracao.entity.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class ClienteRepositoryTest {

  private EntityManager manager;
  private ClienteRepository clienteRepository;

  @BeforeEach
  void setup() {
    var factory = Persistence
      .createEntityManagerFactory("business_h2");

    manager = factory.createEntityManager();
    clienteRepository = new ClienteRepository(manager);
  }

  @AfterEach
  void restore() {
    manager.close();
  }

  @Test
  void deveSalvarUmNovoClienteNoBancoDeDados() { 
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteRepository.save(novoCliente);

    var expectedId = 1;

    assertEquals(expectedId, clienteSalvo.getId());

  }

  @Test
  void deveAtualizarUmClienteNoBancoDeDadosPorId() {
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteRepository.save(novoCliente);

    var atualicaoCliente = new Cliente();

    atualicaoCliente.setId(clienteSalvo.getId());
    atualicaoCliente.setCpf("303.669.380-70");
    atualicaoCliente.setEmail("astrogildo@email.com");
    atualicaoCliente.setDataNascimento("1998-08-13");
    atualicaoCliente.setTelefoneOne("+55 (98) 99999-1111");
    atualicaoCliente.setTelefoneTwo("+55 (98) 99999-2222");

    var clienteAtualizado = clienteRepository.save(atualicaoCliente); 
    assertEquals(clienteSalvo.getId(), clienteAtualizado.getId());
    assertEquals(atualicaoCliente, clienteAtualizado);
  }

  @Test
  void deveBuscarClienteNoBancoDeDadosPorID(){
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteRepository.save(novoCliente);
    var clienteEncontrado = clienteRepository.findById(clienteSalvo.getId());

    assertTrue(clienteEncontrado.isPresent());
    assertEquals(clienteSalvo, clienteEncontrado.get());
  }

  @Test
  void deveRemoverUmClienteNoBancoDeDadosAPartirDoID(){
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteRepository.save(novoCliente);
    var clienteEncontrado = clienteRepository.findById(clienteSalvo.getId());

    assertTrue(clienteEncontrado.isPresent());
    assertEquals(clienteSalvo, clienteEncontrado.get());

    var clienteRemovido = clienteRepository.delete(clienteSalvo.getId());

    assertTrue(clienteRemovido.isPresent());
  }
}
