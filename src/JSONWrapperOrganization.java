import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//object to take entire JSON string from CrunchBase query
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperOrganization {

	//objects contained in returned query
	private Organization organization;
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_ORGANIZATION = "data";
	//creator for JSON parser to use
	@JsonCreator
	public JSONWrapperOrganization(@JsonProperty(JSON_ORGANIZATION) Organization organization){
		this.organization = organization;
	}
	
	//getter for JSON parser to find appropriate value in JSON string
//	@JsonProperty(JSON_ORGANIZATION)
	public Organization getOrganization(){
		return organization;
	}
}
