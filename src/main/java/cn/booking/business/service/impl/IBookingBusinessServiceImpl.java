package cn.booking.business.service.impl;


import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.service.IBookingBusinessService;
import cn.sdk.webservice.WebServiceClient;
@SuppressWarnings(value="all")
@Service("bookingBusinessService")
public class IBookingBusinessServiceImpl implements IBookingBusinessService{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	/**
	 * 获取车辆类型列表
	 */
	public List<IdTypeVO> getCarTypes(LinkedHashMap<String, Object> map) throws Exception {
		List<IdTypeVO> idTypeVOs = null;
		String method = "getCarTypes";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject result = jsonObject.getJSONObject("result");
			String carTypeVO = result.getString("CarTypeVO");
			idTypeVOs =JSON.parseArray(carTypeVO, IdTypeVO.class);
		} catch (Exception e) {
			logger.error("getCarTypes 失败 ， map = " + map);
			throw e;
		}
		return idTypeVOs;
	}
	@Override
	public List<BusinessTypeVO> getBusinessTypes(LinkedHashMap<String, Object> map) throws Exception {
		List<BusinessTypeVO> businessTypeVOs = null;
		String method = "getBusinessTypes";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject result = jsonObject.getJSONObject("result");
			String BusinessTypeVO = result.getString("BusinessTypeVO");
			System.out.println(BusinessTypeVO);
			businessTypeVOs =JSON.parseArray(BusinessTypeVO, BusinessTypeVO.class);
		} catch (Exception e) {
			logger.error("getCarTypes 失败 ， map = " + map);
			throw e;
		}
		return businessTypeVOs;
	}
	@Override
	public List<IdTypeVO> getIdTypes(LinkedHashMap<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public OrgVO findOrgByOrgId(LinkedHashMap<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public OrgVO getOrgsByBusinessTypeId(LinkedHashMap<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
