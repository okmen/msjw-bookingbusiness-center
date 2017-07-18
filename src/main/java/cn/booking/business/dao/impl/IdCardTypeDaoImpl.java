package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.dao.IIdCardTypeDao;
import cn.booking.business.dao.mapper.IdCardTypeMapper;
@Repository
public class IdCardTypeDaoImpl implements IIdCardTypeDao {
	@Autowired
	private IdCardTypeMapper idCardTypeMapper;
	
	@Override
	public int addBatch(List<IdCardTypePo> idCardTypePos) throws Exception {
		return idCardTypeMapper.addBatch(idCardTypePos);
	}

	@Override
	public int deleteAll() throws Exception {
		return idCardTypeMapper.deleteAll();
	}

	@Override
	public List<IdCardTypePo> getAll() throws Exception {
		return idCardTypeMapper.getAll();
	}

	
	
}
