  $(function() { initialize(); });
  function initialize() {
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
  }
