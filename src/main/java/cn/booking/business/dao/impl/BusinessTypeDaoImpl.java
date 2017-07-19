package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.BusinessTypePo;
import cn.booking.business.dao.BusinessTypeDao;
import cn.booking.business.dao.mapper.BusinessTypeMapper;
@Repository
public class BusinessTypeDaoImpl implements BusinessTypeDao {
	@Autowired
	
	private BusinessTypeMapper businessTypeMapper;
	
	@Override
	public int addBatch(List<BusinessTypePo> businessTypePos) throws Exception {
		return businessTypeMapper.addBatch(businessTypePos);
	}

	@Override
	public int deleteAll() throws Exception {
		return businessTypeMapper.deleteAll();
	}

	@Override
	public List<BusinessTypePo> getAll() throws Exception {
		return businessTypeMapper.getAll();
	}
	
}
