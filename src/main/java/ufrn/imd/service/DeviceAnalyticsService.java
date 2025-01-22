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
	
	@CircuitBreaker(name = "servicebeta", fallbackMethod = "fallbackFetchAllDeviceStatuses")
	@Bulkhead(name = "deviceAnalyticsBulkhead", fallbackMethod = "fallbackFetchAllDeviceStatuses")
	@Retry(name = "retryFetchDeviceStatuses", fallbackMethod = "fallbackFetchAllDeviceStatuses")
	private List<DeviceStatus> fetchAllDeviceStatuses() {
        return deviceStatusServiceClient.getAllDeviceStatuses();
    }
	
	@CircuitBreaker(name = "servicebeta", fallbackMethod = "fallbackGetMedianForLightStatusTrue")
	@Bulkhead(name = "deviceAnalyticsBulkhead", fallbackMethod = "fallbackGetMedianForLightStatusTrue")
	@Retry(name = "retryFetchDeviceStatuses", fallbackMethod = "fallbackGetMedianForLightStatusTrue")
	public Double getMedianForLightStatusTrue() {
        List<Double> lightOnDevices = fetchAllDeviceStatuses().stream()
                .filter(DeviceStatus::isLightStatus)
                .map(DeviceStatus::getThermostat)   
                .sorted()                           
                .collect(Collectors.toList());

        return calculateMedian(lightOnDevices);
    }
	
	@CircuitBreaker(name = "servicebeta", fallbackMethod = "fallbackGetMedianThermostat")
	@Bulkhead(name = "deviceAnalyticsBulkhead", fallbackMethod = "fallbackGetMedianThermostat")
	@Retry(name = "retryFetchDeviceStatuses", fallbackMethod = "fallbackGetMedianThermostat")
	public Double getMedianThermostat() {
        List<Double> thermostatValues = fetchAllDeviceStatuses().stream()
                .map(DeviceStatus::getThermostat)
                .sorted()
                .collect(Collectors.toList());

        return calculateMedian(thermostatValues);
    }
	
	@CircuitBreaker(name = "servicebeta", fallbackMethod = "fallbackGetDevicesWithLightOff")
	@Bulkhead(name = "deviceAnalyticsBulkhead", fallbackMethod = "fallbackGetDevicesWithLightOff")
	@Retry(name = "retryFetchDeviceStatuses", fallbackMethod = "fallbackGetDevicesWithLightOff")
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
	
	public List<DeviceStatus> fallbackFetchAllDeviceStatuses(Exception ex) {
        return List.of();
    }

    public Double fallbackGetMedianForLightStatusTrue(Exception ex) {
        return null;
    }

    public Double fallbackGetMedianThermostat(Exception ex) {
        return null;
    }

    public long fallbackGetDevicesWithLightOff(Exception ex) {
        return 0;
    }
}