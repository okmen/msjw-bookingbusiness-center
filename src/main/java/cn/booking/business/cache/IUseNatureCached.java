package cn.booking.business.cache;

public interface IUseNatureCached extends ICacheKey {

	public String getIUseNatureByKey(String key);
	    
	public boolean setIUseNatureType(String key,String value);
}
