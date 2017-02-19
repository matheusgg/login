package br.com.security.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
	 * The constant HIGHEST_ORDER.
	 */
	private static final int HIGHEST_ORDER = 0;

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
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}

}
