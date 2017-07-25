package cn.booking.business.cache.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.booking.business.cache.IBusinessTypeCached;
import cn.sdk.cache.ICacheManger;
@Service
public class IBusinessTypeCachedImpl implements IBusinessTypeCached {
protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<String> cacheManger;
	
	@Override
	public String getIBusinessTypeByKey(String key) {
		return cacheManger.get(key);
	}

	@Override
	public boolean setIBusinessType(String key, String value) {
		return cacheManger.set(key, value);
	}

}
