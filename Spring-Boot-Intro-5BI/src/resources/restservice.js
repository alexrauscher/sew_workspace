app.service("RestService", function($http, $q, $log) {
  
  // Referenz auf dieses Service, die _überall_ in dieser Funktion gültig ist
  var restService = this;
  
  // Tabellenname
  restService.BOOKS = "books";

  
  /**
   * Liefert einen neuen, leeren Satz für die angegebene Tabelle.
   */
  this.neuerSatz = function(tabelle) {
    return new Satz(tabelle);
  };
  
  
  /**
   * Liefert ein Promise auf die Seite bzw. den Datensatz mit dem angegebenen 
   * URL, je nachdem, ob der URL auf ein ganze Seite oder auf einen einzelnen
   * Datensatz zeigt.
   */
  this.get = function(url, params) {
    // Template-Parameter vom URL entfernen
    url = url.replace(/\{.*?\}/g, "");
    $log.debug("RestService: get()", url);
    
    return $http
      .get(url, { params: params })
      .then(function(response) {
        if (response.data.page) {
          // Eine Seite wurde geliefert
          return new Seite(response);
          
        } else {
          // Ein Satz wurde geliefert, den Tabellennamen ermitteln
          var tabelle = /([^\/]+)\/(?:\d+|search)/.exec(url)[1];
          return new Satz(tabelle, response.data);
        }
      }, fehlerAntwort);
  };
  
  
  /**
   * Erzeugt aus einer REST-Response eine Seite von Datensätzen für einen
   * Controller.
   */
  function Seite(response) {
    var
      // Referenz auf dieses Objekt, die _überall_ in diesem Konstruktor gültig ist
      seite = this,
      // Tabellennamen aus der Response herausfinden (Hack)
      tabelle = Object.keys(response.data._embedded)[0],
      embedded = response.data._embedded[tabelle],
      links = response.data._links;
    
    // Properties
    seite.page = response.data.page;

    // Alle _embedded-Elemente als Satz-Objekte speichern
    seite.saetze = [];
    embedded.forEach(function(element) {
      seite.saetze.push(new Satz(tabelle, element));
    });

    
    /**
     * Liefert ein Promise auf eine aktualisierte Version von sich selbst.
     */
    seite.aktualisieren = function() {
      $log.debug("RestService: Seite.aktualisieren()");

      // In links.self.href gibt es keine Parameter, daher stattdessen
      // von links.first.href und seite.page.number ableiten, wenn vorhanden.
      var selfHref = links.first 
        ? links.first.href.replace(/\bpage=\d+/, "page=" + seite.page.number)
        : links.self.href;
      return restService.get(selfHref);
    };

    
    if (links.first) {
      /**
       * Liefert ein Promise auf die erste Seite, wenn es mehrere Seiten gibt
       */
      seite.erste = function() {
        $log.debug("RestService: Seite.erste()");
        return restService.get(links.first.href);
      };
    }


    if (links.last) {
      /**
       * Liefert ein Promise auf die letzte Seite, wenn es mehrere Seiten gibt
       */
      seite.letzte = function() {
        $log.debug("RestService: Seite.letzte()");
        return restService.get(links.last.href);
      };
    }

    
    if (links.next) {
      /**
       * Liefert ein Promise auf die nächste Seite, wenn vorhanden.
       */
      seite.naechste = function() {
        $log.debug("RestService: Seite.naechste()");
        return restService.get(links.next.href);
      };
    }
    
    
    if (links.prev) {
      /**
       * Liefert ein Promise auf die vorige Seite, wenn vorhanden.
       */
      seite.vorige = function() {
        $log.debug("RestService: Seite.vorige()");
        return restService.get(links.prev.href);
      };
    }

    $log.debug("RestService: Seite()", seite);
  }
  
  
  /**
   * Erzeugt aus einem _embedded-Element der angegebenen Tabelle einen Datensatz
   * für einen Controller.
   * Ist kein _embedded-Element angegeben, so wird ein leerer Datensatz erzeugt.
   */
  function Satz(tabelle, embeddedElement) {
    // Referenz auf dieses Objekt, die _überall_ in diesem Konstruktor gültig ist
    var satz = this;
    
    if (embeddedElement) {
      // Alle Properties übernehmen
      satz.inhalt = embeddedElement;
      
      /**
       * Liefert ein Promise auf eine aktualisierte Version von sich selbst.
       */
      satz.aktualisieren = function() {
        // Nur den _Inhalt_ dieses Satzes aktualisieren
        return $http
          .get(satz.inhalt._links.self.href)
          .then(function(response) {
            satz.inhalt = response.data;
            return satz;
          });
      };
      
      /**
       * Aktualisiert diesen Satz in der Datenbank und liefert ein Promise
       * auf sich selbst (mit aktualisiertem Inhalt).
       */
      satz.speichern = function() {
        $log.debug("RestService: Satz.speichern()", satz);
         
        return $http
          .put(
              satz.inhalt._links.self.href, 
              satz.inhalt,
              { headers: { "If-Match": satz.inhalt.etag } })
          .then(function(response) {
            satz.inhalt = response.data;
            return satz;
          }, fehlerAntwort);
      };
      
      /**
       * Entfernt diesen Satz aus der Datenbank und liefert ein leeres Promise.
       */
      satz.entfernen = function() {
        $log.debug("RestService: Satz.entfernen()", satz);

        return $http
          .delete(satz.inhalt._links.self.href)
          .then(function(response) {}, fehlerAntwort);
      };

    } else {
      // Neuer Satz, noch kein Inhalt
      satz.inhalt = {};

      /**
       * Fügt diesen Satz in die Datenbank ein und liefert ein Promise
       * auf die aktuelle Version dieses Satzes.
       */
      satz.speichern = function() {
        $log.debug("RestService: Satz.speichern()", satz);
        
        return $http
          .post("/" + tabelle, satz.inhalt)
          .then(function(response) {
            return new Satz(tabelle, response.data);
          }, fehlerAntwort);
      };
    }
    
    $log.debug("RestService: Satz()", satz);
  }
  
  
  /**
   * Erzeugt aus den unterschiedlichen Fehlerinformationen einer Response ein
   * einheitliches Fehler-Objekt. Bei Status 401 (unauthorized) werden die
   * (ungültigen) Authentifizierungsdaten gelöscht.
   */
  function Fehler(response) {
    this.status = response.status;
    this.statusText = response.statusText;
    this.fehlerTexte = [];
    
    if (response.data.errors) {
      // Validierungsfehler auf einzelner Spalte
      response.data.errors.forEach(function(error) {
        this.fehlerTexte.push(error.message);
      }, this);
      
    } else if (response.data.cause) {
      // Datenbankfehler, zu Grunde liegenden Fehler finden
      for (var cause = response.data.cause; cause.cause; cause = cause.cause) {}
      this.fehlerTexte.push(cause.message.replace(/:([\r\n]|.)*/g, ""));
    }
    
    $log.debug("RestService: Fehler()", this);
  }
  
  
  /**
   * Bereitet den Fehler zur Weiterleitung an eine andere Fehlerfunktion auf.
   */
  function fehlerAntwort(response) {
    return $q.reject(new Fehler(response));
  }
  
  
  /**
   * Ersetzt alle JSON-Date-Strings im angegebenen Objekt durch Date-Objekte.
   * Macht dies rekursiv auch in allen inneren Objekten.
   */
  function parseDates(data) {
    var
      dateRE = /(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(?:\.(\d{3}))?(Z|[+-]\d{2}:?\d{2})/,
      d;
      
    // Objekt angegeben und nicht null?
    if (data && typeof data === "object") {
      // Callback mit jedem Property-Namen aufrufen
      Object.keys(data).forEach(function(name) {
        var property = data[name];
        // JSON-Date-String gefunden?
        if (typeof property === "string" && (d = dateRE.exec(property))) {
          // Den String durch ein Date-Objekt ersetzen
          // Für Safari muss der Konstruktor so aufgerufen werden:
          data[name] = new Date(d[1], d[2]-1, d[3], d[4], d[5], d[6], d[7] || 0);
        } else {
          // JSON-Date-Strings rekursiv durch Date-Objekte ersetzen
          parseDates(property);
        }
      });
    }
    
    return data;
  }

  
  // Die Funktion parseDates() nach jeder $http-Response automatisch aufrufen,
  // bevor die Response als Ergebnis geliefert wird
  $http.defaults.transformResponse.push(parseDates);
  
});
