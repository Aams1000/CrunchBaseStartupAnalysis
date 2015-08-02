import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.jna.platform.win32.Sspi.TimeStamp;

//Organization object mimics the organization object used by CrunchBase. Contains important information about
//the Organization as well as a Relationships object that contains relevant materials (i.e. funding rounds,
//news, etc.)
@JsonIgnoreProperties (ignoreUnknown = true)
public class Organization {
	
	//funding rounds and investors
//	private ArrayList<FundingRound> fundingRounds = new ArrayList<FundingRound>();
//	private ArrayList<Investor> investors = new ArrayList<Investor>();
	
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

	//getters and setters
	public OrganizationProperties getProperties(){
		return properties;
	}
	public Relationships getRelationships(){
		return relationships;
	}
}

//Data Challenge:
//Here at FindTheBest, we are always trying to help our users find and understand the data they need to make important decisions. One of the major challenges in this is figuring out how to extract and manipulate the data so that it may be digested easily by the user.
//
//For instance, imagine we have been tasked with identifying hot startups and investors. As a technical product manager at FindTheBest, we need your help to perform some basic data analysis on a subset of companies using their Crunchbase profiles. Attached is a list of 64 company ids (which can be used to query the Crunchbase API and pull down all relevant information) in which we are interested in performing our analysis over.
//
//We would like you to provide results for the following queries:
//1) Present the top 10 companies in terms of cumulative funding
//2) Present the top 10 companies in terms of change in funding from 2011 to 2012 (who is hot now?)
//3) Present the top 10 investors in terms of cumulative funding
//4) Present the top 10 investors in terms of change in funding from 2011 to 2012 (who is ramping up?)
//
//The API we will use for this one is located here -  http://developer.crunchbase.com/  (you will need to register, for free, if you go over 100k calls/ hour).
//
//A few clarifying points:
//- For 1 & 2, we are only interested in the companies from the list, so out of those 64, we want a list of the top 10 and their cumulative funding numbers.
//- For 2 & 3, we are only interested in the investors that have invested in at least one of the 64 companies listed. That is, the set of investors we want to look at is those that are linked to at least one of the companies on this list (there will likely be many more than 64).
//- We don't need a comprehensive set of data points on all entities
//for companies, we expect at least: name, website, blog, description, founded date, # of employees
//for investors, we expect at least: name, website, blog, description, # of employees (if a company), degree (if a person)
//(Note investors may be people, companies, or financial institutions)
//- The manner in which you present the results to the 4 queries is up to you. For each one we want to see at least a company name and a funding amount, but feel free to get creative with this.
//
//When responding, please zip your source, the database (mysql dump file or csv for each table), and any additional files you used for answering the 4 queries.
