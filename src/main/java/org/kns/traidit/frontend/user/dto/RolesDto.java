/**
 * Created by     : Soujanya

 * Created Date	  : June 20,2014
 * file Name	  : RolesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */


package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.user.model.Roles;

public class RolesDto {
	
	private Integer roleId;
    private String roleName;
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	/* For Populating Dtos from Model                        */	
	public static RolesDto populateRolesDto(Roles role){
		RolesDto rolesDto=new RolesDto();
		rolesDto.setRoleId(role.getRoleId());
		rolesDto.setRoleName(role.getRoleName());
		return rolesDto;
	}
	
	public static ArrayList<RolesDto> populateRolesDto(ArrayList<Roles> roles){
		ArrayList<RolesDto> rolesDto=new ArrayList<RolesDto>();
		for(Roles role:roles){
			RolesDto roleDto=populateRolesDto(role);
			rolesDto.add(roleDto);
		}
		return rolesDto;
		
		
	}

}
