package app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Song extends Persistent {

	@Length(min=0, max=255)
	@NotBlank
	@Column(unique = true, updatable = false)
	private String titel;
	
	@ManyToOne
	@NotNull
	private Interpret interpret;
	
	@Lob
	private String cover;
	
	@NotNull
	@Min(0)
	@JsonIgnore
	private Integer upvotes;
	
	@NotNull
	@Min(0)
	@JsonIgnore
	private Integer downvotes;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date geaendert;
	
	@Transient
	@Column(updatable = false)
	@Formula("100 * upvotes / (upvotes + downvotes + 0.001)")
	private Float zustimmung;
	
	@Transient
	private Boolean vote;

	public Float getZustimmung() {
		return zustimmung;
	}

	public void setZustimmung(Float zustimmung) {
		this.zustimmung = zustimmung;
	}

	public void setVote(Boolean vote) {
		this.vote = vote;
		
		if(vote){
			upvotes++;
		} else {
			downvotes++;
		}
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public Interpret getInterpret() {
		return interpret;
	}

	public void setInterpret(Interpret interpret) {
		this.interpret = interpret;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Integer getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}

	public Integer getDownvotes() {
		return downvotes;
	}

	public void setDownvotes(Integer downvotes) {
		this.downvotes = downvotes;
	}

	public Date getGeaendert() {
		return geaendert;
	}

	public void setGeaendert(Date geaendert) {
		this.geaendert = geaendert;
	}
}
