package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.AppointmentUnitPo;
import cn.booking.business.dao.AppointmentUnitDao;
import cn.booking.business.dao.mapper.AppointmentUnitMapper;
@Repository
public class AppointmentUnitDaoImpl implements AppointmentUnitDao {
	@Autowired
	private AppointmentUnitMapper appointmentUnitMapper;
	@Override
	public int addBatch(List<AppointmentUnitPo> appointmentUnitPos) throws Exception {
		return appointmentUnitMapper.addBatch(appointmentUnitPos);
	}

	@Override
	public int deleteAll() throws Exception {
		return appointmentUnitMapper.deleteAll();
	}

	@Override
	public List<AppointmentUnitPo> getAll() throws Exception {
		return appointmentUnitMapper.getAll();
	}

}
