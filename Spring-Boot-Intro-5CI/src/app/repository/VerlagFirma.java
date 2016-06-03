package app.repository;

import org.springframework.data.rest.core.config.Projection;

import app.model.Verlag;


@Projection(types = Verlag.class)
public interface VerlagFirma {

	public String getFirma();
	
}
