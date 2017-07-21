package cn.booking.business.utils;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.CarTypeVO;

@SuppressWarnings(value="all")
public class CacheTask implements Runnable,Serializable {
	protected static Logger logger = LoggerFactory.getLogger(CacheTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CacheTaskExecute cacheTaskExecute;
	
	private List<CarTypeVO> carTypeVOs;
	
	
	public CacheTask(List<CarTypeVO> carTypeVOs) {
		this.carTypeVOs = carTypeVOs;
	}
	
	@Override
	public void run() {
		try {
			CacheTaskExecute.cacheCarType(carTypeVOs);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
