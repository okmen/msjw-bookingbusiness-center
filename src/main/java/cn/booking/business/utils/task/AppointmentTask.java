package cn.booking.business.utils.task;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.utils.execute.AppointmentExecute;
@SuppressWarnings(value="all")
public class AppointmentTask implements Runnable,Serializable  {
	protected static Logger logger = LoggerFactory.getLogger(AppointmentTask.class);
	
	private AppointmentExecute appointmentExecute;
	
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	private List<AppointmentPo> appointmentPos;
	private String orgId;
	private String businessTypeId;
	
	public AppointmentTask(AppointmentExecute appointmentExecute,List<AppointmentPo> appointmentPos,IBookingBusinessCachedImpl iBookingBusinessCached,
			String orgId,String businessTypeId) {
		this.appointmentExecute = appointmentExecute;
		this.appointmentPos = appointmentPos;
		this.iBookingBusinessCached = iBookingBusinessCached;
		this.orgId = orgId;
		this.businessTypeId = businessTypeId;
	}
	
	@Override
	public void run() {
		try {
			appointmentExecute.execute(appointmentPos, iBookingBusinessCached, orgId, businessTypeId);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
