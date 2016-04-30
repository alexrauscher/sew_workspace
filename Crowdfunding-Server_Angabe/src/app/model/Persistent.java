package app.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


@MappedSuperclass
public class Persistent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Version
	private long version;
	
	
	public long getId() {
		return id;
	}
	
	public long getETag() {
		return version;
	}

}
