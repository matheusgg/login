package br.com.security.config;

import java.security.KeyPair;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import br.com.security.config.properties.JwtProperties;
import br.com.security.model.repository.ClientRepository;
import br.com.security.service.CustomClientDetailsService;
import lombok.RequiredArgsConstructor;

/**
 * The type Authorization server config.
 *
 * @author Matheus
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	/**
	 * The Authentication manager.
	 */
	private final AuthenticationManager authenticationManager;
	/**
	 * The User details service.
	 */
	private final UserDetailsService userDetailsService;
	/**
	 * The Client repository.
	 */
	private final ClientRepository clientRepository;
	/**
	 * The Jwt properties.
	 */
	private final JwtProperties jwtProperties;

	/**
	 * Configure.
	 *
	 * @param endpoints the endpoints
	 * @throws Exception the exception
	 */
	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setKeyPair(this.getKeyPair());
		endpoints.authenticationManager(this.authenticationManager)
				.tokenStore(new JwtTokenStore(jwtAccessTokenConverter))
				.accessTokenConverter(jwtAccessTokenConverter)
				.userDetailsService(this.userDetailsService);
	}

	/**
	 * Configure.
	 *
	 * @param clients the clients
	 * @throws Exception the exception
	 */
	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(this.customClientDetailsService());
	}

	/**
	 * Custom client details service client details service.
	 *
	 * @return the client details service
	 */
	@Bean
	public ClientDetailsService customClientDetailsService() {
		return new CustomClientDetailsService(this.clientRepository);
	}

	/**
	 * Gets key pair.
	 *
	 * @return the key pair
	 */
	private KeyPair getKeyPair() {
		final KeyStoreKeyFactory factory = new KeyStoreKeyFactory(this.jwtProperties.getKeyStore(), this.jwtProperties.getStorePass().toCharArray());
		return factory.getKeyPair(this.jwtProperties.getAlias(), this.jwtProperties.getKeyPass().toCharArray());
	}
}
