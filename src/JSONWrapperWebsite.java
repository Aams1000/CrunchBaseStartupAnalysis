import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//when reading in the websites related to an organization or investor, they come wrapped in a bit of JSON
//this wrapper object simply contains the list of websites
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperWebsite{
	
	//list of websites
	private ArrayList<Website> websites = new ArrayList<Website>();
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_WEBSITES = "items";
	
	//constructor for JSON parser to use
	@JsonCreator
	public JSONWrapperWebsite(@JsonProperty(JSON_WEBSITES) Website[] websites){
		this.websites = new ArrayList<Website>(Arrays.asList(websites));
	}
	
	//getter for websites
	public ArrayList<Website> getWebsites(){
		return websites;
	}
}