import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//when reading in the degrees of an investor, they come wrapped in a bit of JSON
//this wrapper object simply contains the list of investors
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperDegree{
	
	//list of investors
	private ArrayList<Degree> degrees = new ArrayList<Degree>();
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_DEGREES = "items";
	
	//constructor for JSON parser to use
	@JsonCreator
	public JSONWrapperDegree(@JsonProperty(JSON_DEGREES) Degree[] degrees){
		this.degrees = new ArrayList<Degree>(Arrays.asList(degrees));
	}
	
	//getter for investors
	public ArrayList<Degree> getDegrees(){
		return degrees;
	}
}