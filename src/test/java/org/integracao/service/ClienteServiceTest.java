package org.integracao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.integracao.databuilder.ClienteDataBuilder;
import org.integracao.databuilder.ImovelDataBuilder;
import org.integracao.entity.Aluguel;
import org.integracao.entity.Cliente;
import org.integracao.entity.Imovel;
import org.integracao.exceptions.ResourceNotFoundException;
import org.integracao.exceptions.ValorAluguelInvalidoException;
import org.integracao.repository.ClienteRepository;
import org.integracao.repository.ImovelRepository;
import org.integracao.repository.LocacaoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class ClienteServiceTest {

  private EntityManager manager;
  private ClienteService clienteService;
  private ImovelService imovelService;

  @BeforeEach
  void setup() {
    var factory = Persistence
					.createEntityManagerFactory("business_h2");

    manager = factory.createEntityManager();
    var clienteRepository = new ClienteRepository(manager);
    var imovelRepository = new ImovelRepository(manager);
    var locacaoRepository = new LocacaoRepository(manager);

    clienteService = new ClienteService(clienteRepository, locacaoRepository);
    imovelService = new ImovelService(imovelRepository);
  }

  @AfterEach
  void restore() {
    manager.close();
  }

  @Test
  void deveSalvarUmNovoCliente() { 
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteService.cadastrarCliente(novoCliente);

    var expectedId = 1;

    assertEquals(expectedId, clienteSalvo.getId());

  }

  @Test
  void deveAtualizarUmClientePorID() {
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteService.cadastrarCliente(novoCliente);

    var atualicaoCliente = new Cliente();

    atualicaoCliente.setCpf("303.669.380-70");
    atualicaoCliente.setEmail("astrogildo@email.com");
    atualicaoCliente.setDataNascimento("1998-08-13");
    atualicaoCliente.setTelefoneOne("+55 (98) 99999-1111");
    atualicaoCliente.setTelefoneTwo("+55 (98) 99999-2222");

    var clienteAtualizado = clienteService.atualizarDetalhesCliente(clienteSalvo.getId(), atualicaoCliente); 
    assertEquals(clienteSalvo.getId(), clienteAtualizado.getId());
    assertEquals(atualicaoCliente, clienteAtualizado);
  }

  @Test
  void deveBuscarClientePorId(){
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteService.cadastrarCliente(novoCliente);
    var clienteEncontrado = clienteService.buscarCliente(clienteSalvo.getId());

    assertEquals(clienteSalvo, clienteEncontrado);
  }

  @Test
  void deveRemoverUmClientePorId(){
    var novoCliente = new Cliente();
    //]https://www.4devs.com.br/gerador_de_cpf
    novoCliente.setCpf("957.682.770-13");
    novoCliente.setEmail("astrogildo@email.com");
    novoCliente.setDataNascimento("1998-08-13");
    novoCliente.setTelefoneOne("+55 (98) 99999-9999");
    novoCliente.setTelefoneTwo("+55 (98) 99999-9998");

    var clienteSalvo = clienteService.cadastrarCliente(novoCliente);
    var clienteEncontrado = clienteService.buscarCliente(clienteSalvo.getId());

    assertEquals(clienteSalvo, clienteEncontrado);

    var clienteRemovido = clienteService.removerCliente(clienteSalvo.getId());

    assertTrue(clienteRemovido.isPresent());

    var exception = assertThrows(ResourceNotFoundException.class, () -> {
      clienteService.buscarCliente(clienteSalvo.getId());
    });

    assertEquals(String.format("cliente com ID %s nao encontrado", clienteSalvo.getId()), exception.getMessage());
  }

  @Test
  void dadoUmClienteEntaoDeRecuperarTodosOsAlugueisPagos() {
    var clientes = ClienteDataBuilder
      .umGroupoClientes()
      .construirClientes();

    var imoveis = ImovelDataBuilder
      .umGrupoDeImoveis()
      .construirGrupo();

    for(var imovel : imoveis)
      imovelService.cadastrarImovel(imovel);
    
    for(var cliente : clientes) {
      clienteService.cadastrarCliente(cliente);
    }

    var cliente = clienteService.buscarCliente(11);
    var alugueisPagos = clienteService.buscarTodosAlugueisPagosPorCliente(cliente.getId());

    var expected = 2;
    assertEquals(expected, alugueisPagos.size());
  }

  @Test
  void dadoUmClienteEntaoDeRecuperarTodosOsAlugueisPagosComAtraso() {
    var clientes = ClienteDataBuilder
      .umGroupoClientes()
      .construirClientes();

    var imoveis = ImovelDataBuilder
      .umGrupoDeImoveis()
      .construirGrupo();

    for(var imovel : imoveis)
      imovelService.cadastrarImovel(imovel);
    
    for(var cliente : clientes) {
      clienteService.cadastrarCliente(cliente);
    }

    var cliente = clienteService.buscarCliente(27);
    var alugueisPagosComAtraso = clienteService.buscarTodosAlugueisPagosPorClienteComAtraso(cliente.getId());

    var expected = 1;
    assertEquals(expected, alugueisPagosComAtraso.size());
  }

  @Test
  void dadoUmNovoPagamentoDeAluguelComOValorDiferenteDoAluguelDeReferenciaEntaoDeveJogarUmExececao() {
    var clientes = ClienteDataBuilder
      .umGroupoClientes()
      .construirClientes();

    var imoveis = ImovelDataBuilder
      .umGrupoDeImoveis()
      .construirGrupo();

    for(var imovel : imoveis)
      imovelService.cadastrarImovel(imovel);
    
    for(var cliente : clientes) {
      clienteService.cadastrarCliente(cliente);
    }

    var cliente = clienteService.buscarCliente(27);
   
    var aluguel = new Aluguel();

    aluguel.setDataPagamento("2022-11-30");
    aluguel.setDataVencimento("2022-11-30");
    aluguel.setValorPago(225.00D);

    var locacao = cliente.getLocacoes().stream().findFirst().get();

    var exception = assertThrows(ValorAluguelInvalidoException.class, () -> {
      clienteService.adicionarNovoAluguelAluguel(cliente.getId(), locacao.getId(), aluguel);
    });

    var expected = "valor do aluguel a pagar diferente do contrato";
    assertEquals(expected, exception.getMessage());

    var clienteSalvo = clienteService.buscarCliente(27);

    var quantidadeDeAlugueisPagos = 2;
    assertEquals(quantidadeDeAlugueisPagos, clienteSalvo.getLocacoes().get(0).getAlugueis().size());
  }

  @Test
  void dadoUmNovoPagamentoDeAluguelEntaoDeveSalvarORegistro() {
    var clientes = ClienteDataBuilder
      .umGroupoClientes()
      .construirClientes();

    var imoveis = ImovelDataBuilder
      .umGrupoDeImoveis()
      .construirGrupo();

    for(var imovel : imoveis)
      imovelService.cadastrarImovel(imovel);
    
    for(var cliente : clientes) {
      clienteService.cadastrarCliente(cliente);
    }

    var cliente = clienteService.buscarCliente(27);
   
    var aluguel = new Aluguel();

    aluguel.setDataPagamento("2022-11-30");
    aluguel.setDataVencimento("2022-11-30");
    aluguel.setValorPago(224.00D);

    var locacao = cliente.getLocacoes().stream().findFirst().get();
    clienteService.adicionarNovoAluguelAluguel(cliente.getId(), locacao.getId(), aluguel);

    var clienteSalvo = clienteService.buscarCliente(cliente.getId());

    var quantidadeDeAlugueisPagos = 3;
    assertEquals(quantidadeDeAlugueisPagos, clienteSalvo.getLocacoes().get(0).getAlugueis().size());
  }

  @Test
  void dadoUmNovoPagamentoAtrasadoEntaoDeveRetornarOCalculoDoAluguelComAMultaAplicada() {
    var fixedDate = LocalDate.of(2022, 12, 31);

    try(var mockedStatic = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      mockedStatic.when(LocalDate::now).thenReturn(fixedDate);

      var clientes = ClienteDataBuilder
      .umGroupoClientes()
      .construirClientes();

      var imoveis = ImovelDataBuilder
        .umGrupoDeImoveis()
        .construirGrupo();

      for(var imovel : imoveis)
        imovelService.cadastrarImovel(imovel);
      
      for(var cliente : clientes) {
        clienteService.cadastrarCliente(cliente);
      }

      var cliente = clienteService.buscarCliente(27);

      var locacao = cliente.getLocacoes().stream().findFirst().get();
      var aluguel = clienteService.gerarNovoRegistroDePagamento(locacao.getId());

      var esperoAlugelComMulta = 403.20D;
      assertEquals(esperoAlugelComMulta, aluguel.getValorPago());
    }
  }
}
