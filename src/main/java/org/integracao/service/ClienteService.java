package org.integracao.service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.integracao.entity.Aluguel;
import org.integracao.entity.Cliente;
import org.integracao.entity.Locacao;
import org.integracao.exceptions.ResourceNotFoundException;
import org.integracao.exceptions.ValorAluguelInvalidoException;
import org.integracao.repository.ClienteRepository;
import org.integracao.repository.LocacaoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClienteService {
  private final ClienteRepository clienteRepository;
  private final LocacaoRepository locacaoRepository;

  public Cliente cadastrarCliente(Cliente cliente) {
    var clienteSalvo = clienteRepository.save(cliente);
    return clienteSalvo;
  }

  public Cliente buscarCliente(Integer clienteID) {
    var clienteSalvo = clienteRepository.findById(clienteID);

    if(clienteSalvo.isEmpty()) 
      throw new ResourceNotFoundException(String.format("cliente com ID %s nao encontrado", clienteID));
    
    return clienteSalvo.get();
  }

  public Cliente atualizarDetalhesCliente(Integer clienteID, Cliente cliente) {
    var clienteSalvo = buscarCliente(clienteID);
    cliente.setId(clienteSalvo.getId());
    cliente.setLocacoes(clienteSalvo.getLocacoes());

    clienteRepository.save(cliente);

    return cliente;
  }

  public Optional<Cliente> removerCliente(Integer clienteID) {
    return clienteRepository.delete(clienteID);
  }

  public List<Cliente> buscarTodosClientes() {
    return clienteRepository.findAll();
  }

  public List<Aluguel> buscarTodosAlugueisPagosPorCliente(Integer clienteID) {
    var cliente = buscarCliente(clienteID);

    if(Objects.isNull(cliente.getLocacoes()))
      return List.of();

    return cliente
      .getLocacoes()
      .parallelStream()
      .map(Locacao::getAlugueis)
      .flatMap(List::stream)
      .collect(Collectors.toList());
      
  }

  public List<Aluguel> buscarTodosAlugueisPagosPorClienteComAtraso(Integer clienteID) {
    var alugueis = buscarTodosAlugueisPagosPorCliente(clienteID);

    return alugueis
      .stream()
      .filter(aluguel ->aluguel.getDataPagamento().compareTo(aluguel.getDataVencimento()) > 0)
      .collect(Collectors.toList()); 
  }

  public void adicionarNovoAluguelAluguel(Integer clienteID, Integer locacaoID, Aluguel aluguel) {
    var cliente = buscarCliente(clienteID);

    var locacaoOptional = locacaoRepository.findById(locacaoID);

    if(locacaoOptional.isEmpty())
      throw new ResourceNotFoundException(String.format("locacao com ID %s nao foi encontrado para o cliente com ID %s", locacaoID, clienteID));
    
    var locacao = locacaoOptional.get();

    if(locacao.getValorAluguel().compareTo(aluguel.getValorPago()) != 0)
      throw new ValorAluguelInvalidoException("valor do aluguel a pagar diferente do contrato");

    locacao.getAlugueis().add(aluguel);
    locacaoRepository.save(locacao);

  }

  public Aluguel gerarNovoRegistroDePagamento(Integer locacaoID) {
    var locacaoOptional = locacaoRepository.findById(locacaoID);

    if(locacaoOptional.isEmpty())
      throw new ResourceNotFoundException(String.format("locacao com ID %s nao foi encontrado", locacaoID));

    var locacao = locacaoOptional.get();

    var ultimoAluguel = locacao
      .getAlugueis()
      .stream()
      .max((a, b) -> a.getDataVencimento().compareTo(b.getDataVencimento()))
      .get();


    var dataUltimoAlugel = LocalDate.parse(ultimoAluguel.getDataVencimento());
    var today = LocalDate.now();

    dataUltimoAlugel = dataUltimoAlugel.plusMonths(1);

    var aluguel = new Aluguel();
    
    aluguel.setDataPagamento(today.toString());
    aluguel.setDataVencimento(dataUltimoAlugel.toString());


    var atraso = ChronoUnit.DAYS.between(dataUltimoAlugel, today);

    var valorAPagar = locacao.getValorAluguel();
    var valorMaximoAcumuladoDaMulta = valorAPagar * 0.8D; 
    var multaPorAtrasoPorcentagem = 0.033D;

    for(int dia  = 0; dia < atraso; dia++) {
      valorAPagar += valorAPagar * multaPorAtrasoPorcentagem;

      if(valorAPagar >= valorMaximoAcumuladoDaMulta + locacao.getValorAluguel()) {
        valorAPagar = valorMaximoAcumuladoDaMulta + locacao.getValorAluguel();
        break;
      }
    }


    aluguel.setValorPago(BigDecimal
      .valueOf(valorAPagar)
      .setScale(2, RoundingMode.HALF_EVEN)
      .doubleValue());

    return aluguel;
  }
}
