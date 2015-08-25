import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import com.csvreader.CsvWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnalyzeFrenchFintechScene {

    //hashmaps for storing companies and investors by name. Using concurrent structure to avoid race conditions with threads
    private static ConcurrentHashMap<String, Organization> companies;
    private static ConcurrentHashMap<String, OrganizationSummary> summaries;
    private static ConcurrentHashMap<String, Investor> investors;
    
    //number of companies/investors we want to include in our "top ten" lists
    private static final int NUM_RESULTS = 10;
    
    //parameters for calculating changes in funding
    private static final int LAST_YEAR = 2011;
    private static final int CURRENT_YEAR = 2012;
    
    //number of threads to query API, delay to avoid sending in too many requests concurrently
    private static final int NUM_THREADS = 3;
    private static final int API_CALL_DELAY = 1000;
    
	//query-type identifiers for CrunchBaseQueries
	private static final String ORGANIZATION = "Organization";
	private static final String PERSON = "Person";
	private static final String PERMALINK_SEARCH = "Permalink";
	private static final String ORGANIZATION_SEARCH = "Organization";
	private static final String LOCATION_FRANCE = "f134827e36a1fd31a82f950489e103ef";
	private static final String CATEGORY_FINTECH = "e06799a9f78976e749a771ee980a70ec";
	
	//JSON parsing variables
	private static ObjectMapper mapper = new ObjectMapper();
    
    //BufferedReader for reading in file
    private static BufferedReader reader = null;
    private static String COMPANIES_FILE = "src/company_list.txt";
    
    //filenames for database creation
    private static final String FILENAME_INVESTORS_BY_RECENT_FUNDING = "investorsByRecentFunding.csv";
    private static final String FILENAME_COMPANIES_BY_RECENT_FUNDING = "companiesByRecentFunding.csv";
    private static final String FILENAME_INVESTORS_BY_TOTAL_FUNDING = "investorsByTotalFunding.csv";
    private static final String FILENAME_COMPANIES_BY_TOTAL_FUNDING = "companiesByTotalFunding.csv";
    
    public static void main(String[] args) {
     
    	//find companies, generate full profiles
    	summaries = findCompaniesByCategoryAndLocation(CATEGORY_FINTECH, LOCATION_FRANCE);
//        investors = updateInvestorInformation(companies);
//        System.out.println("Finished printing investors.");
//        System.out.println("Number of investors: " + investors.size());
//        
//        getCompaniesByTotalFunding(companies, NUM_RESULTS);
//        getInvestorsByTotalFunding(investors, NUM_RESULTS);
//        getCompaniesByRecentFunding(companies, NUM_RESULTS);
//        getInvestorsByRecentFunding(investors, NUM_RESULTS);
    }
    
    //findCompaniesByCategoryAndLocation function finds CrunchBase companies by given category and location,
    //returns ConcurrentHashMap with their permalinks as keys
    public static ConcurrentHashMap<String, OrganizationSummary> findCompaniesByCategoryAndLocation(String category, String location){
    	ConcurrentHashMap<String, OrganizationSummary> summaries = new ConcurrentHashMap<String, OrganizationSummary>();
    	//get summaries, store permalinks in companies hashmap
    	ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
    	Callable<String> query = new CrunchBaseQuery(ORGANIZATION_SEARCH, category, location);
    	Future<String> futureResult= threadPool.submit(query);
    	try{
    		String result = futureResult.get();
    		if (result != null){
    			JSONWrapperOrganizationSummarySearch summariesData = mapper.readValue(result, JSONWrapperOrganizationSummarySearch.class);
    			System.out.println("Created wrapper.");
    			for (OrganizationSummary summary : summariesData.getSummaryWrapper().getSummaries()){
    				summaries.put(summary.getPermalink(), summary);
    				summary.getProperties().print();
    			}
    		}
    	}
    	catch (InterruptedException | ExecutionException | IOException ex){
    		ex.printStackTrace();
    	}
    	finally{
    		threadPool.shutdown();
    		try{
    			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    		}
    		catch (InterruptedException ex){
    			ex.printStackTrace();
    		}
    	}
    	return summaries;
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

    //updateInvestorInformation updates Investors from our companies with additional API calls
    public static ConcurrentHashMap<String, Investor> updateInvestorInformation(ConcurrentHashMap<String, Organization> companies){

    	//create investors HashMap
    	ConcurrentHashMap<String, Investor> updatedInvestors = new ConcurrentHashMap<String, Investor>();

    	//thread pool to make calls
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        //loop through every company's list of investors, updating as we go
        for (Entry<String, Organization> company : companies.entrySet()){
        		for (Entry<String, Investor> entry : company.getValue().getRelationships().getInvestors().entrySet()){
        			Investor currInvestor = entry.getValue();
        			//if we've already updated this Investor, no need to do it again
        			if (updatedInvestors.get(currInvestor.getPermalink()) != null){
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
    		//update recent investing
    		topCompanies[i].getChangeInFunding(LAST_YEAR, CURRENT_YEAR);
    		topCompanies[i].getProperties().print();
    	}
    	//create CSV file
    	writeCompaniesCSV(topCompanies, FILENAME_COMPANIES_BY_TOTAL_FUNDING);
    	return topCompanies;
    }
    
    //getCompaniesByRecentFunding function sorts companies by largest uptick in recent funding
    public static Organization[] getCompaniesByRecentFunding(ConcurrentHashMap<String, Organization> companies, int numCompanies){
    	//convert ConcurrentHashMap to array, sort
    	Organization[] companiesArray = companies.values().toArray(new Organization[companies.size()]);
    	Arrays.sort(companiesArray, new RecentFundingComparator());
    	Organization[] topCompanies = new Organization[numCompanies];
    	for (int i = 0; i < numCompanies; i++){
    		topCompanies[i] = companiesArray[i];
    		System.out.println("Number " + (i + 1) + ": change in funding of " + topCompanies[i].getChangeInFunding(LAST_YEAR, CURRENT_YEAR));
    		topCompanies[i].getProperties().print();
    	}
    	//create CSV file
    	writeCompaniesCSV(topCompanies, FILENAME_COMPANIES_BY_RECENT_FUNDING);
    	return topCompanies;
    }
    //getInvestorsByTotalFunding function sorts investors by total funding
    public static Investor[] getInvestorsByTotalFunding(ConcurrentHashMap<String, Investor> investors, int numInvestors){
    	//convert ConcurrentHashMap to array, sort
    	Investor[] investorsArray = investors.values().toArray(new Investor[investors.size()]);
    	Arrays.sort(investorsArray, new TotalInvestingComparator());
    	Investor[] topInvestors = new Investor[numInvestors];
    	for (int i = 0; i < numInvestors; i++){
    		topInvestors[i] = investorsArray[i];
    		//update recent funding
    		topInvestors[i].getChangeInFunding(LAST_YEAR, CURRENT_YEAR);
    		topInvestors[i].getProperties().print();
    	}
    	//write CSV file
    	writeInvestorsCSV(topInvestors, FILENAME_INVESTORS_BY_TOTAL_FUNDING);
    	return topInvestors;
    }
    //getInvestorsByRecentFunding function sorts investors by largest uptick in recent funding
    public static Investor[] getInvestorsByRecentFunding(ConcurrentHashMap<String, Investor> investors, int numInvestors){
    	//convert ConcurrentHashMap to array, sort
    	Investor[] investorsArray = investors.values().toArray(new Investor[investors.size()]);
    	Arrays.sort(investorsArray, new RecentInvestingComparator());
    	Investor[] topInvestors = new Investor[numInvestors];
    	for (int i = 0; i < numInvestors; i++){
    		topInvestors[i] = investorsArray[i];
    		System.out.println("Number " + (i + 1) + ": change in funding of " + topInvestors[i].getChangeInFunding(LAST_YEAR, CURRENT_YEAR));
    		topInvestors[i].getProperties().print();
    	}
    	//create CSV file
    	writeInvestorsCSV(topInvestors, FILENAME_INVESTORS_BY_RECENT_FUNDING);
    	return topInvestors;
    }
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
    
    //comparator for sorting companies by total funding
    static class RecentFundingComparator implements Comparator<Organization>{
    	public int compare(Organization a, Organization b){
    		if (a.getChangeInFunding(LAST_YEAR, CURRENT_YEAR) > b.getChangeInFunding(LAST_YEAR, CURRENT_YEAR))
    			return -1;
    		if (a.getChangeInFunding(LAST_YEAR, CURRENT_YEAR) == b.getChangeInFunding(LAST_YEAR, CURRENT_YEAR))
    			return 0;
    		return 1;
    	}
    }
    
    //comparator for sorting investors by total funding
    static class RecentInvestingComparator implements Comparator<Investor>{
    	public int compare(Investor a, Investor b){
    		if (a.getChangeInFunding(LAST_YEAR, CURRENT_YEAR) > b.getChangeInFunding(LAST_YEAR, CURRENT_YEAR))
    			return -1;
    		if (a.getChangeInFunding(LAST_YEAR, CURRENT_YEAR) == b.getChangeInFunding(LAST_YEAR, CURRENT_YEAR))
    			return 0;
    		return 1;
    	}
    }
    
    //CSV writing methods
    public static void writeCompaniesCSV(Organization[] companies, String fileName){

        //check if file exists
        if(new File(fileName).exists())
        	return;
        try {
            // use FileWriter constructor that specifies open for appending
            CsvWriter writer = new CsvWriter(new FileWriter(fileName, true), ',');
            //write headers
            writer.write("permalink");
            writer.write("name");
            writer.write("totalFundingUSD");
            writer.write("website");
            writer.write("blog");
            writer.write("description");
            writer.write("shortDescription");
            writer.write("foundedDate");
            writer.write("minEmployees");
            writer.write("maxEmployees");
            writer.write("stockSymbol");
            writer.write("changeInFunding");
            writer.endRecord();
            //write data
            for (Organization company : companies){
            	OrganizationProperties properties = company.getProperties();
                writer.write(properties.getPermalink());
                writer.write(properties.getName());
                writer.write(Long.toString(properties.getTotalFundingUSD()));
                writer.write(properties.getWebsite());
                writer.write(properties.getBlog());
                writer.write(properties.getDescription());
                writer.write(properties.getShortDescription());
                writer.write(properties.getFoundedDate());
                writer.write(Long.toString(properties.getMinEmployees()));
                writer.write(Long.toString(properties.getMaxEmployees()));
                writer.write(properties.getStockSymbol());
                writer.write(Long.toString(properties.getChangeInFunding()));
                writer.endRecord();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeInvestorsCSV(Investor[] investors, String fileName){

        //check if file exists
        if(new File(fileName).exists())
        	return;
        int FIRST_DEGREE_INDEX = 0;
        int SECOND_DEGREE_INDEX = 1;

        try {
            // use FileWriter constructor that specifies open for appending
            CsvWriter writer = new CsvWriter(new FileWriter(fileName, true), ',');
            //write headers
            writer.write("type");
            writer.write("permalink");
            writer.write("name");
            writer.write("totalInvestingUSD");
            writer.write("website");
            writer.write("blog");
            writer.write("description");
            writer.write("shortDescription");
            writer.write("foundedDate");
            writer.write("minEmployees");
            writer.write("maxEmployees");
            writer.write("stockSymbol");
            writer.write("changeInFunding");
            writer.write("averageInvestment");
            writer.write("numInvestments");
            writer.write("firstName");
            writer.write("lastName");
            writer.write("degree");
            writer.write("subject");
            writer.write("degree");
            writer.write("subject");
            writer.endRecord();
        
            //write data
            for (Investor investor : investors){
            	InvestorProperties properties = investor.getProperties();
            	writer.write(properties.getType());
                writer.write(properties.getPermalink());
                writer.write(properties.getName());
                writer.write(Long.toString(properties.getTotalInvestingUSD()));
                writer.write(properties.getWebsite());
                writer.write(properties.getBlog());
                writer.write(properties.getDescription());
                writer.write(properties.getShortDescription());
                writer.write(properties.getFoundedDate());
                writer.write(Long.toString(properties.getMinEmployees()));
                writer.write(Long.toString(properties.getMaxEmployees()));
                writer.write(properties.getStockSymbol());
                writer.write(Long.toString(properties.getChangeInFunding()));
                writer.write(Long.toString(properties.getAverageInvestment()));
                writer.write(Long.toString(properties.getNumInvestments()));
                writer.write(properties.getFirstName());
                writer.write(properties.getLastName());
                if (properties.getDegrees() != null && !properties.getDegrees().isEmpty()){
                	Degree firstDegree = properties.getDegrees().get(FIRST_DEGREE_INDEX);
                	if (firstDegree != null){
                		writer.write(firstDegree.getProperties().getType());
                		writer.write(firstDegree.getProperties().getSubject());
                	}
                	if (properties.getDegrees().size() > FIRST_DEGREE_INDEX + 1){
                		Degree secondDegree = properties.getDegrees().get(SECOND_DEGREE_INDEX);
                	
	                	if (secondDegree != null){
	                		writer.write(secondDegree.getProperties().getType());
	                		writer.write(secondDegree.getProperties().getSubject());
	                	}
                	}
                }
                writer.endRecord();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}