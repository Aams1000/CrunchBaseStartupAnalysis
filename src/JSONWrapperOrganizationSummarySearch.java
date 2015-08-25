import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//object to take entire JSON string from CrunchBase query for an investor (person or organization)
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperOrganizationSummarySearch {

	//objects contained in returned query
	private JSONWrapperOrganizationSummary summaryWrapper;
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_INVESTOR = "data";
	//creator for JSON parser to use
	@JsonCreator
	public JSONWrapperOrganizationSummarySearch(@JsonProperty(JSON_INVESTOR) JSONWrapperOrganizationSummary summaryWrapper){
		this.summaryWrapper = summaryWrapper;
	}
	
	//getter for JSON parser to find appropriate value in JSON string
	public JSONWrapperOrganizationSummary getSummaryWrapper(){
		return summaryWrapper;
	}
}
