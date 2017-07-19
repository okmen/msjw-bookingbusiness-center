package cn.booking.business.dao;

import java.util.List;

import cn.booking.business.bean.BusinessTypePo;

public interface BusinessTypeDao {
	/**
	 * 批量添加业务类型
	 * @param vehicleNodelPos
	 * @return
	 * @throws Exception
	 */
	public int addBatch(List<BusinessTypePo> vehicleNodelPos)throws Exception;
	/**
	 * 删除整张表
	 * @return
	 */
	public int deleteAll()throws Exception;
	/**
	 * 查询所有
	 * @return
	 * @throws Exception
	 */
	public List<BusinessTypePo> getAll()throws Exception;
}
