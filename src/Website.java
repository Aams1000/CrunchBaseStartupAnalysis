import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//Website represents...a website. The coding challenge requires little information about these,
//so for the sake of time and simplicity I'll read in only a few variables from the API. This class can be
//expanded easily to contain more information
@JsonIgnoreProperties (ignoreUnknown = true)
public class Website {
		
	//properties object
	private WebsiteProperties properties;
	
	//API title for a blog (used by other classes through getter function)
	private final String BLOG_IDENTIFIER = "blog";
	
	//variables for JsonConstructor
	private final String JSON_PROPERTIES = "properties";
	
	//constructor for JSON parser
	public Website(@JsonProperty(JSON_PROPERTIES) WebsiteProperties properties){
		this.properties = properties;
	}
	
	public WebsiteProperties getProperties(){
		return properties;
	}
	public String getBlogIdentifier(){
		return BLOG_IDENTIFIER;
	}
}
