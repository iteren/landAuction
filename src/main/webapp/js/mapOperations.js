var map, infoWindow;
var markers = [];

var $loading = $('#loadingDiv');
$(document)
  .ajaxStop(function () {
    $loading.hide();
    $("#hContainer").removeClass("disabledbutton");
  });

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
		success : function(data) {
			addMapMarker(data);
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
		success : function(data) {
			addMapMarker(data);
		}
	});
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
			if(result.data) {
				for (var i = 0; i < result.data.length; i++) {
					addMapMarker(result.data[i]);
				}
			} 
			if(result.error) {
				showErrorMessage(result.error.message);
			}
		}
	});
}

function addMapMarker(announcement) {
	if(announcement.error) {
		showErrorMessage(announcement.error.message);
		return;
	}
	var plot = announcement.plot;
	var pos = {
		lat : plot.lat,
		lng : plot.lng
	};
	var marker = new google.maps.Marker({
		position : pos,
		map : map,
		title : String(announcement.price).toString(10),
		plot : plot
	});
	marker.addListener('click', function () {
		var link = document.getElementById('plotInfoForm');
      	var importedContent = link.import;
		document.getElementById("plotInfo").innerHTML = importedContent.documentElement.innerHTML;
		
		var description = document.getElementById('pDescription');
		var price = document.getElementById('pPrice');
		var plotCadNum = document.getElementById('pPlotCadNum');
		var sourceLink = document.getElementById('pSourceLink');
		var image = document.getElementById('pImage');
		
		price.innerHTML  = announcement.price + ' ' + announcement.priceCurrency;
		description.innerHTML  = announcement.description;
		sourceLink.innerHTML = announcement.sourceLink;
		sourceLink.href  = announcement.sourceLink;
		plotCadNum.innerHTML  = plot.cadNum;
		image.src = announcement.imageUrl;
	});
	markers.push(marker);
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

function showErrorMessage(body) {
	  // Display error message to the user in a modal
	  $('#alert-modal-title').html('Error during api call');
	  $('#alert-modal-body').html(body);
	  $('#alert-modal').modal('show');
}
