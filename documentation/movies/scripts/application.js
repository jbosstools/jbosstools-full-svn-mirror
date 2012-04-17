!function ($) {
	$(function(){
		var href=location.hash;
		if(href != "") {
			play(href.replace('#',''));
		}
	})
}(window.jQuery)

$(document).ready(function() {
	$('.videocontrol').click(function(e) {
		e.preventDefault(); //prevent href from actually loading
		var href = $(this).attr('href');
		location.hash = href;
		play(href);
	});
})

var videosJSON = {"videos":[
		{"id": "39743315", "ref": "marketplace"},
		{"id": "39606090", "ref": "installation"},
		{"id": "39607154", "ref": "jee6arquillian"},
		{"id": "39607711", "ref": "richfaces"},
		{"id": "39608223", "ref": "forge"},
		{"id": "39743919", "ref": "gwt"},
		{"id": "39608294", "ref": "hibreveng"},
		{"id": "39608326", "ref": "forgereveng"}
		]};

function play(videoRef) {
	var videoId = null;
	for(var i = 0; i < videosJSON.videos.length; i++) {
		if(videosJSON.videos[i].ref==videoRef) {
			videoId = videosJSON.videos[i].id;
			break;
		}
	}
	if(videoId != null) {
		var videoURL = "http://player.vimeo.com/video/" + videoId + "?portrait=0";
		$('#player').load(videoURL, function() {
			$('iframe').attr('src', videoURL);
		});
	}
	return;
}