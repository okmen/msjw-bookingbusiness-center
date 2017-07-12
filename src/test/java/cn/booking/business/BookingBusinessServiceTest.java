package cn.booking.business;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.booking.business.bean.AppTimeHelper;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.service.IBookingBusinessService;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class BookingBusinessServiceTest extends TestCase {
	@Autowired
    @Qualifier("bookingBusinessService")
    private IBookingBusinessService iBookingBusinessService;
	
	@Test
	public void getCarTypes() throws Exception {
		List<CarTypeVO> carTypeVOs = iBookingBusinessService.getCarTypes();
		System.out.println(carTypeVOs);
	}
	
	@Test
	public void getBusinessTypes() throws Exception {
		List<BusinessTypeVO> businessTypeVOs = iBookingBusinessService.getBusinessTypes("0","1","","");
		System.out.println(businessTypeVOs);
	}
	
	@Test
	public void getOrgsByBusinessTypeIdTest() throws Exception {
		String btId = "1";
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
		String orgId = "e4e48584399473d201399b0c4ad62b39";
		String businessTypeId = "1";
		List<String> strings = iBookingBusinessService.getAppointmentDate(orgId, businessTypeId, "", "");
		System.out.println(strings);
	}
	@Test
	public void getAppTimes() throws Exception{
		String orgId = "e4e48584399473d201399b0c4ad62b39";
		String businessTypeId = "1";
		List<AppTimeHelper> appTimeHelpers = iBookingBusinessService.getAppTimes("2017-07-25", orgId, businessTypeId, "", "");
		System.out.println(appTimeHelpers);
	}
	
	@Test
	public void getIdTypes() throws Exception {
			iBookingBusinessService.getIdTypes("402882824747f258014754a501281430", "", "");
	}
	@Test
	public void simpleSendMessage() throws Exception{
		SmsInfoVO smsInfoVO = iBookingBusinessService.simpleSendMessage("13652311206", "0", "1", "", "1", "测试", "622822198502074110", "622822198502074110", "JD30");
		System.out.println(smsInfoVO);
	}
	
	//换领机动车登记证书
	@Test
	public void testCreateVehicleInfo_JD06reateVehicleInfo_JD06() throws Exception{
		CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
		vo.setOrgId("e4e48584399473d201399b0d23452b3a");  //预约地点Id
		vo.setBusinessTypeId("4028828239a4a4c60139a4fb36ef0007");  //业务类型id
		vo.setName("测试张三"); //姓名
		vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
		vo.setIdNumber("430211231222131213"); //证件号码
		vo.setMobile("17688758320");	//手机号码
		vo.setAppointmentDate("2017-03-31 9:00-10:00");  //预约日期
		vo.setAppointmentTime("2017-07-12");	//预约时间
		vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类
		vo.setCarFrame("1234"); 	//车架号
		vo.setPlatNumber("1234");   //车牌号或车架号
		vo.setBookerName("测试张三");  //预约人姓名
		vo.setBookerIdNumber("430211231222131213"); //预约人身份证号码
		vo.setBookerType("0"); 	//预约方式
		vo.setOptlittleCar("1"); 	//车辆产地
		vo.setIndexType("K31"); 	//指标类型
		vo.setIndexNo("K31"); 		//指标号/公证号/车辆识别代号
		vo.setUseCharater("A"); 	//使用性质
		vo.setArg0("DF");  //车辆型号
		vo.setArg1("17688758320"); 	//手机号码
		vo.setArg2("12311");	 	//短信验证码
		iBookingBusinessService.createVehicleInfo_JD06(vo);
	}
}
