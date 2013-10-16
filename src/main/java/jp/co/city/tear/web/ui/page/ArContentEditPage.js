(function() {
    $(initialize);

    function initialize() {
        var similarityThreshold = $('form.saveForm input[type="range"].similarityThreshold');
        var similarityThresholdValueLabel = $('form.saveForm span.similarityThreshold');

        setSimilarityThresholdValueToLabel(similarityThreshold, similarityThresholdValueLabel);

        similarityThreshold.on('change', function() {
            setSimilarityThresholdValueToLabel(similarityThreshold, similarityThresholdValueLabel);
        });
    }

    function setSimilarityThresholdValueToLabel(pSimilarityThreshold, pSimilarityThresholdValueLabel) {
        var val = pSimilarityThreshold.val() - 0; // numberに変換
        if (isNaN(val)) {
            pSimilarityThresholdValueLabel.text('NaN');
            return;
        }
        var absVal = Math.abs(val);
        var text = toText(absVal);
        var sign = val < 0 ? '-' : '+';
        pSimilarityThresholdValueLabel.text(sign + text);
    }

    function toText(pNumber) {
        var s = (Math.floor(pNumber * 100) / 100) + ''; // Safari対策. 0.70に誤差が付与されるので、小数点第３位を切り捨てる.
        switch (s.length) {
        case 1: // '0' or '1'
            return s + '.00';
        case 3: // '0.1' のようなケース
            return s + '0';
        case 4: // '0.15' のようなケース
            return s;
        default: // 想定外
            return s;
        }
    }
})();