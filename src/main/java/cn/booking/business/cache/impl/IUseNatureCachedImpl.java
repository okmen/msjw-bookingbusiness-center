package cn.booking.business.cache.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.booking.business.cache.IUseNatureCached;
import cn.sdk.cache.ICacheManger;
@Service
public class IUseNatureCachedImpl implements IUseNatureCached {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<String> cacheManger;
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<Object> objectcacheManger;
	
	@Override
	public String getIUseNatureByKey(String key) {
		return cacheManger.get(key);
	}

	@Override
	public boolean setIUseNatureType(String key, String value) {
		return cacheManger.set(key, value);
	}
	
}
