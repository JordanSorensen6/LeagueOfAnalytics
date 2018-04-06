var home = (function($) {
    var champions = {};
    var summonerIds = {};

    function init() {
        summonerIdsLookup();
        championsLookup();
        championSelected();
    }

    function swapMap(json) {
        var ret = {};
        for(var key in json) {
            ret[json[key].toLowerCase()] = key;
        }
        return ret;
    }

    function championsLookup() {
        $.get('riot/champions', function(data) {
            champions = swapMap(data.keys);
        });
    }

    function getChampions() {
        return champions;
    }

    function summonerIdsLookup() {
        $('#populate').on('click', function() {
            var s1 = $('#summoner1').val();
            var s2 = $('#summoner2').val();
            var s3 = $('#summoner3').val();
            var s4 = $('#summoner4').val();
            var s5 = $('#summoner5').val();
            $.get('riot/summonerIds?s1=' + s1 + '&s2=' + s2 + '&s3=' + s3
                + '&s4=' + s4 + '&s5=' + s5, function(data) {
                summonerIds = data;
            });
        });
    }

    function getSummonerIds() {
        return summonerIds;
    }

    function championSelected() {
        $('.teamChamp').on('keyup', function() {
            var inputId = $(this).find('>:first-child').attr('id');
            var num = inputId.substring(inputId.length - 1);
            var summonerId = summonerIds['s' + num];
            var key = $('#' + inputId).val().replace(/ /g,'');
            if(champions.hasOwnProperty(key.toLowerCase())) {
                var championId = champions[key.toLowerCase()];
                $.get('riot/championMastery?summonerId=' + summonerId + '&championId=' + championId, function(data) {
                    $('#mastery' + num).val(data);
                });
            }
        })
    }

    return {
        init: init,
        getChampions: getChampions,
        getSummonerIds: getSummonerIds
    };
}(window.jQuery));