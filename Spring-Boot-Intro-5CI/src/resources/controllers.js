app.controller("LoginController",function($scope, RestService, $log) {
  
  $scope.anmeldeDialog = function() {
    delete $scope.fehler;
    $scope.login = {};
  };
  
  
  $scope.anmelden = function() {
    RestService.anmelden($scope.login.name, $scope.login.passwort)
      .then(function(response) {
        delete $scope.login;
        delete $scope.fehler;
        $scope.angemeldet = response.name;
      }, $scope.fehlerInsScope);
  };
  
  
  $scope.abmelden = function() {
    RestService.abmelden();
    delete $scope.angemeldet;
    delete $scope.login;
    delete $scope.fehler;
  };

  
  /**
   * Bringt das angegebene Fehler-Objekt in das $scope.
   */
  $scope.fehlerInsScope = function(fehler) {
    $log.debug("LoginController, Fehler: ", fehler);
    $scope.fehler = fehler;
  };
  
});


app.controller("ListeController", function($scope, RestService, $log) {
  
  /**
   * Geht zur ersten, vorigen, nächsten oder letzten Seite
   */
  $scope.geheZu = function(ziel) {
    $scope.seite[ziel]()
      .then(seiteInsScope, $scope.fehlerInsScope);
  };
  
  
  /**
   * Macht den angegebenen Satz bearbeitbar.
   */
  $scope.bearbeiten = function(satz) {
    $scope.inBearbeitung = satz;
    satz.titel = satz.inhalt.titel;
  };
  
  
  /**
   * Aktualisiert die Seite und beendet die Bearbeitung des derzeit
   * bearbeiteten Satzes.
   */
  $scope.beenden = function() {
    $scope.seite.aktualisieren()
      .then(function(seite) {
        seiteInsScope(seite);
        $scope.fehlerInsScope();
        delete $scope.inBearbeitung;
      }, $scope.fehlerInsScope);
  };
  
  
  /**
   * Speichert den derzeit bearbeiteten Satz, beendet die Bearbeitung und 
   * aktualisiert die Seite. Schlägt das Speichern fehl, so bleibt der Satz 
   * in Bearbeitung.
   */
  $scope.speichern = function() {
    if ($scope.inBearbeitung) {
      $scope.inBearbeitung.inhalt.titel = $scope.inBearbeitung.titel;
      $scope.inBearbeitung.speichern()
      .then($scope.beenden, $scope.fehlerInsScope);
    }
  };
  
  
  /**
   * Löscht den angegebenen Satz und aktualisiert die Seite.
   */
  $scope.entfernen = function(satz) {
    $log.debug("BiblioController, entfernen ", satz);
    satz.entfernen()
      .then($scope.seite.aktualisieren)
      .then(seiteInsScope, $scope.fehlerInsScope);
  };
  
 
  /**
   * Liefert den REST-ID des angegebenen Satzes
   */
  $scope.id = function(satz) {
    return /\d+$/.exec(satz.inhalt._links.self.href)[0];
  };
  
  
  /**
   * Bringt die angegebene Seite in das $scope und entfernt einen etwaigen 
   * Fehler aus dem $scope.
   */
  function seiteInsScope(seite) {
    $log.debug("BiblioController, Seite: ", seite);
    $scope.seite = seite;
    delete $scope.fehler;
  }
  
  
  // Erste Seite von Büchern laden
  function ersteSeite() {
    RestService
      .get("books", { size: 5 })
      .then(seiteInsScope, $scope.fehlerInsScope);
  }
  
  $scope.$watch("angemeldet", ersteSeite);
  
});


app.controller("DetailsController", function($scope, $routeParams) {
  
  // Testen: Buch-ID ins Scope bringen
  $scope.buchId = $routeParams.buch_id;
  $scope.buchTitel = $routeParams.buch_titel;
  $scope.buchSeiten = $routeParams.buch_seiten;
  $scope.buchKurzf = $routeParams.buch_kurzf;
  $scope.buchAutor = $routeParams.buch_autor;
});


