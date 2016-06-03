package app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import app.model.Benutzer;


@RepositoryRestResource(
		path = "benutzer", 
		collectionResourceRel = "benutzer", 
		excerptProjection = BenutzerName.class,
		exported = false)
public interface BenutzerRepository extends PagingAndSortingRepository<Benutzer, Long> {

	public Benutzer findByNameIgnoreCase(String name);
	
}
