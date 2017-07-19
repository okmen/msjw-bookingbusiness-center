package cn.booking.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.booking.business.bean.AppTimeHelper;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateDriveinfoVo;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.DriveInfoVO;
import cn.booking.business.bean.IdCardTypePo;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.bean.UseNaturePo;
import cn.booking.business.bean.VehicleInfoVO;
import cn.booking.business.bean.VehicleNodelPo;
import cn.booking.business.service.IBookingBusinessService;
import cn.sdk.bean.BaseBean;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class BookingBusinessServiceTest extends TestCase {
	@Autowired
    @Qualifier("bookingBusinessService")
    private IBookingBusinessService iBookingBusinessService;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addBatchCarType() throws Exception{
		List<CarTypePo> carTypePos = new ArrayList<CarTypePo>();
		CarTypePo carTypePo1 = new CarTypePo();
		carTypePo1.setCarTypeId("111");
		carTypePo1.setCode("222");
		carTypePo1.setCreateDate(new Date());
		carTypePo1.setDescription("33333333");
		carTypePo1.setName("444444");
		
		CarTypePo carTypePo2 = new CarTypePo();
		carTypePo2.setCarTypeId("111");
		carTypePo2.setCode("222");
		carTypePo2.setCreateDate(new Date());
		carTypePo2.setDescription("33333333");
		carTypePo2.setName("444444");
		carTypePos.add(carTypePo1);
		carTypePos.add(carTypePo2);
		iBookingBusinessService.addBatchCarType(carTypePos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllCarType() throws Exception{
		List<CarTypePo> carTypePos = iBookingBusinessService.getAllCarType();
		System.out.println(carTypePos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteAllCarType() throws Exception{
		int result = iBookingBusinessService.deleteAllCarType();
		System.out.println(result);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addBatchIdCardType() throws Exception{
		List<IdCardTypePo> idCardTypePos = new ArrayList<IdCardTypePo>();
		IdCardTypePo idCardTypePo1 = new IdCardTypePo();
		idCardTypePo1.setIdcardTypeId("33333333333");
		idCardTypePo1.setCode("222");
		idCardTypePo1.setCreateDate(new Date());
		idCardTypePo1.setDescription("33333333");
		idCardTypePo1.setName("444444");
		
		IdCardTypePo idCardTypePo2 = new IdCardTypePo();
		idCardTypePo2.setIdcardTypeId("333333333331111");
		idCardTypePo2.setCode("222111111");
		idCardTypePo2.setCreateDate(new Date());
		idCardTypePo2.setDescription("333333331111");
		idCardTypePo2.setName("4444441111");
		idCardTypePos.add(idCardTypePo1);
		idCardTypePos.add(idCardTypePo2);
		
		iBookingBusinessService.addBatchIdCardType(idCardTypePos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllIdCardType() throws Exception{
		List<CarTypePo> carTypePos = iBookingBusinessService.getAllCarType();
		System.out.println(carTypePos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteAllIdCardType() throws Exception{
		int result = iBookingBusinessService.deleteAllCarType();
		System.out.println(result);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addBatchUseNature() throws Exception{
		List<UseNaturePo> useNaturePos = new ArrayList<UseNaturePo>();
		UseNaturePo useNaturePo1 = new UseNaturePo();
		useNaturePo1.setCode("1111111111");
		useNaturePo1.setCreateDate(new Date());
		useNaturePo1.setId(22l);
		useNaturePo1.setName("333333333333");
		
		UseNaturePo useNaturePo2 = new UseNaturePo();
		useNaturePo1.setCode("222222222");
		useNaturePo1.setCreateDate(new Date());
		useNaturePo1.setId(33l);
		useNaturePo1.setName("44444444");
		useNaturePos.add(useNaturePo1);
		useNaturePos.add(useNaturePo2);
		iBookingBusinessService.addBatchUseNature(useNaturePos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllUseNature() throws Exception{
		List<CarTypePo> carTypePos = iBookingBusinessService.getAllCarType();
		System.out.println(carTypePos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteAllUseNature() throws Exception{
		int result = iBookingBusinessService.deleteAllUseNature();
		System.out.println(result);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addBatchVehicleNodel() throws Exception{
		List<VehicleNodelPo> vehicleNodelPos = new ArrayList<VehicleNodelPo>();
		VehicleNodelPo vehicleNodelPo1 = new VehicleNodelPo();
		vehicleNodelPo1.setCode("11111111111");
		vehicleNodelPo1.setCreateDate(new Date()); 
		vehicleNodelPo1.setId(11L);
		vehicleNodelPo1.setName("2222222222222");
		
		VehicleNodelPo vehicleNodelPo2 = new VehicleNodelPo();
		vehicleNodelPo2.setCode("11111111111");
		vehicleNodelPo2.setCreateDate(new Date()); 
		vehicleNodelPo2.setId(11L);
		vehicleNodelPo2.setName("2222222222222");
		vehicleNodelPos.add(vehicleNodelPo1);
		vehicleNodelPos.add(vehicleNodelPo2);
		iBookingBusinessService.addBatchVehicleNodel(vehicleNodelPos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllVehicleNodel() throws Exception{
		List<VehicleNodelPo> vehicleNodelPos = iBookingBusinessService.getAllVehicleNodel();
		System.out.println(vehicleNodelPos);
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteAllVehicleNodel() throws Exception{
		int result = iBookingBusinessService.deleteAllVehicleNodel();
		System.out.println(result);
	}
	
	/**
	 * 满分学习考试
	 * @throws Exception
	 */
	@Test
	public void testcreateDrivveInfo_10() throws Exception{
		CreateDriveinfoVo cv = new CreateDriveinfoVo();
		cv.setOrgId("e4e48584399473d20139947d9ee82b2a");
		cv.setBusinessTypeId("402882823d681283013d6cfcb345000a");
		cv.setName("测试");
		cv.setIdTypeId("e4e48584399473d20139947f125e2b2c");
		cv.setMobile("13627267056");
		cv.setIdNumber("42138119910422133X");
		cv.setBookerMobile("13627267056");
		cv.setMsgNumber("702679");
		cv.setAppointmentDate("2017-07-26");
		cv.setAppointmentTime("09:00-12:00");
		cv.setBookerName("");
		cv.setBookerIdNumber("");
		cv.setBookerType("");
		BaseBean bean = iBookingBusinessService.createDriveinfo(cv);
		System.out.println(bean);
	}
	
	/**
	 * 持境外驾驶证申请换证
	 * @throws Exception
	 */
	@Test
	public void testcreateDrivveInfo_17() throws Exception{
		CreateDriveinfoVo cv = new CreateDriveinfoVo();
		cv.setOrgId("e4e48584399473d20139947d9ee82b2a");
		cv.setBusinessTypeId("402882824700cd3301470fac3bd403d3");
		cv.setName("测试");
		cv.setIdTypeId("e4e48584399473d20139947f125e2b2c");
		cv.setMobile("13627267056");
		cv.setIdNumber("42138119910422133X");
		cv.setBookerMobile("13627267056");
		cv.setMsgNumber("800737");
		cv.setAppointmentDate("2017-07-26");
		cv.setAppointmentTime("09:00-12:00");
		cv.setBookerName("");
		cv.setBookerIdNumber("");
		cv.setBookerType("");
		BaseBean bean = iBookingBusinessService.createDriveinfo(cv);
		System.out.println(bean);
	}
	/**
	 * 其他业务（驾驶证）
	 * @throws Exception
	 */
	@Test
	public void testcreateDrivveInfo_20() throws Exception{
		CreateDriveinfoVo cv = new CreateDriveinfoVo();
		cv.setOrgId("e4e48584399473d20139947d9ee82b2a");
		cv.setBusinessTypeId("402882824747f258014754a501281430");
		cv.setName("测试");
		cv.setIdTypeId("e4e48584399473d20139947f125e2b2c");
		cv.setMobile("13627267056");
		cv.setIdNumber("42138119910422133X");
		cv.setBookerMobile("13627267056");
		cv.setMsgNumber("811756");
		cv.setAppointmentDate("2017-07-26");
		cv.setAppointmentTime("09:00-12:00");
		cv.setBookerName("");
		cv.setBookerIdNumber("");
		cv.setBookerType("");
		BaseBean bean = iBookingBusinessService.createDriveinfo(cv);
		System.out.println(bean);
	}
	/**
	 * 恢复驾驶资格（逾期一年以上未换证类
	 * @throws Exception
	 */
	@Test
	public void testcreateDrivveInfo_21() throws Exception{
		CreateDriveinfoVo cv = new CreateDriveinfoVo();
		cv.setOrgId("e4e48584399473d20139947d9ee82b2a");
		cv.setBusinessTypeId("4028823f53f433930153f47d98160c69");
		cv.setName("测试");
		cv.setIdTypeId("e4e48584399473d20139947f125e2b2c");
		cv.setMobile("13627267056");
		cv.setIdNumber("42138119910422133X");
		cv.setBookerMobile("13627267056");
		cv.setMsgNumber("937037");
		cv.setAppointmentDate("2017-07-26");
		cv.setAppointmentTime("09:00-12:00");
		cv.setBookerName("");
		cv.setBookerIdNumber("");
		cv.setBookerType("");
		BaseBean bean = iBookingBusinessService.createDriveinfo(cv);
		System.out.println(bean);
	}
	/**
	 * 恢复驾驶资格（逾期一年以上未体检类
	 * @throws Exception
	 */
	@Test
	public void testcreateDrivveInfo_22() throws Exception{
		CreateDriveinfoVo cv = new CreateDriveinfoVo();
		cv.setOrgId("e4e48584399473d20139947d9ee82b2a");
		cv.setBusinessTypeId("4028823f53f433930153f47e34160c89");
		cv.setName("测试");
		cv.setIdTypeId("e4e48584399473d20139947f125e2b2c");
		cv.setMobile("13627267056");
		cv.setIdNumber("42138119910422133X");
		cv.setBookerMobile("13627267056");
		cv.setMsgNumber("796318");
		cv.setAppointmentDate("2017-07-26");
		cv.setAppointmentTime("09:00-12:00");
		cv.setBookerName("");
		cv.setBookerIdNumber("");
		cv.setBookerType("");
		BaseBean bean = iBookingBusinessService.createDriveinfo(cv);
		System.out.println(bean);
	}
	/**
	 * 持军队、武装警察部队机动车驾驶证申领
	 * @throws Exception
	 */
	@Test
	public void testcreateDrivveInfo_11() throws Exception{
		CreateDriveinfoVo cv = new CreateDriveinfoVo();
		cv.setOrgId("e4e48584399473d20139947d9ee82b2a");
		cv.setBusinessTypeId("40288282459835d60145c5001325096a");
		cv.setName("测试");
		cv.setIdTypeId("e4e48584399473d20139947f125e2b2c");
		cv.setMobile("13627267056");
		cv.setIdNumber("42138119910422133X");
		cv.setBookerMobile("13627267056");
		cv.setMsgNumber("182824");
		cv.setAppointmentDate("2017-07-26");
		cv.setAppointmentTime("09:00-12:00");
		cv.setBookerName("");
		cv.setBookerIdNumber("");
		cv.setBookerType("");
		BaseBean bean = iBookingBusinessService.createDriveinfo(cv);
		System.out.println(bean);
	}
	@Test
	public void getCarTypes() throws Exception {
		List<CarTypeVO> carTypeVOs = iBookingBusinessService.getCarTypes();
		System.out.println(carTypeVOs);
	}
	
	@Test
	public void getBusinessTypes() throws Exception {
		List<BusinessTypeVO> businessTypeVOs = iBookingBusinessService.getBusinessTypes("1","","","");
		for (BusinessTypeVO businessTypeVO : businessTypeVOs) {
			System.out.println(businessTypeVO.getCode()+"---");
		}
		
		System.out.println(businessTypeVOs);
	}
	
	@Test
	public void getOrgsByBusinessTypeIdTest() throws Exception {
		String btId = "4028828239a4a4c60139a4fb36ef0007";
		String arg0 = null;
		String arg1 = null;
		List<OrgVO> orgsByBusinessTypeId = iBookingBusinessService.getOrgsByBusinessTypeId(btId, arg0, arg1);
		System.out.println(orgsByBusinessTypeId);
	}
	
	@Test
	public void findOrgByOrgId() throws Exception{
		String orgId = "e4e48584399473d201399b0c4ad62b39";
		OrgVO orgVO = iBookingBusinessService.findOrgByOrgId(orgId);
		System.out.println(orgVO);
	}
	
	@Test
	public void getAppointmentDatethrows() throws Exception{
		String orgId = "4028823f54bc6d150154c1600b1055dc";
		String businessTypeId = "402882824747f258014754a501281430";
		List<String> strings = iBookingBusinessService.getAppointmentDate(orgId, businessTypeId, "", "");
		System.out.println(strings);
	}
	@Test
	public void getAppTimes() throws Exception{
		String orgId = "4028823f54bc6d150154c1600b1055dc";
		String businessTypeId = "402882824747f258014754a501281430";
		BaseBean bean = iBookingBusinessService.getAppTimes("2017-07-18", orgId, businessTypeId, "", "");
		System.out.println(bean.getData());
	}
	
	@Test
	public void getIdTypes() throws Exception {
			iBookingBusinessService.getIdTypes("402882824747f258014754a501281430", "", "");
	}
	@Test
	public void simpleSendMessage() throws Exception{
		SmsInfoVO smsInfoVO = iBookingBusinessService.simpleSendMessage("13627267056", 
				"e4e48584399473d20139947f125e2b2c", "1", "192.168.1.247", "0", "测试", "42138119910422133X", "42138119910422133X", "ZJ10");
		System.out.println(smsInfoVO);
	}
	@Test
	//换领机动车登记证书
	public void testCreateVehicleInfo_JD06reateVehicleInfo_JD06() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d201399b0c4ad62b39");  //预约地点Id
		vo.setBusinessTypeId("4028828239a4a4c60139a4fb36ef0007");  //业务类型id
		vo.setName("测试"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("622822198502074112"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-07-24");  //预约日期
		vo.setAppointmentTime("12:00-17:00");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
		vo.setCarFrame("5563"); 	//车架号
		vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
		vo.setBookerName("测试");  //预约人姓名
		vo.setBookerIdNumber("622822198502074112"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar(""); 	//车辆产地
		vo.setIndexType(""); 	//指标类型
		vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("123"); 	//使用性质
		vo.setModelName("DH");  //车辆型号
		vo.setBookerMobile("17688758320"); 	//手机号码
		vo.setMsgNumber("464032");	 	//短信验证码
		vo.setRzjs("11");
		BaseBean bean = iBookingBusinessService.createVehicleInfo(vo);
		System.out.println(bean.toJson());
	}
	
	//补换机动车号牌
	@Test
	public void testCreateVehicleInfo_JD02() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d201399b0818122b37");  //预约地点Id
		vo.setBusinessTypeId("e4e48584399473d201399483d85a2b31");  //业务类型id
		vo.setName("测试"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("622822198502074113"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-07-24");  //预约日期
		vo.setAppointmentTime("12:00-17:00");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
		vo.setCarFrame("5563"); 	//车架号
		vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
		vo.setBookerName("测试");  //预约人姓名
		vo.setBookerIdNumber("622822198502074113"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar(""); 	//车辆产地
		vo.setIndexType(""); 	//指标类型
		vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("123"); 	//使用性质
		vo.setModelName("DH");  //车辆型号
		vo.setBookerMobile("17688758320"); 	//手机号码
		vo.setMsgNumber("464032");	 	//短信验证码
		vo.setRzjs("");
		BaseBean bean = iBookingBusinessService.createVehicleInfo(vo);
		System.out.println(bean.toJson());
	}
	
	//申领、补领机动车登记证书
	@Test
	public void testCreateVehicleInfo_JD13() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d20139947d9ee82b2a");  //预约地点Id
		vo.setBusinessTypeId("4028828244914e4401454fe33d2c3216");  //业务类型id
		vo.setName("测试"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("622822198502074114"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-07-15");  //预约日期
		vo.setAppointmentTime("09:00-12:00");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
		vo.setCarFrame("5563"); 	//车架号
		vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
		vo.setBookerName("测试");  //预约人姓名
		vo.setBookerIdNumber("622822198502074114"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar(""); 	//车辆产地
		vo.setIndexType(""); 	//指标类型
		vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("123"); 	//使用性质
		vo.setModelName("DH");  //车辆型号
		vo.setBookerMobile("17688758320"); 	//手机号码
		vo.setMsgNumber("464032");	 	//短信验证码
		vo.setRzjs("");
		BaseBean bean = iBookingBusinessService.createVehicleInfo(vo);
		System.out.println(bean.toJson());
	}
	
	//补换领机动车行驶证
	@Test
	public void testCreateVehicleInfo_JD01() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d201399b1173262b40");  //预约地点Id
		vo.setBusinessTypeId("e4e48584399473d201399482cac32b30");  //业务类型id
		vo.setName("测试"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("622822198502074114"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-07-15");  //预约日期
		vo.setAppointmentTime("09:00-12:00");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
		vo.setCarFrame("5563"); 	//车架号
		vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
		vo.setBookerName("测试");  //预约人姓名
		vo.setBookerIdNumber("622822198502074114"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar(""); 	//车辆产地
		vo.setIndexType(""); 	//指标类型
		vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("123"); 	//使用性质
		vo.setModelName("DH");  //车辆型号
		vo.setBookerMobile("17688758320"); 	//手机号码
		vo.setMsgNumber("464032");	 	//短信验证码
		vo.setRzjs("");
		BaseBean bean = iBookingBusinessService.createVehicleInfo(vo);
		System.out.println(bean.toJson());
	}
	
	//补换检验合格标志
	@Test
	public void testCreateVehicleInfo_JD30() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d201399b0c4ad62b39");  //预约地点Id
		vo.setBusinessTypeId("402882824700cd3301470fa98b3b03ce");  //业务类型id
		vo.setName("测试"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("622822198502074115"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-07-15");  //预约日期
		vo.setAppointmentTime("09:00-12:00");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
		vo.setCarFrame("5563"); 	//车架号
		vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
		vo.setBookerName("测试");  //预约人姓名
		vo.setBookerIdNumber("622822198502074115"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar(""); 	//车辆产地
		vo.setIndexType(""); 	//指标类型
		vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("123"); 	//使用性质
		vo.setModelName("DH");  //车辆型号
		vo.setBookerMobile("17688758320"); 	//手机号码
		vo.setMsgNumber("464032");	 	//短信验证码
		vo.setRzjs("");
		BaseBean bean = iBookingBusinessService.createVehicleInfo(vo);
		System.out.println(bean.toJson());
	}
	
	//机动车变更登记（套牌车换证）
	@Test
	public void testCreateVehicleInfo_JD36() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d20139947d9ee82b2a");  //预约地点Id
		vo.setBusinessTypeId("4028823f4f73319c014f73518ad00199");  //业务类型id
		vo.setName("测试"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("622822198502074116"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-07-15");  //预约日期
		vo.setAppointmentTime("09:00-12:00");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
		vo.setCarFrame("5563"); 	//车架号
		vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
		vo.setBookerName("测试");  //预约人姓名
		vo.setBookerIdNumber("622822198502074116"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar(""); 	//车辆产地
		vo.setIndexType(""); 	//指标类型
		vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("123"); 	//使用性质
		vo.setModelName("DH");  //车辆型号
		vo.setBookerMobile("17688758320"); 	//手机号码
		vo.setMsgNumber("464032");	 	//短信验证码
		vo.setRzjs("");
		BaseBean bean = iBookingBusinessService.createVehicleInfo(vo);
		System.out.println(bean.toJson());
	}
	
	@Test
	public void cancel() throws Exception{
		SmsInfoVO smsInfoVO = iBookingBusinessService.cancel("1", "170719151135", "18682050170");
		System.out.println(smsInfoVO);
	}
	@Test
	public void getDriveInfo() throws Exception{
		BaseBean driveInfo = iBookingBusinessService.getDriveInfo("17071416520C", "42138119910422133X", "", "");
		System.out.println(driveInfo);
	}
	@Test
	public void getVehicleInfo() throws Exception{
		BaseBean vehicleInfo = iBookingBusinessService.getVehicleInfo("17071311121E", "622822198502074110", "", "","");
		System.out.println(vehicleInfo);

	}
}
