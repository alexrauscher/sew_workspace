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
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Song extends Persistent {
	@Pattern(regexp = ".{0,250}")
	@NotBlank
	@NotNull
	@Column(unique = true, updatable = false)
	private String titel;
	
	@ManyToOne
	@NotNull
	private Interpret interpret;
	
	@Lob
	private String cover;
	
	@NotNull
	@Min(0)
	private Integer upvotes;
	
	@NotNull
	@Min(0)
	private Integer downvotes;
	
	@Formula("100*upvotes/(upvotes + downvotes + 0.001)")
	@Column(updatable = false)
	private float zustimmung;
	
	@Formula("100*upvotes/(upvotes + downvotes + 0.001)")
	private boolean vote;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date geaendert;
	
	public float getZustimmung() {
		return zustimmung;
	}

	public void setZustimmung(float zustimmung) {
		this.zustimmung = zustimmung;
	}

	public boolean isVote() {
		return vote;
	}

	public void setVote(boolean vote) {
		this.vote = vote;
		updateVote(vote);
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
	
	private void updateVote(boolean vote){
		if(vote == true){
			upvotes += 1;
		}else{
			downvotes += 1;
		}
	}
	
}
