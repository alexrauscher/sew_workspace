package app.model;

import org.springframework.data.rest.core.config.Projection;


@Projection(types = Verlag.class)
public interface VerlagFirma {

	public String getFirma();
	
}
