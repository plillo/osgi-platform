package it.hash.osgi.geoJson;


public class Coordinate implements Comparable<Coordinate>{
	
		protected double longitude ;
		protected double latitude;
		
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(latitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(longitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
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
			Coordinate other = (Coordinate) obj;
			if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)
					&& (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)))
				return false;
			return true;
		}
		@Override
		public int compareTo(Coordinate coordinate) {
			if ((this.latitude==coordinate.getLatitude()) && (this.longitude==coordinate.getLongitude()))
					return 0;
			
			return 1;
		}
		
		@Override
		public String toString(){
			String s="["+this.getLatitude()+","+getLongitude()+"]";
			
			
			return s;
		
			
		}

}
