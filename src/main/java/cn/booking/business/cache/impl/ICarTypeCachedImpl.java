package cn.booking.business.cache.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.booking.business.cache.ICarTypeCached;
import cn.sdk.cache.ICacheManger;
@Service
public class ICarTypeCachedImpl implements ICarTypeCached {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<String> cacheManger;
	
	
	@Override
	public String getICarTypeByKey(String key) {
		return cacheManger.get(key);
	}

	@Override
	public boolean setICarType(String key, String value) {
		//15天=1296000秒
		return cacheManger.set(key, value, 1296000);
	}
	
}
