package cn.booking.business.dao.mapper;

import java.util.List;

import cn.booking.business.bean.CarTypePo;
import cn.booking.business.bean.IdCardTypePo;

public interface IdCardTypeMapper {
	/**
	 * 批量添加车辆类型
	 * @param idCardTypePos
	 * @return
	 * @throws Exception
	 */
	public int addBatch(List<IdCardTypePo> idCardTypePos)throws Exception;
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
	public List<IdCardTypePo> getAll()throws Exception;
}
