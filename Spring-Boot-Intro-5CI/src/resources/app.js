// Namensraum und Abhängigkeiten des Moduls festlegen
var app = angular.module("biblioApp", [ "ngRoute" ]);


// Konfigurieren
app.config(function($logProvider) {
  // $log.debug() wirksam werden lassen
	$logProvider.debugEnabled(true);
});

// Routing
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.html5Mode(false).hashPrefix("");
  
  $routeProvider
      .when("/details/:buch_id&:buch_titel&:buch_seiten&:buch_kurzf&:buch_autor", { templateUrl: "details.html" })
      .otherwise({ templateUrl: "liste.html" });
});