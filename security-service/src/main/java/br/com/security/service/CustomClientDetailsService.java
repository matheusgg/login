package br.com.security.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import br.com.security.model.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Custom client details service.
 *
 * @author Matheus
 */
@Slf4j
@RequiredArgsConstructor
public class CustomClientDetailsService implements ClientDetailsService {

	/**
	 * The constant CLIENT_ID_CACHE.
	 */
	public static final String CLIENT_ID_CACHE = "clientIdCache";
	/**
	 * The constant CLIENT_ID_NOT_FOUND_MESSAGE.
	 */
	private static final String CLIENT_ID_NOT_FOUND_MESSAGE = "Client not found";
	/**
	 * The Client details repository.
	 */
	private final ClientRepository clientDetailsRepository;

	/**
	 * Load client by client id client details.
	 *
	 * @param clientId the client id
	 * @return the client details
	 * @throws ClientRegistrationException the client registration exception
	 */
	@Override
	@Cacheable(cacheNames = CLIENT_ID_CACHE, unless = "#result == null")
	public ClientDetails loadClientByClientId(final String clientId) throws ClientRegistrationException {
		log.info("Loading client {}", clientId);
		return this.clientDetailsRepository.findOneByClientId(clientId).orElseThrow(() -> new NoSuchClientException(CLIENT_ID_NOT_FOUND_MESSAGE));
	}
}
