app.controller("CrowdfundingController", function($scope, RestService, $log) {
	$scope.neuerBet = true;
	
	$scope.neueBeteiligung = function() {
		$scope.neuerBet = false;
	}
	
	$scope.beenden = function() {
		$scope.seite.aktualisieren()
			.then(function(seite) {
				seiteInsScope(seite);
				$scope.neuerBet = true;
			});
	}
	
	$scope.speichern = function() {
		
	};
	
	$scope.geheZu = function(ziel){
		$scope.seite[ziel]()
			.then(seiteInsScope, fehlerInsScope);
	};
	
	$scope.findByDatum = function() {
		RestService
		.get("beteiligungen/search/findByOrderByDatumDesc", {size: 10})
		.then(seiteInsScope,fehlerInsScope);
	};
	
	function findByProjektDatum(seite) {
		seiteInsScope(seite);
	};
	
	function seiteInsScope(seite) {
		$log.debug("Seite:", seite);
		$scope.seite = seite;
		delete $scope.fehler;
	};
	
	function seiteInsScopeAll(seite) {
		$log.debug("Seite:", seite);
		$scope.seitenAll = seite;
	};
	
	function fehlerInsScope(fehler) {
		$scope.fehler = fehler;
	};
	
	RestService
		.get("beteiligungen", {size: 10})
		.then(seiteInsScope,fehlerInsScope);
	

	
	RestService
	.get("beteiligungen/search/findByOrderByProjektAscDatumDesc", {size: 10})
	.then(findByProjektDatum,fehlerInsScope);
	
	RestService
		.get("projekte/search/findByBeendetFalseOrderByBeschreibungAsc", {size: 1000})
		.then(seiteInsScopeAll,fehlerInsScope);
});