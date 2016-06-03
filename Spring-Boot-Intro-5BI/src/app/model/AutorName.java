package app.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Autor.class)
public interface AutorName {

	@Value("#{target.vorname} #{target.nachname}")
	public String getName();
	
}
