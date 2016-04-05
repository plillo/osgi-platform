package it.hash.osgi.user;

import java.util.Map;

public class AttributeValue {

	private String attributeUuid;
	private Map<String, Object> value;
	
	public String getAttributeUuid() {
		return attributeUuid;
	}
	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}
	public Map<String, Object> getValue() {
		return value;
	}
	public void setValue(Map<String, Object> value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeUuid == null) ? 0 : attributeUuid.hashCode());
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
		AttributeValue other = (AttributeValue) obj;
		if (attributeUuid == null) {
			if (other.attributeUuid != null)
				return false;
		} else if (!attributeUuid.equals(other.attributeUuid))
			return false;
		return true;
	}
}
