package cn.booking.business.cache;

public interface IIdCardTypeCached extends ICacheKey {
	
	public String getIIdCardTypeByKey(String key);
	    
	public boolean setIIdCardType(String key,String value);
}
