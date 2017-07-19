package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.dao.AppointmentDao;
import cn.booking.business.dao.mapper.AppointmentMapper;
@Repository
public class AppointmentDaoImpl implements AppointmentDao {

	@Autowired
	private AppointmentMapper appointmentMapper;
	
	@Override
	public int addBatch(List<AppointmentPo> appointmentPos) throws Exception {
		return appointmentMapper.addBatch(appointmentPos);
	}

	@Override
	public int deleteAll() throws Exception {
		return appointmentMapper.deleteAll();
	}

	@Override
	public List<AppointmentPo> getAll() throws Exception {
		return appointmentMapper.getAll();
	}
	
}
