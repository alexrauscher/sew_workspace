// Namensraum und Abhängigkeiten des Moduls festlegen
var app = angular.module("biblioApp", []);


// Konfigurieren
app.config(function($logProvider) {
  // $log.debug() wirksam werden lassen
	$logProvider.debugEnabled(true);
});
