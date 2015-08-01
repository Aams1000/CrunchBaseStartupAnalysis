import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//FundingRound object represents a funding round. The coding challenge requires little information about these,
//so for the sake of time and simplicity I'll read in only a few variables from the API. This class can be
//expanded easily to contain more information
@JsonIgnoreProperties (ignoreUnknown = true)
public class FundingRound {
		
	//properties object
	private FundingRoundProperties properties;
	
	//variables for JsonConstructor
	private final String JSON_PROPERTIES = "properties";
	
	//constructor for JSON parser
	public FundingRound(@JsonProperty(JSON_PROPERTIES) FundingRoundProperties properties){
		this.properties = properties;
	}
	
	public FundingRoundProperties getProperties(){
		return properties;
	}
}
