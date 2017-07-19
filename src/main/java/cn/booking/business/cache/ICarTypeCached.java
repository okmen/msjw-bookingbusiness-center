package cn.booking.business.cache;

public interface ICarTypeCached extends ICacheKey{
	
    public String getICarTypeByKey(String key);
    
    public boolean setICarType(String key,String value);
}
