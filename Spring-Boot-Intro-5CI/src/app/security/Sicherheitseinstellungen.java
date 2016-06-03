package app.security;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import app.model.Benutzer;
import app.repository.BenutzerRepository;


/**
 * Legt folgende Sicherheitseinstellungen für diese Anwendung fest:
 * <ul>
 * <li>Alle Pfade außer {@code /profile/**} sind ohne Authentifizierung 
 *  zugänglich, das DB-Webinterface nur von {@code localhost} aus.</li>
 * <li>Es wird BASIC-Authentifizierung verwendet.
  *  <u>Achtung: unsicher über HTTP!</u></li>
 * <li>Ein Benutzer mit {@code test/test} ist festgelegt.</li>
 * <li>Es werden keine Cookies gesetzt.</li>
 * </ul>
 * 
 * Liefert die für {@linkplain CreatedBy <code>@CreatedBy</code>} und
 * {@linkplain LastModifiedBy <code>@LastModifiedBy</code>} benötigte 
 * Infrastruktur.
 * 
 * @author F. Kasper, ksp@htl.rennweg.at
 */
@Configuration
@EnableJpaAuditing(modifyOnCreate = false)
public class Sicherheitseinstellungen extends WebSecurityConfigurerAdapter {

	@Resource
	private BenutzerRepository benutzerRepository;
	
	
	/**
	 * Konfiguriert die HTTP-Sicherheitseinstellungen. Siehe die einzelnen
	 * Kommentare für Details. 
	 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	// Alle Pfade außer /profile/** sind ohne Authentifizierung zugänglich
    	// (das DB-Webinterface automatisch nur von localhost aus)
    	http.authorizeRequests()
    		.antMatchers("/profile/**").authenticated()
    		.anyRequest().permitAll();
    	
    	// HTTP-BASIC-Authentifizierung für @PreAuthorize und /profile/**
    	http.httpBasic();
    	    	
    	// Schon _vor_ der Authentifizierung als anonyme Benutzerin anmelden, da
    	// selbst @PreAuthorize("permitAll") irgendeine Authentifizierung benötigt
    	http.addFilterBefore(
    			new AnonymousAuthenticationFilter(getClass().getName()), 
    			BasicAuthenticationFilter.class);
    	
    	// Keine HTTP-Session erzeugen, keine Cookies setzen
    	http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Inhalte in IFRAMEs darstellen lassen (für DB-Webinterface nötig)
    	http.headers()
    		.frameOptions().disable();
    	
    	// Keine CSRF-Tokens verwenden (für DB-Webinterface nötig)
		http.csrf().disable();
    }

    
    /**
     * Definiert einen einzelnen Benutzer mit dem Namen "test" und dem
     * Passwort "test".
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth
    		// Benutzerinformationen unter dem Benutzernamen nachschlagen
    		.userDetailsService(new UserDetailsService() {
    			public UserDetails loadUserByUsername(String name) {
    				// Liefert den Benutzer zum Namen, oder null
    				Benutzer benutzer = benutzerRepository.findByNameIgnoreCase(name);
    				if (benutzer != null) {
    					return benutzer;
    				} else {
    					throw new UsernameNotFoundException(name);
    				}
    			}
    		})
    		
    		// Passworte in der DB müssen BCrypt-codiert sein 
    		.passwordEncoder(new BCryptPasswordEncoder());
    }

    
    /**
     * Liefert die für {@linkplain CreatedBy <code>@CreatedBy</code>} und
     * {@linkplain LastModifiedBy <code>@LastModifiedBy</code>} benötigte 
     * Infrastruktur.
     */
    @Bean
    public AuditorAware<BenutzerDetails> auditorProvider() {
    	return new AuditorAware<BenutzerDetails>() {
    		
    	    @Override
    	    public BenutzerDetails getCurrentAuditor() {
    	    	Authentication auth = 
    	    			SecurityContextHolder.getContext().getAuthentication();
    	    	return (auth == null || !auth.isAuthenticated())
    	    			? null
    					: (BenutzerDetails) auth.getPrincipal();
    	    }
    	};
    }
    
}
