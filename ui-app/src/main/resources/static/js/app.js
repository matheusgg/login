var appModule = angular.module('ui-app', ['ngStorage', 'ngRoute']);

appModule.service('SecurityService', ['$http', '$localStorage', function ($http, $localStorage) {
	var baseUrl = 'http://localhost:8080';
	var self = this;

	this.retrieveToken = function (tokenName) {
		return $localStorage[tokenName];
	};

	this.storeTokens = function (accessToken, refreshToken) {
		$localStorage.access_token = accessToken;
		$localStorage.refresh_token = refreshToken;
	};

	this.login = function (username, password, scope) {
		var data = {};
		if (username) {
			data = $.param({
				grant_type: 'password',
				scope: 'read write',
				username: username,
				password: password
			});
		} else {
			data = $.param({
				grant_type: 'refresh_token',
				refresh_token: self.retrieveToken('refresh_token'),
			});
		}

		var request = {
			method: 'POST',
			url: baseUrl + '/oauth/token',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
				'Authorization': 'Basic dHJ1c3Q6c2VjcmV0'
			},
			data: data
		};

		$http(request).then(function successCallback(response) {
			var data = response.data;
			self.storeTokens(data['access_token'], data['refresh_token']);
			self.getUserProfiles(scope);
		}, function errorCallback(response) {
			console.log(response);
			if (response.status === 400) {
				scope.message = 'User not found';
				scope.username = '';
				scope.password = '';
			}
		});
	};

	this.getUserProfiles = function (scope) {
		var request = {
			method: 'GET',
			url: baseUrl + '/users/me',
			headers: {
				'Authorization': 'Bearer ' + self.retrieveToken('access_token')
			}
		};

		$http(request).then(function successCallback(response) {
			var data = response.data;
			scope.username = data.name;
			data.authorities.forEach(function (entry) {
				scope.profiles.push(entry.authority);
			});
			scope.authenticated = true;
		}, function errorCallback(response) {
			console.log(response);
			var status = response.status + '';
			if (status.startsWith("4")) {
				self.login(null, null, scope)
			}
		});
	};

	this.logout = function () {
		delete $localStorage.access_token;
		delete $localStorage.refresh_token;
	}
}]);

appModule.controller('AppController', ['$scope', 'SecurityService', function ($scope, SecurityService) {
	this.defineVariables = function () {
		$scope.username = '';
		$scope.password = '';
		$scope.message = '';
		$scope.profiles = [];
		$scope.authenticated = false;
	};

	this.loadUser = function () {
		if (SecurityService.retrieveToken('access_token')) {
			SecurityService.getUserProfiles($scope);
		}
	};

	this.login = function () {
		if ($scope.username && $scope.password) {
			SecurityService.login($scope.username, $scope.password, $scope);
		}
	};

	this.logout = function () {
		SecurityService.logout();
		this.defineVariables();
	};

	this.defineVariables();
	this.loadUser();
}]);