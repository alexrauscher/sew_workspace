package app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import app.model.Interpret;

@RepositoryRestResource(
		path = "interpreten",
		collectionResourceRel = "interpreten")
public interface InterpretRepo extends PagingAndSortingRepository<Interpret, Long> {

}
