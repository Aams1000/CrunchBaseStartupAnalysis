import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//Investor object contains an organization or a person
//I really don't like how in the API every possible type of company, group, foundation, etc. is represented as an Organization
//but Person is its own separate object. It makes building a class like this a little difficult in Java
@JsonIgnoreProperties (ignoreUnknown = true)
public class Investor {
	
	//possible types (didn't end up using these, but keeping them here for reference)
	private final String PERSON = "Person";
	private final String ORGANIZATION = "Organization";
	
	//investor type, boolean indicators
	private String type;
	private boolean isPerson = false;
	private boolean isOrganization = false;
	
	//properties object
	private InvestorProperties properties;
	
	//relationships object for Investments
	private Relationships relationships;
	
	//variables for JsonConstructor
	private final String JSON_INVESTOR_TYPE = "type";
	private final String JSON_PROPERTIES = "properties";
	private final String JSON_RELATIONSHIPS = "relationships";
	
	//constructor for JSON parser
	public Investor(@JsonProperty(JSON_INVESTOR_TYPE) String type, @JsonProperty(JSON_PROPERTIES) InvestorProperties properties,
			@JsonProperty(JSON_RELATIONSHIPS) Relationships relationships){
		this.type = type;
		this.properties = properties;
		this.relationships = relationships;
		//assign variables not included directly in API profile
		properties.setType(type);
		long totalInvestingUSD = findTotalInvestingUSD();
		properties.setTotalInvestingUSD(totalInvestingUSD);
		properties.calculateAverageInvestment();
		//investors do not have their blog stored directly in their API profiles
		properties.setBlogURL(findBlogURL());
		//people don't have their number of investments stored directly in the API profile
		if (type.equals(PERSON)){
			properties.setNumInvestments(findNumInvestments());
			if (relationships != null)
				properties.setDegrees(relationships.getDegrees());
		}
	}
	//findTotalInvestingUSD function sums up all Investor's investments
	private long findTotalInvestingUSD(){
		long totalInvestments = 0;
		//hmm, looks like this function might have made it crash. perhaps relationships was null at that point?
		if (relationships == null)
			return 0;
		ArrayList<Investment> investments = relationships.getInvestments();
		if(investments != null){
			for (Investment investment : investments){
				long moneyInvested = totalInvestments += investment.getProperties().getMoneyInvestedUSD();
				//if (moneyInvested != null)
					totalInvestments += moneyInvested;
			}
		}
		return totalInvestments;
	}
	//findBlogURL function loops through websites and returns blog address (if it exists)
	private String findBlogURL(){
		if (relationships == null)
			return null;
		ArrayList<Website> websites = relationships.getWebsites();
		if (websites != null){
			for (Website website : websites){
				if (website.getProperties().getType().equals(website.getBlogIdentifier()))
					return website.getProperties().getURL();
			}
		}
		return null;
	}
	//findTotalInvestingUSD function sums up all Investor's investments
		private long findNumInvestments(){
			//hmm, looks like this function might have made it crash. perhaps relationships was null at that point?
			if (relationships == null)
				return 0;
			if (relationships.getInvestments() != null)
				return relationships.getInvestments().size();
			return 0;
		}
	
	//getters
	public String getPermalink(){
		return properties.getPermalink();
	}
	
	public InvestorProperties getProperties(){
		return properties;
	}
	public String getType(){
		return type;
	}
	public Relationships getRelationships(){
		return relationships;
	}

}

//for investors, we expect at least: name, website, blog, description, # of employees (if a company), degree (if a person)
