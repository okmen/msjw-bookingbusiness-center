package cn.booking.business.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.booking.business.bean.CarTypeVO;

public class CacheTaskExecute{
	protected static Logger logger = LoggerFactory.getLogger(CacheTaskExecute.class);
	
	public static void cacheCarType(List<CarTypeVO> carTypeVOs) throws Exception {
		//从缓存中取出，异步操作(调用第三方，比较缓存中数据,有变动则更新到mysql和redis)
		List<CarTypeVO> carTypeVOsNew = TransferThirdParty.getCarTypes();
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
	}
	
}
