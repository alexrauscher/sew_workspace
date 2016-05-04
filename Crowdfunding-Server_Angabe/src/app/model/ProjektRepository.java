package app.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
	path = "projekte",
	collectionResourceRel = "projekte",
	excerptProjection = ProjektEigenschaften.class
)
public interface ProjektRepository extends PagingAndSortingRepository<Projekt, Long>{

	//public Page<Projekt> findByOrderByBeschreibungAndBeendetFalse(Pageable p);
}
