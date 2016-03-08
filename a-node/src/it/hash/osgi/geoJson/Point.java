package it.hash.osgi.geoJson;


public class Point extends Geometry implements Comparable<Point>{

	protected Coordinate coordinate;
	public Point(Double longitude,Double latitude){
		super();
		this.coordinate= new Coordinate();
		this.coordinate.setLongitude(longitude);
		this.coordinate.setLatitude(latitude);
	}

	
	public Coordinate getCoordinates() {
		return coordinate;
	}

	public void setCoordinates(Double longitude,Double latitude){
		this.coordinate.setLongitude(longitude);
		this.coordinate.setLatitude(latitude);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.coordinate == null) ? 0 : this.coordinate.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if(other.coordinate==null)
			if (coordinate != null)
			  return false;
	
	    if (!coordinate.equals(other.coordinate))
			return false;
		return true;
	}

	
	public String toGeoJson() {
		String s= "{ type : \"Point\","+ "coordinates: ["+ getCoordinates().getLongitude()
				+","+getCoordinates().getLatitude()+"]}";
		return s;	
	}
	@Override
	public int compareTo(Point point) {
 
		return this.getCoordinates().compareTo(point.getCoordinates());
	}
	
	
	}
