import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//Organization object mimics the organization object used by CrunchBase. Contains important information about
//the Organization as well as a Relationships object that contains relevant materials (i.e. funding rounds,
//news, etc.)
@JsonIgnoreProperties (ignoreUnknown = true)
public class Organization {
		
	//Relationships object contains all relevant Investors, FundingRounds, news, etc.
	private Relationships relationships;
	
	//properties object
	private OrganizationProperties properties;
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_PROPERTIES = "properties";
	private final String JSON_RELATIONSHIPS = "relationships";
	
	//constructor for JSON parser
	@JsonCreator
	public Organization(@JsonProperty(JSON_PROPERTIES) OrganizationProperties properties, @JsonProperty(JSON_RELATIONSHIPS) Relationships relationships){
		this.properties = properties;
		this.relationships = relationships;
		//find blog address if it exists
		properties.setBlogURL(findBlogURL());
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
	
	//getChangeInFunding takes two years, returns the change in funding between them
	public long getChangeInFunding(int lastYear, int currentYear){
		if (relationships == null)
			return 0;
		if (relationships.getFundingRounds() == null || relationships.getFundingRounds().isEmpty())
			return 0;
		long lastYearFunding = 0;
		long currentFunding = 0;
		long changeInFunding = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat(relationships.getFundingRounds().get(0).getDateFormat());
		//loop through FundingRounds for a
		for (FundingRound fundingRound : relationships.getFundingRounds()){
			//a null date seemed to crash here...let's try to check beforehand
			if (fundingRound.getProperties().getDateAnnounced() == null)
				continue;
			//get year
			Date date = null;
			try {
				date = (Date) dateFormat.parse(fundingRound.getProperties().getDateAnnounced());
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
			if (date == null)
				continue;
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    int year = calendar.get(Calendar.YEAR);
		    if (year == lastYear)
		    	lastYearFunding += fundingRound.getProperties().getMoneyRaisedUSD();
		    if (year == currentYear)
		    	currentFunding += fundingRound.getProperties().getMoneyRaisedUSD(); 
		}
		changeInFunding = currentFunding - lastYearFunding;
		//update properties
		properties.setChangeInFunding(changeInFunding);
		return changeInFunding;
	}

	//getters and setters
	public OrganizationProperties getProperties(){
		return properties;
	}
	public Relationships getRelationships(){
		return relationships;
	}
}