package it.hash.osgi.geojson;

import java.io.Serializable;



public class Point extends Geometry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5518618745771158738L;
	
	
		
	public Point(Double longitude,Double latitude){
		super();
		this.addCoordinates(longitude, latitude);
	}
	@Override
	public Coordinate getCoordinates() {
		return coordinates.get(0);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.coordinates == null) ? 0 : this.coordinates.hashCode());
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
		if(other.coordinates==null)
			if (coordinates != null)
			  return false;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.get(0).equals(other.coordinates.get(0)))
			return false;
		return true;
	}
	
	}
