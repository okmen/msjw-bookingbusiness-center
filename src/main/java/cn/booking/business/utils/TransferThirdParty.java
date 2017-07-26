package cn.booking.business.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.bean.BusinessTypePo;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.OrgVO;
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
	/**
	 * 获取可预约时间
	 * @param iBookingBusinessCached
	 * @param orgId
	 * @param businessTypeId
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws Exception
	 */
	public static List<String> getAppointmentDate(IBookingBusinessCachedImpl iBookingBusinessCached,String orgId, String businessTypeId, String arg0, String arg1)throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("businessTypeId", businessTypeId);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		List<String> dateTime = new ArrayList<String>();
		String jkId = "JK09";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			StringBuffer str=new StringBuffer();
			str.append("<root><orgId>").append(orgId).append("</orgId>");
			str.append("<businessTypeId>").append(businessTypeId).append("</businessTypeId>").append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			logger.info("请求报文："+str.toString());
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			JSONObject result = jsonObject.getJSONObject("result");
			if ("00".equals(code)) {
				String date = result.getString("string");
				dateTime = JSON.parseArray(date, String.class);
			} else {
			}
		} catch (Exception e) {
			logger.error("getAppointmentDate 失败 ， map = " + map);
			throw e;
		}
		return dateTime;
	}
	
	
	public static List<OrgVO> getOrgsByBusinessTypeId(IBookingBusinessCachedImpl iBookingBusinessCached, String btId, String arg0, String arg1) throws Exception {
		String jkId = "JK05";
		List<OrgVO> orgVOs = new ArrayList<OrgVO>();
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("btId", btId);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			StringBuffer str=new StringBuffer();
			str.append("<root>");
			str.append("<btId>").append(btId).append("</btId>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			JSONObject result = jsonObject.getJSONObject("result");
			String OrgVO = result.getString("OrgVO");
			try{
			orgVOs = JSON.parseArray(OrgVO, OrgVO.class);
			}catch (Exception e) {
				OrgVO vo=JSON.parseObject(OrgVO, OrgVO.class);
				orgVOs.add(vo);
			}
		} catch (Exception e) {
			logger.error("getOrgsByBusinessTypeId 失败 ， btId = " + btId + ", arg0=" + arg0 + ",arg1=" + arg1);
			throw e;
		}
		return orgVOs;
	}
	
	public static List<IdTypeVO> getIdTypes(IBookingBusinessCachedImpl iBookingBusinessCached, String businessTypeId, String arg0, String arg1) throws Exception {
		List<IdTypeVO> idTypeVos = null;
		String jkId = "JK06";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("businessTypeId", businessTypeId);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			StringBuffer str=new StringBuffer();
			str.append("<root>");
			str.append("<businessTypeId>").append(businessTypeId).append("</businessTypeId>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			JSONObject result = jsonObject.getJSONObject("result");
			String IdTypeVO = result.getString("IdTypeVO");
			idTypeVos = JSON.parseArray(IdTypeVO, IdTypeVO.class);
		} catch (Exception e) {
			logger.error("getCarTypes 失败 ， map = " + map);
			throw e;
		}
		return idTypeVos;
	}
	
	public static List<BusinessTypeVO> getBusinessTypes(IBookingBusinessCachedImpl iBookingBusinessCached,String type, String part, String arg0, String arg1) throws Exception {
		List<BusinessTypeVO> businessTypeVOs = new ArrayList<BusinessTypeVO>();
		String jkId = "JK04";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("type", type);
		map.put("part", part);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			StringBuffer str=new StringBuffer();
			str.append("<root><type>").append(type).append("</type>");
			str.append("<part>").append(part).append("</part>").append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			JSONObject result = jsonObject.getJSONObject("result");
			String BusinessTypeVO = result.getString("BusinessTypeVO");
			businessTypeVOs = JSON.parseArray(BusinessTypeVO, BusinessTypeVO.class);
			
		} catch (Exception e) {
			logger.error("getBusinessTypes 失败 ， map = " + map);
			throw e;
		}
		return businessTypeVOs;
	}
}
