package br.com.security.model;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.ToString;

/**
 * The type User.
 *
 * @author Matheus
 */
@Data
@ToString(exclude = "password")
@Document(collection = "oauth_user_details")
public class User implements UserDetails {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 5062753172118045080L;
	/**
	 * The Id.
	 */
	@Id
	private String id;
	/**
	 * The Username.
	 */
	@Indexed(unique = true)
	private String username;
	/**
	 * The Password.
	 */
	private String password;
	/**
	 * The Account non expired.
	 */
	private boolean accountNonExpired;
	/**
	 * The Account non locked.
	 */
	private boolean accountNonLocked;
	/**
	 * The Credentials non expired.
	 */
	private boolean credentialsNonExpired;
	/**
	 * The Enabled.
	 */
	private boolean enabled;
	/**
	 * The Authorities.
	 */
	@Transient
	private Collection<? extends GrantedAuthority> authorities;
	/**
	 * The Roles.
	 */
	private String[] roles;

	/**
	 * Gets authorities.
	 *
	 * @return the authorities
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return createAuthorityList(this.roles);
	}
}
