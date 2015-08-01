import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//object to take entire JSON string from CrunchBase query
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONQueryWrapperInvestor {

	//objects contained in returned query
	private Investor investor;
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_INVESTOR = "data";
	//creator for JSON parser to use
	@JsonCreator
	public JSONQueryWrapperInvestor(@JsonProperty(JSON_INVESTOR) Investor investor){
		this.investor = investor;
	}
	
	//getter for JSON parser to find appropriate value in JSON string
	//	@JsonProperty(JSON_ORGANIZATION)
	public Investor getInvestor(){
		return investor;
	}
}
