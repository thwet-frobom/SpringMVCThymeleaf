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
import com.amh.pm.entity.User;
import com.amh.pm.service.OrganizationService;
import com.amh.pm.service.UserService;

@Controller
public class OrganizationController {

	private OrganizationService organizationService;

	private UserService userService;

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

	@RequestMapping(value = "/organizations", method = RequestMethod.GET)
	public String showOrganizationsLists(Model model, HttpServletRequest request) {

		session = request.getSession(false);
		if (session == null) {
			return "redirect:login";
		} else {

			int userId = (Integer) session.getAttribute("userId");

			List<Organization> organizations = this.organizationService.findAll();
			List<Organization> organizationNotOwner = new ArrayList<Organization>();
			List<Organization> organizationOwner = new ArrayList<Organization>();

			for (Organization organization : organizations) {
				if (organization.getOwner().getId() == userId) {
					organizationOwner.add(organization);
				} else {
					organizationNotOwner.add(organization);
				}
			}
			
			model.addAttribute("organizationsNotOwner", organizationNotOwner);
			model.addAttribute("organizationsOwners", organizationOwner);

			return "organizations";
		}
	}

	@RequestMapping(value = "/organization/{id}/members", method = RequestMethod.GET)
	public String addOrganizationMember(@PathVariable("id") int id, Model model, HttpServletRequest request) {

		session = request.getSession(false);
		if (session == null) {
			return "redirect:/login";
		} else {
			session = request.getSession(true);
			session.setAttribute("organizationId", id);

			List<User> userNames = userService.findUserNameByOrgnId(id);

			Organization organizationId = organizationService.findById(id);
			int userid = (Integer) session.getAttribute("userId");

			if (organizationId.getOwner().getId() == userid) {
				model.addAttribute("orgMembers", userNames);
				model.addAttribute("user", new User());
				model.addAttribute("organizationId", id);
				session.setAttribute("orgMemberName", userNames);

				return "organizationMember";
			} else {
				model.addAttribute("orgMembers", userNames);
				return "organizationMemberList";
			}
		}
	}

	@RequestMapping(value = "/organization/{id}/members/new", method = RequestMethod.POST)
	public String addOrganizationMember(@PathVariable("id") int orgid, @Validated @ModelAttribute User user,
			BindingResult result, Model model, HttpServletRequest request) {

		User u = userService.findUserIdByName(user.getName());

		Organization organization = organizationService.findById(orgid);

		session = request.getSession(true);

		if (user.getName().isEmpty()) {
			model.addAttribute("userNameEmptyError", "Please Fill User Name");
			model.addAttribute("organizationId", orgid);
			addOrganizationMember(orgid, model, request);
			return "organizationMember";
		} else {
			if (u == null || organization == null) {
				String noUserName = "User name does not exists.";

				model.addAttribute("noNameError", noUserName);
			} else {

				if (organization.getUser().contains(u)) {

					String member = "Exist Member!";

					model.addAttribute("existMember", member);
				} else {

					organization.getUser().add(u);

					organizationService.save(organization);
				}
			}

			addOrganizationMember(orgid, model, request);

			return "organizationMember";
		}
	}

	@RequestMapping(value = "/organizations/new", method = RequestMethod.GET)
	public String showOrganizationNewForm(Model model, HttpServletRequest request) {
		session = request.getSession(false);
		if (session == null) {
			return "redirect:/login";
		} else {
			model.addAttribute("organization", new Organization());
			return "addorganization";
		}
	}

	@RequestMapping(value = "/organizations/new", method = RequestMethod.POST)
	public String createOrganization(@Validated @ModelAttribute Organization organization, BindingResult result,
			Model model, HttpServletRequest request) {
		Organization orgResult = organizationService.findOrganizationByName(organization.getName());
		if (result.hasErrors()) {
			return "addorganization";
		} else {
			if (orgResult == null) {
				int userId = (Integer) session.getAttribute("userId");
				User user = userService.findById(userId);

				organization.setOwner(user);
				organizationService.save(organization);
			} else {
				String orgerror = "This organization name is already exists:";
				model.addAttribute("orgError", orgerror);
				showOrganizationsLists(model, request);
				return "addorganization";
			}

			return "redirect:/organizations";
		}

	}

}
