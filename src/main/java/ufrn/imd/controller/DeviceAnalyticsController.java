package ufrn.imd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.service.DeviceAnalyticsService;

@RestController
@RequestMapping("/device-analytics")
public class DeviceAnalyticsController {
	private final DeviceAnalyticsService analyticsService;
	
    public DeviceAnalyticsController(DeviceAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
	
	@GetMapping("/median/light-status")
    public ResponseEntity<Double> getMedianForLightStatusTrue() {
        Double median = analyticsService.getMedianForLightStatusTrue();
        return ResponseEntity.ok(median);
    }

    @GetMapping("/median/thermostat")
    public ResponseEntity<Double> getMedianThermostat() {
        Double median = analyticsService.getMedianThermostat();
        return ResponseEntity.ok(median);
    }

    @GetMapping("/count/light-off")
    public ResponseEntity<Long> getDevicesWithLightOff() {
        long count = analyticsService.getDevicesWithLightOff();
        return ResponseEntity.ok(count);
    }
}