package org.integracao.repository;

import java.util.List;

import org.integracao.contracts.email.EmailStatus;
import org.integracao.entity.EmailEntity;

import jakarta.persistence.EntityManager;

public class EmailRepository extends DAO<EmailEntity, Integer>{

  public EmailRepository(EntityManager manager) {
    super(manager, EmailEntity.class);
  }

  public List<EmailEntity> buscarEmailPorStatus(EmailStatus status) {
    var jpql = " SELECT e FROM EmailEntity e WHERE e.status = :status";
    var  query = manager.createQuery(jpql, EmailEntity.class);
    query.setParameter("status", status.getName());

    return query.getResultList(); 
  }
}
