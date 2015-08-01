import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AnalyzeStartupScene {

    //hashmap for stories companies by name. Using concurrent structure to avoid race conditions with threads
    private static ConcurrentHashMap<String, Organization> companies;
    
    //number of threads to query API
    private static final int NUM_THREADS = 5;
    
	//query-type identifiers for CrunchBaseQueries
	private static final String ORGANIZATION = "Organization";
	private static final String PERSON = "Person";
	
	//JSON parsing variables
	private static ObjectMapper mapper = new ObjectMapper();
    
    //BufferedReader for reading in file
    private static BufferedReader reader = null;
    private static String COMPANIES_FILE = "src/company_list.txt";
    
    public static void main(String[] args) {
        CrunchBaseQuery testQuery = new CrunchBaseQuery("facebook", ORGANIZATION);
        
        companies = generateCompanies(COMPANIES_FILE);
        System.out.println("Done generating companies.");
        //print out investors
        for (Entry<String, Organization> entry : companies.entrySet()){
        	Relationships relationships = entry.getValue().getRelationships();
        	System.out.println("Inside for loop.");
        	if (relationships.getInvestors() != null){
        		System.out.println("Relationships not null.");
        		for (Entry<String, Investor> investorEntry : relationships.getInvestors().entrySet()){
        			Investor investor = investorEntry.getValue();
        			investor.getProperties().print();
        		}
        	}
        }
        System.out.println("Finished printing investors.");   
    }
    
    //read in Organization names from file, constructs companies out of them.
    public static ConcurrentHashMap<String, Organization> generateCompanies(String fileName){
                    
        //hashmap to store companies
        ConcurrentHashMap<String, Organization> newCompanies = new ConcurrentHashMap<String, Organization>();
        //thread pool to make calls
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        //read in file
        try{
            reader = new BufferedReader(new FileReader(fileName));
            String line = "";
            
            //parse each line, create Organization object
            while((line = reader.readLine()) != null){
                //System.out.println(line);
                Callable<String> query = new CrunchBaseQuery(line, ORGANIZATION);
                Future<String> futureResult = threadPool.submit(query);
                //create Organization object from JSON string
                try{
                	String result = futureResult.get();
                	if (result != null){
                		JSONWrapperOrganization organizationData = mapper.readValue(result, JSONWrapperOrganization.class);
                		newCompanies.put(line, organizationData.getOrganization());
                		//organizationData.getOrganization().getProperties().print();
                	}
                }
                catch (InterruptedException | ExecutionException ex) {
                	ex.printStackTrace();
                }
            }
        }
        catch(IOException ex){
        	ex.printStackTrace();
        }
        finally{
            try{
            	reader.close();
            }
            catch(IOException ex){
            	ex.printStackTrace();
            }
        }

        return newCompanies;
    }

}

