import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//OrganizationProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class FundingRoundProperties {
	
	//important information to show our users. As mentioned elsewhere,
	//this class can easily be expanded to include more information
	private String type;
	private long moneyRaisedUSD;
	private String dateAnnounced;
	private String dateClosed;

	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_TYPE = "funding_type";
	private final String JSON_MONEY_RAISED = "money_raised";
	private final String JSON_DATE_ANNOUNCED = "announced_on";
	private final String JSON_DATE_CLOSED = "closed_on";
	
	//constructor takes all values from JSON string
	@JsonCreator
	public FundingRoundProperties(@JsonProperty(JSON_TYPE) String type, @JsonProperty(JSON_MONEY_RAISED) long moneyRaisedUSD,
			@JsonProperty(JSON_DATE_ANNOUNCED) String dateAnnounced, @JsonProperty(JSON_DATE_CLOSED) String dateClosed){
		this.type = type;
		this.moneyRaisedUSD = moneyRaisedUSD;
		this.dateAnnounced = dateAnnounced;
		this.dateClosed = dateClosed;
	}
	
	//getters
	public long getMoneyRaisedUSD(){
		return moneyRaisedUSD;
	}
	public String getDateAnnounced(){
		return dateAnnounced;
	}
	public String getDateClosed(){
		return dateClosed;
	}
	//print function prints everything!
	public void print(){
		System.out.println("Funding type: " + type);
		System.out.println("Money raised: $" + moneyRaisedUSD);
		System.out.println("Date announced: " + dateAnnounced);
		System.out.println("Date closed: " + dateClosed);
		System.out.println();
	}
}
