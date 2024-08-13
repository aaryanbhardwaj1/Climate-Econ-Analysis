package climate;

import java.util.ArrayList;

import org.w3c.dom.css.Counter;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    */ 
    public StateNode getFirstState () {
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     */
    public void createLinkedStructure ( String inputFile ) {
        
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {
        
        String[] arr = inputLine.split(",");
        String currentStateName = arr[2];
        StateNode newState = new StateNode();
        newState.setName(currentStateName);
        StateNode ptr = firstState;

        if(firstState == null){
            firstState = newState;
        }
        
        while(ptr != null){
            if(ptr.name.equals(newState.name)){
                ;
            }else if(ptr.next == null){
                ptr.next = newState;
            }
            ptr = ptr.next;
        }
    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        String[] arr = inputLine.split(",");
        String currentCountyName = arr[1];
        String currentStateName = arr[2];
        CountyNode newCounty = new CountyNode();
        newCounty.setName(currentCountyName);
        StateNode ptr = firstState;

        while(ptr != null){
            if(ptr.name.equals(currentStateName)){
                if(ptr.down == null){
                    ptr.down = newCounty;
                }else{
                    CountyNode countyptr = ptr.down;
                    while(countyptr != null){
                        if(newCounty.name.equals(countyptr.name)){
                            ;
                        }else if(countyptr.next == null){
                            countyptr.next = newCounty;
                        }
                        countyptr = countyptr.next;
                    }
                }
            }
            ptr = ptr.next;
        }
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        String[] arr = inputLine.split(",");
        String stateName = arr[2];
        String countyName = arr[1];
        String currentCommName = arr[0];
        StateNode ptr = firstState;

        Data communityData = new Data();
        communityData.prcntAfricanAmerican = Double.parseDouble(arr[3]);
        communityData.prcntNative = Double.parseDouble(arr[4]);
        communityData.prcntAsian = Double.parseDouble(arr[5]);
        communityData.prcntWhite = Double.parseDouble(arr[8]);
        communityData.prcntHispanic = Double.parseDouble(arr[9]);
        communityData.disadvantaged = arr[19];
        communityData.PMlevel = Double.parseDouble(arr[49]);
        communityData.chanceOfFlood = Double.parseDouble(arr[37]);
        communityData.prcntPovertyLine = Double.parseDouble(arr[121]);

        CommunityNode newCommunity = new CommunityNode();
        newCommunity.name = currentCommName;
        newCommunity.info = communityData;

        while(ptr != null){
            if(ptr.name.equals(stateName)){
                CountyNode ptr2 = ptr.down;
                while(ptr2 != null){
                    if(ptr2.name.equals(countyName)){
                        if(ptr2.down == null){
                            ptr2.down = newCommunity;
                            return;
                        }else{
                        CommunityNode ptr3 = ptr2.down;
                        while(ptr3 != null){
                            if(ptr3.name.equals(currentCommName)){
                                return;
                            }else if(ptr3.next == null){
                                ptr3.next = newCommunity;
                                return;
                            }
                            ptr3 = ptr3.next;
                        }
                    }
                }
                ptr2 = ptr2.next;
            }
        }
        ptr = ptr.next;
        }
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        int numDis = 0;
        StateNode ptr = firstState;
        while(ptr != null){
            CountyNode ptr2 = ptr.down;
            while(ptr2 != null){
                CommunityNode ptr3 = ptr2.down;
                while(ptr3 != null){
                    Data currObject = ptr3.info;
                    if((race.equals("African American")) && (currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntAfricanAmerican() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("Asian American")) && (currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntAsian() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("Hispanic American")) && (currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntHispanic() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("Native American")) && (currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntNative() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("White American")) && (currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntWhite() * 100) >= userPrcntage)){
                        numDis++;
                    }
                    ptr3 = ptr3.next;
                }
                ptr2 = ptr2.next;
            }
            ptr = ptr.next;
        }
        return numDis;
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        int numDis = 0;
        StateNode ptr = firstState;
        while(ptr != null){
            CountyNode ptr2 = ptr.down;
            while(ptr2 != null){
                CommunityNode ptr3 = ptr2.down;
                while(ptr3 != null){
                    Data currObject = ptr3.info;
                    if((race.equals("African American")) && !(currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntAfricanAmerican() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("Asian American")) && !(currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntAsian() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("Hispanic American")) && !(currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntHispanic() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("Native American")) && !(currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntNative() * 100) >= userPrcntage)){
                        numDis++;
                    }else if((race.equals("White American")) && !(currObject.getAdvantageStatus().equals("True")) && ((currObject.getPrcntWhite() * 100) >= userPrcntage)){
                        numDis++;
                    }
                    ptr3 = ptr3.next;
                }
                ptr2 = ptr2.next;
            }
            ptr = ptr.next;
        }
        return numDis;
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        ArrayList<StateNode> listOfStates = new ArrayList<StateNode>();
        StateNode ptr = firstState;
        while(ptr != null){
            CountyNode ptr2 = ptr.down;
            while(ptr2 != null){
                CommunityNode ptr3 = ptr2.down;
                while(ptr3 != null){
                    Data currObject = ptr3.info;
                    if(currObject.PMlevel >= PMlevel && !(listOfStates.contains(ptr))){
                        listOfStates.add(ptr);
                    }
                    ptr3 = ptr3.next;
                }
                ptr2 = ptr2.next;
            }
            ptr = ptr.next;
        }
        return listOfStates;
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        int numDis = 0;
        StateNode ptr = firstState;
        while(ptr != null){
            CountyNode ptr2 = ptr.down;
            while(ptr2 != null){
                CommunityNode ptr3 = ptr2.down;
                while(ptr3 != null){
                    Data currObject = ptr3.info;
                    if(currObject.chanceOfFlood >= userPercntage){
                        numDis++;
                    }
                    ptr3 = ptr3.next;
                }
                ptr2 = ptr2.next;
            }
            ptr = ptr.next;
        }
        return numDis;
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        ArrayList<CommunityNode> listOfCommunities = new ArrayList<CommunityNode>();

        StateNode ptr = firstState;
        while(ptr != null){
            if(ptr.name.equals(stateName)){
                CountyNode ptr2 = ptr.down;
                while(ptr2 != null){
                    CommunityNode ptr3 = ptr2.down;
                    while(ptr3 != null){
                        if(listOfCommunities.size() < 10){
                            listOfCommunities.add(ptr3);
                        }else{
                            CommunityNode lowestIndex = listOfCommunities.get(0);
                            for(CommunityNode check : listOfCommunities){
                                if(check.getInfo().prcntPovertyLine < lowestIndex.getInfo().prcntPovertyLine){
                                    lowestIndex = check;
                                }
                            }
                            if(ptr3.getInfo().getPercentPovertyLine() > lowestIndex.getInfo().getPercentPovertyLine()){
                                int replaceIndex = listOfCommunities.indexOf(lowestIndex);
                                listOfCommunities.set(replaceIndex, ptr3);
                            }
                        }
                        ptr3 = ptr3.next;
                    }
                    ptr2 = ptr2.next;
                }
            }
            ptr = ptr.next;
        }

        return listOfCommunities;
    }
}

