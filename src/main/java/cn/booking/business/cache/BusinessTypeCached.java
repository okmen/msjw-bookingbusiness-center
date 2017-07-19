package cn.booking.business.cache;

public interface BusinessTypeCached {
	
	public String getBusinessTypeByKey(String key);
    
	public boolean setBusinessType(String key,String value);
}
