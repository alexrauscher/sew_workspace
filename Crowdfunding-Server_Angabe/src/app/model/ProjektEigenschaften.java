package app.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(types = Projekt.class)
public interface ProjektEigenschaften {
	
	public String getBeschreibung();
	public String getGrafik();
	public Integer getZielBetrag();
	public Integer getRestLaufzeit();
	
}
