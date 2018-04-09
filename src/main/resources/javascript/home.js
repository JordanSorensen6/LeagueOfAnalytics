var home = (function($) {
    var champions = {};
    var summonerIds = {};

    function init() {
        summonerIdsLookup();
        championsLookup();
        championSelected();
        anyChampSelection();
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
                    console.log("Entered function with value: " + data);
                });
            }
        })
    }

    function anyChampSelection(){
        $('.teamChamp').on('keyup', function(){
            var id = $(this).find('>:first-child').attr('id');
            checkForMatchup('team', id);
        })
        $('.oppChamp').on('keyup', function(){
            var id = $(this).find('>:first-child').attr('id');
            checkForMatchup('opponent', id);
        })
    }

    function checkForMatchup(team, id){
        var opponent = getOpponent(id);
        var role = getRole(id);

        if(document.getElementById(opponent).value != '')//Nonempty opponent. We can look for matchup.
        {
            var c1;
            var c2;
            if(id.includes('champion'))
            {
                c1 = document.getElementById(id).value;
                c2 = document.getElementById(opponent).value;
            }
            else
            {
                c1 = document.getElementById(opponent).value;
                c2 = document.getElementById(id).value;
            }

            c1 = c1.replace(/ /g,'');
            c2 = c2.replace(/ /g,'');

            if(champions.hasOwnProperty(c1.toLowerCase())){
                if(champions.hasOwnProperty(c2.toLowerCase())){
                    console.log('opponent nonempty value is: '+ document.getElementById(opponent).value);
                    $.get('matchup/champions?c1=' + c1 + '&c2=' + c2 + '&role=' + role, function(data) {
                        //console.log("data returned: " + data);
                        if(role == 'Top')
                            document.getElementById("percentage1").value = data;
                        else if(role == 'Jungle')
                            document.getElementById("percentage2").value = data;
                        else if(role == 'Middle')
                            document.getElementById("percentage3").value = data;
                        else if(role == 'ADC')
                            document.getElementById("percentage4").value = data;
                        else if(role == 'Support')
                            document.getElementById("percentage5").value = data;
                    });
                }
            }

        }
        else {//Don't check for matchup info. The opponent is empty.
            console.log('No opponent.');
        }
    }

    function getRole(teamAndRole)
    {
        var role;
        if(teamAndRole.includes('1'))
            role = 'Top';
        else if(teamAndRole.includes('2'))
            role = 'Jungle';
        else if(teamAndRole.includes('3'))
            role = 'Middle';
        else if(teamAndRole.includes('4'))
            role = 'ADC';
        else if(teamAndRole.includes('5'))
            role = 'Support';

        return role;
    }

    function getOpponent(teamAndRole)
    {
        if(teamAndRole.toString().includes('champion')) {
            teamAndRole = teamAndRole.replace('champion', 'opponent');
            return teamAndRole;
        }
        else{
            teamAndRole = teamAndRole.replace('opponent', 'champion');
            return teamAndRole;
        }
    }

    return {
        init: init,
        getChampions: getChampions,
        getSummonerIds: getSummonerIds
    };
}(window.jQuery));