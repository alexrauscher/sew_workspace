package app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import app.model.Reise;

@RepositoryRestResource(
		path = "reisen", 
		collectionResourceRel = "reisen"
)
public interface ReiseRepository extends PagingAndSortingRepository<Reise, Long> {

	public Page<Reise> findByOrderByDestinationLandAscDestinationOrtAscBeginnAsc(Pageable p);
	
	public Page<Reise> findByBuchbarTrueAndFreiePlaetzeGreaterThanEqualOrderByBeginnAsc(@Param("freiePlaetze") Integer f, Pageable p);
}
