package com.amh.pm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.amh.pm.entity.Project;

@Repository
public class ProjectDaoImpl implements ProjectDao{

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void save(Project project) {
        // TODO Auto-generated method stub
        entityManager.merge(project);
    }

    @Override
    public void delete(Project project) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(Project project) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Project> findAll() {
        // TODO Auto-generated method stub
        return entityManager.createQuery("SELECT p FROM Project p", Project.class).getResultList();
    }

    @Override
    public Project findById(int id) {
        // TODO Auto-generated method stub
        return entityManager.find(Project.class, id);
    }

}
