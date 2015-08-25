import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//OrganizationSummary object contains the summary of an organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class OrganizationSummary {
	
	//properties object
	private OrganizationSummaryProperties properties;

	//variables for JsonConstructor
	private final String JSON_PROPERTIES = "properties";
	
	//constructor for JSON parser
	public OrganizationSummary(@JsonProperty(JSON_PROPERTIES) OrganizationSummaryProperties properties){
		this.properties = properties;
		if (properties == null)
			System.out.println("Properties is null.");
	}
	
	//getters
	public String getPermalink(){
		return properties.getPermalink();
	}
	public OrganizationSummaryProperties getProperties(){
		return properties;
	}
}