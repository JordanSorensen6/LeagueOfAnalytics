var home = (function($) {
    function init(){
        click();
    }
    function click(){
        $('#click').on('click', function() {
            $.get('riot/get', function(data){
                alert(data.api);
            })
        });
    }
    return {
        init: init
    }
}(window.jQuery));