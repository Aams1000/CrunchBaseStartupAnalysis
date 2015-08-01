import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//Investor object contains an organization or a person
//I really don't like how in the API every possible type of company, group, foundation, etc. is represented as an Organization
//but Person is its own separate object. It makes building a class like this a little difficult in Java
@JsonIgnoreProperties (ignoreUnknown = true)
public class Investor {
	
	//possible types
	private final String PERSON = "Person";
	private final String ORGANIZATION = "Organization";
	
	//investor type, boolean indicators
	private String type;
	private boolean isPerson = false;
	private boolean isOrganization = false;
	
	//properties object
	private InvestorProperties properties;
	
	//variables for JsonConstructor
	private final String JSON_INVESTOR_TYPE = "type";
	private final String JSON_PROPERTIES = "properties";
	
	//constructor for JSON parser
	public Investor(@JsonProperty(JSON_INVESTOR_TYPE) String type, @JsonProperty(JSON_PROPERTIES) InvestorProperties properties){
		//this.organization = organization;
		this.type = type;
		this.properties = properties;
		properties.setType(type);
		System.out.println("Investor type: " + type.trim());
	}
	
	//getters
	public String getPermalink(){
		return properties.getPermalink();
	}
	
	public InvestorProperties getProperties(){
		return properties;
	}
	

}

//for investors, we expect at least: name, website, blog, description, # of employees (if a company), degree (if a person)
