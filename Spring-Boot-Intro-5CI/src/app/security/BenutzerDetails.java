package app.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Stellt die Benutzerdetails in einer Form zur Verfügung, die für die 
 * Authentifizierung mit Hilfe von {@link UserDetails} und für 
 * {@link PreAuthorize} geeignet ist.
 * 
 * @author F. Kasper, ksp@htl.rennweg.at
 */
public interface BenutzerDetails extends UserDetails {

	/** Liefert den Primärschlüssel (für {@link PreAuthorize}) */
	public long getId();
	
	
	/** Liefert den Benutzernamen */
	public String getName();
	
	
	/**
	 * Liefert das Passwort in der Form, wie es in der Benutzerdatenbank
	 * gespeichert ist, d.h. als Hashwert.
	 */
	@JsonIgnore
	public String getPasswort();

	
	/**
	 * Liefert die Benutzerrolle und kann überschrieben werden, falls es mehrere
	 * unterschiedliche Benutzerrollen gibt.
	 */
	public default String getRolle() {
		return "ROLE_USER";
	}
	
	
	/*----------- Ab hier wird Interface UserDetails implementiert -----------*/
	
	@Override
	@JsonIgnore
	public default Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(getRolle()));
	}

	
	@Override
	@JsonIgnore
	public default String getPassword() {
		return getPasswort();
	}

	
	@Override
	@JsonIgnore
	public default String getUsername() {
		return getName();
	}

	
	@Override
	@JsonIgnore
	public default boolean isAccountNonExpired() {
		return true;
	}

	
	@Override
	@JsonIgnore
	public default boolean isAccountNonLocked() {
		return true;
	}

	
	@Override
	@JsonIgnore
	public default boolean isCredentialsNonExpired() {
		return true;
	}

	
	@Override
	@JsonIgnore
	public default boolean isEnabled() {
		return true;
	}

}
