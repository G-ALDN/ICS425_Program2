package edu.metrostate.program2.service;

import edu.metrostate.program2.model.AppUser;
import edu.metrostate.program2.model.Task;
import edu.metrostate.program2.repo.TaskRepo;
import edu.metrostate.program2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTaskService {


    @Autowired
    UserRepo userRepo;

    @Autowired
    TaskRepo taskRepo;

    public AppUser getUser(long userid) {
       return userRepo.findById(userid).orElse(new AppUser());
    }

    public AppUser getUser(String username) {
        return userRepo.findByUsername(username).orElse(new AppUser());
    }
    public void createUser(AppUser user) {
        userRepo.save(user);
    }
    public void updateUser(AppUser user) {
        userRepo.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    public Task getTask(long taskid) {
        return taskRepo.getReferenceById(taskid);
    }

    public void addTask(Task task) {
        taskRepo.save(task);
    }

    public void updateTask(Task task) {
        taskRepo.save(task);
    }

    public void deleteTask(long taskid) {
        taskRepo.delete(taskRepo.findById(taskid).orElse(new Task()));
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public List<Task> getTasksByUser(long userid) {
        return taskRepo.findAllByUserId(userid).orElse(null);
    }

    public List<Task> getTasksByCategory(String category, long userid) {
        return taskRepo.findAllByCategoryAndUserId(category, userid).orElse(null);
    }

    public List<Task> getCompletedTasks(long userid) {
        return taskRepo.findAllByCompletedAndUserId(true, userid).orElse(null);
    }

    public List<Task> getUncompletedTasks(long userid) {
        return taskRepo.findAllByCompletedAndUserId(false, userid).orElse(null);
    }

}
