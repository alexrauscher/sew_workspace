package app.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
		path = "beteiligungen",
		collectionResourceRel = "beteiligungen"
	)
public interface BeteiligungRepositroy extends PagingAndSortingRepository<Beteiligung, Long>{

	public Page<Beteiligung> findByOrderByDatumAsc(Pageable p);
	
	public Page<Beteiligung> findByOrderByProjektAscDatumDesc(Pageable p);
}
