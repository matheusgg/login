package br.com.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Data;

/**
 * The type Jwt properties.
 *
 * @author Matheus
 */
@Data
@ConfigurationProperties("br.com.security.jwt")
public class JwtProperties {
	/**
	 * The Alias.
	 */
	private String alias;
	/**
	 * The Store pass.
	 */
	private String storePass;
	/**
	 * The Key store.
	 */
	private Resource keyStore;
	/**
	 * The Key pass.
	 */
	private String keyPass;
}
