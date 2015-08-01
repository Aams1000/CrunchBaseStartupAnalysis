import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//This is a nice way CrunchBase organizes all groups, people, funding rounds, etc. related to an object.
//For our purposes here, each Organization will have a Relationships object containing FundingRounds and
//Investors. This object also provides a foundation for pulling and organizing more data
@JsonIgnoreProperties (ignoreUnknown = true)
public class Relationships {
	
	//ConcurrentHashMap of Investors
	private ConcurrentHashMap<String, Investor> investors = new ConcurrentHashMap<String, Investor>();
	
	/*****************************ADD FUNDINGROUNDS**************************/
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_INVESTORS = "investors";
	
	//constructor for JSON parser to use
	@JsonCreator
	public Relationships(@JsonProperty(JSON_INVESTORS) JSONWrapperInvestor investors){
		//convert list into ConcurrentHashMap for easy use
		if (investors != null){
			for (Investor investor : investors.getInvestors()){
				this.investors.put(investor.getPermalink(), investor);
				
			}
			System.out.println("Number of investors: " + this.investors.size());
		}
	}
	
	//getters
	public ConcurrentHashMap<String, Investor> getInvestors(){
		return investors;
	}

}
