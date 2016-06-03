package app.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;


public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
	
	// POST, PUT, PATCH verhindern (-> Status 405, Method not allowed)
	@Override
	@RestResource(exported = false)
	public <S extends Book> S save(S entity);
	
	public Page<Book> findByTitelContainingIgnoreCaseOrderByCircaSeiten(@Param("titel") String t, Pageable p);
	
	public Page<Book> findByCircaSeitenBetween(@Param("min") int min, @Param("max") int max, Pageable p);
	
}
