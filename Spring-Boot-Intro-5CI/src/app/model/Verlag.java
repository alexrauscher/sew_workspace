package app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class Verlag extends Persistent {

	@Column(unique = true)
	private String firma;
	
	private String adresse;
	
	@OneToMany(mappedBy = "verlag", cascade = CascadeType.REMOVE)
	private Set<Book> buecher;
	
	
	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	
	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	
	public Set<Book> getBuecher() {
		return buecher;
	}

	public void setBuecher(Set<Book> buecher) {
		this.buecher = buecher;
	}
	
}
