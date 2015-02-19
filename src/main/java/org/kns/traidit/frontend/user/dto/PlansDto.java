/**
 * Created by     : Soujanya

 * Created Date	  : July 24,2014
 * file Name	  : PlansDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */




package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.user.model.Plans;
import org.kns.traidit.backend.user.model.Roles;

public class PlansDto {
	
	
	private Integer planId;
	private String planName;
	private Integer subscriptionDays;
	private Double price;
	public Integer getPlanId() {
		return planId;
	}
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public Integer getSubscriptionDays() {
		return subscriptionDays;
	}
	public void setSubscriptionDays(Integer subscriptionDays) {
		this.subscriptionDays = subscriptionDays;
	}
	
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	/* For Populating Dtos from Model                        */	
	public static PlansDto populatePlansDto(Plans plan){
		PlansDto plansDto=new PlansDto();
		plansDto.setPlanId(plan.getPlanId());
		plansDto.setPlanName(plan.getPlanName());
		plansDto.setPrice(plan.getPrice());
		plansDto.setSubscriptionDays(plan.getSubscriptionDays());
		return plansDto;
	}
	
	public static ArrayList<PlansDto> populatePlansDto(ArrayList<Plans> plans){
		ArrayList<PlansDto> plansDto=new ArrayList<PlansDto>();
		for(Plans plan:plans){
			PlansDto planDto=populatePlansDto(plan);
			plansDto.add(planDto);
		}
		return plansDto;
		
		
	}


}
