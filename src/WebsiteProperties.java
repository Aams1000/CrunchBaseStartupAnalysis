import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//WebsiteProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class WebsiteProperties {
	
	//important information to show our users. As mentioned elsewhere,
	//this class can easily be expanded to include more information
	private String type;
	private String url;

	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_TYPE = "website";
	private final String JSON_URL = "url";
	
	//constructor takes all values from JSON string
	@JsonCreator
	public WebsiteProperties(@JsonProperty(JSON_TYPE) String type, @JsonProperty(JSON_URL) String url){
		this.type = type;
		this.url = url;
	}

	//print function prints everything!
	public void print(){
		System.out.println("Website: " + type);
		System.out.println("URL: " + url);
		System.out.println();
	}
	
	//getters
	public String getType(){
		return type;
	}
	public String getURL(){
		return url;
	}
}