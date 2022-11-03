package org.integracao.repository;

import org.integracao.entity.Aluguel;

import jakarta.persistence.EntityManager;

public class AluguelRepository extends DAO<Aluguel, Integer>{
  public AluguelRepository(EntityManager manager) {
    super(manager, Aluguel.class);
  }
}
