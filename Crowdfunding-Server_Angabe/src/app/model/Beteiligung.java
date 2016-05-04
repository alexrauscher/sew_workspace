package app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

import app.model.Projekt;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "sponsor", "projekt" }))
public class Beteiligung extends Persistent{
	
	@Pattern(regexp = ".{0,250}")
	@NotNull
	@NotBlank
	@Column(updatable = false)
	private String sponsor;

	@ManyToOne
	@NotNull
	private Projekt projekt;
	
	@NotNull
	@Min(1)
	private Integer betrag;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date datum;
	
	@Formula("select sum(b.betrag) from beteiligung b where b.projekt_id = projekt_id and b.datum <= datum")
	@Transient
	@Column(updatable = false)
	private Integer bisherBetrag;

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	public Integer getBetrag() {
		return betrag;
	}

	public void setBetrag(Integer betrag) {
		this.betrag = betrag;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public Integer getBisherBetrag() {
		return bisherBetrag;
	}

	public void setBisherBetrag(Integer bisherBetrag) {
		this.bisherBetrag = bisherBetrag;
	}
}
