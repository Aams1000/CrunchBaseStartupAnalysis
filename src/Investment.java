import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//FundingRound object represents a funding round. The coding challenge requires little information about these,
//so for the sake of time and simplicity I'll read in only a few variables from the API. This class can be
//expanded easily to contain more information
@JsonIgnoreProperties (ignoreUnknown = true)
public class Investment {
		
	//properties object
	private InvestmentProperties properties;
	
	//relationships object (used to access investment date through it's FundingRound)
	private Relationships relationships;
	
	//variables for JsonConstructor
	private final String JSON_PROPERTIES = "properties";
	private final String JSON_RELATIONSHIPS = "relationships";
	
	//index of FundingRound we want to pull the date from (usually there is only one to choose from)
	private final int RELEVANT_FUNDING_ROUND = 0;
	
	//constructor for JSON parser
	public Investment(@JsonProperty(JSON_PROPERTIES) InvestmentProperties properties,
			@JsonProperty(JSON_RELATIONSHIPS) Relationships relationships){
		this.properties = properties;
		this.relationships = relationships;
		//pass date to InvestmentProperties
		if (relationships.getFundingRounds().isEmpty() == false){
			String dateAnnounced = relationships.getFundingRounds().get(RELEVANT_FUNDING_ROUND).getProperties().getDateAnnounced();
			properties.setDateAnnounced(dateAnnounced);
		}
	}
	
	public InvestmentProperties getProperties(){
		return properties;
	}
}
