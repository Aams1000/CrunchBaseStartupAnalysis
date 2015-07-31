import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.client.fluent.Request;

public class CrunchBaseQuery implements Callable<String> {

	//web address and authorizations
	private final String CRUNCHBASE_ADDRESS = "https://api.crunchbase.com/v/3/";
	private final String API_KEY = "dbe33de6dee8cf391459ea5ee6de55a5";
	
	//formats for query fields
	private final String ORGANIZATIONS_SEARCH = "organizations/";
	private final String API_KEY_PREFIX = "user_key=";
	private final String SEARCH_SIGNIFIER = "?";
	
	//query-type identifiers used as parameters
	private final String ORGANIZATION = "Organization";
	private final String PERSON = "Person";
	
	//url to query, returned JSON string
	private String url = null;
	private String result = null;
	
	//constructor takes organization name as parameter, sends in CrunchBase query
	public CrunchBaseQuery(String permalink, String type){
		//make sure type is valid
		if (type.equals(ORGANIZATION) || type.equals(PERSON))
			url = formatURL(permalink.trim(), type);
	}
	
	//call function returns JSON string from query. Required as part of Callable interface
	public String call(){
		queryAPI(url);
		return result;
	}
	
	//format url to use in API request
	private String formatURL(String permalink, String type){
		
		StringBuffer query = new StringBuffer();
		query.append(CRUNCHBASE_ADDRESS);
		//identify type of query
		if (type.equals(ORGANIZATION)){
			query.append(ORGANIZATIONS_SEARCH);
		}
		else{
			//ADD PERSON SEARCH
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
		
		//System.out.println(result);
	}
}
