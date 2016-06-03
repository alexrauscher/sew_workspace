package app.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "vorname", "nachname" }))
public class Autor extends Persistent {

	private String vorname;
	
	@NotBlank
	private String nachname;
	
	@ManyToMany(mappedBy = "autoren")
	private Set<Book> buecher;
	
	
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

	
	public Set<Book> getBuecher() {
		return buecher;
	}

	public void setBuecher(Set<Book> buecher) {
		this.buecher = buecher;
	}
	
}
