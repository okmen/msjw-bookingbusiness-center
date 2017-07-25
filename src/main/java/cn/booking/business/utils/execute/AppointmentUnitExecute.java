package cn.booking.business.utils.execute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.bean.AppointmentUnitPo;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.cache.AppointmentCached;
import cn.booking.business.cache.AppointmentUnitCached;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.AppointmentDao;
import cn.booking.business.dao.AppointmentUnitDao;
import cn.booking.business.utils.TransferThirdParty;

public class AppointmentUnitExecute {
	protected static Logger logger = LoggerFactory.getLogger(AppointmentExecute.class);

	@Autowired
	private AppointmentUnitCached appointmentUnitCached;
	@Autowired
	private AppointmentUnitDao appointmentUnitDao;
	
	public void execute(List<AppointmentUnitPo> appointmentUnitPosOld, IBookingBusinessCachedImpl iBookingBusinessCached,String btId)throws Exception {
		String key = ICacheKey.AppointmentUnitCached + btId;
		// 从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
		List<OrgVO> orgVOs = TransferThirdParty.getOrgsByBusinessTypeId(iBookingBusinessCached, btId, "", "");
		
		List<AppointmentUnitPo> appointmentUnitPosNew = new ArrayList<>();
		for(OrgVO orgVO : orgVOs){
			AppointmentUnitPo appointmentUnitPo = new AppointmentUnitPo();
			appointmentUnitPo.setAppointmentUnitId(orgVO.getId());
			appointmentUnitPo.setCode(orgVO.getCode());
			appointmentUnitPo.setDescription(orgVO.getDescription());
			appointmentUnitPo.setName(orgVO.getName());
			appointmentUnitPo.setPointx(orgVO.getPointx());
			appointmentUnitPo.setPointy(orgVO.getPointy());
			appointmentUnitPosNew.add(appointmentUnitPo);
		}
		if(!appointmentUnitPosOld.equals(appointmentUnitPosNew)){
			//更新到数据库和redis
			appointmentUnitDao.deleteAll();
			appointmentUnitDao.addBatch(appointmentUnitPosNew);
			appointmentUnitCached.setAppointmentUnit(key, JSON.toJSONString(appointmentUnitPosNew));
		}
	}
}
