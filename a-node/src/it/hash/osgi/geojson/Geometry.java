package it.hash.osgi.geojson;

import java.util.ArrayList;
import java.util.List;

public abstract class Geometry {
	
	protected String type;
	protected List<Coordinate> coordinates;
	
	
	public Geometry(){
		type=this.getClass().getSimpleName();
	}
	
	
	public String getType() {
		return type;
	}


	public void setType(String type){
		this.type=type;
	}
	public abstract Object getCoordinates() ;
	

	public List<Coordinate> addCoordinates(Double longitude,Double latitude) {
		if (this.coordinates ==null )
			  this.coordinates=new ArrayList<Coordinate>();
		Coordinate c = new Coordinate();
		c.setLongitude(longitude);
		c.setLatitude(latitude);
		
		this.coordinates.add(c);
		
		return this.coordinates;
	
	}

public List<Coordinate> removeCoordinates(Double longitude,Double latitude)
{
	Coordinate c = new Coordinate();
	c.setLongitude(longitude);
	c.setLatitude(latitude);
	
	if (this.coordinates.contains(c))
		this.coordinates.remove(c);
	
	return this.coordinates;
}
	
	@Override 
	public String toString(){
		String s=null;
		int i=0;
		if (this.coordinates.size()>1){
	
		for( i=0;i< this.coordinates.size()-1;i++)
		   s+="["+this.coordinates.get(i).getLatitude()+","+this.coordinates.get(i).getLongitude()+"],";
	}
		s+="["+this.coordinates.get(i).getLatitude()+","+this.coordinates.get(i).getLongitude()+"]";
		
		
		return s;
	}
	
	@Override
	public abstract int hashCode() ;
	
	
	@Override
	public abstract boolean equals(Object obj) ;
	



}
