package br.com.security.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.security.model.User;

/**
 * The interface User repository.
 *
 * @author Matheus
 */
public interface UserRepository extends MongoRepository<User, String> {

	/**
	 * Find one by username and password optional.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the optional
	 */
	Optional<User> findOneByUsernameAndPassword(String username, String password);

	/**
	 * Find one by username optional.
	 *
	 * @param username the username
	 * @return the optional
	 */
	Optional<User> findOneByUsername(String username);

}
