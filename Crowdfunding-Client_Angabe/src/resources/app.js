/******************************************************************************
 **                                                                          **
 **        In dieser Datei sollten keine Änderungen notwendig sein!!         **
 **                                                                          **
 *****************************************************************************/

// Namensraum und Abhängigkeiten des Moduls festlegen
var app = angular.module("crowdfunding", []);


// Konfigurieren
app.config(function ($logProvider) {
  // $log.debug() wirksam werden lassen
  $logProvider.debugEnabled(true);
});
