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
import cn.booking.business.bean.OrgVO;
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
		OrgVO orgVO = iBookingBusinessService.getOrgsByBusinessTypeId(btId, arg0, arg1);
		System.out.println(orgVO);
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
	
}
