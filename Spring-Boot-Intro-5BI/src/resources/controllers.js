app.controller("BiblioController",function($scope, RestService, $log) {
    
  /**
   * Geht zur ersten, vorigen, nächsten oder letzten Seite
   */
  $scope.geheZu = function(ziel) {
    $scope.seite[ziel]()
      .then(seiteInsScope, fehlerInsScope);
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
        delete $scope.inBearbeitung;
      }, fehlerInsScope);
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
      .then($scope.beenden, fehlerInsScope);
    }
  };
  
  
  /**
   * Löscht den angegebenen Satz und aktualisiert die Seite.
   */
  $scope.entfernen = function(satz) {
    $log.debug("BiblioController, entfernen ", satz);
    satz.entfernen()
      .then($scope.seite.aktualisieren)
      .then(seiteInsScope, fehlerInsScope);
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
  
  
  /**
   * Bringt das angegebene Fehler-Objekt in das $scope.
   */
  function fehlerInsScope(fehler) {
    $log.debug("BiblioController, Fehler: ", fehler);
    $scope.fehler = fehler;
  }
  
  
  // Erste Seite von Büchern laden
  RestService
    .get(RestService.BOOKS, { size: 5 })
    .then(seiteInsScope, fehlerInsScope);
  
});









