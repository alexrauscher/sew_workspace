package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ort", "land" }))
public class Destination extends Persistent {
	
	@NotBlank
	@NotNull
	@Column(updatable = false)
	@Length(max=255)
	private String ort;
	
	@NotBlank
	@NotNull
	@Column(updatable = false)
	@Length(max=255)
	private String land;
	
	@NotNull
	@Lob
	private String bild;

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getBild() {
		return bild;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}
	
}
