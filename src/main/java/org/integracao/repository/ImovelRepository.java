package org.integracao.repository;

import java.util.List;

import org.integracao.entity.Imovel;

import jakarta.persistence.EntityManager;

public class ImovelRepository extends DAO<Imovel, Integer> {
  public ImovelRepository(EntityManager manager) {
    super(manager, Imovel.class);
  }

  public List<Imovel> buscarImoveisPor(String tipoImovel, String bairro, boolean disponivel) {
    var jpql = " SELECT i FROM Imovel i WHERE i.tipo = :tipo and i.bairro = :bairro";
    var  query = manager.createQuery(jpql, Imovel.class);
    query.setParameter("tipo", tipoImovel);
    query.setParameter("bairro", bairro);

    return query.getResultList();
  }

  public List<Imovel> buscarImoveisPor(Double valorAluguel, boolean disponivel) {
    var jpql = " SELECT i FROM Imovel i WHERE i.valorAluguel <= :valor";
    var  query = manager.createQuery(jpql, Imovel.class);
    query.setParameter("valor", valorAluguel);

    return query.getResultList();
  }
}
