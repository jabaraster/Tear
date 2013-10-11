(function() {
    $(initialize);

    function initialize() {
        var similarityThreshold = $('form.saveForm input[type="range"].similarityThreshold');
        var similarityThresholdValueLabel = $('form.saveForm span.similarityThreshold');

        similarityThresholdValueLabel.text(similarityThreshold.val());

        similarityThreshold.on('change', function() {
            var val = similarityThreshold.val() + '';
            if (val === '1') {
                similarityThresholdValueLabel.text('1.00');
            } else if (val === '0') {
                similarityThresholdValueLabel.text('0.00');
            } else if (val.length === 3) {
                similarityThresholdValueLabel.text(val + '0');
            } else {
                similarityThresholdValueLabel.text(val);
            }
        });
    }
})();