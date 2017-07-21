package cn.booking.business.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.cache.IBookingBusinessCached;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.sdk.webservice.WebServiceClient;

/**
 * 调用第三方封装
 * @author Mbenben
 *
 */
@SuppressWarnings(value="all")
public class TransferThirdParty {
	private static Logger logger = Logger.getLogger(TransferThirdParty.class);
	
	public static List<CarTypeVO> getCarTypes(IBookingBusinessCachedImpl iBookingBusinessCached) throws Exception {
		List<CarTypeVO> carTypeVOs = null;
		String jkId = "JK07";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			String data = "<root></root>";
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, data, account, password);
			JSONObject result = jsonObject.getJSONObject("result");
			String carTypeVO = result.getString("CarTypeVO");
			carTypeVOs = JSON.parseArray(carTypeVO, CarTypeVO.class);
			//存mysql
			List<CarTypePo> carTypePos = new ArrayList<CarTypePo>();
			for(CarTypeVO carTypeVO2 : carTypeVOs){
				CarTypePo carTypePo = new CarTypePo();
				carTypePo.setCarTypeId(carTypeVO2.getId());
				carTypePo.setCode(carTypeVO2.getCode());
				carTypePo.setCreateDate(new Date());
				carTypePo.setDescription(carTypeVO2.getDescription());
				carTypePo.setName(carTypeVO2.getName());
				carTypePos.add(carTypePo);
			}
		} catch (Exception e) {
			logger.error("getCarTypes 失败",e);
			throw e;
		}
		return carTypeVOs;
	}
}
