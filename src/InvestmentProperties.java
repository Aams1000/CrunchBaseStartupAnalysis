import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//InvestmentProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class InvestmentProperties {
	
	//important information to show our users. As mentioned elsewhere,
	//this class can easily be expanded to include more information
	private long moneyInvestedUSD;
	private String dateAnnounced;

	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_MONEY_INVESTED_USD = "money_invested_usd";
	
	//constructor takes all values from JSON string
	@JsonCreator
	public InvestmentProperties(@JsonProperty(JSON_MONEY_INVESTED_USD) long moneyInvestedUSD){
		this.moneyInvestedUSD = moneyInvestedUSD;
	}
	
	//getters and setters
	public long getMoneyInvestedUSD(){
		return moneyInvestedUSD;
	}
	public String getDateAnnounced(){
		return dateAnnounced;
	}
	public void setDateAnnounced(String dateAnnounced){
		this.dateAnnounced = dateAnnounced;
	}

	//print function prints everything!
	public void print(){
		System.out.println("Money invested: $" + moneyInvestedUSD);
		System.out.println("Date announced: " + dateAnnounced);
		System.out.println();
	}
}
