package app.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
		path = "verlage", 
		collectionResourceRel = "verlage", 
		excerptProjection = VerlagFirma.class)
public interface VerlagRepository extends PagingAndSortingRepository<Verlag, Long> {

}
