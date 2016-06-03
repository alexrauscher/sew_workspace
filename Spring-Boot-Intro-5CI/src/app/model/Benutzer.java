package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.security.BenutzerDetails;


@Entity
public class Benutzer extends Persistent implements BenutzerDetails {

	@Column(unique = true)
	@NotBlank
	private String name;
	
	@NotBlank
	@JsonIgnore
	private String passwort;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	
}
