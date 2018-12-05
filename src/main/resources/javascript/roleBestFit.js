var roleBestFit = (function($) {

    function init(updateFunc) {
        $('#roleBestFitBtn').on('click', function() {
            bestFit(updateFunc);
        })
    }

    function bestFit(updateFunc) {
        var champs = getOppChamps();
        var params = 'champs=' + champs[0];
        var i;
        for(i = 1; i < champs.length; i++) {
            params += '&champs=' + champs[i];
        }
        $.ajax({
            type: "GET",
            url: "/best-fit?" + params,
            dataType: "json",
            success: function(data) {
                setAllOppImagesToPlaceholder();
                for(var key in data) {
                    if(data.hasOwnProperty(key)) {
                        setChampToRole(updateFunc, key, data[key], home.findAvgRank());
                    }
                }
            }
        });
    }

    function setChampToRole(updateFunc, position, champion, avg) {
        switch(position) {
            case 'top':
                updateFunc(champion, 'opponent1');
                setOpponentMatchups(champion, avg, 'Top');
                break;
            case 'jungle':
                updateFunc(champion, 'opponent2');
                setOpponentMatchups(champion, avg, 'Jungle');
                break;
            case 'middle':
                updateFunc(champion, 'opponent3');
                setOpponentMatchups(champion, avg, 'Middle');
                break;
            case 'bottom':
                updateFunc(champion, 'opponent4');
                setOpponentMatchups(champion, avg, 'Bottom');
                break;
            case 'support':
                updateFunc(champion, 'opponent5');
                setOpponentMatchups(champion, avg, 'Support');
                break;
        }
    }

    function setAllOppImagesToPlaceholder() {
        $('.oppChampImg').each(function(index) {
           $(this).attr('src', '/resources/images/champion/placeholderOpponent.png');
        });
    }

    function getOppChamps() {
        var champions = [];
        $imgs = $('.oppChampImg').each(function(index) {
            var champ = getChampNameFromImage($(this).attr('src'))
            if(champ !== 'placeholderOpponent')
                champions.push(champ);
        });
        return champions;
    }

    function getChampNameFromImage(src)
    {
        var patt = new RegExp("\\/champion\\/.*\\.png");
        var result = patt.exec(src);
        result = result.toString().replace("/champion/", "").replace(".png", "");
        return result;
    }

    function formatWR(winRate){
        return Math.round((1-parseFloat(winRate.toString())) * 10000)/100;
    }

    function setOpponentMatchups(opponent, league, role){
        var matchups = [];
        $.get('matchup/opponent?opponent=' + opponent + '&league=' + league + '&role=' + role, function(data) {
            for(var i = 0; i < data.length; i++)
            {
                var tup = [data[i]["opponentChampion"].replace("MonkeyKing", "Wukong"), formatWR(data[i]["winRate"])];
                if(parseFloat(tup[1]) >= 50)
                    matchups.push(tup);
            }
            setupChampionCounters(matchups, role);
        });
    }

    return {
        init: init
    };
}(window.jQuery));