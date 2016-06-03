package app.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import app.model.Autor;

@Projection(types = Autor.class)
public interface AutorName {

	@Value("#{target.vorname} #{target.nachname}")
	public String getName();
	
}
