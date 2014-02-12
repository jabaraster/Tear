$(function() {
	$('#logClearer').click(function() {
		$('#logArea').html('');
	});
});

function appendLog(pDescriptor, pArContentId) {
	var logAreaContainer = $('#logAreaContainer');
	var logArea = $('#logArea');
	logArea.append('<div>' + pDescriptor + 'がAR:' + pArContentId + 'を再生したログを登録しました.</div>');
	logAreaContainer.scrollTop(logArea.height());
}