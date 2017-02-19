package br.com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.security.config.properties.JwtProperties;

/**
 * The type Security service application.
 *
 * @author Matheus
 */
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityServiceApplication {

	/**
	 * Main.
	 *
	 * @param args the args
	 */
	public static void main(final String... args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}
}
