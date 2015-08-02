import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//Degree represents an investor's degree. The coding challenge requires little information about these,
//so for the sake of time and simplicity I'll read in only a few variables from the API. This class can be
//expanded easily to contain more information
@JsonIgnoreProperties (ignoreUnknown = true)
public class Degree {
		
	//properties object
	private DegreeProperties properties;

	//variables for JsonConstructor
	private final String JSON_PROPERTIES = "properties";
	
	//constructor for JSON parser
	public Degree(@JsonProperty(JSON_PROPERTIES) DegreeProperties properties){
		this.properties = properties;		
	}
	
	public DegreeProperties getProperties(){
		return properties;
	}
}
