package org.integracao.repository;

import java.util.Optional;

import org.integracao.entity.Locacao;

import jakarta.persistence.EntityManager;

public class LocacaoRepository extends DAO<Locacao, Integer> {
  public LocacaoRepository(EntityManager manager) {
    super(manager, Locacao.class);
  }

  public Optional<Locacao> buscarLocacaoPorClienteId(Integer clienteID) {
    var jpql = "SELECT l FROM Locacao l WHERE l.cliente.id = :clienteID";
    var query = manager.createQuery(jpql, clazz);
    query.setParameter("clienteID", clienteID);

    var result = query.getResultList();

    if(result.size() == 0)
      return Optional.empty();

    return Optional.ofNullable(result.get(0));
  }
}
