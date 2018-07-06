
import java.util.*;
import java.util.Map.Entry;

public class Main {
    
    private static String ACTIVE_USER = "Shakirin";
    
    //database
    private static ArrayList users = new ArrayList();
    private static ArrayList oaps = new ArrayList();
    private static ArrayList oaprates = new ArrayList();
    
    //active user
    private static User activeUser = null;
    
    //similarity scores collection
    private static Map<String, Double> collSimilarityScores = null;
    
    public static void main(String[] args) {

        boolean dataset = false;
        
        initDataset(); //add data into dataset
        
        //set active user
        for (int i = 0; i < users.size(); i++) {
            User u = (User) users.get(i);
            if(u.getName().equals(ACTIVE_USER))
                activeUser = u;
        }
        
        //find sum and average ratings
//        calculateRatings();

        //show all ratings
//        displayRatings();

        //get rating predictions
        predictRatings();
        
        //display nearest neighbors limit by 'k'
//        displayNearestSimilarity(3);
    }

    //Initialize Dataset
    private static void initDataset() {
        
        boolean inituser = false;
        boolean initoaps = false;
        boolean initoaprates = false;
        
        //add data for User   
        users.add(new User("Shakirin", "Melaka", "24"));
        users.add(new User("User A", "Negeri Sembilan", "22"));
        users.add(new User("User B", "Kelantan", "22"));
        users.add(new User("User C", "Pulau Pinang", "22"));
        inituser = true;
        
        //add data for OutdoorActivityPlace
        oaps.add(new OutdoorActivityPlace(1, "Melaka Tropical Fruit Farms", "Melaka"));
        oaps.add(new OutdoorActivityPlace(2, "Air Panas Paabas", "Melaka"));
        oaps.add(new OutdoorActivityPlace(3, "Taman Botanikal Ayer Keroh", "Melaka"));
        oaps.add(new OutdoorActivityPlace(4, "Sungai Chilling", "Selangor"));
        oaps.add(new OutdoorActivityPlace(5, "Kem Sungai Pauh", "Cameron Highland"));
        oaps.add(new OutdoorActivityPlace(6, "Pantai Saujana", "Port Dickson"));
        initoaps = true;
        
        //add data for OAPRating
        // Rating must between 1-5 only.....
        for (int ui = 0; ui < users.size(); ui++) {
            User u = (User) users.get(ui);
            
            for (int oapi = 0; oapi < oaps.size(); oapi++) {
                OutdoorActivityPlace oap = (OutdoorActivityPlace) oaps.get(oapi);
                
                //show name and place;
                //auto ratings
                int randomNumber = getRandomNumberInRange();
//                oaprates.add(new OAPRating(u.getName(), oap.getPlaceName(), getRandomNumberInRange()));
                oaprates.add(new OAPRating(u.getName(), oap.getPlaceName(), randomNumber));
            }
            if(ui == (users.size() - 1)) {
                initoaprates = true;
            }
        } 
    }
    //END: Initialize Dataset
    
    // calcualte ratings 
    private static void calculateRatings() {
        
        System.out.println("[CALCULATE RATINGS]\n");
        
        // FIND average rating for all outdoor places        
        for (int oapi = 0; oapi < oaps.size(); oapi++) {
           
            OutdoorActivityPlace oap = (OutdoorActivityPlace) oaps.get(oapi);
            
            int rateCount = 0;
            int rateSum = 0;
            double rateAvg = 0;
            
            for (int ui = 0; ui < users.size(); ui++) {
                
                User user = (User) users.get(ui);
                
                for (int oapri = 0; oapri < oaprates.size(); oapri++) {
                    OAPRating oapr = (OAPRating) oaprates.get(oapri);
                    
                    if(oap.getPlaceName().equals(oapr.getPlaceName())) {
                        if(user.getName().equals(oapr.getUsername())) {
                            if(oapr.getRate() > 0) {
                                rateCount++;
                                rateSum += oapr.getRate();
                            }
                        }
                    }
                }
                
            }
            
            rateAvg = ((double) rateSum) / rateCount;
            
            System.out.println("PlaceName: " + oap.getPlaceName() + "[RateCount: " + rateCount + ", RateSum:" + rateSum + ", RateAvg: " + rateAvg + "]");
        }
        //END: FIND average rating for all outdoor places
        
        System.out.println("");
    }
    // END: calculate ratings
    
    // view all ratings
    private static void displayRatings() {
        for (int i = 0; i < oaprates.size(); i++) {
            OAPRating oapr = (OAPRating) oaprates.get(i);
            System.out.println(oapr.toString());
        }
        System.out.println();
    }
    
    private static void displayRatings(User user) {
        for (int i = 0; i < oaprates.size(); i++) {
            OAPRating oapr = (OAPRating) oaprates.get(i);
            
            if(oapr.getUsername().equals(user.getName()))
                System.out.println(oapr.toString());
        }
        System.out.println();
    }
    // END: view all ratings
    
    // kNN @ Nearest-Neighbor Collaborative Filtering Algorithm - V2
    private static void predictRatings() {
        displayRatings(activeUser);
        
        //roll up the recommender engine: Nearest Neighbor CF Algorithm
        initNearestNeighbor(activeUser);
    }
    
    private static void initNearestNeighbor(User currentUser) {
        collSimilarityScores = new HashMap<>();
        
        double similarityScore = 0;
        
        //get other users
        for (int i = 0; i < users.size(); i++) {
            User otherUser = (User) users.get(i);
            
            //get similarity
            if(!currentUser.getName().equals(otherUser.getName()))
                similarityScore = euclideanSimilarity(currentUser, otherUser);
            else
                similarityScore = -1;
            
            collSimilarityScores.put(otherUser.getName(), similarityScore);
        }

        //sort all similarity scores (ORDER BY -> ASC = true, DESC = false)
        collSimilarityScores = sortSimilarityByComparator(collSimilarityScores, false);
        
        //find weighted average and give recommendation based on k Nearest Neighbor
        computeRecommendation(currentUser);
    }
    
    //---- find similarity using Euclidean Distance Similarity technique
    private static double euclideanSimilarity(User currentUser, User otherUser) {
        
        double sumOapRatePow = 0;
        double sqrtSumOapRate = 0;
        double similarity = 0;
        
//        System.out.println("Between Username: " + currentUser.getName() + "," +otherUser.getName());
        
        //get all outdoor activity places
        for (int i = 0; i < oaps.size(); i++) {
            OutdoorActivityPlace oap = (OutdoorActivityPlace) oaps.get(i);
            
            int curRate = 0;
            int otherRate = 0;
            int diff = 0;
            
            double oapRatePow = 0;
            
            //get all ratings based on each outdoor activity places
            for (int j = 0; j < oaprates.size(); j++) {
                OAPRating oapr = (OAPRating) oaprates.get(j);
                
                //get current user rating for each outdoor activity places
                if((oapr.getPlaceName().equals(oap.getPlaceName())) &&  (oapr.getUsername().equals(currentUser.getName()))) {
                    curRate = oapr.getRate();
                }
                
                //get other user rating for each outdoor activity places
                if((oapr.getPlaceName().equals(oap.getPlaceName())) &&  (oapr.getUsername().equals(otherUser.getName()))) {
                    otherRate = oapr.getRate();
                }
            }
            
            //if both current user & other user rating is not 0!!!
            if(curRate != 0 && otherRate != 0) {
                //find the differences between current user and other user ratings
                diff = curRate - otherRate;

                //power the diff by 2
                oapRatePow = Math.pow(((double)diff), 2);

                //sum all ratings power of 2 for each outdoor activity places
                sumOapRatePow += oapRatePow;
            }
        }
        
        sqrtSumOapRate = Math.sqrt(sumOapRatePow);
        
        //calculate similarity
        similarity = 1 / (1 + sqrtSumOapRate);
        
        return similarity;
    }
    
    //---- sort similarity by value using LinkedList and Comparator
    private static Map<String, Double> sortSimilarityByComparator(Map<String, Double> unsorted, final boolean order) {
        
        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsorted.entrySet());
        
        //sort list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>(){
        
            public int compare(Entry<String, Double> object1, Entry<String, Double> object2) {
                if(order) {
                    return object1.getValue().compareTo(object2.getValue());
                } else {
                    return object2.getValue().compareTo(object1.getValue());
                }
            }
        });
        
        // maintain insertion order of sorted map with LinkedList
        Map<String, Double> sorted = new LinkedHashMap<>();
        for(Entry<String, Double> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        
        return sorted;
    }
    
    //find weighted average and give recommendations
    private static void computeRecommendation(User currentUser) {
        System.out.println("[Recommendation Results]");
        for (int oi = 0; oi < oaps.size(); oi++) {
            OutdoorActivityPlace oap = (OutdoorActivityPlace) oaps.get(oi);
            
            int k = 3;
            double weightedSum = 0;
            double simSum = 0;
            boolean isRated = false;
            
            //get all sorted users with similarity score
            Set setEntry = collSimilarityScores.entrySet();
            Iterator iterator = setEntry.iterator();
            for (int i = 0; i < k; i++) {
                Map.Entry simData =  (Map.Entry) iterator.next();
                
                String userName = (String) simData.getKey();
                double simScore = (double) simData.getValue();
                    
                //get all ratings
                for (int j = 0; j < oaprates.size(); j++) {
                    OAPRating oapr = (OAPRating) oaprates.get(j);

                    //check if current user have rated this places
                    if(oapr.getPlaceName().equals(oap.getPlaceName()) && oapr.getUsername().equals(currentUser.getName())) {
                        if(oapr.getRate() > 0)
                            isRated = true;
                        else
                            isRated = false;
                    } 
                    
                    //sum all ratings by k Nearest Neighbor for each outdoor activity places
                    if(oapr.getPlaceName().equals(oap.getPlaceName()) && oapr.getUsername().equals(userName)) {
                        weightedSum += (oapr.getRate() * simScore);
                        simSum += simScore;
                    }
                }
            }
            
            //calculate weighted average for k Nearest Neighbors
            double weightedAvg = weightedSum / simSum;
            
            //give outdoor activity places recommendations
            if(!isRated && weightedAvg >= 3) {
                System.out.println("PlaceName: " + oap.getPlaceName() + " | Weighted Average Ratings: " + weightedAvg + " | Recommended!");
            }
        }
        
        System.out.println();
    }
    
    private static void displayNearestSimilarity(int k) {
        //limit k=5 (5 nearest neighbors)
        Set setEntry = collSimilarityScores.entrySet();
        Iterator iterator = setEntry.iterator();
        for (int i = 0; i < k; i++) {
            Map.Entry mapData =  (Map.Entry) iterator.next();
            System.out.println("Name: " + mapData.getKey() + " -> Similarity Score: " + mapData.getValue());
        }
    }
    // END: kNN @ Nearest-Neighbor Collaborative Filtering Algorithm - V2
    
    //auto generate random integer for ratings
    private static int getRandomNumberInRange() {

        int min = 0, max = 5;
        
        if (min >= max) {
                throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
