package cn.booking.business.utils.execute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.IIdCardTypeCached;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.IIdCardTypeDao;
import cn.booking.business.utils.TransferThirdParty;

public class IdCardTypeExecute {
	protected static Logger logger = LoggerFactory.getLogger(IdCardTypeExecute.class);

	@Autowired
	private IIdCardTypeDao iIdCardTypeDao;
	@Autowired
	private IIdCardTypeCached iIdCardTypeCached;
	
	public void execute(List<IdTypeVO> idTypeVOsOld, IBookingBusinessCachedImpl iBookingBusinessCached,String businessTypeId)throws Exception {
		String key = ICacheKey.IIdCardTypeCached + businessTypeId;
		List<IdTypeVO> idTypeVOsNew = TransferThirdParty.getIdTypes(iBookingBusinessCached, businessTypeId, "", "");
		if(!idTypeVOsOld.equals(idTypeVOsNew)){
			
			List<IdCardTypePo> idCardTypePos = new ArrayList<IdCardTypePo>();
			for(IdTypeVO idTypeVO : idTypeVOsNew){
				IdCardTypePo idCardTypePo = new IdCardTypePo();
				idCardTypePo.setCode(idTypeVO.getCode());
				idCardTypePo.setCreateDate(new Date());
				idCardTypePo.setDescription(idTypeVO.getDescription());
				idCardTypePo.setIdcardTypeId(idTypeVO.getId());
				idCardTypePo.setName(idTypeVO.getName());
				idCardTypePos.add(idCardTypePo);
			}
			//更新到数据库和redis
			iIdCardTypeDao.deleteAll();
			iIdCardTypeDao.addBatch(idCardTypePos);
			iIdCardTypeCached.setIIdCardType(key, JSON.toJSONString(idCardTypePos));
		}
	}
}
