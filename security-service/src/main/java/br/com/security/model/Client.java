package br.com.security.model;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import lombok.Data;

/**
 * The type Client details.
 *
 * @author Matheus
 */
@Data
@Document(collection = "oauth_client_details")
public class Client implements ClientDetails {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 7194053458193235651L;
	/**
	 * The Id.
	 */
	@Id
	private String id;
	/**
	 * The Client id.
	 */
	@Indexed(unique = true)
	private String clientId;
	/**
	 * The Client secret.
	 */
	private String clientSecret;
	/**
	 * The Scope.
	 */
	private Set<String> scope = emptySet();
	/**
	 * The Resource ids.
	 */
	private Set<String> resourceIds = emptySet();
	/**
	 * The Authorized grant types.
	 */
	private Set<String> authorizedGrantTypes = emptySet();
	/**
	 * The Registered redirect uri.
	 */
	private Set<String> registeredRedirectUri;
	/**
	 * The Auto approve scopes.
	 */
	private Set<String> autoApproveScopes;
	/**
	 * The Authorities.
	 */
	private List<GrantedAuthority> authorities = emptyList();
	/**
	 * The Access token validity seconds.
	 */
	private Integer accessTokenValiditySeconds;
	/**
	 * The Refresh token validity seconds.
	 */
	private Integer refreshTokenValiditySeconds;
	/**
	 * The Additional information.
	 */
	private Map<String, Object> additionalInformation = new HashMap<>();

	/**
	 * Is secret required boolean.
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isSecretRequired() {
		return this.clientSecret != null;
	}

	/**
	 * Is scoped boolean.
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isScoped() {
		return this.scope != null && !this.scope.isEmpty();
	}

	/**
	 * Is auto approve boolean.
	 *
	 * @param scope the scope
	 * @return the boolean
	 */
	@Override
	public boolean isAutoApprove(final String scope) {
		return this.autoApproveScopes.stream().anyMatch(auto -> TRUE.toString().equals(auto) || scope.matches(auto));
	}
}
