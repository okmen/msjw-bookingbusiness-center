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
	public List<IdTypeVO> getCarTypes() throws Exception {
		List<IdTypeVO> idTypeVOs = null;
		String method = "getCarTypes";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("arg0", "");
		map.put("arg1", "");
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
	public List<BusinessTypeVO> getBusinessTypes(String type,String part,String arg0,String arg1) throws Exception {
		List<BusinessTypeVO> businessTypeVOs = null;
		String method = "getBusinessTypes";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("type",type);
		map.put("part",part);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject result = jsonObject.getJSONObject("result");
			String BusinessTypeVO = result.getString("BusinessTypeVO");
			System.out.println(BusinessTypeVO);
			businessTypeVOs =JSON.parseArray(BusinessTypeVO, BusinessTypeVO.class);
		} catch (Exception e) {
			logger.error("getBusinessTypes 失败 ， map = " + map);
			throw e;
		}
		return businessTypeVOs;
	}
	
	@Override
	public IdTypeVO getIdTypes(String businessTypeId,String arg0,String arg1) throws Exception {
		IdTypeVO idTypeVO = new IdTypeVO();
		String method = "getIdTypes";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("businessTypeId",businessTypeId);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject result = jsonObject.getJSONObject("result");
			String BusinessTypeVO = result.getString("BusinessTypeVO");
			System.out.println(BusinessTypeVO);
			idTypeVO =JSON.parseObject(BusinessTypeVO, IdTypeVO.class);
		} catch (Exception e) {
			logger.error("getCarTypes 失败 ， map = " + map);
			throw e;
		}
		return idTypeVO;
	}
	@Override
	public OrgVO findOrgByOrgId(String orgId) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orgId", orgId);
		OrgVO orgVO = new OrgVO();
		String method = "findOrgByOrgId";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			String result = jsonObject.getString("result");
			if("00".equals(code)){
				orgVO =JSON.parseObject(result, OrgVO.class);
				orgVO.setCode("0000");
			}else{
				orgVO.setMsg(msg);
				orgVO.setCode(code);
			}
		} catch (Exception e) {
			logger.error("findOrgByOrgId 失败 ， orgId = " + orgId);
			throw e;
		}
		return orgVO;
	}
	@Override
	public OrgVO getOrgsByBusinessTypeId(String btId,String arg0,String arg1) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("btId", btId);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		OrgVO orgVO = new OrgVO();
		String method = "getOrgsByBusinessTypeId";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			String result = jsonObject.getString("result");
			if("00".equals(code)){
				orgVO =JSON.parseObject(result, OrgVO.class);
				orgVO.setCode("0000");
			}else{
				orgVO.setMsg(msg);
				orgVO.setCode(code);
			}
		} catch (Exception e) {
			logger.error("getOrgsByBusinessTypeId 失败 ， btId = " + btId + ", arg0=" +arg0 + ",arg1=" + arg1);
			throw e;
		}
		return orgVO;
	}
}
