package cn.booking.business.cache;

public interface AppointmentCached {
	
	public String getAppointmentByKey(String key);
    
	public boolean setAppointment(String key,String value);
}
