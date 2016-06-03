/******************************************************************************
 **                                                                          **
 **           In dieser Datei den/die Controller implementieren!!            **
 **                                                                          **
 *****************************************************************************/
app.controller("ListController", function($scope, RestService, $log) {
	$scope.classTitel = "btn btn-default navbar-btn";
	$scope.classZustimmung = "btn btn-default navbar-btn";
	
	$scope.hideIcons = function() {
		$scope.bildSpeichern = true;
	}
	
	$scope.titelSuchen = function(suchtext) {
		var suchUrl = "/songs/search/findByTitelContainingIgnoreCaseOrderByTitelAsc?titel="+ suchtext;
		console.log(suchUrl);
		RestService
		.get(suchUrl, {size:10})
		.then(seiteInsScope, fehlerInsScope);
	}
	
	$scope.findBy = function (url, suchtext){
		var suchUrl;
		if(url === "titel"){
			suchUrl = "/songs/search/findByTitelContainingIgnoreCaseOrderByTitelAsc?titel="+ suchtext;
			$scope.classZustimmung = "btn btn-default navbar-btn";
			$scope.classTitel = "btn btn-success navbar-btn";
		}
		if(url === "zustimmung"){
			suchUrl = "/songs/search/findByTitelContainingIgnoreCaseOrderByZustimmungDescTitelAsc?titel="+ suchtext;
			$scope.classZustimmung = "btn btn-success navbar-btn";
			$scope.classTitel = "btn btn-default navbar-btn";
		}
		RestService
		.get(suchUrl, {size:10})
		.then(seiteInsScope, fehlerInsScope);
	}
	
	$scope.geheZu = function(ziel) {
		$scope.seite[ziel]()
			.then(seiteInsScope, fehlerInsScope);
	}
	
	function seiteInsScope(seite) {
		$log.debug("ListController, Seite:", seite);
		$scope.seite = seite;
		delete $scope.fehler;
	}
	
	function fehlerInsScope(fehler) {
		$log.debug("ListController, Fehler:", fehler);
		$scope.fehler = fehler;
	}
	
	$scope.getSongs = function() {
		songsLaden();
		$scope.suchtext = "";
	}
	
	function songsLaden() {
		RestService
		.get("songs", {size:10})
		.then(seiteInsScope, fehlerInsScope);
	}
	
	songsLaden();
	
	$scope.bearbeiten = function(satz) {
		$scope.inBearbeitung = satz;
	};
		  
	$scope.speichern = function(satz) {
		delete $scope.inBearbeitung;
		satz.speichern().then(satz.aktualisieren, fehlerInsScope);
	}
	
	$scope.satzAendern = function(satz, bewertung) {
		console.log(satz);
		satz.inhalt.vote = bewertung;
		$scope.speichern(satz);
	}
});