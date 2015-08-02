import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//OrganizationProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class InvestorProperties {
	
	//possible investor types
	private final String PERSON = "Person";
	private final String ORGANIZATION = "Organization";

	//important information to show our users
	private String type;
	private String permalink; //we can use the permalink as a unique identifier! Wahoo!
	private String name;
	private long totalFundingUSD;
	private long totalInvestingUSD;
	private long averageInvestment;
	private String website;
	private String blog;
	private String description;
	private String shortDescription;
	private String foundedDate;
	private long minEmployees;
	private long maxEmployees;
	private String stockSymbol;
	private long numInvestments;
	private String firstName;
	private String lastName;
	public long changeInFunding;
	private ArrayList<Degree> degrees;

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
	private final String JSON_STOCK_SYMBOL = "stock_symbol";
	private final String JSON_NUM_INVESTMENTS = "number_of_investments";
	private final String JSON_FIRST_NAME = "first_name";
	private final String JSON_LAST_NAME = "last_name";
	
	//constructor takes all values from JSON string
	@JsonCreator
	public InvestorProperties(@JsonProperty(JSON_PERMALINK) String permalink, @JsonProperty(JSON_NAME) String name,
			@JsonProperty(JSON_SHORT_DESCRIPTION) String shortDescription, @JsonProperty(JSON_DESCRIPTION) String description,
			@JsonProperty(JSON_FOUNDED_DATE) String foundedDate, @JsonProperty(JSON_MIN_EMPLOYEES) long minEmployees,
			@JsonProperty(JSON_MAX_EMPLOYEES) long maxEmployees, @JsonProperty(JSON_TOTAL_FUNDING_USD) long totalFundingUSD,
			@JsonProperty(JSON_WEBSITE) String website, @JsonProperty(JSON_STOCK_SYMBOL) String stockSymbol, 
			@JsonProperty(JSON_NUM_INVESTMENTS) long numInvestments, @JsonProperty(JSON_FIRST_NAME) String firstName,
			@JsonProperty(JSON_LAST_NAME) String lastName){
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
		this.numInvestments = numInvestments;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	//to be called in Investor class after setting total investments and number of investments
	public void calculateAverageInvestment(){
		if (numInvestments == 0)
			return;
		averageInvestment = totalInvestingUSD / numInvestments;
	}
	//Investor constructor passes type value through setter function
	public void setType(String type){
		this.type = type;
	}
	//same for totalInvestingUSD
	public void setTotalInvestingUSD(long totalInvestingUSD){
		this.totalInvestingUSD = totalInvestingUSD;
	}
	public void setNumInvestments(long numInvestments){
		this.numInvestments = numInvestments;
	}
	public void setBlogURL(String blog){
		this.blog = blog;
	}
	public void setDegrees(ArrayList<Degree> degrees){
		this.degrees = degrees;
	}
	//setter for change in funding after it's calculated
	public void setChangeInFunding(long changeInFunding){
		this.changeInFunding = changeInFunding;
	}
	//getters
	public String getPermalink(){
		return permalink;
	}
	public long getTotalInvestingUSD(){
		return totalInvestingUSD;
	}
	public String getName(){
		return name;
	}
	public String getWebsite(){
		return website;
	}
	public String getBlog(){
		return blog;
	}
	public String getDescription(){
		return description;
	}
	public String getShortDescription(){
		return shortDescription;
	}
	public String getFoundedDate(){
		return foundedDate;
	}
	public long getMinEmployees(){
		return minEmployees;
	}
	public long getMaxEmployees(){
		return maxEmployees;
	}
	public String getStockSymbol(){
		return stockSymbol;
	}
	public long getChangeInFunding(){
		return changeInFunding;
	}
	public long getNumInvestments(){
		return numInvestments;
	}
	public long getAverageInvestment(){
		return averageInvestment;
	}
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getType(){
		return type;
	}
	public ArrayList<Degree> getDegrees(){
		return degrees;
	}
	
	//print function prints everything!
	public void print(){
		if (type.equals(ORGANIZATION)){
			System.out.println("Type: " + type);
			System.out.println("Permalink: " + permalink);
			System.out.println("Name: " + name);
			System.out.println("Short description: " + shortDescription);
			System.out.println("Description: " + description);
			System.out.println("Founded date: " + foundedDate);
			System.out.println("Min employees: " + minEmployees);
			System.out.println("Max employees: " + maxEmployees);
			System.out.println("Number of investments: " + numInvestments);
			System.out.println("Total investments: " + totalInvestingUSD);
			System.out.println("Average investment: " + averageInvestment);
			System.out.println("Stock symbol: " + stockSymbol);
			System.out.println("Website: " + website);
			System.out.println("Blog: " + blog);
			System.out.println();
		}
		else if (type.equals(PERSON)){
			System.out.println("Type: " + type);
			System.out.println("Permalink: " + permalink);
			System.out.println("First name: " + firstName);
			System.out.println("Last name:" + lastName);
			System.out.println("Number of investments: " + numInvestments);
			System.out.println("Total investments: $" + totalInvestingUSD);
			System.out.println("Average investment: " + averageInvestment);
			System.out.println("Website: " + website);
			System.out.println("Blog: " + blog);
			for (Degree degree : degrees)
				degree.getProperties().print();
			System.out.println();
		}
	}
}
