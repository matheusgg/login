package br.com.security.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.security.model.Client;

/**
 * The interface Client repository.
 *
 * @author Matheus
 */
public interface ClientRepository extends MongoRepository<Client, String> {

	/**
	 * Find one by client id optional.
	 *
	 * @param clientId the client id
	 * @return the optional
	 */
	Optional<Client> findOneByClientId(String clientId);
}
