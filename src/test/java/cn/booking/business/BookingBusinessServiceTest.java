package cn.booking.business;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.IdTypeVO;
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
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("arg0", "");
		map.put("arg1", "");
		List<IdTypeVO> idTypeVOs = iBookingBusinessService.getCarTypes(map);
		System.out.println(idTypeVOs);
	}
	
	@Test
	public void getBusinessTypes() throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("type", "0");
		map.put("part", "1");
		map.put("arg0", "");
		map.put("arg1", "");
		List<BusinessTypeVO> businessTypeVOs = iBookingBusinessService.getBusinessTypes(map);
		System.out.println(businessTypeVOs);
	}
}
