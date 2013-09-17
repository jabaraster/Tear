    function callback(pHtml) {
      $('#container').html(pHtml);
    }
    $('#bt').click(function() {
//        $('#scripts').html('<script src="http://tear.herokuapp.com/rest/user/s?callback=callback"></script>');
      $('#scripts').html('<script src="http://localhost:8081/rest/jsonp/s?callback=callback"></script>');
      // TODO 上記URLのリクエスト処理に時間がかかっているときに、画面全体がロックされるようだとたいへんまずい. 要検証.
    });
