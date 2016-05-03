package app.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Formula;

@Entity
public class Projekt extends Persistent{
	
	@Pattern(regexp = ".{0,250}")
	private String beschreibung;
	
	@Lob
	private String grafik;
	
	private Integer zielBetrag;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ende;
	
	@Formula("timestampdiff('day', now(), ende)")
	@Transient
	@Column(updatable = false)
	private Integer restLaufzeit;
	
	@Transient
	@Column(updatable = false)
	private boolean beendet;

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getGrafik() {
		return grafik;
	}

	public void setGrafik(String grafik) {
		this.grafik = grafik;
	}

	public Integer getZielBetrag() {
		return zielBetrag;
	}

	public void setZielBetrag(Integer zielBetrag) {
		this.zielBetrag = zielBetrag;
	}

	public Date getEnde() {
		return ende;
	}

	public void setEnde(Date ende) {
		this.ende = ende;
	}

	public Integer getRestLaufzeit() {
		return restLaufzeit;
	}

	public void setRestLaufzeit(Integer restLaufzeit) {
		this.restLaufzeit = restLaufzeit;
		wurdeBeendet(restLaufzeit);
	}

	public boolean isBeendet() {
		return beendet;
	}

	public void setBeendet(boolean beendet) {
		this.beendet = beendet;
	}
	
	private void wurdeBeendet(int restLaufzeit) {
		if(restLaufzeit > 0){
			beendet = false;
		}else{
			beendet = true;
		}
	}
}
