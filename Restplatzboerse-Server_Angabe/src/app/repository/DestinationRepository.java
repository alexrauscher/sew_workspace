package app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import app.model.Destination;

@RepositoryRestResource(
		path = "destinationen", 
		collectionResourceRel = "destinationen",
		excerptProjection = DestinationsEigenschaften.class
)
public interface DestinationRepository extends PagingAndSortingRepository<Destination, Long> {
	
}
