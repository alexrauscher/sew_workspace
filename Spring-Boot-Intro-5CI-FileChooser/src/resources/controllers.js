app.controller("BiblioController",function($scope, RestService, $log) {
  
  // Der zuletzt von der Datenbank gelesene Satz
  var zuletztGeholt;
  
  /**
   * Lädt das nächste Buch
   */
  $scope.naechstes = function() {
    $scope.satz.naechster().then(satzInsScope, fehlerInsScope);
  };
  
  
  /**
   * Lädt das vorige Buch
   */
  $scope.voriges = function() {
    $scope.satz.voriger().then(satzInsScope, fehlerInsScope);
  };
  
  
  /**
   * Entfernt das aktuelle Buch
   */
  $scope.entfernen = function() {
    zuletztGeholt = null;
    $scope.satz.entfernen().then(satzInsScope, fehlerInsScope);
  };
  
  
  /**
   * Speichert das aktuelle (neue oder geänderte) Buch
   */
  $scope.speichern = function() {
    $scope.satz.speichern().then(satzInsScope, fehlerInsScope);
  }
  
  
  /**
   * Lädt ein neues, leeres Buch
   */
  $scope.neues = function() {
    $scope.satz = RestService.neuerSatz(RestService.BOOKS);
  };

  
  /**
   * Lädt das zuletzt geladene Buch nochmals
   */
  $scope.abbrechen = function() {
    satzInsScope();
    fehlerInsScope();
  };
  
  
  /**
   * Bringt ein Satz-Objekt in das $scope nach folgender Reihung:
   * 
   * 1. den soeben von der Datenbank geholten Satz; falls nicht vorhanden:
   * 2. den zuletzt von der Datenbank geholten Satz; falls nicht vorhanden:
   * 3. einen neuen, leeren Datensatz
   * 
   * Entfernt das Fehler-Objekt aus dem $scope.
   */
  function satzInsScope(satz) {
    $log.debug("BiblioController, Satz: ", satz);
    
    // $dirty-Zustand des Formulars löschen
    $scope.formular.$setPristine();
    
    if (satz && satz.aktualisieren) {
      // Den soeben von der Datenbank geholten Satz anzeigen
      $scope.satz = satz;
      zuletztGeholt = satz;
      
    } else if (zuletztGeholt) {
      // Den zuletzt von der Datenbank geholten Satz anzeigen
      zuletztGeholt.aktualisieren().then(satzInsScope);
      zuletztGeholt = null;
      
    } else {
      // Einen neuen, leeren Satz anzeigen
      $scope.neues();
    }
    
    delete $scope.fehler;
  }  
  
  
  /**
   * Bringt das angegebene Fehler-Objekt in das $scope.
   */
  function fehlerInsScope(fehler) {
    $log.debug("BiblioController, Fehler: ", fehler);
    
    $scope.fehler = fehler;
  }
  
  
  // Erstes Buch laden
  RestService.ersterSatz(RestService.BOOKS).then(satzInsScope, fehlerInsScope);
  
});

