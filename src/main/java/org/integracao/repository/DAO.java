package org.integracao.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.integracao.entity.BaseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class DAO<T, ID> {

    @PersistenceContext
    protected final EntityManager manager;
    protected final Class<T> clazz;

    protected DAO(EntityManager manager, Class<T> clazz) {
        this.manager = manager;
        this.clazz = clazz;
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(manager.find(clazz, id));
    }


    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        
        var criteriaBuilder = manager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
    
        criteriaQuery.from(clazz);
    
        return manager
            .createQuery(criteriaQuery)
            .getResultList();

    }

    public T save(T entity) {       
        if(!(entity instanceof BaseEntity)) {
            throw new RuntimeException();
        }
        
        var target = (BaseEntity) entity;
        
        if( Objects.isNull(target.getId()) )
            manager.persist(entity);
        else
            entity = manager.merge(entity);

        return entity;
    }

    public Optional<T> delete(ID id) {
        var entity  = findById(id);
        
        if(entity.isEmpty())
            return Optional.empty();

        manager.remove(entity.get());
        return entity;
    }
}