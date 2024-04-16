package main;
/**
 * @author Kenneth S. Sepulveda
 * Station class manages getting and setting the city name and also does the same for its distance.
 */
public class Station {
    
        // Private fields for the Station class
        private String cityName;
        private int distance;
	
	public Station(String name, int dist) {
            this.cityName = name;
            this.distance = dist;
	}
	/**
	 * gets the CityName
	 * @return
	 */
	public String getCityName() {
            return cityName;
	}
	/**
	 * sets the city name
	 * @param cityName - Name of the City
	 */
	public void setCityName(String cityName) {
            this.cityName = cityName;
	}
	/**
	 * 
	 * @return gets the distances
	 */
	public int getDistance() {
            return distance;
	}
	/**
	 * sets the distance 
	 * @param distance
	 */
	public void setDistance(int distance) {
            this.distance = distance;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		return this.getCityName().equals(other.getCityName()) && this.getDistance() == other.getDistance();
	}
	@Override
	/**
	 * Converts the distance and CityName into a string
	 */
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}

}

