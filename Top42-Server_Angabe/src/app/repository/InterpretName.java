package app.repository;

import org.springframework.data.rest.core.config.Projection;

import app.model.Interpret;

@Projection(types = Interpret.class)
public interface InterpretName {
	public String getName();
}
