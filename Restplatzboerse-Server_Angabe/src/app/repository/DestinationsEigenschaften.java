package app.repository;

import org.springframework.data.rest.core.config.Projection;

import app.model.Destination;

@Projection(types = Destination.class)
public interface DestinationsEigenschaften {
	
	public String getOrt();
	
	public String getLand();
	
	public String getBild();
}
