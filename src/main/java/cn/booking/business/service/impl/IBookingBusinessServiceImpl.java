package cn.booking.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.booking.business.bean.AppTimeHelper;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateDriveinfoVo;
import cn.booking.business.bean.CreateTemporaryLicenseVehicleInfoVo;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.DriveInfoVO;
import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.bean.UseNaturePo;
import cn.booking.business.bean.VehicleInfoVO;
import cn.booking.business.bean.VehicleNodelPo;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.ICarTypeCached;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.ICarTypeDao;
import cn.booking.business.dao.IIdCardTypeDao;
import cn.booking.business.dao.IUseNatureDao;
import cn.booking.business.dao.IVehicleNodelDao;
import cn.booking.business.service.IBookingBusinessService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.sdk.webservice.WebServiceClient;

@SuppressWarnings(value = "all")
@Service("bookingBusinessService")
public class IBookingBusinessServiceImpl implements IBookingBusinessService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IBookingBusinessCachedImpl iBookingBusinessCached;
	@Autowired
	private ICarTypeDao iCarTypeDao;
	@Autowired
	private IIdCardTypeDao idCardTypeDao;
	@Autowired
	private IUseNatureDao useNatureDao;
	@Autowired
	private IVehicleNodelDao iVehicleNodelDao;
	@Autowired
	private ICarTypeDao carTypeDao;
	@Autowired
	private ICarTypeCached iCarTypeCached;
	
	public List<CarTypeVO> getCarTypes() throws Exception {
		List<CarTypeVO> carTypeVOs = null;
		String json = iCarTypeCached.getICarTypeByKey(ICacheKey.ICarTypeCached);
		if(StringUtils.isNotBlank(json)){
			carTypeVOs = JSON.parseArray(json, CarTypeVO.class);
			//异步调用第三方接口比较缓存中的数据，如果有变化则更新到数据库和缓存，没有变化则直接返回
		}else{
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
				carTypeDao.addBatch(carTypePos);
				//存redis
				iCarTypeCached.setICarType(ICacheKey.ICarTypeCached, JSON.toJSONString(carTypeVOs));
			} catch (Exception e) {
				logger.error("getCarTypes 失败",e);
				throw e;
			}
		}
		return carTypeVOs;
	}

	@Override
	public List<BusinessTypeVO> getBusinessTypes(String type, String part, String arg0, String arg1) throws Exception {
		//先查询缓存，没有则查询数据库，没有则调用接口，存 数据库，存缓存
		
		List<BusinessTypeVO> businessTypeVOs = null;
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

	@Override
	public List<IdTypeVO> getIdTypes(String businessTypeId, String arg0, String arg1) throws Exception {
		String jkId = "JK06";
		List<IdTypeVO> idTypeVos = null;
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

	@Override
	public OrgVO findOrgByOrgId(String orgId) throws Exception {
		String jkId = "JK08";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orgId", orgId);
		OrgVO orgVO = new OrgVO();
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			StringBuffer str=new StringBuffer();
			str.append("<root>");
			str.append("<orgId>").append(orgId).append("</orgId>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			String result = jsonObject.getString("result");
			if ("00".equals(code)) {
				orgVO = JSON.parseObject(result, OrgVO.class);
				orgVO.setCode("0000");
			} else {
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
	public List<OrgVO> getOrgsByBusinessTypeId(String btId, String arg0, String arg1) throws Exception {
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

	@Override
	public BaseBean getAppTimes(String date, String orgId, String businessTypeId, String carTypeId,
			String optlittleCar) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		BaseBean bean=new BaseBean();
		map.put("date", date);
		map.put("orgId", orgId);
		map.put("businessTypeId", businessTypeId);
		map.put("carTypeId", carTypeId);
		map.put("optlittleCar", null == optlittleCar ? "" : optlittleCar);
		List<AppTimeHelper> dateTime = new ArrayList<AppTimeHelper>();
		String jkId = "JK10";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			StringBuffer str=new StringBuffer("<root>");
			str.append("<date>").append(date).append("</date>");
			str.append("<orgId>").append(orgId).append("</orgId>");
			str.append("<businessTypeId>").append(businessTypeId).append("</businessTypeId>");
			str.append("<carTypeId>").append(carTypeId).append("</carTypeId>");
			str.append("<optlittleCar>").append(optlittleCar).append("</optlittleCar>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);

			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			try{
				JSONObject result = jsonObject.getJSONObject("result");
				if ("00".equals(code)) {
					String data1=result.getString("com.rich.admin.entity.appointment.AppTimeHelper");
					dateTime = JSON.parseArray(data1, AppTimeHelper.class);
					String date1 = result.getString("com.rich.admin.entity.appointment.AppTimeHelper");
					dateTime = JSON.parseArray(date1, AppTimeHelper.class);
					bean.setData(dateTime);
					
				} 
			}catch (Exception e) {
				String result = jsonObject.getString("result");
				bean.setData(result);
				logger.error("获取预约时间错误："+result);	
			}
			bean.setCode(code);
			bean.setMsg(msg);
		} catch (Exception e) {
			logger.error("getAppointmentDate 失败 ， map = " + map);
			throw e;
		}
		return bean;
	}

	@Override
	public List<String> getAppointmentDate(String orgId, String businessTypeId, String arg0, String arg1)
			throws Exception {
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

	/**
	 * 发送短信验证码 
	 * Description TODO(发送短信验证码)
	 * @param mobile 获取短信验证码的手机号
	 * @param idType 本次预约所填写的证件种类ID,由于六年免检没有证件种类，请传”0”
	 * @param lx 1:驾驶证业务  2:机动车业务（六年免检业务传3）  3:其他（包括六年免检业务）
	 * @param ip 用户客户端IP
	 * @param bookerType ‘0’非代办（或本人） ‘1’普通代办 ‘2’专业代办（企业代办）
	 * @param bookerName 代办人姓名： 0’非代办（或本人）情况请传本次预约业务填写的姓名
	 * @param bookerIdNumber 代办人身份证号码： 0’非代办（或本人）情况请传本次预约业务填写的证件号码
	 * @param idNumber 本次预约业务填写的证件号码
	 * @param codes 本次预约的具体业务类型（例如：JD01）
	 * @return
	 * @throws Exception
	 */
	public SmsInfoVO simpleSendMessage(String mobile, String idType, String lx, String ip, String bookerType,
			String bookerName, String bookerIdNumber, String idNumber, String codes) throws Exception {
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
		logger.debug("发送短信验证码, map = " + map);
		SmsInfoVO smsInfoVO = null;
		String jkId = "JK26";
		JSONObject jsonObject = new JSONObject();
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			StringBuffer str=new StringBuffer("<root>");
			str.append("<mobile>").append(mobile).append("</mobile>");
			str.append("<idTypeId>").append(idType).append("</idTypeId>");
			str.append("<lx>").append(lx).append("</lx>");
			str.append("<ip>").append(ip).append("</ip>");
			str.append("<bookerType>").append(bookerType).append("</bookerType>");
			str.append("<bookerName>").append(bookerName).append("</bookerName>");
			str.append("<bookerIdNumber>").append(bookerIdNumber).append("</bookerIdNumber>");
			str.append("<idNumber>").append(idNumber).append("</idNumber>");
			str.append("<codes>").append(codes).append("</codes>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
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

	public BaseBean createVehicleInfo(CreateVehicleInfoVo vehicleInfoVo) throws Exception {
		BaseBean refBean = new BaseBean();
		logger.debug("【预约类服务】机动车预约信息写入createVehicleInfo... 业务类型id=" + vehicleInfoVo.getBusinessTypeId());

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("orgId", vehicleInfoVo.getOrgId()); // 预约地点
		map.put("businessTypeId", vehicleInfoVo.getBusinessTypeId()); // 业务类型id
		map.put("name", vehicleInfoVo.getName()); // 姓名
		map.put("idTypeId", vehicleInfoVo.getIdTypeId()); // 证件种类id
		map.put("idNumber", vehicleInfoVo.getIdNumber()); // 证件号码
		map.put("mobile", vehicleInfoVo.getMobile()); // 手机号码
		map.put("appointmentDate", vehicleInfoVo.getAppointmentDate()); // 预约日期
		map.put("appointmentTime", vehicleInfoVo.getAppointmentTime()); // 预约时间
		map.put("carTypeId", vehicleInfoVo.getCarTypeId()); // 号牌种类
		map.put("carFrame", vehicleInfoVo.getCarFrame()); // 车架号
		map.put("platNumber", vehicleInfoVo.getPlatNumber()); // 车牌号或车架号
		map.put("bookerName", vehicleInfoVo.getBookerName()); // 预约人姓名
		map.put("bookerIdNumber", vehicleInfoVo.getBookerIdNumber()); // 预约人身份证号码
		map.put("bookerType", vehicleInfoVo.getBookerType()); // 预约方式
		map.put("rzjs",null == vehicleInfoVo.getRzjs() ? "" : vehicleInfoVo.getRzjs()); // 认证角色
		map.put("optlittleCar",null == vehicleInfoVo.getOptlittleCar() ? "" : vehicleInfoVo.getOptlittleCar()); // 车辆产地
		map.put("indexType ",null == vehicleInfoVo.getIndexType() ? "" : vehicleInfoVo.getIndexType()); // 指标类型
		map.put("indexNo ", null == vehicleInfoVo.getIndexNo() ? "" : vehicleInfoVo.getIndexNo()); // 指标号/公证号/车辆识别代号
		map.put("useCharater", null == vehicleInfoVo.getUseCharater() ? "" : vehicleInfoVo.getUseCharater()); // 使用性质
		map.put("modelName",  null == vehicleInfoVo.getModelName() ? "" : vehicleInfoVo.getModelName()); // 车辆型号
		map.put("bookerMobile", vehicleInfoVo.getBookerMobile()); // 手机号码
		map.put("msgNumber", vehicleInfoVo.getMsgNumber()); // 短信验证码
		JSONObject jsonObject = new JSONObject();
		String jkId = "JK03";
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			StringBuffer str=new StringBuffer("<root>");
			str.append("<orgId>").append(vehicleInfoVo.getOrgId()).append("</orgId>");
			str.append("<businessTypeId>").append(vehicleInfoVo.getBusinessTypeId()).append("</businessTypeId>");
			str.append("<name>").append(vehicleInfoVo.getName()).append("</name>");
			str.append("<idTypeId>").append(vehicleInfoVo.getIdTypeId()).append("</idTypeId>");
			str.append("<idNumber>").append(vehicleInfoVo.getIdNumber()).append("</idNumber>");
			str.append("<mobile>").append(vehicleInfoVo.getMobile()).append("</mobile>");
			str.append("<appointmentDate>").append(vehicleInfoVo.getAppointmentDate()).append("</appointmentDate>");
			str.append("<appointmentTime>").append(vehicleInfoVo.getAppointmentTime()).append("</appointmentTime>");
			str.append("<carTypeId>").append(vehicleInfoVo.getCarTypeId()).append("</carTypeId>");
			str.append("<carFrame>").append(vehicleInfoVo.getCarFrame()).append("</carFrame>");
			str.append("<platNumber>").append(vehicleInfoVo.getPlatNumber()).append("</platNumber>");
			str.append("<bookerName>").append(vehicleInfoVo.getBookerName()).append("</bookerName>");
			str.append("<bookerIdNumber>").append(vehicleInfoVo.getBookerIdNumber()).append("</bookerIdNumber>");
			str.append("<bookerType>").append(vehicleInfoVo.getBookerType()).append("</bookerType>");
			str.append("<rzjs>").append(vehicleInfoVo.getRzjs()).append("</rzjs>");
			str.append("<optlittleCar>").append(vehicleInfoVo.getOptlittleCar()).append("</optlittleCar>");
			str.append("<indexType>").append(vehicleInfoVo.getIndexType()).append("</indexType>");
			str.append("<indexNo>").append(vehicleInfoVo.getIndexNo()).append("</indexNo>");
			str.append("<useCharater>").append(vehicleInfoVo.getUseCharater()).append("</useCharater>");
			str.append("<modelName>").append(vehicleInfoVo.getModelName()).append("</modelName>");
			str.append("<bookerMobile>").append(vehicleInfoVo.getBookerMobile()).append("</bookerMobile>");
			str.append("<msgNumber>").append(vehicleInfoVo.getMsgNumber()).append("</msgNumber>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			String result = jsonObject.getString("result");
			refBean.setCode(code);
			refBean.setMsg(msg);
			refBean.setData(result);
			logger.debug("【预约类服务】机动车预约信息写入结果:" + jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】机动车预约信息写入异常 ， map = " + map);
			throw e;
		}
		return refBean;
	}
	

	@Override
	public BaseBean createDriveinfo(CreateDriveinfoVo createDriveinfoVo) throws Exception {
		BaseBean refBean = new BaseBean();
		logger.debug("【预约类服务】驾驶证预约信息写入createDriveinfo... 业务类型id=" + createDriveinfoVo.getBusinessTypeId());
		StringBuffer sb = new StringBuffer();
		sb.append("<root>")
		.append("<orgId>").append(createDriveinfoVo.getOrgId()).append("</orgId>")	
		.append("<businessTypeId>").append(createDriveinfoVo.getBusinessTypeId()).append("</businessTypeId>")	
		.append("<name>").append(createDriveinfoVo.getName()).append("</name>")	
		.append("<idTypeId>").append(createDriveinfoVo.getIdTypeId()).append("</idTypeId>")	
		.append("<idNumber>").append(createDriveinfoVo.getIdNumber()).append("</idNumber>")	
		.append("<mobile>").append(createDriveinfoVo.getMobile()).append("</mobile>")	
		.append("<appointmentDate>").append(createDriveinfoVo.getAppointmentDate()).append("</appointmentDate>")	
		.append("<appointmentTime>").append(createDriveinfoVo.getAppointmentTime()).append("</appointmentTime>")	
		.append("<bookerName>").append(createDriveinfoVo.getBookerName()).append("</bookerName>")	
		.append("<bookerIdNumber>").append(createDriveinfoVo.getBookerIdNumber()).append("</bookerIdNumber>")	
		.append("<bookerType>").append(createDriveinfoVo.getBookerType()).append("</bookerType>")	
		.append("<bookerMobile>").append(createDriveinfoVo.getBookerMobile()).append("</bookerMobile>")	
		.append("<msgNumber>").append(createDriveinfoVo.getMsgNumber()).append("</msgNumber>")
		.append("</root>");
		logger.debug("【预约类服务】驾驶证预约信息, createDriveinfoVo = " + createDriveinfoVo);
		JSONObject jsonObject = new JSONObject();
		String jkId = "JK01";
		String method = "createDriveinfo";
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, sb.toString(), account, password);
			refBean.setCode(jsonObject.getString("code"));
			refBean.setMsg(jsonObject.getString("msg"));
			refBean.setData(jsonObject.getString("result"));
			logger.debug("【预约类服务】驾驶证预约信息写入结果:" + jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约信息写入异常 ， createDriveinfoVo = " + createDriveinfoVo);
			throw e;
		}
		return refBean;
	}
	public SmsInfoVO cancel(String businessType, String bookNumber, String mobile) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<root>")
		.append("<businessType>").append(businessType).append("</businessType>")	
		.append("<bookNumber>").append(bookNumber).append("</bookNumber>")	
		.append("<mobile>").append(mobile).append("</mobile>")	
		.append("</root>");
		SmsInfoVO smsInfoVO = null;
		String method = "cancel";
		JSONObject jsonObject = new JSONObject();
		String jkId = "JK11";
		try {
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, sb.toString(), account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
            String  result = jsonObject.getString("result");
			smsInfoVO = JSON.parseObject(JSON.toJSONString(jsonObject), SmsInfoVO.class);
		} catch (Exception e) {
			logger.error("cancel 失败 ， businessType = " + businessType + "bookNumber = " + bookNumber + "mobile = " +mobile );
			throw e;
		}
		return smsInfoVO;
	}
	
	/**
	 * 获取驾驶证预约信息
	 * @Description: TODO(获取驾驶证预约信息)
	 * @param bookerNumber 预约号  必填
	 * @param idNumber 证件号码  必填
	 * @param businessTypeId 业务类型ID
	 * @param organizationId 预约单位ID
	 * @return
	 * @throws Exception
	 */
	public BaseBean getDriveInfo(String bookerNumber, String idNumber, String businessTypeId, String organizationId)
			throws Exception {
		logger.debug("【预约类服务】获取驾驶证预约信息getDriveInfo...");
		BaseBean baseBean = new BaseBean();
		String jkId = "JK24";	//接口编号
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("<root>")
			.append("<bookNumber>").append(bookerNumber).append("</bookNumber>")				//预约人
			.append("<idNumber>").append(idNumber).append("</idNumber>")						//证件号码
			.append("<businessTypeId>").append(businessTypeId).append("</businessTypeId>")		//业务类型ID*
			.append("<organizationId>").append(organizationId).append("</organizationId>")		//预约单位ID*
			.append("</root>");
			String data = sb.toString();
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			logger.debug("【预约类服务】获取驾驶证预约信息请求报文xml， data = " + data);
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, data, account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			Object obj = jsonObject.get("result");
			if ("00".equals(code)) {
				if(obj instanceof JSONObject && obj != null){
					JSONObject result = (JSONObject) obj;
					baseBean.setCode(MsgCode.success);
					baseBean.setData(JSON.parseObject(result.getString("DriveInfoVO"), DriveInfoVO.class));
				}else{
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("未查询到记录");

				}
			}else {
				baseBean.setCode(code);
				baseBean.setMsg(msg);
			}
			logger.debug("【预约类服务】获取驾驶证预约信息结果:" + jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】获取驾驶证预约信息异常， sb = " + sb.toString());
			throw e;
		}
		return baseBean;
	}

	/**
	 * 获取机动车预约信息
	 * @Description: TODO(获取机动车预约信息)
	 * @param bookerNumber 预约号  必填
	 * @param idNumber 证件号码  必填
	 * @param platNumber 车牌号
	 * @param businessTypeId 业务类型ID
	 * @param organizationId 预约单位ID
	 * @return
	 * @throws Exception
	 */
	public BaseBean getVehicleInfo(String bookerNumber, String idNumber, String platNumber, String businessTypeId,
			String organizationId) throws Exception {
		logger.debug("【预约类服务】获取机动车预约信息getVehicleInfo...");
		BaseBean baseBean = new BaseBean();
		String jkId = "JK23";	//接口编号

		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<root>")
			.append("<bookNumber>").append(bookerNumber).append("</bookNumber>")				//预约人
			.append("<idNumber>").append(idNumber).append("</idNumber>")						//证件号码
			.append("<platNumber>").append(platNumber).append("</platNumber>")					//车牌号*
			.append("<businessTypeId>").append(businessTypeId).append("</businessTypeId>")		//业务类型ID*
			.append("<organizationId>").append(organizationId).append("</organizationId>")		//预约单位ID*
			.append("</root>");
			String data = sb.toString();
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			logger.debug("【预约类服务】获取机动车预约信息请求报文xml， data = " + data);
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, data, account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			Object obj = jsonObject.get("result");
			if ("00".equals(code)) {
				if(obj instanceof JSONObject && obj != null){
					JSONObject result = (JSONObject) obj;
					baseBean.setCode(MsgCode.success);
					baseBean.setData(JSON.parseObject(result.getString("VehicleInfoVO"), VehicleInfoVO.class));
				}else{
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("未查询到记录");
				}
			}else {
				baseBean.setCode(code);
				baseBean.setMsg(msg);
			}
			logger.debug("【预约类服务】获取机动车预约信息结果:" + jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】获取机动车预约信息异常， sb = " + sb.toString());
			throw e;
		}
		return baseBean;

	}
	
	/**
	 * 核发临牌
	 * @Description: TODO(核发临牌)
	 * @param vo 核发临牌Vo
	 * @return
	 * @throws Exception
	 */
	public BaseBean createTemporaryLicenseVehicleInfo(CreateTemporaryLicenseVehicleInfoVo vo) throws Exception {
		logger.debug("【预约类服务】核发临牌预约信息写入createTemporaryLicenseVehicleInfo... 业务类型ID = " + vo.getBusinessTypeId());
		BaseBean baseBean = new BaseBean();
		String jkId = "JK16";	//接口编号
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<root>")
			.append("<orgId>").append(vo.getOrgId()).append("</orgId>")									//预约地点ID
			.append("<businessTypeId>").append(vo.getBusinessTypeId()).append("</businessTypeId>")		//业务类型ID
			.append("<name>").append(vo.getName()).append("</name>")									//姓名
			.append("<idTypeId>").append(vo.getIdTypeId()).append("</idTypeId>")						//证件种类ID
			.append("<idNumber>").append(vo.getIdNumber()).append("</idNumber>")						//证件号码
			.append("<mobile>").append(vo.getMobile()).append("</mobile>")								//手机号码                                                                                                                
			.append("<address>").append(vo.getAdress()).append("</address>")							//居住地址
			.append("<appointmentDate>").append(vo.getAppointmentDate()).append("</appointmentDate>")	//预约日期
			.append("<appointmentTime>").append(vo.getAppointmentTime()).append("</appointmentTime>")	//预约时间
			.append("<carTypeId>").append(vo.getCarTypeId()).append("</carTypeId>")						//车辆类型ID
			.append("<carFrame>").append(vo.getCarFrame()).append("</carFrame>")						//车架号
			.append("<platNumber>").append(vo.getPlatNumber()).append("</platNumber>")					//车牌号或车架号(机动车注册，核发临牌，机动车转移登记（市内过户），机动车变更登记（夫妻变更）这些业务由于没有车牌号码，就传车架号)
			.append("<chineseBrand>").append(vo.getChineseBrand()).append("</chineseBrand>")			//中文品牌
			.append("<vehicleType>").append(vo.getVehicleType()).append("</vehicleType>")				//车辆型号
			.append("<passengerNumber>").append(vo.getPassengerNumber()).append("</passengerNumber>")	//载客人数(请确保载客人数是数值的字符串：“整数值”)
			.append("<engineNumber>").append(vo.getEngineNumber()).append("</engineNumber>")			//发动机号
			.append("<bookerName>").append(vo.getBookerName()).append("</bookerName>")					//预约人*
			.append("<bookerIdNumber>").append(vo.getBookerIdNumber()).append("</bookerIdNumber>")		//预约人身份证号码*
			.append("<bookerType>").append(vo.getBookerType()).append("</bookerType>")					//预约方式*(‘0’本人)
			.append("<rzjs>").append(vo.getRzjs()).append("</rzjs>")									//认证角色*(‘2’企业星级用户，其他，非企业星级用户)
			.append("<bookerMobile>").append(vo.getBookerMobile()).append("</bookerMobile>")			//手机号
			.append("<msgNumber>").append(vo.getMsgNumber()).append("</msgNumber>")						//短信验证码
			.append("</root>");
			String data = sb.toString();
			String url = iBookingBusinessCached.getStcUrl();
			String account = iBookingBusinessCached.getCgsaccount();
			String password = iBookingBusinessCached.getCgspassword();
			logger.debug("【预约类服务】核发临牌预约信息请求报文xml， data = " + data);
			//jsonObject = WebServiceClient.vehicleAdministrationWebService(url, method, map);
			JSONObject jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, data, account, password);
			
			String code = jsonObject.getString("code");
			if("00".equals(code)){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(jsonObject.getString("msg"));
				baseBean.setData(jsonObject.getString("result"));
			}else if("01".equals(code) || "02".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(jsonObject.getString("msg"));
				baseBean.setData(jsonObject.getString("result"));
			}else if("03".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(jsonObject.getString("result"));
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(jsonObject.getString("msg"));
				baseBean.setData(jsonObject.getString("result"));
			}
			logger.debug("【预约类服务】核发临牌预约信息写入结果:" + jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】核发临牌预约信息写入异常 ， vo = " + vo);
			throw e;
		}
		return baseBean;
	}

	
	
	@Override
	public int addBatchCarType(List<CarTypePo> carTypePos) throws Exception {
		return carTypeDao.addBatch(carTypePos);
	}

	@Override
	public int addBatchIdCardType(List<IdCardTypePo> idCardTypePos) throws Exception {
		return idCardTypeDao.addBatch(idCardTypePos);
	}

	@Override
	public int addBatchUseNature(List<UseNaturePo> useNaturePos) throws Exception {
		return useNatureDao.addBatch(useNaturePos);
	}

	@Override
	public int addBatchVehicleNodel(List<VehicleNodelPo> vehicleNodelPos) throws Exception {
		return iVehicleNodelDao.addBatch(vehicleNodelPos);
	}

	@Override
	public int deleteAllCarType() throws Exception {
		return carTypeDao.deleteAll();
	}

	@Override
	public int deleteAllIdCardType() throws Exception {
		return idCardTypeDao.deleteAll();
	}

	@Override
	public int deleteAllUseNature() throws Exception {
		return useNatureDao.deleteAll();
	}

	@Override
	public int deleteAllVehicleNodel() throws Exception {
		return iVehicleNodelDao.deleteAll();
	}

	@Override
	public List<CarTypePo> getAllCarType() throws Exception {
		return carTypeDao.getAll();
	}

	@Override
	public List<IdCardTypePo> getAllIdCardType() throws Exception {
		return idCardTypeDao.getAll();
	}

	@Override
	public List<UseNaturePo> getAllUseNature() throws Exception {
		return useNatureDao.getAll();
	}

	@Override
	public List<VehicleNodelPo> getAllVehicleNodel() throws Exception {
		return iVehicleNodelDao.getAll();
	}
	
}
