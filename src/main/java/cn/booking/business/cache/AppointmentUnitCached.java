package cn.booking.business.cache;

public interface AppointmentUnitCached {

	public String getAppointmentUnitByKey(String key);

	public boolean setAppointmentUnit(String key, String value);
}
