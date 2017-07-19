package cn.booking.business.dao;

import java.util.List;

import cn.booking.business.bean.AppointmentUnitPo;

public interface AppointmentUnitDao {
	/**
	 * 批量添加业务类型
	 * @param carTypePos
	 * @return
	 * @throws Exception
	 */
	public int addBatch(List<AppointmentUnitPo> appointmentUnitPos)throws Exception;
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
	public List<AppointmentUnitPo> getAll()throws Exception;
}
