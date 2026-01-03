package com.main;

import com.main.dto.RegisterRequest;
import com.main.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.main.entity.Role.ADMIN;
import static com.main.entity.Role.MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(
		basePackages = "com.main",
		repositoryFactoryBeanClass =
				org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean.class
)
public class SecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service) {
		return args -> {

			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.phone("1111111111")
					.role(ADMIN)
					.build();

			var adminAuth = service.registerSystemUser(admin);
			System.out.println("ADMIN TOKEN: " + adminAuth.getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("Manager")
					.lastname("Manager")
					.phone("2222222222")
					.role(MANAGER)
					.build();

			var managerAuth = service.registerSystemUser(manager);
			System.out.println("MANAGER TOKEN: " + managerAuth.getAccessToken());
		};
	}

}
