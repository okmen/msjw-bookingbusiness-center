package cn.booking.business.utils.task;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.cache.IBookingBusinessCached;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.utils.execute.IdCardTypeExecute;

@SuppressWarnings(value="all")
public class IdCardTypeTask implements Runnable,Serializable {
	protected static Logger logger = LoggerFactory.getLogger(IdCardTypeTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IdCardTypeExecute idCardTypeExecute;
	
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	
	private List<IdTypeVO> idTypeVOs;
	
	private String businessTypeId;
	
	public IdCardTypeTask(IdCardTypeExecute idCardTypeExecute,List<IdTypeVO> idTypeVOs,IBookingBusinessCachedImpl iBookingBusinessCached,String businessTypeId) {
		this.idCardTypeExecute = idCardTypeExecute;
		this.idTypeVOs = idTypeVOs;
		this.iBookingBusinessCached = iBookingBusinessCached;
		this.businessTypeId = businessTypeId;
	}
	
	@Override
	public void run() {
		try {
			idCardTypeExecute.execute(idTypeVOs,iBookingBusinessCached,businessTypeId);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
