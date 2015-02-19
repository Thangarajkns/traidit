package org.kns.traidit.backend.trade.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public enum Ratings {

	Delighted(1),Satisfied(2),Average(3),Unsatisfied(4),Angry(5);
	
	public Integer rate;
	// Reverse-lookup map for getting a day from an abbreviation
    private static final Map<Integer, Ratings> lookup = new HashMap<Integer, Ratings>();
    
    static {
        for (Ratings d : Ratings.values())
            lookup.put(d.getRate(), d);
    }
    
	Ratings(Integer val){
		rate = val;
	}
	
	public Integer getRate(){
		return rate;
	}
	
	public static Ratings getState(Integer rate) {
        return lookup.get(rate);
    }
	
    @Override
    public String toString(){
            String val = this.name();
            if(val.equals("Unsatisfied"))
                    val = "Not Satisfied";
            return val;
    }

}