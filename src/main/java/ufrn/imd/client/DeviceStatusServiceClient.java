package ufrn.imd.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import ufrn.imd.model.DeviceStatus;

@FeignClient(name = "SPRING-DATABASE-SERVICE")
public interface DeviceStatusServiceClient {
	
	@PostMapping("/device-status/devices")
	@CircuitBreaker(name = "createDevice")
	@Bulkhead(name = "createDevice")
	@Retry(name = "createDevice")
	DeviceStatus createDevice(@RequestBody DeviceStatus device);
	
	@GetMapping("/device-status/devices")
	@CircuitBreaker(name = "getAllDeviceStatuses")
	@Bulkhead(name = "getAllDeviceStatuses")
	@Retry(name = "getAllDeviceStatuses")
	List<DeviceStatus> getAllDeviceStatuses();
	
	@GetMapping("/device-status/devices/{id}")
	@CircuitBreaker(name = "getDeviceStatusById")
	@Bulkhead(name = "getDeviceStatusById")
	@Retry(name = "getDeviceStatusById")
	DeviceStatus getDeviceStatusById(@PathVariable("id") Long id);
	
	@PutMapping("/device-status/devices/{id}")
	@CircuitBreaker(name = "updateDeviceStatus")
	@Bulkhead(name = "updateDeviceStatus")
	@Retry(name = "updateDeviceStatus")
	DeviceStatus updateDeviceStatus(@PathVariable("id") Long id, @RequestBody DeviceStatus device);
	
	@DeleteMapping("/device-status/devices/{id}")
	@CircuitBreaker(name = "deleteDevice")
	@Bulkhead(name = "deleteDevice")
	@Retry(name = "deleteDevice")
    void deleteDevice(@PathVariable("id") Long id);
	
	default DeviceStatus fallbackCreateDevice(DeviceStatus device, Throwable ex) {
		handleException("fallbackCreateDevice", ex);
        return null;
    }
	
	default List<DeviceStatus> fallbackGetAllDeviceStatuses(Throwable ex) {
		handleException("fallbackGetAllDeviceStatuses", ex);
        return List.of();
    }
	
	default DeviceStatus fallbackGetDeviceStatusById(Long id, Throwable ex) {
		handleException("fallbackGetDeviceStatusById", ex);
        return null;
    }
	
	default DeviceStatus fallbackUpdateDeviceStatus(Long id, DeviceStatus device, Throwable ex) {
		handleException("fallbackGetDeviceStatusById", ex);
        return null;
    }

	default void fallbackDeleteDevice(Long id, Throwable ex) {
    	handleException("fallbackDeleteDevice", ex);
    }
    
    private void handleException(String context, Throwable ex) {
    	System.err.println("Error occurred in [" + context + "]: " + ex.getMessage());
        if (ex instanceof IllegalArgumentException) {
            System.err.println("Invalid argument: " + ex.getMessage());
        } else if (ex instanceof NullPointerException) {
            System.err.println("Null value encountered.");
        } else {
            System.err.println("Unexpected error: " + ex.getMessage());
        }
        ex.printStackTrace();
    }
}