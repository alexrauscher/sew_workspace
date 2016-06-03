package app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

@Entity
public class Reise extends Persistent {
	
	@ManyToOne(optional = false)
	@JoinColumn( updatable = false)
	private Destination destination;
	
	@NotNull
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginn;
	
	@NotNull
	@Column(updatable = false)
	@Min(1)
	private Integer dauer;
	
	@NotNull
	@Min(0)
	private Integer freiePlaetze;
	
	@Formula("timestampdiff('day', now(), beginn) >= 2 and freiePlaetze > 0")
	@Transient
	private boolean buchbar;
	
	@Min(0)
	@Transient
	private Integer buchen;

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Date getBeginn() {
		return beginn;
	}

	public void setBeginn(Date beginn) {
		this.beginn = beginn;
	}

	public Integer getDauer() {
		return dauer;
	}

	public void setDauer(Integer dauer) {
		this.dauer = dauer;
	}

	public Integer getFreiePlaetze() {
		return freiePlaetze;
	}

	public void setFreiePlaetze(Integer freiePlaetze) {
		this.freiePlaetze = freiePlaetze;
	}
	
	public boolean isBuchbar() {
		return buchbar;
	}

	public void setBuchen(Integer buchen) {
		if(buchen <= freiePlaetze){
			plaetzeBuchen(buchen);
			this.buchen = buchen;
		}
	}
	
	private void plaetzeBuchen(Integer plaetze) {
		freiePlaetze -= plaetze;
	}
	

}
