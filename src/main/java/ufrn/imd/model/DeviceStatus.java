package ufrn.imd.model;

public class DeviceStatus {
	private Long id;
	private boolean lightStatus;
	private double thermostat;
	
	public Long getId() {
		return id;
	}
	
	public boolean isLightStatus() {
		return lightStatus;
	}
	
	public double getThermostat() {
		return thermostat;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setLightStatus(boolean lightStatus) {
		this.lightStatus = lightStatus;
	}
	
	public void setThermostat(double thermostat) {
		this.thermostat = thermostat;
	}
}