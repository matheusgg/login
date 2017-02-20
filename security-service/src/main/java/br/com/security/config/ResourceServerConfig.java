package br.com.security.config;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * The type Resource server config.
 *
 * @author Matheus
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig {

	/**
	 * The constant ALLOWED_WILD_CARD.
	 */
	private static final String ALLOWED_WILD_CARD = "*";

	/**
	 * Password encoder b crypt password encoder.
	 *
	 * @return the b crypt password encoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Cors filter filter registration bean.
	 *
	 * @return the filter registration bean
	 */
	@Bean
	public FilterRegistrationBean corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin(ALLOWED_WILD_CARD);
		config.addAllowedHeader(ALLOWED_WILD_CARD);
		config.addAllowedMethod(ALLOWED_WILD_CARD);
		source.registerCorsConfiguration("/**", config);
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter(source));
		registrationBean.setOrder(HIGHEST_PRECEDENCE);
		return registrationBean;
	}

}
