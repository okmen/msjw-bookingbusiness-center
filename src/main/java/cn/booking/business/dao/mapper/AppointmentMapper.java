package cn.booking.business.dao.mapper;

import java.util.List;

import cn.booking.business.bean.AppointmentPo;

public interface AppointmentMapper {
	/**
	 * 批量添加业务类型
	 * @param carTypePos
	 * @return
	 * @throws Exception
	 */
	public int addBatch(List<AppointmentPo> appointmentPos)throws Exception;
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
	public List<AppointmentPo> getAll()throws Exception;
}
