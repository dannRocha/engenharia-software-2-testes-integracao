package org.integracao.repository;

import org.integracao.entity.Cliente;

import jakarta.persistence.EntityManager;

public class ClienteRepository extends DAO<Cliente, Integer> {
  public ClienteRepository(EntityManager manager) {
    super(manager, Cliente.class);
  }
}
