package cn.booking.business.cache;

public interface IBusinessTypeCached extends ICacheKey{
	
	public String getIBusinessTypeByKey(String key);
    
	public boolean setIBusinessType(String key,String value);
}
