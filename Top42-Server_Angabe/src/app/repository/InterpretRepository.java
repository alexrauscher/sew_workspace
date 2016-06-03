package app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import app.model.Interpret;


@RepositoryRestResource(
		path = "interpreten",
		collectionResourceRel = "interpreten",
		excerptProjection = InterpretName.class
	)
public interface InterpretRepository extends PagingAndSortingRepository<Interpret, Long>{

}
