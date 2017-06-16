package com.amh.pm.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amh.pm.entity.Organization;
import com.amh.pm.entity.Project;
import com.amh.pm.entity.Task;
import com.amh.pm.entity.User;
import com.amh.pm.service.OrganizationService;
import com.amh.pm.service.ProjectService;
import com.amh.pm.service.TaskService;
import com.amh.pm.service.UserService;

@Controller
public class TaskController {

    private OrganizationService organizationService;

    private UserService userService;

    private ProjectService projectService;

    private TaskService taskService;

    HttpSession session;

    @Autowired(required = true)
    @Qualifier(value = "organizationService")
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectService")
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired(required = true)
    @Qualifier(value = "taskService")
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/organizations/{organizationId}/projects/{projectId}/tasks", method = RequestMethod.GET)
    public String showTasksLists(@PathVariable("organizationId") int organizationId, @PathVariable("projectId") int projectId, Model model, HttpServletRequest request) {

        session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        } else {
            int userId = (Integer) session.getAttribute("userId");
            List<Task> tasks = taskService.findAll();
            List<Task> tasksTitles = new ArrayList<Task>();

            for (Task task : tasks) {
                if (task.getProject().getId() == projectId) {
                    tasksTitles.add(task);
                }
            }

            model.addAttribute("projectId", projectId);
            model.addAttribute("organizationId", organizationId);
            model.addAttribute("taskTitles", tasksTitles);
            return "taskList";
        }
    }

    @RequestMapping(value = "/organizations/{organizationId}/projects/{projectId}/tasks/new", method = RequestMethod.GET)
    public String showAddTasks(@PathVariable("organizationId") int organizationId, @PathVariable("projectId") int projectId, Model model, HttpServletRequest request) {

        session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        } else {
            List<User> users = userService.findUserNameByOrgnId(organizationId);
            
            model.addAttribute("organizationId", organizationId);
            model.addAttribute("projectId", projectId);
            model.addAttribute("userNames",users);
            model.addAttribute("task", new Task());
            return "addTask";
        }
    }

    @RequestMapping(value = "/organizations/{organizationId}/projects/{projectId}/tasks/new", method = RequestMethod.POST)
    public String addNewTasks(@PathVariable("organizationId") int organizationId, @PathVariable("projectId") int projectId, @Validated @ModelAttribute Task task,
            BindingResult result, Model model, HttpServletRequest request) {

        if(result.hasErrors()){
            model.addAttribute("organizationId", organizationId);
            model.addAttribute("projectId", projectId);
            return "addTask";
        }else{
            session = request.getSession(true);
           // User user = userService.findById(task.getAssignee().getId());
            Project project = projectService.findById(projectId);
            task.setProject(project);
            //task.setAssignee(user);

            model.addAttribute("organizationId", organizationId);
            model.addAttribute("projectId", projectId);
            taskService.save(task);
            showTasksLists(organizationId, projectId, model, request);
            return "taskList";
        }
    }
    
    @RequestMapping(value = "/organizations/{organizationId}/projects/{projectId}/tasks/{taskId}", method = RequestMethod.GET)
    public String tasksDetails(@PathVariable("organizationId") int organizationId, @PathVariable("projectId") int projectId, @PathVariable("taskId") int taskId, Model model, HttpServletRequest request) {

        session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        } else {
            Task tasks = taskService.findById(taskId);
            
            model.addAttribute("taskDetails", tasks);
            return "taskDetails";
        } 
    }
}
