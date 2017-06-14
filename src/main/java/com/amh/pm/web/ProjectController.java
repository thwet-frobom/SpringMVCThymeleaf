package com.amh.pm.web;

import java.util.ArrayList;
import java.util.Date;
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
import com.amh.pm.entity.User;
import com.amh.pm.service.OrganizationService;
import com.amh.pm.service.ProjectService;
import com.amh.pm.service.UserService;

@Controller
public class ProjectController {

    private OrganizationService organizationService;

    private UserService userService;

    private ProjectService projectService;

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

    @RequestMapping(value = "/organizations/{id}/projects", method = RequestMethod.GET)
    public String showProjectLists(@PathVariable("id") int id, Model model, HttpServletRequest request) {

        session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        } else {
            int userId = (Integer) session.getAttribute("userId");
            Organization organizationOwner = organizationService.findById(id);

            if (userId == organizationOwner.getOwner().getId()) {

                session = request.getSession(true);
                session.setAttribute("organizationId", id);

                List<Project> projects = projectService.findAll();
                List<Project> projectNames = new ArrayList<Project>();

                for (Project project : projects) {
                    if (project.getManager().getId() == id) {
                        projectNames.add(project);
                    }
                }

                model.addAttribute("organizationId", id);
                model.addAttribute("projectNames", projectNames);
                return "projectList";

            } else {
                List<Project> projects = projectService.findAll();
                List<Project> projectNames = new ArrayList<Project>();

                for (Project project : projects) {
                    if (project.getManager().getId() == id) {
                        projectNames.add(project);
                    }
                }
                model.addAttribute("projectNames", projectNames);
                return "showProjectsList";
            }
        }
    }

    @RequestMapping(value = "/organizations/{id}/projects/new", method = RequestMethod.GET)
    public String showProjectAddForm(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("organizationId", id);
            model.addAttribute("project", new Project());
            return "addProject";
        }
    }

    @RequestMapping(value = "/organizations/{id}/projects/new", method = RequestMethod.POST)
    public String addNewProject(@PathVariable("id") int id, @Validated @ModelAttribute Project project, BindingResult result, Model model, HttpServletRequest request) {

        if (result.hasErrors()) {
            model.addAttribute("organizationId", id);
            return "addProject";
        } else {
            if (project.getScheduleStartDate() == null || project.getScheduleEndDate() == null || project.getActualStartDate() == null || project.getActualEndDate() == null) {
                String dateEmptyError = "Please Fill All Dates";
                model.addAttribute("dateEmptyError", dateEmptyError);
                return "addProject";
            } else {
                Organization organization = organizationService.findById(id);
                project.setManager(organization);
                Date scheduleStartDate = project.getScheduleStartDate();
                Date scheduleEndDate = project.getScheduleEndDate();
                Date actualStartDate = project.getActualStartDate();
                Date actualEndDate = project.getActualEndDate();

                if (scheduleStartDate.compareTo(scheduleEndDate) > 0) {
                    String dateError = "Schedule End Date Must Be Greate Than Schedule Start Date.";
                    model.addAttribute("organizationId", id);
                    model.addAttribute("scheduleEndDateError", dateError);
                    return "addProject";
                } else if (actualStartDate.compareTo(actualEndDate) > 0) {
                    String dateError = "Actual End Date Must Be Greater Than Actual Start Date.";
                    model.addAttribute("organizationId", id);
                    model.addAttribute("actualEndDateError", dateError);
                    return "addProject";
                } else if (scheduleStartDate.compareTo(actualStartDate) > 0) {
                    String dateError = "Actual Start Date Must Be Greater Than Schedule Start Date.";
                    model.addAttribute("organizationId", id);
                    model.addAttribute("actualStartDateError", dateError);
                    return "addProject";
                } else {
                    projectService.save(project);
                    showProjectLists(id, model, request);
                    return "projectList";
                }
            }
        }
    }
}
