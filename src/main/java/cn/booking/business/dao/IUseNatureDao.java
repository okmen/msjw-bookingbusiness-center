package cn.booking.business.dao;

import java.util.List;

import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.UseNaturePo;

public interface IUseNatureDao {
	/**
	 * 批量添加车辆类型
	 * @param useNaturePos
	 * @return
	 * @throws Exception
	 */
	public int addBatch(List<UseNaturePo> useNaturePos)throws Exception;
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
	public List<UseNaturePo> getAll()throws Exception;
}
