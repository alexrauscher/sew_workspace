package app.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;
import app.model.Projekt;

@Entity
public class Beteiligung extends Persistent{
	
	@Pattern(regexp = ".{0,250}")
	private String sponsor;

	@ManyToOne
	private Projekt projekt;
}
