package app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import app.model.Song;

@RepositoryRestResource(
		path = "songs",
		collectionResourceRel = "songs",
		excerptProjection = SongName.class)
public interface SongRepo extends PagingAndSortingRepository<Song, Long> {

	public Page<Song> findByTitelContainingIgnoreCaseOrderByTitelAsc(@Param("Titel") String t, Pageable p); 
	
	public Page<Song> findByTitelContainingIgnoreCaseOrderByZustimmungDescTitelAsc(@Param("Titel") String t, Pageable p);
	
	
	
}
