package cn.booking.business.utils.execute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.ICarTypeCached;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.ICarTypeDao;
import cn.booking.business.utils.TransferThirdParty;

public class CarTypeExecute {
	protected static Logger logger = LoggerFactory.getLogger(CarTypeExecute.class);

	@Autowired
	private ICarTypeDao iCarTypeDao;
	@Autowired
	private ICarTypeCached iCarTypeCached;
	
	public void execute(List<CarTypeVO> carTypeVOsOld, IBookingBusinessCachedImpl iBookingBusinessCached)throws Exception {
		String key = ICacheKey.ICarTypeCached;
		List<CarTypeVO> carTypeVOsNew = TransferThirdParty.getCarTypes(iBookingBusinessCached);
		if(!carTypeVOsOld.equals(carTypeVOsNew)){
			List<CarTypePo> carTypePos = new ArrayList<CarTypePo>();
			for(CarTypeVO carTypeVO : carTypeVOsNew){
				CarTypePo carTypePo = new CarTypePo();
				carTypePo.setCarTypeId(carTypeVO.getId());
				carTypePo.setCode(carTypeVO.getCode());
				carTypePo.setCreateDate(new Date());
				carTypePo.setDescription(carTypeVO.getDescription());
				carTypePo.setName(carTypeVO.getName());
				carTypePos.add(carTypePo);
			}
			//更新到数据库和redis
			iCarTypeDao.deleteAll();
			iCarTypeDao.addBatch(carTypePos);
			iCarTypeCached.setICarType(key, JSON.toJSONString(carTypePos));
		}
	}
}
