package app.repository;

import org.springframework.data.rest.core.config.Projection;

import app.model.Benutzer;


@Projection(types = Benutzer.class)
public interface BenutzerName {

	public String getName();
	
}
