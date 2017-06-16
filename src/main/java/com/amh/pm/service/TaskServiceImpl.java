package com.amh.pm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amh.pm.dao.TaskDao;
import com.amh.pm.entity.Task;

@Service
public class TaskServiceImpl implements TaskService{

    private TaskDao taskDao;
    
    public void setTaskDao(TaskDao taskDao){
        this.taskDao = taskDao;
    }
    
    @Override
    @Transactional
    public void save(Task task) {
        // TODO Auto-generated method stub
        taskDao.save(task);
    }

    @Override
    @Transactional
    public void delete(Task task) {
        // TODO Auto-generated method stub
        
    }

    @Override
    @Transactional
    public void update(Task task) {
        // TODO Auto-generated method stub
        
    }

    @Override
    @Transactional
    public List<Task> findAll() {
        // TODO Auto-generated method stub
        return taskDao.findAll();
    }

    @Override
    @Transactional
    public Task findById(int id) {
        // TODO Auto-generated method stub
        return taskDao.findById(id);
    }

}
