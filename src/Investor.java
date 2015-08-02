import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	//getChangeInFunding takes two years, returns the change in funding between them
	public long getChangeInFunding(int lastYear, int currentYear){
		if (relationships == null)
			return 0;
		if (relationships.getInvestments() == null || relationships.getInvestments().isEmpty())
			return 0;
		long lastYearFunding = 0;
		long currentFunding = 0;
		long changeInFunding = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat(relationships.getInvestments().get(0).getDateFormat());
		//loop through FundingRounds for a
		for (Investment investment : relationships.getInvestments()){
			//a null date seemed to crash here...let's try to check beforehand
			if (investment.getProperties().getDateAnnounced() == null)
				continue;
			//get year
			Date date = null;
			try {
				date = (Date) dateFormat.parse(investment.getProperties().getDateAnnounced());
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
			if (date == null)
				continue;
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    int year = calendar.get(Calendar.YEAR);
		    if (year == lastYear)
		    	lastYearFunding += investment.getProperties().getMoneyInvestedUSD();
		    if (year == currentYear)
		    	currentFunding += investment.getProperties().getMoneyInvestedUSD(); 
		}
		changeInFunding = currentFunding - lastYearFunding;
		//update change in funding
		properties.setChangeInFunding(changeInFunding);
		return changeInFunding;
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