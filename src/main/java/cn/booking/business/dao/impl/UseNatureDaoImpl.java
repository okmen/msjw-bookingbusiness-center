package cn.booking.business.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.UseNaturePo;
import cn.booking.business.dao.IUseNatureDao;
import cn.booking.business.dao.mapper.IUseNatureMapper;
@Repository
public class UseNatureDaoImpl implements IUseNatureDao  {
	@Autowired
	private IUseNatureMapper iUseNatureMapper;

	@Override
	public int addBatch(List<UseNaturePo> useNaturePos) throws Exception {
		return iUseNatureMapper.addBatch(useNaturePos);
	}

	@Override
	public int deleteAll() throws Exception {
		return iUseNatureMapper.deleteAll();
	}

	@Override
	public List<UseNaturePo> getAll() throws Exception {
		return iUseNatureMapper.getAll();
	}
	
}
