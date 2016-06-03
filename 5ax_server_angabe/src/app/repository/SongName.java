package app.repository;

import org.springframework.data.rest.core.config.Projection;

import app.model.Song;

@Projection(types = Song.class)
public interface SongName {

	public String getName();
}
