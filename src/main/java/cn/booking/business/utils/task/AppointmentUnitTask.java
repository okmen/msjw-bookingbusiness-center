package cn.booking.business.utils.task;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.AppointmentUnitPo;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.utils.execute.AppointmentUnitExecute;

public class AppointmentUnitTask implements Runnable,Serializable  {
	protected static Logger logger = LoggerFactory.getLogger(AppointmentUnitTask.class);
	
	
	private AppointmentUnitExecute appointmentUnitExecute;
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	private List<AppointmentUnitPo> appointmentUnitPos;
	private String btId;
	
	
	public AppointmentUnitTask(AppointmentUnitExecute appointmentUnitExecute,List<AppointmentUnitPo> appointmentUnitPos,IBookingBusinessCachedImpl iBookingBusinessCached,String btId) {
		this.appointmentUnitExecute = appointmentUnitExecute;
		this.appointmentUnitPos = appointmentUnitPos;
		this.iBookingBusinessCached = iBookingBusinessCached;
		this.btId = btId;
	}
	
	@Override
	public void run() {
		try {
			appointmentUnitExecute.execute(appointmentUnitPos, iBookingBusinessCached, btId);
		} catch (Exception e) {
			logger.error("异步执行错误",e);
		}
	}
}
