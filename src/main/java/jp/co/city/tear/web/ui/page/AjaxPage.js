function prepareFileUpload(pButtonId, pFormId, pCallbackUrl) {
//    Wicket.Ajax.ajax({ f: pFormId, c: pButtonId, e: 'click', m: 'post', u: pCallbackUrl, mp: true });
//    Wicket.Ajax.ajax({"f":pFormId,"mp":true,"u":pCallbackUrl,"e":"click","c":pButtonId,"sc":"customAjaxUploader","m":"POST"});
//return;
    $('#' + pButtonId).click(function() {
        send(pButtonId, pFormId, pCallbackUrl);
        return false;
    });
}

function send(pButtonId, pFormId, pCallbackUrl) {
    var fd = new FormData(document.getElementById(pFormId));
    fd.append("customAjaxUploader", "");
    $.ajax({
        async: true,
        url: /*$('#' + pFormId).attr('action'),*/  pCallbackUrl + "&wicket-ajax=true&wicket-ajax-baseurl=ajax%3F28",
        type: 'post',
        data: fd,
        contentType: false,
        processData: false,
//        dataType: "text", // これがないと、jQueryがレスポンスをパースしてしまうのでWicket.Ajax.process(pData)でエラーが起きてしまう.
        success: function(pData) {
//            Wicket.Ajax.process(pData); // TODO このコードで１回目はうまく動くのだが・・・
//            window.location.reload(); // TODO リロードでお茶を濁した方が安全か・・・
        },
        xhr: function() {
            var xhr = $.ajaxSettings.xhr();
            if (!xhr.upload) {
                return;
            }
            $(xhr.upload).on('progress', function(e) {
                e = e.originalEvent;
                console.log(e.loaded + '/' + e.total);
            });
            return xhr;
        }
    });
}
/*
    var root = $('div.progressArea');
    root.find('button').click(function() {
      var fd = new FormData();
      console.log($('li.progressable input[type="file"]').get(0));
      fd.append("data", $('li.progressable input[type="file"]').get(0).files[0]);
      $.ajax({
        async: true,
        url: '/rest/arContent/1/content',
        type: 'post',
        data: fd,
        contentType: false,
        processData: false,
        xhr: function() {
          var xhr = $.ajaxSettings.xhr();
          if (!xhr.upload) {
            return;
          }
          xhr.upload.addEventListener('progress',function(e){
            var prog = Math.max(0, Math.round(100 * e.loaded / e.total));
            var pb = root.find('.progressBar');
            pb.find('> div').css('width', prog + '%');
            pb.find('> div').text('　' + e.loaded + '/' + e.total + '(' + prog + '%)');
          });
          return xhr;
        }
      }).done(function(e) {
        console.log(e);
      });
      return false;
    });
 */
