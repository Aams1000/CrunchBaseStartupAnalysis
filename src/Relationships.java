import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//This is a nice way CrunchBase organizes all groups, people, funding rounds, etc. related to an object.
//This object also provides a foundation for pulling and organizing more data
@JsonIgnoreProperties (ignoreUnknown = true)
public class Relationships {
	
	//ConcurrentHashMap of Investors
	private ConcurrentHashMap<String, Investor> investors = new ConcurrentHashMap<String, Investor>();
	
	//ArrayLists of FundingRounds, Investments, Websites, and Degrees (these will never be too big, and the API isn't giving any good unique identifiers
	//apart from the node ID in the CrunchBase system, so searching through this linearly for analysis is fine)
	private ArrayList<FundingRound> fundingRounds = new ArrayList<FundingRound>();
	private ArrayList<Investment> investments = new ArrayList<Investment>();
	private ArrayList<Website> websites = new ArrayList<Website>();
	private ArrayList<Degree> degrees = new ArrayList<Degree>();
	
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_INVESTORS = "investors";
	private final String JSON_FUNDING_ROUNDS = "funding_rounds";
	private final String JSON_INVESTMENTS = "investments";
	private final String JSON_WEBSITES = "websites";
	private final String JSON_DEGREES = "degrees";
	
	//constructor for JSON parser to use
	@JsonCreator
	public Relationships(@JsonProperty(JSON_INVESTORS) JSONWrapperInvestor investors,
			@JsonProperty(JSON_FUNDING_ROUNDS) JSONWrapperFundingRound fundingRounds,
			@JsonProperty(JSON_INVESTMENTS) JSONWrapperInvestment investments,
			@JsonProperty(JSON_WEBSITES) JSONWrapperWebsite websites,
			@JsonProperty(JSON_DEGREES) JSONWrapperDegree degrees){
		//convert list into ConcurrentHashMap for easy use
		if (investors != null){
			for (Investor investor : investors.getInvestors()){
				this.investors.put(investor.getPermalink(), investor);
			}
		}
		//access FundingRounds
		if (fundingRounds != null){
			for (FundingRound fundingRound : fundingRounds.getFundingRounds()){
				this.fundingRounds.add(fundingRound);
			}
		}
		//access Investments
		if (investments != null){
			for (Investment investment : investments.getInvestments()){
				this.investments.add(investment);
			}
		}
		//access websites
		if (websites != null){
			for (Website website : websites.getWebsites()){
				this.websites.add(website);
			}
		}
		//access degrees
		if (degrees != null){
			for (Degree degree : degrees.getDegrees()){
				this.degrees.add(degree);
			}
		}
	}
	
	//getters
	public ConcurrentHashMap<String, Investor> getInvestors(){
		return investors;
	}
	public ArrayList<FundingRound> getFundingRounds(){
		return fundingRounds;
	}
	public ArrayList<Investment> getInvestments(){
		return investments;
	}
	public ArrayList<Website> getWebsites(){
		return websites;
	}
	public ArrayList<Degree> getDegrees(){
		return degrees;
	}
}
