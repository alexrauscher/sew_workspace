/******************************************************************************
 **                                                                          **
 **        In dieser Datei sollten keine Änderungen notwendig sein!!         **
 **                                                                          **
 *****************************************************************************/

// Namensraum und Abhängigkeiten des Moduls festlegen
// Namensraum und Abhängigkeiten des Moduls festlegen
var app = angular.module("top42", []);


// Konfigurieren
app.config(function ($logProvider) {
  // $log.debug() wirksam werden lassen
  $logProvider.debugEnabled(true);
});
