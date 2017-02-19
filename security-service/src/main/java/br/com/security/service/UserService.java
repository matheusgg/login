package br.com.security.service;

import static java.util.concurrent.TimeUnit.HOURS;
import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.security.model.User;
import br.com.security.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type User service.
 *
 * @author Matheus
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {

	/**
	 * The constant USERNAME_NOT_FOUND_MESSAGE.
	 */
	private static final String USERNAME_NOT_FOUND_MESSAGE = "Username not found";
	/**
	 * The constant INVALID_PASSWORD_MESSAGE.
	 */
	private static final String INVALID_PASSWORD_MESSAGE = "Invalid password";
	/**
	 * The User repository.
	 */
	private final UserRepository userRepository;
	/**
	 * The Redis template.
	 */
	private final RedisTemplate<String, String> redisTemplate;
	/**
	 * The Password encoder.
	 */
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * Additional authentication checks.
	 *
	 * @param userDetails    the user details
	 * @param authentication the authentication
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		final String rawPassword = authentication.getCredentials().toString();
		final String encodedPassword = userDetails.getPassword();
		if (!this.passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new BadCredentialsException(INVALID_PASSWORD_MESSAGE);
		}
	}

	/**
	 * Retrieve user user details.
	 *
	 * @param username       the username
	 * @param authentication the authentication
	 * @return the user details
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		log.info("Retrieving user {}", username);
		return this.loadUserByUsername(username);
	}

	/**
	 * Load user by username user details.
	 *
	 * @param username the username
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		log.info("Loading user {}", username);
		final User user = this.userRepository.findOneByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_MESSAGE));
		final List<String> profiles = new ArrayList<>(authorityListToSet(user.getAuthorities()));
		this.addUserProfilesToCache(username, profiles);
		return user;
	}

	/**
	 * Load user profiles list.
	 *
	 * @param username the username
	 * @return the list
	 */
	public List<String> loadUserProfiles(final String username) {
		log.info("Loading {} profiles", username);
		final List<String> profiles;
		if (this.redisTemplate.hasKey(username)) {
			profiles = this.redisTemplate.opsForList().range(username, 0, -1);
		} else {
			final UserDetails user = this.loadUserByUsername(username);
			profiles = new ArrayList<>(authorityListToSet(user.getAuthorities()));
		}
		return profiles;
	}

	/**
	 * Add user profiles to cache.
	 *
	 * @param username the username
	 * @param profiles the profiles
	 */
	private void addUserProfilesToCache(final String username, final Collection<String> profiles) {
		log.info("Adding {} profiles to cache", username);
		this.redisTemplate.delete(username);
		this.redisTemplate.opsForList().rightPushAll(username, profiles);
		this.redisTemplate.expire(username, 8, HOURS);
	}
}
