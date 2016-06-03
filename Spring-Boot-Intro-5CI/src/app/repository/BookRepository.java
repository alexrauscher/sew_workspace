package app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import app.model.Book;


public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
	
	@PreAuthorize("isAuthenticated()")
	public Page<Book> findAll(Pageable pageable);
	
	@PreAuthorize("isAuthenticated() and principal.id == #b.besitzer.id")
	public void delete(@Param("b") Book entity);
	
	public Page<Book> findByTitelContainingIgnoreCaseOrderByCircaSeiten(@Param("titel") String t, Pageable p);
	
	public Page<Book> findByCircaSeitenBetween(@Param("min") int min, @Param("max") int max, Pageable p);
	
}
