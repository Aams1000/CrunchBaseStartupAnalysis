import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//OrganizationProperties contains all important information about an Organization
@JsonIgnoreProperties (ignoreUnknown = true)
public class OrganizationSummaryProperties {
	
	//important information to show our users
	private String type;
	private String permalink; //we can use the permalink as a unique identifier! Wahoo!
	private String name;
	private String shortDescription;
	private String website;
	private String profileImageURL;
	private String facebook;
	private String twitter;
	private String linkedin;
	private String city;
	private String region;
	private String country;

	//CrunchBase data labels for JSON to locate appropriate values
	private final String JSON_PERMALINK = "permalink";
	private final String JSON_NAME = "name";
	private final String JSON_SHORT_DESCRIPTION = "short_description";
	private final String JSON_WEBSITE = "homepage_url";
	private final String JSON_PROFILE_IMAGE_URL = "profile_image_url";
	private final String JSON_FACEBOOK = "facebook_url";
	private final String JSON_TWITTER = "twitter_url";
	private final String JSON_LINKEDIN = "linkedin_url";
	private final String JSON_CITY = "city_name";
	private final String JSON_REGION = "region_name";
	private final String JSON_COUNTRY = "country_code";
	
	//constructor takes all values from JSON string
	@JsonCreator
	public OrganizationSummaryProperties(@JsonProperty(JSON_PERMALINK) String permalink, @JsonProperty(JSON_NAME) String name,
			@JsonProperty(JSON_SHORT_DESCRIPTION) String shortDescription,
			@JsonProperty(JSON_WEBSITE) String website, @JsonProperty(JSON_PROFILE_IMAGE_URL) String profileImageURL,
			@JsonProperty(JSON_FACEBOOK) String facebook, @JsonProperty(JSON_TWITTER) String twitter,
			@JsonProperty(JSON_LINKEDIN) String linkedin, @JsonProperty(JSON_CITY) String city,
			@JsonProperty(JSON_REGION) String region, @JsonProperty(JSON_COUNTRY) String country){
		this.permalink = permalink;
		this.name = name;
		this.website = website;
		this.shortDescription = shortDescription;
		this.profileImageURL = profileImageURL;
		this.facebook = facebook;
		this.twitter = twitter;
		this.linkedin = linkedin;
		this.city = city;
		this.region = region;
		this.country = country;
	}
	
	//getters
	public String getPermalink(){
		return permalink;
	}
	public String getName(){
		return name;
	}
	public String getWebsite(){
		return website;
	}
	public String getShortDescription(){
		return shortDescription;
	}
	public String getType(){
		return type;
	}
	public String getProfileImageURL(){
		return profileImageURL;
	}
	public String getFacebook(){
		return facebook;
	}
	public String getTwitter(){
		return twitter;
	}
	public String getLinkedin(){
		return linkedin;
	}
	public String getCity(){
		return city;
	}
	public String getRegion(){
		return region;
	}
	public String getCountry(){
		return country;
	}
	
	//print function prints everything!
	public void print(){

		//System.out.println("Type: " + type);
		System.out.println("Permalink: " + permalink);
		System.out.println("Name: " + name);
		System.out.println("Short description: " + shortDescription);
		System.out.println("Profile image URL: " + profileImageURL);
		System.out.println("Website: " + website);
		System.out.println("Facebook: " + facebook);
		System.out.println("Twitter: " + twitter);
		System.out.println("LinkedIn: " + linkedin);
		System.out.println("City: " + city);
		System.out.println("Region: " + region);
		System.out.println("Country: " + country);
		System.out.println();

	}
}
