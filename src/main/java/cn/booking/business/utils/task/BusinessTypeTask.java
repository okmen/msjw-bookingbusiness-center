package cn.booking.business.utils.task;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.utils.execute.BusinessTypeExecute;

public class BusinessTypeTask implements Runnable,Serializable{
	protected static Logger logger = LoggerFactory.getLogger(BusinessTypeTask.class);
	
	private BusinessTypeExecute businessTypeExecute;
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	private List<BusinessTypeVO> businessTypeVOs;
	private String type;
	
	
	public BusinessTypeTask(BusinessTypeExecute businessTypeExecute,List<BusinessTypeVO> businessTypeVOs,IBookingBusinessCachedImpl iBookingBusinessCached,String type) {
		this.businessTypeExecute = businessTypeExecute;
		this.businessTypeVOs = businessTypeVOs;
		this.iBookingBusinessCached = iBookingBusinessCached;
		this.type = type;
	}
	
	@Override
	public void run() {
		try {
			businessTypeExecute.execute(businessTypeVOs, iBookingBusinessCached, type);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
