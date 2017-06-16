package com.amh.pm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.amh.pm.entity.Organization;
import com.amh.pm.entity.Task;

@Repository
public class TaskDaoImpl implements TaskDao{

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void save(Task task) {
        // TODO Auto-generated method stub
        entityManager.merge(task);
    }

    @Override
    public void delete(Task task) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(Task task) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Task> findAll() {
        // TODO Auto-generated method stub
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    @Override
    public Task findById(int id) {
        // TODO Auto-generated method stub
        return entityManager.find(Task.class, id);
    }

}
