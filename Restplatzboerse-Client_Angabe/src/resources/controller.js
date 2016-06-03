/******************************************************************************
 **                                                                          **
 **           In dieser Datei den/die Controller implementieren!!            **
 **                                                                          **
 *****************************************************************************/

app.controller("RestplatzController", function($scope, RestService, $log){
	$scope.classBuchbar = "btn btn-default navbar-btn";
	$scope.classAlle = "btn btn-default navbar-btn";
	
	$scope.geheZu = function(ziel) {
		$scope.seite[ziel]()
			.then(seiteInsScope, fehlerInsScope);
	}
	
	$scope.sortieren = function(sortierung){
		var url;
		if(sortierung === 'buchbar' && $scope.freiePlaetze >= 1){
			$scope.sortBuchbar();
		}
		if(sortierung === 'alle'){
			url = "reisen/search/findByOrderByDestinationLandAscDestinationOrtAscBeginnAsc";
			$scope.classAlle = "btn btn-success navbar-btn";
			$scope.classBuchbar = "btn btn-default navbar-btn";
			RestService
			.get(url, { size: 5 })
			.then(seiteInsScope, $scope.fehlerInsScope);
		}
	}
	
	$scope.sortBuchbar = function() {
		url = "reisen/search/findByBuchbarTrueAndFreiePlaetzeGreaterThanEqualOrderByBeginnAsc?freiePlaetze=" + $scope.freiePlaetze;
		$scope.classBuchbar = "btn btn-success navbar-btn";
		$scope.classAlle = "btn btn-default navbar-btn";
		if(url != null){
			RestService
			.get(url, { size: 5 })
			.then(seiteInsScope, $scope.fehlerInsScope);
		}
	}
	
	$scope.reset = function() {
		$scope.freiePlaetze = "";
	}
	
	$scope.bearbeiten = function(satz){
		$scope.inBearbeitung = satz;
		$scope.auswahl = [];
		for(var i = 1; i<= satz.inhalt.freiePlaetze; i++){
			$scope.auswahl.push(i);
		}
	}
	
	$scope.beenden = function(){
		delete $scope.inBearbeitung;
	};
	
	$scope.speichern = function(satz, item){
		satz.inhalt.freiePlaetze -= item;
		satz.speichern()
			.then(satz.aktualisieren, $scope.fehlerInsScope);
		delete $scope.inBearbeitung;
	}
	
	function seiteInsScope(seite){
		$log.debug("RestplatzController, Seite: ", seite);
		$scope.seite = seite;
		delete $scope.fehler;
	}
	
	function fehlerInsScope(fehler){
		$log.debug("RestplatzController, Fehler: ", fehler);
		$scope.fehler = fehler;
	}
	
	function ersteSeite (){ //statt reisen eigene Tabelle pls.
		RestService
		.get("reisen", { size: 5 })
		.then(seiteInsScope, $scope.fehlerInsScope);
	}
	
	ersteSeite();
});