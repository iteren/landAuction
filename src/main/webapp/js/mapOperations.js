var map, infoWindow;
var markers = [];

var $loading = $('#loadingDiv');
$(document)
  .ajaxStop(function () {
    $loading.hide();
    $("#hContainer").removeClass("disabledbutton");
  });

function reloadPlot() {
	var pJson = document.getElementById('pJson');
	$loading.show();
    $("#hContainer").addClass("disabledbutton");
	$.ajax({
		type : "POST",
		url : window.location.pathname + 'rest/announcement/reload',
		data : pJson.innerHTML,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(resp) {
			searchLocations(resp);
		}
	});
}

function deletePlot() {
	var pJson = document.getElementById('pJson');
	$loading.show();
    $("#hContainer").addClass("disabledbutton");
	$.ajax({
		type : "POST",
		url : window.location.pathname + 'rest/announcement/delete',
		data : pJson.innerHTML,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(resp) {
			searchLocations(resp);
		}
	});
}

function parsePlotAdd() {
	var sourceLink = document.getElementById('sourceLink');
	$loading.show();
    $("#hContainer").addClass("disabledbutton");
	$.ajax({
		type : "POST",
		url : window.location.pathname + 'rest/announcement/parseAdd',
		data : JSON.stringify({
			sourceLink : sourceLink.value
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(resp) {
			addProcessPlotResp(resp);
		}
			
	});
}

function addPlot() {
	var title = document.getElementById('title');
	var description = document.getElementById('description');
	var price = document.getElementById('price');
	var ownerName = document.getElementById('ownerName.First');
	var ownerPhone = document.getElementById('ownerPhone');
	var ownerEmail = document.getElementById('ownerEmail');
	var plotCadNum = document.getElementById('plotCadNum');
	var sourceLink = document.getElementById('sourceLink');
	$loading.show();
    $("#hContainer").addClass("disabledbutton");
	$.ajax({
		type : "POST",
		url : window.location.pathname + 'rest/announcement/add',
		data : JSON.stringify({
			title : title.value,
			description : description.placeholder,
			price : price.value,
			owner : {
				name : ownerName.value,
				phone : ownerPhone.value,
				email : ownerEmail.value
			},
			sourceLink : sourceLink.value,
			plot : {
				cadNum : plotCadNum.value
			}
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(resp) {
			addProcessPlotResp(resp);
		}
	});
}

function addPlotFromStorage() {
	var plotCadNum = document.getElementById('modalCadNum').value;
	var accouncement = JSON.parse(localStorage.getItem('cadNum.dialog.announcement'));
	accouncement.plot.cadNum = plotCadNum;
	$loading.show();
    $("#hContainer").addClass("disabledbutton");
	$.ajax({
		type : "POST",
		url : window.location.pathname + 'rest/announcement/add',
		data : JSON.stringify(accouncement),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(resp) {
			addProcessPlotResp(resp);
		}
	});
	$('#cadNum-modal').modal('hide');
}

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center : {
			lat : 49.811,
			lng : 23.992
		},
		zoom : 15
	});

	// Try HTML5 geolocation.
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var pos = {
				lat : position.coords.latitude,
				lng : position.coords.longitude
			};

			map.setCenter(pos);

			map.addListener('click', function(e) {
				var inptLat = document.getElementById('inptLat')
				var inptLon = document.getElementById('inptLon')
				inptLat.value = e.latLng.lat()
				inptLon.value = e.latLng.lng()
			});
			map.addListener('idle', searchLocations);

		}, function() {
			handleLocationError(true, infoWindow, map.getCenter());
		});
	} else {
		// Browser doesn't support Geolocation
		handleLocationError(false, infoWindow, map.getCenter());
	}
}

function searchLocations(e) {
	clearMarkers();
	var bounds = map.getBounds();
	var NECorner = bounds.getNorthEast();
	var SWCorner = bounds.getSouthWest();
	var NWCorner = new google.maps.LatLng(NECorner.lat(), SWCorner.lng());
	var SECorner = new google.maps.LatLng(SWCorner.lat(), NECorner.lng());
	$.ajax({
		type : "POST",
		url : window.location.pathname + 'rest/announcement/list',
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		data : JSON.stringify({
			neCorner : NECorner,
			swCorner : SWCorner,
			nwCorner : NWCorner,
			seCorner : SECorner
		}),
		success : function(result) {
			if(result.success == true) {
				for (var i = 0; i < result.data.length; i++) {
					addMapMarker(result.data[i]);
				}
			} else {
				showErrorMessage(result.error.message);
			}
		}
	});
}

function addProcessPlotResp(resp) {
	if(resp.success == false && resp.error.action == 'cadNum.dialog.show') {
		localStorage.setItem('cadNum.dialog.announcement', JSON.stringify(resp.data));
		$('#cadNum-modal').modal('show');
		return;
	} 
	
	if(resp.success == false) {
		showErrorMessage(resp.error.message);
		return;
	}
	
	addMapMarker(resp.data);
	var plot = resp.data.plot;
	var pos = {
		lat : plot.lat,
		lng : plot.lng
	};
	map.setCenter(pos);
}

function addMapMarker(announcement) {
	var plot = announcement.plot;
	var pos = {
		lat : plot.lat,
		lng : plot.lng
	};
	var marker = new google.maps.Marker({
		position : pos,
		map : map,
		title : String('Ціна: ' + announcement.price + ' ' + announcement.priceCurrency + ' Розмір: ' + plot.size).toString(10),
		plot : plot
	});
	marker.addListener('click', function () {
		switchAddPlotTab('plotInfoForm');
		$("#plotInfo-tab").addClass("active");
		$("#addForm-tab").removeClass("active");
		$("#searchForm-tab").removeClass("active");
		var link = document.getElementById('plotInfoForm');
      	var importedContent = link.import;
		
		var description = document.getElementById('pDescription');
		var price = document.getElementById('pPrice');
		var plotCadNum = document.getElementById('pPlotCadNum');
		var sourceLink = document.getElementById('pSourceLink');
		var image = document.getElementById('pImage');
		var pJson = document.getElementById('pJson');
		
		pJson.innerHTML  = JSON.stringify(announcement);
		price.innerHTML  = announcement.price + ' ' + announcement.priceCurrency;
		description.innerHTML  = announcement.description;
		sourceLink.innerHTML = announcement.sourceLink;
		sourceLink.href  = announcement.sourceLink;
		plotCadNum.innerHTML  = plot.cadNum;
		image.src = announcement.imageUrl;
	});
	markers.push(marker);
}

function showErrorMessage(body) {
	  // Display error message to the user in a modal
	  $('#alert-modal-title').html('Error during api call');
	  $('#alert-modal-body').html(body);
	  $('#alert-modal').modal('show');
}

function setMapOnAll(map) {
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(map);
	}
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
	setMapOnAll(null);
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
	infoWindow.setPosition(pos);
	infoWindow.setContent(browserHasGeolocation ? 'Error: The Geolocation service failed.'
			: 'Error: Your browser doesn\'t support geolocation.');
	infoWindow.open(map);
}

function switchAddPlotTab(elementId) {
	var link = document.getElementById(elementId);
  	var importedContent = link.import;
	document.getElementById("addPlotMenu").innerHTML = importedContent.documentElement.innerHTML;
}

function resizeImage(img) {
	var childwidth = $("#pImage").width();
	var parentwidth = $("#pImage").parent().width();
	var percent = parentwidth / childwidth;
	img.width = parentwidth;
	img.height = img.height * percent;
}