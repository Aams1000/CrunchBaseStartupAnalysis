import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.client.fluent.Request;

//CrunchBase query sends in a call to the API. Depending on the constructor parameters,
//it will query either the organizations or people sections of the data base
public class CrunchBaseQuery implements Callable<String> {

	//web address and authorizations
	private final String CRUNCHBASE_ADDRESS = "https://api.crunchbase.com/v/3/";
	private final String API_KEY = "dbe33de6dee8cf391459ea5ee6de55a5";
	
	//formats for query fields
	private final String ORGANIZATIONS_SIGNIFIER = "organizations/";
	private final String PEOPLE_SIGNIFIER = "people/";
	private final String API_KEY_PREFIX = "&user_key=";
	private final String SEARCH_SIGNIFIER = "?";
	private final String LOCATION_SIGNIFIER = "location_uuids=";
	private final String CATEGORY_SIGNIFIER = "&category_uuids=";
	
	//query-type identifiers used as parameters
	private final String PERMALINK_SEARCH = "Permalink";
	private final String ORGANIZATION_SEARCH = "Organization";
	private final String ORGANIZATION = "Organization";
	private final String PERSON = "Person";
	
	//url to query, returned JSON string
	private String url = null;
	private String result = null;
	
	//constructor takes organization name as parameter, sends in CrunchBase query
	public CrunchBaseQuery(String type, String parameterOne, String parameterTwo){
		//check type of query
		if (type.equals(PERMALINK_SEARCH)){
			//permalink search. make sure type is valid
			String permalink = parameterOne;
			String permalinkType = parameterTwo;
			if (permalinkType.equals(ORGANIZATION) || permalinkType.equals(PERSON))
				url = formatPermalinkURL(permalink.trim(), permalinkType);
		}
		else if (type.equals(ORGANIZATION_SEARCH)){
			//organization search
			String category = parameterOne;
			String location = parameterTwo;
			url = formatOrganizationURL(category.trim(), location.trim());
		}
	}
	
	//call function returns JSON string from query. Required as part of Callable interface
	public String call(){
		queryAPI(url);
		return result;
	}
	
	//formatOrganizationURL formats url to use when making a general query for organizations
	public String formatOrganizationURL(String category, String location){
		
		StringBuffer query = new StringBuffer();
		query.append(CRUNCHBASE_ADDRESS);
		//identify type of query
		query.append(ORGANIZATIONS_SIGNIFIER);
		query.append(SEARCH_SIGNIFIER);
		query.append(LOCATION_SIGNIFIER);
		query.append(location);
		query.append(CATEGORY_SIGNIFIER);
		query.append(category);
		query.append(API_KEY_PREFIX);
		query.append(API_KEY);
		System.out.println(query.toString());
		return query.toString();
	}
	
	//format url to use in API request
	private String formatPermalinkURL(String permalink, String type){
		
		StringBuffer query = new StringBuffer();
		query.append(CRUNCHBASE_ADDRESS);
		//identify type of query
		if (type.equals(ORGANIZATION)){
			query.append(ORGANIZATIONS_SIGNIFIER);
		}
		else{
			query.append(PEOPLE_SIGNIFIER);
		}
		query.append(permalink);
		query.append(SEARCH_SIGNIFIER);
		query.append(API_KEY_PREFIX);
		query.append(API_KEY);
		System.out.println(query.toString());
		return query.toString();
	}
	
	//queryAPI function takes API url and queries the CrunchBase API
	private void queryAPI(String url){
		//request movie profile from OMDB
		try{
			result = Request.Get(url).execute().returnContent().asString();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
