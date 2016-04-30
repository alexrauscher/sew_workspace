package app.model;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

@Entity
public class Projekt extends Persistent{
	
	@Pattern(regexp = ".{0,250}")
	private String beschreibung;
	
	@Lob
	private String grafik;
	
	private int zielBetrag;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ende;
}
