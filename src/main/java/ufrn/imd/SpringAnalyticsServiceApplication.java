package ufrn.imd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SpringAnalyticsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAnalyticsServiceApplication.class, args);
	}
	
	@Bean
    @LoadBalanced
    RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}