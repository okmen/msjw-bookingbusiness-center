package cn.booking.business.utils.execute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.booking.business.bean.BusinessTypePo;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.cache.IBusinessTypeCached;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.BusinessTypeDao;
import cn.booking.business.utils.TransferThirdParty;

public class BusinessTypeExecute {
	protected static Logger logger = LoggerFactory.getLogger(BusinessTypeExecute.class);

	@Autowired
	private IBusinessTypeCached businessTypeCached;
	@Autowired
	private BusinessTypeDao businessTypeDao;
	
	public void execute(List<BusinessTypeVO> businessTypeVOsOld, IBookingBusinessCachedImpl iBookingBusinessCached,String type)throws Exception {
		String key = ICacheKey.IBusinessTypeCached + type;
		List<BusinessTypeVO> businessTypeVOsNew = TransferThirdParty.getBusinessTypes(iBookingBusinessCached, type, "", "", "");
		if(!businessTypeVOsOld.equals(businessTypeVOsNew)){
			List<BusinessTypePo> businessTypePos = new ArrayList<BusinessTypePo>();
			for(BusinessTypeVO businessTypeVO : businessTypeVOsNew){
				BusinessTypePo businessTypePo = new BusinessTypePo();
				businessTypePo.setBusinessId(businessTypeVO.getId());
				businessTypePo.setCode(businessTypeVO.getCode());
				businessTypePo.setDescription(businessTypeVO.getDescription());
				businessTypePo.setLx(businessTypeVO.getLx());
				businessTypePo.setName(businessTypeVO.getName());
				businessTypePos.add(businessTypePo);
			}
			//更新到数据库和redis
			businessTypeDao.deleteAll();
			businessTypeDao.addBatch(businessTypePos);
			businessTypeCached.setIBusinessType(key, JSON.toJSONString(businessTypePos));
		}
	}
}
