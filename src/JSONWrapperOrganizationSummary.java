import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//when reading in the investors and funding rounds of an organization, they come wrapped in a bit of JSON
//this wrapper object simply contains the list of investors
@JsonIgnoreProperties (ignoreUnknown = true)
public class JSONWrapperOrganizationSummary{
	
	//list of investors
	private ArrayList<OrganizationSummary> summaries = new ArrayList<OrganizationSummary>();
	
	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_ORGANIZATION_SUMMARIES = "items";
	
	//constructor for JSON parser to use
	@JsonCreator
	public JSONWrapperOrganizationSummary(@JsonProperty(JSON_ORGANIZATION_SUMMARIES) OrganizationSummary[] summaries){
		if (summaries == null)
			System.out.println("Summaries is null.");
		this.summaries = new ArrayList<OrganizationSummary>(Arrays.asList(summaries));
	}
	
	//getter for investors
	public ArrayList<OrganizationSummary> getSummaries(){
		return summaries;
	}
}