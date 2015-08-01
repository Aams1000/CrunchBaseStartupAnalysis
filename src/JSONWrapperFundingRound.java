import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//when reading in the Investors and FundingRounds of an organization, they come wrapped in a bit of JSON
//this wrapper object simply contains the list of FundingRounds
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperFundingRound{
	
	//list of FundingRounds
	private ArrayList<FundingRound> fundingRounds = new ArrayList<FundingRound>();
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_FUNDING_ROUND = "items";
	
	//constructor for JSON parser to use
	@JsonCreator
	public JSONWrapperFundingRound(@JsonProperty(JSON_FUNDING_ROUND) FundingRound[] fundingRounds){
		this.fundingRounds = new ArrayList<FundingRound>(Arrays.asList(fundingRounds));
	}
	
	//getter for investors
	public ArrayList<FundingRound> getFundingRounds(){
		return fundingRounds;
	}
}