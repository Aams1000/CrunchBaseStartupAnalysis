import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//when reading in the Investors and FundingRounds of an organization, they come wrapped in a bit of JSON
//this wrapper object simply contains the list of FundingRounds
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperInvestment{
	
	//list of FundingRounds
	private ArrayList<Investment> investments = new ArrayList<Investment>();
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_INVESTMENT = "items";
	
	
	//constructor for JSON parser to use
	@JsonCreator
	public JSONWrapperInvestment(@JsonProperty(JSON_INVESTMENT) Investment[] investments){
		this.investments = new ArrayList<Investment>(Arrays.asList(investments));
	}
	
	//getter for investors
	public ArrayList<Investment> getInvestments(){
		return investments;
	}
}