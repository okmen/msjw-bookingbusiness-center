package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.CarTypePo;
import cn.booking.business.dao.ICarTypeDao;
import cn.booking.business.dao.mapper.CarTypeMapper;
@Repository
public class CarTypeDaoImpl implements ICarTypeDao {
	
	@Autowired
	private CarTypeMapper carTypeMapper;
	
	@Override
	public int addBatch(List<CarTypePo> carTypePos) throws Exception {
		return carTypeMapper.addBatch(carTypePos);
	}

	@Override
	public int deleteAll() throws Exception {
		return carTypeMapper.deleteAll();
	}

	@Override
	public List<CarTypePo> getAll() throws Exception {
		return carTypeMapper.getAll();
	}
	
}
