package app.model;

import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;


@Embeddable
public class Person {

	private String vorname;
	
	@NotBlank
	private String nachname;
	
	
	public String getVorname() {
		return vorname;
	}
	
	public void setVorname(String vorname) {
		// Umgebende Leerzeichen entfernen
		this.vorname = (vorname != null) ? vorname.trim() : vorname;
	}
	
	
	public String getNachname() {
		return nachname;
	}
	
	public void setNachname(String nachname) {
		// Umgebende Leerzeichen entfernen
		this.nachname = (nachname != null) ? nachname.trim() : nachname;
	}
	
}
