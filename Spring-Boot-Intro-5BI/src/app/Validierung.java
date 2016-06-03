package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.validation.Validator;


/**
 * Bewirkt, dass bei Validierungsfehlern statt des HTTP-Status 500 (server error)
 * der HTTP-Status 400 (bad request) mit genauen Fehlerinformationen ausgeliefert
 * wird.
 * 
 * @author F. Kasper, ksp@htl.rennweg.at
 */
@Configuration
public class Validierung extends RepositoryRestMvcConfiguration {

    @Autowired
    private Validator validator;

    @Override
    protected void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
    }
    
}