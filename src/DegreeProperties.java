import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//WebsiteProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class DegreeProperties {
	
	//important information to show our users. As mentioned elsewhere,
	//this class can easily be expanded to include more information
	private String name;
	private String subject;
	private String school;

	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_NAME = "degree_type_name";
	private final String JSON_SUBJECT = "degree_subject";
	
	//constructor takes all values from JSON string
	@JsonCreator
	public DegreeProperties(@JsonProperty(JSON_NAME) String name, @JsonProperty(JSON_SUBJECT) String subject){
		this.name = name;
		this.subject = subject;
	}

	//print function prints everything!
	public void print(){
		//System.out.println("Institution: " + school);
		System.out.println("Degree: " + name);
		System.out.println("Subject: " + subject);
		System.out.println();
	}
	
	//setter function for school
	public void setSchool(String school){
		this.school = school;
	}
}