package cn.booking.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.booking.business.cache.ICarTypeCached;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class CachedTest extends TestCase {
	@Autowired
    private ICarTypeCached carTypeCached;
	
	@Test
	public void getICarTypeByKey() {
		System.out.println(carTypeCached.getICarTypeByKey("1111"));;
	}

	@Test
	public void setICarType() {
		System.out.println(carTypeCached.setICarType("1111", "xxxxxxxxxxxxxxxxxxxxxx"));
	}
}
