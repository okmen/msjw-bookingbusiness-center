package cn.booking.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.booking.business.bean.AppTimeHelper;
import cn.booking.business.bean.AppointmentPo;
import cn.booking.business.bean.AppointmentUnitPo;
import cn.booking.business.bean.BusinessTypePo;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateDriveinfoVo;
import cn.booking.business.bean.CreateTemporaryLicenseVehicleInfoVo;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.DriveInfoVO;
import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.IndexTypeVo;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.bean.UseCharater;
import cn.booking.business.bean.UseNaturePo;
import cn.booking.business.bean.VehicleInfoVO;
import cn.booking.business.bean.VehicleNodelPo;
import cn.booking.business.cache.AppointmentCached;
import cn.booking.business.cache.AppointmentUnitCached;
import cn.booking.business.cache.IBusinessTypeCached;
import cn.booking.business.cache.ICacheKey;
import cn.booking.business.cache.ICarTypeCached;
import cn.booking.business.cache.IIdCardTypeCached;
import cn.booking.business.cache.impl.IBookingBusinessCachedImpl;
import cn.booking.business.dao.AppointmentDao;
import cn.booking.business.dao.AppointmentUnitDao;
import cn.booking.business.dao.BusinessTypeDao;
import cn.booking.business.dao.ICarTypeDao;
import cn.booking.business.dao.IIdCardTypeDao;
import cn.booking.business.dao.IUseNatureDao;
import cn.booking.business.dao.IVehicleNodelDao;
import cn.booking.business.service.IBookingBusinessService;
import cn.booking.business.utils.TransferThirdParty;
import cn.booking.business.utils.execute.AppointmentExecute;
import cn.booking.business.utils.execute.AppointmentUnitExecute;
import cn.booking.business.utils.execute.BusinessTypeExecute;
import cn.booking.business.utils.execute.CarTypeExecute;
import cn.booking.business.utils.execute.IdCardTypeExecute;
import cn.booking.business.utils.task.AppointmentTask;
import cn.booking.business.utils.task.AppointmentUnitTask;
import cn.booking.business.utils.task.BusinessTypeTask;
import cn.booking.business.utils.task.CarTypeTask;
import cn.booking.business.utils.task.IdCardTypeTask;
import cn.sdk.bean.BaseBean;
import cn.sdk.thread.BilinThreadPool;
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
	private IIdCardTypeDao iIdCardTypeDao;
	@Autowired
	private BusinessTypeDao businessTypeDao;
	@Autowired
	private AppointmentDao appointmentDao;
	@Autowired
	private AppointmentUnitDao appointmentUnitDao;
	@Autowired
	private ICarTypeCached iCarTypeCached;
	@Autowired
	private IBusinessTypeCached iBusinessTypeCached;
	@Autowired
	private IIdCardTypeCached idCardTypeCached;
	@Autowired
	private AppointmentCached appointmentCached;
	@Autowired
	private AppointmentUnitCached appointmentUnitCached;
	@Autowired
	private BusinessTypeExecute businessTypeExecute;
	
	@Autowired
	@Qualifier("bilinThreadPool")
	private BilinThreadPool bilinThreadPool;
    
    @Autowired
    @Qualifier("idCardTypeExecute")
    private IdCardTypeExecute idCardTypeExecute;
    
    @Autowired
    @Qualifier("appointmentExecute")
    private AppointmentExecute appointmentExecute;
    
    @Autowired
    @Qualifier("appointmentUnitExecute")
    private AppointmentUnitExecute appointmentUnitExecute;
    
    @Autowired
    @Qualifier("carTypeExecute")
    private CarTypeExecute carTypeExecute;
    
	
	public List<CarTypeVO> getCarTypes() throws Exception {
		List<CarTypeVO> carTypeVOs = null;
		String json = iCarTypeCached.getICarTypeByKey(ICacheKey.ICarTypeCached);
		//异步调用第三方接口比较缓存中的数据，如果有变化则更新到数据库和缓存，没有变化则直接返回
		if(StringUtils.isNotBlank(json)){
			carTypeVOs = JSON.parseArray(json, CarTypeVO.class);
			for(CarTypeVO carTypeVO : carTypeVOs){
				carTypeVO.setId(carTypeVO.getCarTypeId());
			}
			//从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
			try {
				if(bilinThreadPool != null) {
					bilinThreadPool.execute(new CarTypeTask(carTypeExecute, carTypeVOs, iBookingBusinessCached));
				}
			}catch(Exception e){
				logger.error("存储到缓存 错误", e);
			}
		}else{
			List<CarTypeVO> carTypeVOs2 = TransferThirdParty.getCarTypes(iBookingBusinessCached);
			carTypeVOs = carTypeVOs2;
			List<CarTypePo> carTypePos = new ArrayList<CarTypePo>();
			for(CarTypeVO carTypeVO2 : carTypeVOs2){
				CarTypePo carTypePo = new CarTypePo();
				carTypePo.setCarTypeId(carTypeVO2.getId());
				carTypePo.setCode(carTypeVO2.getCode());
				carTypePo.setCreateDate(new Date());
				carTypePo.setDescription(carTypeVO2.getDescription());
				carTypePo.setName(carTypeVO2.getName());
				carTypePos.add(carTypePo);
			}
			carTypeDao.addBatch(carTypePos);
			iCarTypeCached.setICarType(ICacheKey.ICarTypeCached, JSON.toJSONString(carTypePos));
		}
		return carTypeVOs;
	}

	@Override
	public List<BusinessTypeVO> getBusinessTypes(String type, String part, String arg0, String arg1) throws Exception {
		//先查询缓存，没有则查询数据库，没有则调用接口，存 数据库，存缓存
		List<BusinessTypeVO> businessTypeVOs = new ArrayList<BusinessTypeVO>();
		String json = iBusinessTypeCached.getIBusinessTypeByKey(ICacheKey.IBusinessTypeCached + type);
		if(StringUtils.isNotBlank(json)){
			List<BusinessTypePo> businessTypePos = JSON.parseArray(json, BusinessTypePo.class);
			for(BusinessTypePo businessTypePo : businessTypePos){
				BusinessTypeVO businessTypeVO2 = new BusinessTypeVO();
				businessTypeVO2.setId(businessTypePo.getBusinessId());
				businessTypeVO2.setCode(businessTypePo.getCode());
				businessTypeVO2.setDescription(businessTypePo.getDescription());
				businessTypeVO2.setLx(businessTypePo.getLx());
				businessTypeVO2.setName(businessTypePo.getName());
				businessTypeVOs.add(businessTypeVO2);
			}
			try {
				if(bilinThreadPool != null) {
					bilinThreadPool.execute(new BusinessTypeTask(businessTypeExecute, businessTypeVOs, iBookingBusinessCached, type));
				}
			}catch(Exception e){
				logger.error("存储到缓存 错误", e);
			}
		}else{
			businessTypeVOs = TransferThirdParty.getBusinessTypes(iBookingBusinessCached, type, part, arg0, arg1);
			List<BusinessTypePo> businessTypePos = new ArrayList<BusinessTypePo>();
			for(BusinessTypeVO businessTypeVO2 : businessTypeVOs){
				BusinessTypePo businessTypePo = new BusinessTypePo();
				businessTypePo.setBusinessId(businessTypeVO2.getId());
				businessTypePo.setCode(businessTypeVO2.getCode());
				businessTypePo.setDescription(businessTypeVO2.getDescription());
				businessTypePo.setLx(businessTypeVO2.getLx());
				businessTypePo.setName(businessTypeVO2.getName());
				businessTypePos.add(businessTypePo);
			}
			businessTypeDao.addBatch(businessTypePos);
			iBusinessTypeCached.setIBusinessType(ICacheKey.IBusinessTypeCached + type, JSON.toJSONString(businessTypePos));
		}
		return businessTypeVOs;
	}

	@Override
	public List<IdTypeVO> getIdTypes(String businessTypeId, String arg0, String arg1 ,String type) throws Exception {
		List<IdTypeVO> idTypeVos = new ArrayList<>();
		List<IdTypeVO> idTypeVos2 = new ArrayList<>();
		String json = idCardTypeCached.getIIdCardTypeByKey(type + businessTypeId + ICacheKey.IIdCardTypeCached);
		if(StringUtils.isNotBlank(json)&&!("[]").equals(json)){
			idTypeVos = JSON.parseArray(json, IdTypeVO.class);
			for(IdTypeVO idTypeVO : idTypeVos){
				idTypeVO.setId(idTypeVO.getIdcardTypeId());
			}
			//从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
			try {
				if(bilinThreadPool != null) {
					bilinThreadPool.execute(new IdCardTypeTask(idCardTypeExecute, idTypeVos, iBookingBusinessCached,businessTypeId));
				}
			}catch(Exception e){
				logger.error("存储到缓存 错误", e);
			}
		}else{
			idTypeVos2 = TransferThirdParty.getIdTypes(iBookingBusinessCached, businessTypeId, arg0, arg1);
			if ("0".equals(type)) {
				List<String> list = new ArrayList<>();
				list.add("居民身份证");
				list.add("军官证");
				list.add("军官离退休证");
				list.add("外交人员身份证明");
				list.add("士兵证");
				list.add("境外人员身份证明");
				for (IdTypeVO idTypeVO : idTypeVos2) {
					if (list.contains(idTypeVO.getName())) {
						idTypeVos.add(idTypeVO);
					}
				}
			}else if("1".equals(type)){
				for (IdTypeVO idTypeVO : idTypeVos2) {
					String name = idTypeVO.getName();
					if (!"居民户口簿".equals(name)) {
						idTypeVos.add(idTypeVO);
					}
				}
			}else{
				idTypeVos = idTypeVos2;
			}
			//存mysql
			List<IdCardTypePo> idCardTypePos = new ArrayList<IdCardTypePo>();
			for(IdTypeVO idTypeVO2 : idTypeVos2){
				IdCardTypePo idCardTypePo = new IdCardTypePo();
				idCardTypePo.setCode(idTypeVO2.getCode());
				idCardTypePo.setCreateDate(new Date());
				idCardTypePo.setDescription(idTypeVO2.getDescription());
				idCardTypePo.setIdcardTypeId(idTypeVO2.getId());
				idCardTypePo.setName(idTypeVO2.getName());
				idCardTypePos.add(idCardTypePo);
			}
			iIdCardTypeDao.addBatch(idCardTypePos);
			
			List<IdCardTypePo> idCardTypePos2 = new ArrayList<IdCardTypePo>();
			for(IdTypeVO idTypeVO2 : idTypeVos){
				IdCardTypePo idCardTypePo = new IdCardTypePo();
				idCardTypePo.setCode(idTypeVO2.getCode());
				idCardTypePo.setCreateDate(new Date());
				idCardTypePo.setDescription(idTypeVO2.getDescription());
				idCardTypePo.setIdcardTypeId(idTypeVO2.getId());
				idCardTypePo.setName(idTypeVO2.getName());
				idCardTypePos2.add(idCardTypePo);
			}
			idCardTypeCached.setIIdCardType(type + businessTypeId + ICacheKey.IIdCardTypeCached, JSON.toJSONString(idCardTypePos2));
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
		String key = ICacheKey.AppointmentUnitCached + btId;
		List<OrgVO> orgVOs = new ArrayList<OrgVO>();
		String json = appointmentUnitCached.getAppointmentUnitByKey(key);
		if(StringUtils.isNotBlank(json)){
			List<AppointmentUnitPo> appointmentUnitPos = JSON.parseArray(json, AppointmentUnitPo.class);
			for(AppointmentUnitPo appointmentUnitPo : appointmentUnitPos){
				OrgVO orgVO = new OrgVO();
				orgVO.setCode(appointmentUnitPo.getCode());
				orgVO.setDescription(appointmentUnitPo.getDescription());
				orgVO.setId(appointmentUnitPo.getAppointmentUnitId());
				orgVO.setName(appointmentUnitPo.getName());
				orgVO.setPointx(appointmentUnitPo.getPointx());
				orgVO.setPointy(appointmentUnitPo.getPointy());
				orgVOs.add(orgVO);
			}
			//从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
			try {
				if(bilinThreadPool != null) {
					bilinThreadPool.execute(new AppointmentUnitTask(appointmentUnitExecute, appointmentUnitPos, iBookingBusinessCached,btId));
				}
			}catch(Exception e){
				logger.error("存储到缓存 错误", e);
			}
		}else{
			orgVOs = TransferThirdParty.getOrgsByBusinessTypeId(iBookingBusinessCached, btId, arg0, arg1);
			List<AppointmentUnitPo> appointmentUnitPos = new ArrayList<AppointmentUnitPo>();
			for(OrgVO orgVO : orgVOs){
				AppointmentUnitPo appointmentUnitPo = new AppointmentUnitPo();
				appointmentUnitPo.setAppointmentUnitId(orgVO.getId());
				appointmentUnitPo.setCode(orgVO.getCode());
				appointmentUnitPo.setDescription(orgVO.getDescription());
				appointmentUnitPo.setName(orgVO.getName());
				appointmentUnitPo.setPointx(orgVO.getPointx());
				appointmentUnitPo.setPointy(orgVO.getPointy());
				appointmentUnitPos.add(appointmentUnitPo);
			}
			appointmentUnitDao.addBatch(appointmentUnitPos);
			//存redis
			appointmentUnitCached.setAppointmentUnit(key, JSON.toJSONString(appointmentUnitPos));
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
			str.append("<optlittleCar>").append(null == optlittleCar ? "" : optlittleCar).append("</optlittleCar>");
			str.append("</root>");
			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			logger.info("请求报文："+str.toString());
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
		List<String> dateStr = null;
		String key = ICacheKey.AppointmentCached + orgId + "_" + businessTypeId;
		List<AppointmentPo> appointmentPos = new ArrayList<AppointmentPo>();
		String json = appointmentCached.getAppointmentByKey(key);
		//异步调用第三方接口比较缓存中的数据，如果有变化则更新到数据库和缓存，没有变化则直接返回
		if(StringUtils.isNotBlank(json)){
			appointmentPos = JSON.parseArray(json, AppointmentPo.class);
			dateStr = new ArrayList<String>();
			for(AppointmentPo appointmentPo : appointmentPos){
				dateStr.add(appointmentPo.getAppointment());
			}
			//从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
			try {
				if(bilinThreadPool != null) {
					bilinThreadPool.execute(new AppointmentTask(appointmentExecute,appointmentPos, iBookingBusinessCached, orgId, businessTypeId));
				}
			}catch(Exception e){
				logger.error("存储到缓存 错误", e);
			}
		}else{
			List<String> strings = TransferThirdParty.getAppointmentDate(iBookingBusinessCached, orgId, businessTypeId, arg0, arg1);
			for(String string : strings){
				AppointmentPo appointmentPo = new AppointmentPo(string, new Date());
				appointmentPos.add(appointmentPo);
			}
			//存mysql
			appointmentDao.addBatch(appointmentPos);
			//存redis
			appointmentCached.setAppointment(key, JSON.toJSONString(appointmentPos));
			dateStr = strings;
		}
		return dateStr;
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
			logger.info("请求报文："+str.toString());
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
		logger.info("【预约类服务】机动车预约信息写入createVehicleInfo... 业务类型id=" + vehicleInfoVo.getBusinessTypeId());

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
			str.append("<rzjs>").append(null == vehicleInfoVo.getRzjs() ? "Show" : vehicleInfoVo.getRzjs()).append("</rzjs>");
			str.append("<optlittleCar>").append(null == vehicleInfoVo.getOptlittleCar() ? "" : vehicleInfoVo.getOptlittleCar()).append("</optlittleCar>");
			str.append("<indexType>").append(null == vehicleInfoVo.getIndexType() ? "" : vehicleInfoVo.getIndexType()).append("</indexType>");
			str.append("<indexNo>").append(null == vehicleInfoVo.getIndexNo() ? "" : vehicleInfoVo.getIndexNo()).append("</indexNo>");
			str.append("<useCharater>").append(null == vehicleInfoVo.getUseCharater() ? "" : vehicleInfoVo.getUseCharater()).append("</useCharater>");
			str.append("<modelName>").append(null == vehicleInfoVo.getModelName() ? "" : vehicleInfoVo.getModelName()).append("</modelName>");
			str.append("<bookerMobile>").append(vehicleInfoVo.getBookerMobile()).append("</bookerMobile>");
			str.append("<msgNumber>").append(vehicleInfoVo.getMsgNumber()).append("</msgNumber>");
			str.append("</root>");
			logger.info("【预约类服务】机动车预约信息请求报文xml， data = " + str);

			jsonObject = WebServiceClient.vehicleAdministrationWebServiceNew(url, jkId, str.toString(), account, password);
			String code = jsonObject.getString("code");
			String msg = jsonObject.getString("msg");
			String result = jsonObject.getString("result");
			refBean.setCode(code);
			refBean.setMsg(msg);
			refBean.setData(result);
			logger.info("【预约类服务】机动车预约信息写入结果:" + jsonObject);
		} catch (Exception e) {
			logger.error("【预约类服务】机动车预约信息写入异常 ， map = " + map);
			throw e;
		}
		return refBean;
	}
	

	@Override
	public BaseBean createDriveinfo(CreateDriveinfoVo createDriveinfoVo) throws Exception {
		BaseBean refBean = new BaseBean();
		logger.info("【预约类服务】驾驶证预约信息写入createDriveinfo... 业务类型id=" + createDriveinfoVo.getBusinessTypeId());
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
		logger.info("【预约类服务】驾驶证预约信息请求报文xml， data = " + sb.toString());
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
			logger.info("【预约类服务】驾驶证预约信息写入结果:" + jsonObject);
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
		logger.info("【预约类服务】取消预约请求报文xml， data = " + sb.toString());
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
			logger.info("【预约类服务】取消预约结果:" + smsInfoVO);
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
		logger.info("【预约类服务】获取驾驶证预约信息getDriveInfo...");
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
			logger.info("【预约类服务】获取驾驶证预约信息请求报文xml， data = " + data);
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
					baseBean.setMsg("未查询到相应记录");

				}
			}else {
				baseBean.setCode(code);
				baseBean.setMsg(msg);
			}
			logger.info("【预约类服务】获取驾驶证预约信息结果:" + jsonObject);
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
		logger.info("【预约类服务】获取机动车预约信息getVehicleInfo...");
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
			logger.info("【预约类服务】获取机动车预约信息请求报文xml， data = " + data);
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
			logger.info("【预约类服务】获取机动车预约信息结果:" + jsonObject);
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
		logger.info("【预约类服务】核发临牌预约信息写入createTemporaryLicenseVehicleInfo... 业务类型ID = " + vo.getBusinessTypeId());
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
			logger.info("【预约类服务】核发临牌预约信息请求报文xml， data = " + data);
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
			logger.info("【预约类服务】核发临牌预约信息写入结果:" + jsonObject);
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

	
	@Override
	public Map<String, String> getCarModelArray() throws Exception {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("K31", "小型普通客车");
		map.put("K32", "小型越野客车");
		map.put("K33", "小型轿车");
		map.put("K34", "小型专用客车");
		map.put("K41", "微型普通客车");
		map.put("K42", "微型越野客车");
		map.put("K43", "微型轿车");
		map.put("M11", "普通正三轮摩托车");
		map.put("M12", "轻便正三轮摩托车");
		map.put("M13", "正三轮载客摩托车");
		map.put("M14", "正三轮载货摩托车");
		map.put("M15", "侧三轮摩托车");
		map.put("M21", "普通二轮摩托车");
		map.put("M22", "轻便二轮摩托车");
		map.put("N11", "三轮汽车");
		map.put("K11", "大型普通客车");
		map.put("K12", "大型双层客车");
		map.put("K13", "大型卧铺客车");
		map.put("K14", "大型铰接客车");
		map.put("K15", "大型越野客车");
		map.put("K16", "大型轿车");
		map.put("K17", "大型专用客车");
		map.put("K21", "中型普通客车");
		map.put("K22", "中型双层客车");
		map.put("K23", "中型卧铺客车");
		map.put("K24", "中型铰接客车");
		map.put("K25", "中型越野客车");
		map.put("K26", "中型轿车");
		map.put("K27", "中型专用客车");
		map.put("B11", "重型普通半挂车");
		map.put("B12", "重型厢式半挂车");
		map.put("B13", "重型罐式半挂车");
		map.put("B14", "重型平板半挂车");
		map.put("B15", "重型集装箱半挂车");
		map.put("B16", "重型自卸半挂车");
		map.put("B17", "重型特殊结构半挂车");
		map.put("B18", "重型仓栅式半挂车");
		map.put("B19", "重型旅居半挂车");
		map.put("B1A", "重型专项作业半挂车");
		map.put("B1B", "重型低平板半挂车");
		map.put("B21", "中型普通半挂车");
		map.put("B22", "中型厢式半挂车");
		map.put("B23", "中型罐式半挂车");
		map.put("B24", "中型平板半挂车");
		map.put("B25", "中型集装箱半挂车");
		map.put("B26", "中型自卸半挂车");
		map.put("B27", "中型特殊结构半挂车");
		map.put("B28", "中型仓栅式半挂车");
		map.put("B29", "中型旅居半挂车");
		map.put("B2A", "中型专项作业半挂车");
		map.put("B2B", "中型低平板半挂车");
		map.put("B31", "轻型普通半挂车");
		map.put("B32", "轻型厢式半挂车");
		map.put("B33", "轻型罐式半挂车");
		map.put("B34", "轻型平板半挂车");
		map.put("B35", "轻型自卸半挂车");
		map.put("B36", "轻型仓栅式半挂车");
		map.put("B37", "轻型旅居半挂车");
		map.put("B38", "轻型专项作业半挂车");
		map.put("B39", "轻型低平板半挂车");
		map.put("D11", "无轨电车");
		map.put("D12", "有轨电车");
		map.put("G11", "重型普通全挂车");
		map.put("G12", "重型厢式全挂车");
		map.put("G13", "重型罐式全挂车");
		map.put("G14", "重型平板全挂车");
		map.put("G15", "重型集装箱全挂车");
		map.put("G16", "重型自卸全挂车");
		map.put("G17", "重型仓栅式全挂车");
		map.put("G18", "重型旅居全挂车");
		map.put("G19", "重型专项作业全挂车");
		map.put("G21", "中型普通全挂车");
		map.put("G22", "中型厢式全挂车");
		map.put("G23", "中型罐式全挂车");
		map.put("G24", "中型平板全挂车");
		map.put("G25", "中型集装箱全挂车");
		map.put("G26", "中型自卸全挂车");
		map.put("G27", "中型仓栅式全挂车");
		map.put("G28", "中型旅居全挂车");
		map.put("G29", "中型专项作业全挂车");
		map.put("G31", "轻型普通全挂车");
		map.put("G32", "轻型厢式全挂车");
		map.put("G33", "轻型罐式全挂车");
		map.put("G34", "轻型平板全挂车");
		map.put("G35", "轻型自卸全挂车");
		map.put("G36", "轻型仓栅式全挂车");
		map.put("G37", "轻型旅居全挂车");
		map.put("G38", "轻型专项作业全挂车");
		map.put("H11", "重型普通货车");
		map.put("H12", "重型厢式货车");
		map.put("H13", "重型封闭货车");
		map.put("H14", "重型罐式货车");
		map.put("H15", "重型平板货车");
		map.put("H16", "重型集装厢车");
		map.put("H17", "重型自卸货车");
		map.put("H18", "重型特殊结构货车");
		map.put("H19", "重型仓栅式货车");
		map.put("H21", "中型普通货车");
		map.put("H22", "中型厢式货车");
		map.put("H23", "中型封闭货车");
		map.put("H24", "中型罐式货车");
		map.put("H25", "中型平板货车");
		map.put("H26", "中型集装厢车");
		map.put("H27", "中型自卸货车");
		map.put("H28", "中型特殊结构货车");
		map.put("H29", "中型仓栅式货车");
		map.put("H31", "轻型普货车");
		map.put("H32", "轻型厢式货车");
		map.put("H33", "轻型封闭货车");
		map.put("H34", "轻型罐式货车");
		map.put("H35", "轻型平板货车");
		map.put("H37", "轻型自卸货车");
		map.put("H38", "轻型特殊结构货车");
		map.put("H39", "轻仓栅式货车");
		map.put("H41", "微型普通货车");
		map.put("H42", "微型厢式货车");
		map.put("H43", "微型封闭货车");
		map.put("H44", "微型罐式货车");
		map.put("H45", "微型自卸货车");
		map.put("H46", "微型特殊结构货车");
		map.put("H47", "微型仓栅式货车");
		map.put("H51", "普通低速货车");
		map.put("H52", "厢式低速货车");
		map.put("H53", "罐式低速货车");
		map.put("H54", "自卸低速货车");
		map.put("H55", "仓栅式低速货车");
		map.put("J11", "轮式装载机械");
		map.put("J12", "轮式挖掘机械");
		map.put("J13", "轮式平地机械");
		map.put("Q11", "重型半挂牵引车");
		map.put("Q12", "重型全挂牵引车");
		map.put("Q21", "中型半挂牵引车");
		map.put("Q22", "中型全挂牵引车");
		map.put("Q31", "轻型半挂牵引车");
		map.put("Q32", "轻型全挂牵引车");
		map.put("T11", "大型轮式拖拉机");
		map.put("T21", "小型轮式拖拉机");
		map.put("T22", "手扶拖拉机");
		map.put("T23", "手扶变形运输机");
		map.put("X99", "其它");
		map.put("Z11", "大型专项作业车");
		map.put("Z21", "中型专项作业车");
		map.put("Z31", "小型专项作业车");
		map.put("Z41", "微型专项作业车");
		map.put("Z51", "重型专项作业车");
		map.put("Z71", "轻型专项作业车");
		return map;
	}

	@Override
	public List<UseCharater> getUseCharater() throws Exception {
		List<UseCharater> list = new ArrayList<>();
		list.add(new UseCharater("A", "非运营"));
		list.add(new UseCharater("B", "公路客运"));
		list.add(new UseCharater("C", "公交客运"));
		list.add(new UseCharater("E", "旅游客运"));
		list.add(new UseCharater("F", "货运"));
		list.add(new UseCharater("G", "租赁"));
		return list;
	}

	@Override
	public List<IndexTypeVo> getIndexTypes() throws Exception {
		List<IndexTypeVo> list = new ArrayList<>();
		list.add(new IndexTypeVo("ZLZB", "增量指标"));
		list.add(new IndexTypeVo("GXZB", "更新指标"));
		list.add(new IndexTypeVo("QTZB", "其他指标"));
		list.add(new IndexTypeVo("BAZB", "备案车辆指标"));
		list.add(new IndexTypeVo("ESCLZB", "二手车辆指标"));
		list.add(new IndexTypeVo("ESCZZZB", "二手车周转指标"));
		list.add(new IndexTypeVo("WZB", "无指标"));
		return list;
	}

	@Override
	public String getTemplateSendUrl() {
		String url = iBookingBusinessCached.getTemplateSendUrl() + "?";
		logger.info("获取到的域名地址是：" + url);
		return url;
	}
	
}
