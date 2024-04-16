package main;
/**
 * 
 * 
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
	 * 
	 * @return
	 */
	public String getCityName() {
            return cityName;
	}
	/**
	 * 
	 * @param cityName
	 */
	public void setCityName(String cityName) {
            this.cityName = cityName;
	}
	/**
	 * 
	 * @return
	 */
	public int getDistance() {
            return distance;
	}
	/**
	 * 
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
	 * 
	 * 
	 */
	public String toString() {
		return "(" + this.getCityName() + ", " + this.getDistance() + ")";
	}

}

