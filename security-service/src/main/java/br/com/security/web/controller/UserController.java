package br.com.security.web.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type User controller.
 *
 * @author Matheus
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	/**
	 * The User service.
	 */
	private final UserService userService;

	/**
	 * User principal.
	 *
	 * @param principal the principal
	 * @return the principal
	 */
	@RequestMapping("/me")
	public Principal user(final Principal principal) {
		return principal;
	}

	/**
	 * Profiles list.
	 *
	 * @param username the username
	 * @return the list
	 */
	@RequestMapping("/{username}/profiles")
	public List<String> profiles(@PathVariable final String username) {
		return this.userService.loadUserProfiles(username);
	}

	/**
	 * Handle username not found exception response entity.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<Object> handleUsernameNotFoundException(final UsernameNotFoundException ex) {
		final Map<String, Object> response = new HashMap<>();
		response.put("error", ex.getClass().getSimpleName());
		response.put("error_description", ex.getMessage());
		log.warn(response.toString());
		return new ResponseEntity<>(response, NOT_FOUND);
	}
}
