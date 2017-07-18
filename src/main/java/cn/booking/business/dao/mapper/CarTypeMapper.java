package cn.booking.business.dao.mapper;

import java.util.List;

import cn.booking.business.bean.CarTypePo;

public interface CarTypeMapper {
	/**
	 * 批量添加车辆类型
	 * @param carTypePos
	 * @return
	 * @throws Exception
	 */
	public int addBatch(List<CarTypePo> carTypePos)throws Exception;
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
	public List<CarTypePo> getAll()throws Exception;
}
