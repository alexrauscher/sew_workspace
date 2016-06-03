app.service("RestService", function($http, $q, $log) {
  
  // Tabellenname
  this.BOOKS = "books";
  
  
  /**
   * Liefert einen neuen, leeren Satz für die angegebene Tabelle.
   */
  this.neuerSatz = function(tabelle) {
    return new Satz(tabelle);
  };
  
  
  /**
   * Liefert ein Promise auf den ersten Datensatz der angegebenen Tabelle.
   */
  this.ersterSatz = function(tabelle) {
    return get("/" + tabelle + "?size=1&sort=titel");
  };
  
  
  /**
   * Liefert ein Promise auf den Datensatz mit dem angegebenen URL, oder auf
   * einen leeren Datensatz, wenn für diesen URL kein Datensatz existiert.
   */
  function get(url) {
    // Template-Parameter vom URL entfernen
    url = url.replace(/\{.*?\}/g, "");
    $log.debug("RestService: get()", url);
    
    return $http
      .get(url)
      .then(function(response) {
        return new Satz(response);
      }, fehlerAntwort);
  };
  
  
  /**
   * Erzeugt aus einer REST-Response einen Datensatz für einen Controller.
   * Ist das Argument ein Tabellenname oder eine Response ohne Daten, so wird
   * ein leerer Datensatz erzeugt.
   */
  function Satz(response) {
    if (response.data && response.data.page.totalElements) {
      var
        // Tabellennamen aus der Response herausfinden (Hack)
        tabelle = Object.keys(response.data._embedded)[0],
        links = response.data._links,
        that = this;
      
      // Properties
      this.inhalt = response.data._embedded[tabelle][0];
      this.page   = response.data.page;
      
      // JSON-Date-Strings in Date-Objekte umwandeln
      for (var p in this.inhalt) {
        if (/\d{4}(-\d{2}){2}T\d{2}(:\d{2}){2}(\.\d{3})?(Z|[+-]\d{2}:?\d{2})/.test(this.inhalt[p])) {
          this.inhalt[p] = new Date(this.inhalt[p]);
        }
      }
      
      /**
       * Liefert ein Promise auf eine aktualisierte Version von sich selbst.
       */
      this.aktualisieren = function() {
        // Falls der sort-Parameter angegeben ist, gibt es in links.self.href 
        // keine Seitennummer, daher nicht links.self.href verwenden, sondern
        // nur den _Inhalt_ dieses Satzes ersetzen; die Links bleiben gleich.
        return $http
          .get(this.inhalt._links.self.href)
          .then(function(response) {
            that.inhalt = response.data;
            return that;
          });
      };
      
      if (links.next) {
        /**
         * Liefert ein Promise auf den nächsten Satz, wenn vorhanden.
         */
        this.naechster = function() {
          $log.debug("RestService: Satz.naechster()");
          return get(links.next.href);
        };
      }
      
      if (links.prev) {
        /**
         * Liefert ein Promise auf den vorigen Satz, wenn vorhanden.
         */
        this.voriger = function() {
          $log.debug("RestService: Satz.voriger()");
          return get(links.prev.href);
        };
      }
      
      /**
       * Aktualisiert diesen Satz in der Datenbank und liefert ein Promise
       * auf sich selbst (mit aktualisiertem Inhalt).
       */
      this.speichern = function() {
        $log.debug("RestService: Satz.speichern()", this);
         
        return $http
          .put(
              this.inhalt._links.self.href, 
              this.inhalt)
          .then(function(response) {
            that.inhalt = response.data;
            return that;
          }, fehlerAntwort);
      };
      
      /**
       * Entfernt diesen Satz aus der Datenbank und liefert ein Promise
       * auf denjenigen Satz, der sich nun an derselben Stelle befindet, oder
       * auf den vorhergehenden Satz, falls der letzte Satz entfernt wurde,
       * oder auf einen leeren Satz, falls der einzige Satz entfernt wurde.
       */
      this.entfernen = function() {
        $log.debug("RestService: Satz.entfernen()", this);

        return $http
          .delete(this.inhalt._links.self.href)
          .then(function(response) {
            // Letzter Satz entfernt?
            if (links.next) {
              // Nein, nächster Satz rückt nach
              // Workaround für den ab Spring Boot 1.3.0 fehlenden page-Parameter 
              // in links.self.href
              var page = /\bpage=(\d+)/.exec(links.next.href)[1];
              return get(links.next.href.replace(/\bpage=\d+/, "page=" + (page-1)));

            } else if (links.prev) {
              // Ja, zum vorigen Satz gehen
              return get(links.prev.href);
            
            } else {
              // Der einzige Satz wurde entfernt, Tabellennamen für neuen Satz
              // ermitteln
              var tabelle = /(?:\/([^\/\?]+))+/.exec(links.self.href)[1];
              return new Satz(tabelle);
            }
          }, fehlerAntwort);
      };

    } else {
      // Neuer Satz, noch kein Inhalt
      this.inhalt = {};

      /**
       * Fügt diesen Satz in die Datenbank ein und liefert ein Promise
       * auf den letzten Satz (das ist hoffentlich der neu eingefügte Satz).
       */
      this.speichern = function() {
        $log.debug("RestService: Satz.speichern()", this);
        var url;
        
        return $http
          .post("/" + response, this.inhalt)
          .then(function(response) {
            // URL zur ersten Seite bauen durch Ersetzen von /id
            url = response.data._links.self.href.replace(/\/\d+$/, "?size=1");
            return get(url);
          })
          .then(function(satz) {
            // URL zur letzten Seite bauen
            url += "&page=" + (satz.page.totalElements-1);
            return get(url);
          }, fehlerAntwort);
      };
    }
    
    $log.debug("RestService: Satz()", this);
  }
  
  
  /**
   * Erzeugt aus den unterschiedlichen Fehlerinformationen einer Response ein
   * einheitliches Fehler-Objekt.
   */
  function Fehler(response) {
    this.status = response.status;
    this.statusText = response.statusText;
    this.fehlerTexte = [];
    
    if (response.data.errors) {
      // Validierungsfehler auf einzelner Spalte
      response.data.errors.forEach(function(error) {
        this.fehlerTexte.push("'" + error.property + "' " + error.message);
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

});
