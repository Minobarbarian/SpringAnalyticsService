package ufrn.imd.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ufrn.imd.model.DeviceStatus;

@FeignClient(name = "SPRING-DATABASE-SERVICE")
public interface DeviceStatusServiceClient {
	@PostMapping("/device-status/devices")
	DeviceStatus createDevice(@RequestBody DeviceStatus device);
	
	@GetMapping("/device-status/devices")
	List<DeviceStatus> getAllDeviceStatuses();
	
	@GetMapping("/device-status/devices/{id}")
	DeviceStatus getDeviceStatusById(@PathVariable("id") Long id);
	
	@PutMapping("/device-status/devices/{id}")
	DeviceStatus updateDeviceStatus(@PathVariable("id") Long id, @RequestBody DeviceStatus device);
	
	@DeleteMapping("/device-status/devices/{id}")
    void deleteDevice(@PathVariable("id") Long id);
}