package ufrn.imd.service;

import java.util.List;
import java.util.stream.Collectors;

import ufrn.imd.client.DeviceStatusServiceClient;
import ufrn.imd.model.DeviceStatus;

import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class DeviceAnalyticsService {
	private final DeviceStatusServiceClient deviceStatusServiceClient;
	
	public DeviceAnalyticsService(DeviceStatusServiceClient deviceStatusServiceClient) {
		this.deviceStatusServiceClient = deviceStatusServiceClient;
	}
	
	public List<DeviceStatus> fetchAllDeviceStatuses() {
        return deviceStatusServiceClient.getAllDeviceStatuses();
    }
	
	public Double getMedianForLightStatusTrue() {
        List<Double> lightOnDevices = fetchAllDeviceStatuses().stream()
                .filter(DeviceStatus::isLightStatus)
                .map(DeviceStatus::getThermostat)   
                .sorted()                           
                .collect(Collectors.toList());

        return calculateMedian(lightOnDevices);
    }
	
	public Double getMedianThermostat() {
        List<Double> thermostatValues = fetchAllDeviceStatuses().stream()
                .map(DeviceStatus::getThermostat)
                .sorted()
                .collect(Collectors.toList());

        return calculateMedian(thermostatValues);
    }
	public long getDevicesWithLightOff() {
        return fetchAllDeviceStatuses().stream()
                .filter(device -> !device.isLightStatus())
                .count();
    }
	
	private Double calculateMedian(List<Double> values) {
        if (values.isEmpty()) {
            return null;
        }

        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        } else {
            return values.get(size / 2);
        }
    }
}