package app.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book extends Persistent {

	@Column(unique = true)
	@NotBlank
	private String titel;
	
	@ManyToOne
	@LastModifiedBy
	private Benutzer bearbeiter;
	
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date bearbeitetAm;
	
	@ManyToMany
	@NotEmpty
	private Set<Autor> autoren; 
	
	@ManyToOne(optional = false)
	private Verlag verlag;
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	@NotEmpty(message = "Zu jedem Buch muss es zumindest eine Ausgabe geben")
	private Set<Ausgabe> ausgaben;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date erschienen;
	
	@Min(1)
	private Integer seiten;
	
	@Formula("floor(seiten/100+1) * 100")
	private Integer circaSeiten;

	@Lob
	private String kurzfassung;
	
	@Lob
	private String cover;	// Coverbild als Data-URL
	
	@Transient
	@AssertTrue(message = "Dieses Buch hat zu wenige Seiten im Verhältnis zur Länge seines Titels")
	private boolean genugSeiten = true;
	
	
	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		// Umgebende Leerzeichen entfernen
		this.titel = (titel != null) ? titel.trim() : titel;
		updateGenugSeiten();
	}

	
	public Benutzer getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(Benutzer bearbeiter) {
		this.bearbeiter = bearbeiter;
	}
	

	public Date getBearbeitetAm() {
		return bearbeitetAm;
	}

	public void setBearbeitetAm(Date bearbeitetAm) {
		this.bearbeitetAm = bearbeitetAm;
	}

	
	public Set<Autor> getAutoren() {
		return autoren;
	}

	public void setAutoren(Set<Autor> autoren) {
		this.autoren = autoren;
	}

	
	public Verlag getVerlag() {
		return verlag;
	}

	public void setVerlag(Verlag verlag) {
		this.verlag = verlag;
	}

	
	public Set<Ausgabe> getAusgaben() {
		return ausgaben;
	}

	public void setAusgaben(Set<Ausgabe> ausgaben) {
		this.ausgaben = ausgaben;
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

	
	public Integer getCircaSeiten() {
		return circaSeiten;
	}

	public void setCircaSeiten(Integer circaSeiten) {
		this.circaSeiten = circaSeiten;
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
