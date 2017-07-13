package cn.booking.business.service.impl;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.booking.business.bean.AppTimeHelper;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.service.IBookingBusinessService;
import cn.sdk.bean.BaseBean;
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
	public List<CarTypeVO> getCarTypes() throws Exception {
		List<CarTypeVO> carTypeVOs = null;
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
			carTypeVOs =JSON.parseArray(carTypeVO, CarTypeVO.class);
		} catch (Exception e) {
			logger.error("getCarTypes 失败 ， map = " + map);
			throw e;
		}
		return carTypeVOs;
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
	public List<IdTypeVO> getIdTypes(String businessTypeId,String arg0,String arg1) throws Exception {
		List<IdTypeVO> idTypeVos = null;
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
			String IdTypeVO = result.getString("IdTypeVO");
			idTypeVos =JSON.parseArray(IdTypeVO, IdTypeVO.class);
		} catch (Exception e) {
			logger.error("getCarTypes 失败 ， map = " + map);
			throw e;
		}
		return idTypeVos;
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
	public List<OrgVO> getOrgsByBusinessTypeId(String btId,String arg0,String arg1) throws Exception {
		List<OrgVO> orgVOs = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("btId", btId);
		map.put("arg0", null == arg0 ? "" : arg0);
		map.put("arg1", null == arg1 ? "" : arg1);
		String method = "getOrgsByBusinessTypeId";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			JSONObject result = jsonObject.getJSONObject("result");
			String OrgVO = result.getString("OrgVO");
			orgVOs = JSON.parseArray(OrgVO, OrgVO.class);
			
		} catch (Exception e) {
			logger.error("getOrgsByBusinessTypeId 失败 ， btId = " + btId + ", arg0=" +arg0 + ",arg1=" + arg1);
			throw e;
		}
		return orgVOs;
	}

	@Override
	public List<AppTimeHelper> getAppTimes(String date, String orgId, String businessTypeId, String carTypeId,String optlittleCar) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("date", date);
		map.put("orgId", orgId);
		map.put("businessTypeId", businessTypeId);
		map.put("carTypeId", carTypeId);
		map.put("optlittleCar", optlittleCar);
		List<AppTimeHelper> dateTime = new ArrayList<AppTimeHelper>();
		String method = "getAppTimes";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			JSONObject result = jsonObject.getJSONObject("result");
			if("00".equals(code)){
				String date1 = result.getString("string");
				dateTime = JSON.parseArray(date1, AppTimeHelper.class);
			}else{
				
			}
		} catch (Exception e) {
			logger.error("getAppointmentDate 失败 ， map = " + map);
			throw e;
		}
		return dateTime;
	}

	@Override
	public List<String> getAppointmentDate(String orgId, String businessTypeId, String arg0, String arg1)throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("businessTypeId", businessTypeId);
		map.put("arg0", arg0);
		map.put("arg1", arg1);
		List<String> dateTime = new ArrayList<String>();
		String method = "getAppointmentDate";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			JSONObject result = jsonObject.getJSONObject("result");
			if("00".equals(code)){
				String date = result.getString("string");
				dateTime = JSON.parseArray(date, String.class);
			}else{
			}
		} catch (Exception e) {
			logger.error("getAppointmentDate 失败 ， map = " + map);
			throw e;
		}
		return dateTime;
	}

	@Override
	public SmsInfoVO simpleSendMessage(String mobile,String idType,String lx,String ip,String bookerType,String bookerName,String bookerIdNumber,String idNumber,String codes) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("idType", idType);
		map.put("lx", lx);
		map.put("ip", ip);
		map.put("bookerType", bookerType);
		map.put("bookerName", bookerName);
		map.put("bookerIdNumber", bookerIdNumber);
		map.put("idNumber", idNumber);
		map.put("codes", codes);
		SmsInfoVO smsInfoVO = null;
		String method = "simpleSendMessage";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			String result = jsonObject.getString("result");
			smsInfoVO = JSON.parseObject(JSON.toJSONString(jsonObject), SmsInfoVO.class);
		} catch (Exception e) {
			logger.error("getAppointmentDate 失败 ， map = " + map);
			throw e;
		}
		return smsInfoVO;
	}

	@Override
	public BaseBean createVehicleInfo(CreateVehicleInfoVo vehicleInfoVo) throws Exception {
		BaseBean refBean = new BaseBean();
		logger.debug("【预约类服务】机动车预约信息写入createVehicleInfo... 业务类型id="+vehicleInfoVo.getBusinessTypeId());

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orgId", vehicleInfoVo.getOrgId());  //预约地点
		map.put("businessTypeId", vehicleInfoVo.getBusinessTypeId());  //业务类型id
		map.put("name", vehicleInfoVo.getName());	//姓名
		map.put("idTypeId", vehicleInfoVo.getIdTypeId());	//证件种类id
		map.put("idNumber", vehicleInfoVo.getIdNumber());	//证件号码
		map.put("mobile", vehicleInfoVo.getMobile());		//手机号码
		map.put("appointmentDate", vehicleInfoVo.getAppointmentDate());	//预约日期
		map.put("appointmentTime", vehicleInfoVo.getAppointmentTime());	//预约时间
		map.put("carTypeId", vehicleInfoVo.getCarTypeId());	//号牌种类
		map.put("carFrame", vehicleInfoVo.getCarFrame());	//车架号
		map.put("platNumber", vehicleInfoVo.getPlatNumber());	//车牌号或车架号
		map.put("bookerName", vehicleInfoVo.getBookerName());	//预约人姓名
		map.put("bookerIdNumber", vehicleInfoVo.getBookerIdNumber());	//预约人身份证号码
		map.put("bookerType", vehicleInfoVo.getBookerType());	//预约方式
		map.put("rzjs", vehicleInfoVo.getRzjs());    //认证角色
		map.put("optlittleCar", vehicleInfoVo.getOptlittleCar());	//车辆产地
		map.put("indexType ", vehicleInfoVo.getIndexType());	//指标类型
		map.put("indexNo ", vehicleInfoVo.getIndexNo());	//指标号/公证号/车辆识别代号
		map.put("useCharater", vehicleInfoVo.getUseCharater());	//使用性质
		map.put("arg0", vehicleInfoVo.getArg0());	//车辆型号
		map.put("arg1", vehicleInfoVo.getArg1());	//手机号码
		map.put("arg2", vehicleInfoVo.getArg2());	//短信验证码
		
		String method = "newCreateVehicleInfo";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			refBean.setCode(jsonObject.getString("code"));
			refBean.setMsg(jsonObject.getString("msg"));
			refBean.setData(jsonObject.getString("result"));
			logger.debug("【预约类服务】机动车预约信息写入结果:"+jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】机动车预约信息写入异常 ， map = " + map);
			throw e;
		}
		return refBean;
	}
	
}