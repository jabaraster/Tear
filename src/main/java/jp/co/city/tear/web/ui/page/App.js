var App = {};

App.focus = function(pId) {
	var tag = document.getElementById(pId);
	if (tag) {
		tag.focus();
	}
};