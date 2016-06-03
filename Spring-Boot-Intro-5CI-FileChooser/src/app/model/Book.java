package app.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "titel", "autor" }))
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank
	private String titel;
	
	@NotBlank
	private String autor;
	
	@Enumerated(EnumType.STRING)
	private Ausgabe ausgabe;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date erschienen;
	
	@Min(1)
	private Integer seiten;

	@Lob
	private String kurzfassung;
	
	@Lob
	private String cover;	// Coverbild als Data-URL
	
	@Transient
	@AssertTrue(message = "Dieses Buch hat zu wenige Seiten im Verhältnis zur Länge seines Titels")
	private boolean genugSeiten;
	
	
	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		// Umgebende Leerzeichen entfernen
		this.titel = (titel != null) ? titel.trim() : titel;
		updateGenugSeiten();
	}

	
	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	
	public Ausgabe getAusgabe() {
		return ausgabe;
	}

	public void setAusgabe(Ausgabe ausgabe) {
		this.ausgabe = ausgabe;
	}

	
	public Date getErschienen() {
		return erschienen;
	}

	public void setErschienen(Date erschienen) {
		this.erschienen = erschienen;
	}

	
	public Integer getSeiten() {
		return seiten;
	}

	public void setSeiten(Integer seiten) {
		this.seiten = seiten;
		updateGenugSeiten();
	}

	
	public String getKurzfassung() {
		return kurzfassung;
	}

	public void setKurzfassung(String kurzfassung) {
		this.kurzfassung = kurzfassung;
	}

	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}


	private void updateGenugSeiten() {
		genugSeiten = (titel == null) || (seiten >= titel.length() * 10);
	}
	
}
