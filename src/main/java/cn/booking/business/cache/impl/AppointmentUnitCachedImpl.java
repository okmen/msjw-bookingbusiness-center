package cn.booking.business.cache.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.dubbo.config.annotation.Service;

import cn.booking.business.cache.AppointmentUnitCached;
import cn.sdk.cache.ICacheManger;
@Service
public class AppointmentUnitCachedImpl implements AppointmentUnitCached {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<String> cacheManger;
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<Object> objectcacheManger;
	
	@Override
	public String getAppointmentUnitByKey(String key) {
		return cacheManger.get(key);
	}

	@Override
	public boolean setAppointmentUnit(String key, String value) {
		return cacheManger.set(key, value);
	}

}