package app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import app.model.Song;

public interface SongRepository extends PagingAndSortingRepository<Song, Long>{

	public Page<Song> findByTitelContainingIgnoreCaseOrderByTitelAsc(@Param("titel") String t, Pageable p);
	
	public Page<Song> findByTitelContainingIgnoreCaseOrderByZustimmungDescTitelAsc(@Param("titel") String x, Pageable p);
}
