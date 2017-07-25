package cn.booking.business.utils.execute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.cache.AppointmentCached;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.AppointmentDao;
import cn.booking.business.utils.TransferThirdParty;

public class AppointmentExecute {
	protected static Logger logger = LoggerFactory.getLogger(AppointmentExecute.class);

	@Autowired
	private AppointmentCached appointmentCached;
	@Autowired
	private AppointmentDao appointmentDao;
	
	public void execute(List<AppointmentPo> appointmentPos, IBookingBusinessCachedImpl iBookingBusinessCached,String orgId,String businessTypeId)throws Exception {
		String key = ICacheKey.AppointmentCached + orgId + "_" + businessTypeId;
		// 从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
		List<String> strings = TransferThirdParty.getAppointmentDate(iBookingBusinessCached, orgId, businessTypeId, "", "");
		List<String> stringsOld = new ArrayList<String>();
		for(AppointmentPo appointmentPo : appointmentPos){
			stringsOld.add(appointmentPo.getAppointment());
		}
		if(!strings.equals(stringsOld)){
			List<AppointmentPo> appointmentPos2 = new ArrayList<AppointmentPo>();
			for(String string : strings){
				AppointmentPo appointmentPo = new AppointmentPo(string, new Date());
				appointmentPos2.add(appointmentPo);
			}
			//更新到数据库和redis
			appointmentDao.deleteAll();
			appointmentDao.addBatch(appointmentPos2);
			appointmentCached.setAppointment(key, JSON.toJSONString(appointmentPos2));
		}
	}
}
