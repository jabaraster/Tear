(function() {
	var uploadId = '${uploadId}';
	var callbackUrl = '${callbackUrl}';
	var formId = '${formId}';
	$(initialize);
	
	function initialize() {
		$('#' + uploadId).on('change', function() {
			Wicket.Ajax.ajax({ u: callbackUrl, m: 'POST', f: formId, mp: true, dummy: '' });
			return false;
		});
	}
})();
