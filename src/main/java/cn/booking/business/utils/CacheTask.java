package cn.booking.business.utils;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.cache.IBookingBusinessCached;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;

@SuppressWarnings(value="all")
public class CacheTask implements Runnable,Serializable {
	protected static Logger logger = LoggerFactory.getLogger(CacheTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CacheTaskExecute cacheTaskExecute;
	
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	
	private List<CarTypeVO> carTypeVOs;
	
	
	public CacheTask(List<CarTypeVO> carTypeVOs,IBookingBusinessCachedImpl iBookingBusinessCached) {
		this.carTypeVOs = carTypeVOs;
		this.iBookingBusinessCached = iBookingBusinessCached;
	}
	
	@Override
	public void run() {
		try {
			CacheTaskExecute.cacheCarType(carTypeVOs,iBookingBusinessCached);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
