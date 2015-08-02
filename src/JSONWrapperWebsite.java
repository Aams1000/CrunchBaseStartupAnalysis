import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//when reading in the investors and funding rounds of an organization, they come wrapped in a bit of JSON
//this wrapper object simply contains the list of investors
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperWebsite{
	
	//list of investors
	private ArrayList<Website> websites = new ArrayList<Website>();
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_INVESTORS = "items";
	
	//constructor for JSON parser to use
	@JsonCreator
	public JSONWrapperWebsite(@JsonProperty(JSON_INVESTORS) Website[] websites){
		this.websites = new ArrayList<Website>(Arrays.asList(websites));
	}
	
	//getter for investors
	public ArrayList<Website> getWebsites(){
		return websites;
	}
}