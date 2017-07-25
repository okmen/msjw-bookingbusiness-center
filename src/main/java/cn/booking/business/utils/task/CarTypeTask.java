package cn.booking.business.utils.task;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.utils.execute.CarTypeExecute;

public class CarTypeTask implements Runnable,Serializable {
	protected static Logger logger = LoggerFactory.getLogger(CarTypeTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CarTypeExecute carTypeExecute;
	
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	
	private List<CarTypeVO> carTypeVOs;
	
	
	public CarTypeTask(CarTypeExecute carTypeExecute,List<CarTypeVO> carTypeVOs,IBookingBusinessCachedImpl iBookingBusinessCached) {
		this.carTypeExecute = carTypeExecute;
		this.carTypeVOs = carTypeVOs;
		this.iBookingBusinessCached = iBookingBusinessCached;
	}
	
	@Override
	public void run() {
		try {
			carTypeExecute.execute(carTypeVOs,iBookingBusinessCached);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
