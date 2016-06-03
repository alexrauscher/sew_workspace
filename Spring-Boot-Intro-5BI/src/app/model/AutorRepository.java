package app.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
		path = "autoren", 
		collectionResourceRel = "autoren", 
		excerptProjection = AutorName.class)
public interface AutorRepository extends PagingAndSortingRepository<Autor, Long>{

}
