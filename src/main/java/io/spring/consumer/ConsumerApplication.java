package io.spring.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@RestController
	class ConsumerController {

		final ProducerService producerService;

		ConsumerController(ProducerService producerService) {
			this.producerService = producerService;
		}

		@GetMapping("/hello")
		public String simpleEndPoint() {
			return producerService.hello();
		}

	}

	@Configuration
	class WebSecurity extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint())
					.permitAll().and().httpBasic().disable().csrf().disable();
		}
	}

	@FeignClient(name = "${producer.name}")
	interface ProducerService {

		@GetMapping("/api/hello")
		String hello();
	}

}
