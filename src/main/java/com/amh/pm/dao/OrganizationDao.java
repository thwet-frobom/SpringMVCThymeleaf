package com.amh.pm.dao;

import java.util.List;

import com.amh.pm.entity.Organization;

public interface OrganizationDao {
	public void save(Organization organization);

	public void delete(Organization organization);

	public void update(Organization organization);

	public List<Organization> findAll();

	public Organization findById(int id);
	
	public  Organization findOrganizationByName(String orgName);
}
