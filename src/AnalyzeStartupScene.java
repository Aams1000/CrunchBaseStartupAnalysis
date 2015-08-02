import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AnalyzeStartupScene {

    //hashmaps for storing companies and investors by name. Using concurrent structure to avoid race conditions with threads
    private static ConcurrentHashMap<String, Organization> companies;
    private static ConcurrentHashMap<String, Investor> investors;
    
    //number of companies/investors we want to include in our "top ten" lists
    private static final int NUM_RESULTS = 10;
    
    //number of threads to query API, delay to avoid sending in too many requests concurrently
    private static final int NUM_THREADS = 3;
    private static final int API_CALL_DELAY = 1000;
    
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
//        for (Entry<String, Organization> entry : companies.entrySet()){
//        	Relationships relationships = entry.getValue().getRelationships();
        	//System.out.println("Inside for loop.");
//        	if (relationships.getInvestors() != null){
//        		System.out.println("Relationships not null.");
//        		for (Entry<String, Investor> investorEntry : relationships.getInvestors().entrySet()){
//        			Investor investor = investorEntry.getValue();
//        			investor.getProperties().print();
//        		}
//        	}
//        	//print out funding rounds
//          	if (relationships.getFundingRounds() != null){
//        		for (FundingRound fundingRound : relationships.getFundingRounds()){
//        			fundingRound.getProperties().print();
//        		}
//        	}
//          	if (relationships.getInvestments() != null){
//          		for (Investment investment : relationships.getInvestments()){
//          			investment.getProperties().print();
//          		}
//          	}
//        }
//        try {
//			TimeUnit.SECONDS.sleep(20);
//		} catch (InterruptedException ex) {
//			ex.printStackTrace();
//		}
        
        investors = updateInvestorInformation(companies);
        System.out.println("Finished printing investors.");
        System.out.println("Number of investors: " + investors.size());
        
        getCompaniesByTotalFunding(companies, NUM_RESULTS);
        getInvestorsByTotalFunding(investors, NUM_RESULTS);
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
                //let's not make too many requests in a short window of time
                try {
					Thread.sleep(API_CALL_DELAY);
				} 
                catch (InterruptedException ex) {
					ex.printStackTrace();
				}
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
            //shutdown ExecutorService
            threadPool.shutdown();
            try {
            	threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException ex) {
              ex.printStackTrace();
            }
        }

        return newCompanies;
    }

    //updateInvestorInformation updates Investors from our inputted companies with additional API calls
    public static ConcurrentHashMap<String, Investor> updateInvestorInformation(ConcurrentHashMap<String, Organization> companies){
    	
    	int repeatInvestors = 0;
    	//create investors HashMap
    	ConcurrentHashMap<String, Investor> updatedInvestors = new ConcurrentHashMap<String, Investor>();
    	
    	//mapper.readerForUpdating(object).readValue(json);
    	//thread pool to make calls
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        //loop through every company's list of investors, updating as we go
        for (Entry<String, Organization> company : companies.entrySet()){
        		for (Entry<String, Investor> entry : company.getValue().getRelationships().getInvestors().entrySet()){
        			Investor currInvestor = entry.getValue();
        			//if we've already updated this Investor, no need to do it again
        			if (updatedInvestors.get(currInvestor.getPermalink()) != null){
        				repeatInvestors++;
        				continue;
        			}
        			Callable<String> query = new CrunchBaseQuery(entry.getKey(), currInvestor.getType());
        			Future<String> futureResult = threadPool.submit(query);
        			//let's pause so as not to overwhelm the API with calls
        			try {
    					Thread.sleep(API_CALL_DELAY);
    				} 
                    catch (InterruptedException ex) {
    					ex.printStackTrace();
    				}
        			//update currInvestor with JSON string
                    try{
                    	String result = futureResult.get();
                    	if (result != null){
                    		JSONQueryWrapperInvestor investorData = mapper.readValue(result, JSONQueryWrapperInvestor.class);
                    		currInvestor = investorData.getInvestor();
                    		//add to investors hashmap
                    		updatedInvestors.put(currInvestor.getPermalink(), currInvestor);
                    		
                    		
                    		currInvestor.getProperties().print();
                    		
                    	}
                    }
                    catch (InterruptedException | ExecutionException | IOException ex) {
                    	ex.printStackTrace();
                    }	
        		}
        	}
	        //shutdown ExecutorService
	        threadPool.shutdown();
	        try {
	        	threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	        }
	        catch (InterruptedException ex) {
	          ex.printStackTrace();
	        }
    	return updatedInvestors;
    }
    
    //getCompaniesByTotalFunding function sorts companies by total funding
    public static Organization[] getCompaniesByTotalFunding(ConcurrentHashMap<String, Organization> companies, int numCompanies){
    	//convert ConcurrentHashMap to array, sort
    	Organization[] companiesArray = companies.values().toArray(new Organization[companies.size()]);
    	Arrays.sort(companiesArray, new TotalFundingComparator());
    	Organization[] topCompanies = new Organization[numCompanies];
    	for (int i = 0; i < numCompanies; i++){
    		topCompanies[i] = companiesArray[i];
    		topCompanies[i].getProperties().print();
    	}
    	return topCompanies;
    }
    
    //getCompaniesByRecentFunding function sorts companies by largest uptick in recent funding
    
    //getInvestorsByTotalFunding function sorts investors by total funding
    public static Investor[] getInvestorsByTotalFunding(ConcurrentHashMap<String, Investor> investors, int numInvestors){
    	//convert ConcurrentHashMap to array, sort
    	Investor[] investorsArray = investors.values().toArray(new Investor[investors.size()]);
    	Arrays.sort(investorsArray, new TotalInvestingComparator());
    	Investor[] topInvestors = new Investor[numInvestors];
    	for (int i = 0; i < numInvestors; i++){
    		topInvestors[i] = investorsArray[i];
    		topInvestors[i].getProperties().print();
    	}
    	return topInvestors;
    }
    //getInvestorsByRecentFunding function sorts investors by largest uptick in recent funding
    
    //comparator for sorting companies by total funding
    static class TotalFundingComparator implements Comparator<Organization>{
    	public int compare(Organization a, Organization b){
    		if (a.getProperties().getTotalFundingUSD() > b.getProperties().getTotalFundingUSD())
    			return -1;
    		if (a.getProperties().getTotalFundingUSD() == b.getProperties().getTotalFundingUSD())
    			return 0;
    		return 1;
    	}
    }
    //comparator for sorting investors by total funding
    static class TotalInvestingComparator implements Comparator<Investor>{
    	public int compare(Investor a, Investor b){
    		if (a.getProperties().getTotalInvestingUSD() > b.getProperties().getTotalInvestingUSD())
    			return -1;
    		if (a.getProperties().getTotalInvestingUSD() == b.getProperties().getTotalInvestingUSD())
    			return 0;
    		return 1;
    	}
    }
}