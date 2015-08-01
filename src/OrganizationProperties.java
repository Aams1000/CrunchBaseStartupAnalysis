import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.jna.platform.win32.Sspi.TimeStamp;

//OrganizationProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class OrganizationProperties {

	//important information to show our users
	private String permalink; //we can use the permalink as a unique identifier! Wahoo!
	private String name;
	private long totalFundingUSD;
	private String website;
	private URL blog;
	private String description;
	private String shortDescription;
	private String foundedDate;
	private long minEmployees;
	private long maxEmployees;
	private String stockSymbol;
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_PERMALINK = "permalink";
	private final String JSON_NAME = "name";
	private final String JSON_SHORT_DESCRIPTION = "short_description";
	private final String JSON_DESCRIPTION = "description";
	private final String JSON_FOUNDED_DATE = "founded_on";
	private final String JSON_MIN_EMPLOYEES = "num_employees_min";
	private final String JSON_MAX_EMPLOYEES = "num_employees_max";
	private final String JSON_TOTAL_FUNDING_USD = "total_funding_usd";
	private final String JSON_WEBSITE = "homepage_url";
	//private final String JSON_BLOG = "";
	private final String JSON_STOCK_SYMBOL = "stock_symbol";
	
	
	/*************************ADD BLOG******************************/
	
	//constructor takes all values from JSON string
	@JsonCreator
	public OrganizationProperties(@JsonProperty(JSON_PERMALINK) String permalink, @JsonProperty(JSON_NAME) String name,
			@JsonProperty(JSON_SHORT_DESCRIPTION) String shortDescription, @JsonProperty(JSON_DESCRIPTION) String description,
			@JsonProperty(JSON_FOUNDED_DATE) String foundedDate, @JsonProperty(JSON_MIN_EMPLOYEES) long minEmployees,
			@JsonProperty(JSON_MAX_EMPLOYEES) long maxEmployees, @JsonProperty(JSON_TOTAL_FUNDING_USD) long totalFundingUSD,
			@JsonProperty(JSON_WEBSITE) String website, @JsonProperty(JSON_STOCK_SYMBOL) String stockSymbol){
		this.permalink = permalink;
		this.name = name;
		this.shortDescription = shortDescription;
		this.description = description;
		this.foundedDate = foundedDate;
		this.minEmployees = minEmployees;
		this.maxEmployees = maxEmployees;
		this.totalFundingUSD = totalFundingUSD;
		this.stockSymbol = stockSymbol;
		this.website = website;

	}
	
	//getters
	public long getTotalFundingUSD(){
		return totalFundingUSD;
	}
	
	//print function prints everything!
	public void print(){
		System.out.println("Permalink: " + permalink);
		System.out.println("Name: " + name);
		System.out.println("Short description: " + shortDescription);
		System.out.println("Description: " + description);
		System.out.println("Founded date: " + foundedDate);
		System.out.println("Min employees: " + minEmployees);
		System.out.println("Max employees: " + maxEmployees);
		System.out.println("Total funding (USD): " + totalFundingUSD);
		System.out.println("Stock symbol: " + stockSymbol);
		System.out.println("Website: " + website);
		System.out.println();
	}
	
	
}
