package cn.booking.business.cache;

public interface IVehicleNodelCached extends ICacheKey{
	
	public String getVehicleNodelByKey(String key);
    
	public boolean setVehicleNodelType(String key,String value);
}
