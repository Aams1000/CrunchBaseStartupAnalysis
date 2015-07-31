//Investor object contains an organization or a person
//I really don't like how every possible type of company, group, foundation, etc. is represented as a single object
//but Person is its own separate object. It makes building a class like this a little difficult in Java
public class Investor {
	
	//possible types
	private final String PERSON = "Person";
	private final String ORGANIZATION = "Organization";
	
	//the group/individual investing
	private Organization organization = null;
	private Person person = null;
	
	//investor type, boolean indicators
	private String type;
	private boolean isPerson = false;
	private boolean isOrganization = false;
	
	//variables for JsonConstructor
	private final String JSON_INVESTOR_TYPE = "type";
	//constructor for organization
	public Investor(@JsonProperty(JSON_INVESTOR_TYPE) String type){
		//this.organization = organization;
		type = ORGANIZATION;
		isOrganization = true;
	}
	
	//constructor for person
	public Investor(Person person){
		this.person = person;
		type = PERSON;
		isPerson = true;
	}
}
