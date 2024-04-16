package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.*;
import interfaces.*;
/**
 * @author Kenneth S. Sepulveda
 * TrainStationManager class manages train stations and calculates shortest routes and travel times.
 */
public class TrainStationManager {
    private Map<String, List<Station>> stations = new HashTableSC<>(1, new SimpleHashFunction<>());
    private Map<String, String> visitedCities = new HashTableSC<>(1, new SimpleHashFunction<>());
    private Map<String, Stack<String>> visitedCitiesStack = new HashTableSC<>(1, new SimpleHashFunction<>());
    private Map<String, Station> shortestDistances = new HashTableSC<>(1, new SimpleHashFunction<>());
    private Map<String, Double> travelTimes = new HashTableSC<>(1, new SimpleHashFunction<>());

    
    private Stack<Station> stationStack = new ArrayListStack <>();
    private Stack<Station> stationStack_copy = new ArrayListStack <>();
    
    private String startingCity = "Westside";
    private ArrayList<String> keys = new ArrayList<>(1);
    private Set<String> stationVisited_set = new HashSet<>();
    /**
     * Constructor for TrainStationManager.
     * Reads station data from a CSV file and populates the station map.
     *
     * @param station_file The name of the CSV file containing station data.
     */ 
    public TrainStationManager(String station_file) { 
        // Implement file reading and station data population here
        // Create a HashMap to store source cities and their stations        // Implement file reading and station data population here
        // Create a HashMap to store source cities and their stations

        // Read data from CSV and populate the map
        try (BufferedReader reader = new BufferedReader(new FileReader("inputFiles/" +station_file))) {
            String line;
            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr.length == 3) {
                    String srcCity = arr[0].trim();
                    String destCity = arr[1].trim();
                    int distance = Integer.parseInt(arr[2].trim());

                    Station stationAB = new Station(destCity, distance);
                    Station stationBA = new Station(srcCity, distance);
                    
                    //to fill the keys arraylist
                    if(!keys.contains(srcCity)){
                        keys.add(srcCity);
                    }
                    if(!keys.contains(destCity)){
                        keys.add(destCity);
                    }

                    // Check if the source city is in the map
                    if (stations.containsKey(srcCity)) {
                        // Check if the destination station is already in the source city's list
                        boolean found = false;
                        for (Station station : stations.get(srcCity)) {
                            if (station.getCityName().equals(stationAB.getCityName())) {
                                found = true;
                                break;
                            }
                        }
                        // If the destination station is not in the source city's list, add it
                        if (!found) {
                            stations.get(srcCity).add(stationAB);
                        }
                    } else {
                        // If the source city is not in the map, create a new list with the destination station
                        List<Station> newList = new ArrayList<>();
                        newList.add(stationAB);
                        stations.put(srcCity, newList);
                    }

                    // Repeat the process for the destination city
                    if (stations.containsKey(destCity)) {
                        boolean found = false;
                        for (Station station : stations.get(destCity)) {
                            if (station.getCityName().equals(stationBA.getCityName())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            stations.get(destCity).add(stationBA);
                        }
                    } else {
                        List<Station> newList = new ArrayList<>();
                        newList.add(stationBA);
                        stations.put(destCity, newList);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("NO SE PUDO LEER");
        }
        
        shortestDistances = findShortestDistance();
    }
    
    /*
        This method is for finding the Shortest Distance from a specific station to the others. Also, it works as a  
    form of fill the visitedCities map, making easier to find the Travel Times.
    
    The find Shortest Distance method work the same way that the one mentioned in "Shortcuts to Victory", with a 
    few modifications, that can fill the visitedCities map.
    */
    /**
     * Finds the shortest distance from a specific station to the others and fills the visitedCities map.
     * Uses Dijkstra's algorithm to find shortest routes.
     *
     * @return A map of station names to their shortest distance from the starting city.
     */
    private Map<String, Station> findShortestDistance() {
        // Implement Dijkstra's algorithm to find shortest routes

        shortestDistances.put(startingCity, new Station(startingCity, 0));
        
        
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            for (Station station : stations.get(key)) {
                if (!station.getCityName().equals(startingCity)) {
                    if (!shortestDistances.containsKey(station.getCityName())){
                        shortestDistances.put(station.getCityName(), new Station(startingCity, Integer.MAX_VALUE));
                    }
                }
            }
        }

        stationStack.push(shortestDistances.get(startingCity));
        //se toma el primer elemento y se mete al stack


        while (!stationStack.isEmpty()){

            Station currentStation = stationStack.pop();                            //pop ciudad_actual 
            stationVisited_set.add(currentStation.getCityName());                   // se agrega la estacion actual al set
            List<Station> neighbors = stations.get(currentStation.getCityName());   // se hace una lista de vecinos



            if (neighbors != null){
                for (Station neighbor : neighbors){

                    int stationstationDistance = 0;
                    for (Station temp_station : stations.get(currentStation.getCityName())) {
                        if (temp_station.getCityName().equals(neighbor.getCityName())) {
                            stationstationDistance = temp_station.getDistance();
                        }
                    }

                    int A = shortestDistances.get(neighbor.getCityName()).getDistance();
                    int B = stationstationDistance;
                    int C = shortestDistances.get(currentStation.getCityName()).getDistance();

                    if (A > (B+C)){
                        shortestDistances.get(neighbor.getCityName()).setDistance(B+C);
                        //SE GUARDAN DE MODO QUE A ES LA ESTACION actual y C ES LA ESTACION previa a A
                        visitedCities.put(neighbor.getCityName(), currentStation.getCityName());
                    }
                    if (!stationVisited_set.isMember(neighbor.getCityName())){
                        int stack_number = neighbor.getDistance();
                        if (!stationStack.isEmpty()){
                            if (stack_number <= stationStack.top().getDistance()){
                                stationStack.push(neighbor);
                            }else {
                                sortStack(neighbor, stationStack);
                            }
                        }else{
                            stationStack.push(neighbor);
                        }

                    }
                }
            }
        }

        return shortestDistances;
    }
    /**
     * 
     * @param neighbor
     * @param stackToSort
     * 
     */
    public void sortStack(Station neighbor, Stack<Station> stackToSort) {
        int stack_number = neighbor.getDistance();

        while (!stackToSort.isEmpty() && stack_number > stackToSort.top().getDistance()) { // Agregamos una comprobación de si la pila está vacía
            stationStack_copy.push(stackToSort.pop());
        }
        stackToSort.push(neighbor);

        while (!stationStack_copy.isEmpty()){
            stackToSort.push(stationStack_copy.pop());
        }
    }
    /**
     * 
     * @return
     */
    public Map<String, Double> getTravelTimes() {
    // Implement travel time calculation here	
            // 5 minutes per kilometer
            // 15 min per station

        /*
            Por cada ciudad hacer una lista que retorne las ciudades por las que se pasa, por lo que tenemos
        la cantidad con arr.size() y ademas las ciudades para hacer el trayecto
        */
        
        //keys represent all the stations names
    	  for (int i = 0; i < keys.size(); i++) {
    		    String key = keys.get(i);
    		    Stack<String> citiesStack = new ArrayListStack<>();
    		    if (key != null) {
    		      String visitedCity = visitedCities.get(key);
    		      if (visitedCity != null) {
    		        while (!visitedCity.equals(startingCity)) {
    		          citiesStack.push(visitedCity); // Push visited city onto the stack
    		          visitedCity = visitedCities.get(visitedCity);
    		        }
    		      }
    		    }
    		    visitedCitiesStack.put(key, citiesStack);
    		  }

    		  for (int i = 0; i < keys.size(); i++) {
    		    String key = keys.get(i);
    		    double distance = shortestDistances.get(key).getDistance();
    		    travelTimes.put(key, visitedCitiesStack.get(key).size() * 15 + 2.5 * distance);
    		  }
    		  return travelTimes;
    		}

////////////////////////   GETTERs AND SETTERs 
    /**
     * 
     * @return
     */
	public Map<String, List<Station>> getStations() {
            return stations;
        }


	/**
	 * 
	 * @param cities
	 */
	public void setStations(Map<String, List<Station>> cities) {
            this.stations = cities;
        }


	/**
	 * 
	 * @return
	 */
	public Map<String, Station> getShortestRoutes() {
            return shortestDistances;
        }


	/**
	 * 
	 * @param shortestRoutes
	 */
	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
            this.shortestDistances = shortestRoutes;
        }

	
	/**
	 * BONUS EXERCISE THIS IS OPTIONAL
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return (String) String representation of the path taken to reach stationName.
	 */
	public String traceRoute(String stationName) {
	    StringBuilder route = new StringBuilder();
	    route.append(startingCity);

	    Stack<String> stack = visitedCitiesStack.get(stationName);
	    if (stack != null) {
	        while (!stack.isEmpty()) {
	            route.append("->").append(stack.pop());
	        }
	    }
	    if (!stationName.equals(startingCity)) {
	        route.append("->").append(stationName);
	    }

	    return route.toString();
	}

}