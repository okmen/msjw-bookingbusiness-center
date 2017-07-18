package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.UseNaturePo;
import cn.booking.business.bean.VehicleNodelPo;
import cn.booking.business.dao.IVehicleNodelDao;
import cn.booking.business.dao.mapper.IVehicleNodelMapper;
@Repository
public class VehicleNodelDaoImpl implements IVehicleNodelDao {
	@Autowired
	private IVehicleNodelMapper iVehicleNodelMapper;
	@Override
	public int addBatch(List<VehicleNodelPo> vehicleNodelPos) throws Exception {
		return iVehicleNodelMapper.addBatch(vehicleNodelPos);
	}

	@Override
	public int deleteAll() throws Exception {
		return iVehicleNodelMapper.deleteAll();
	}

	@Override
	public List<VehicleNodelPo> getAll() throws Exception {
		return iVehicleNodelMapper.getAll();
	}
	
}
