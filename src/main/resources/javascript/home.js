var home = (function($) {
    var champions = {};
    var summonerIds = {};

    function init() {
        $('#nav-home').addClass('active');
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
             champions = swapMap(data);
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
            var summonerId = summonerIds[$('#summoner' + num).val()];
            var key = $('#' + inputId).val().replace(/ /g,'');
            //var d = new Date();
            if(champions.hasOwnProperty(key.toLowerCase())) {
                var championId = champions[key.toLowerCase()];
                $.get('riot/championMastery?summonerId=' + summonerId + '&championId=' + championId, function(data) {
                    //$('#mastery' + num).val(data);
                    var image = document.getElementById('mastery' + num);
                    console.log("updating mastery with: " + data);
                    image.src = "/resources/images/L"+data+".png";
                });
            }
        })
    }

    function anyChampSelection(){
        $('.teamChamp').on('keyup', function(){
            var id = $(this).find('>:first-child').attr('id');
            checkForMatchup('team', id);
        });
        $('.oppChamp').on('keyup', function(){
            var id = $(this).find('>:first-child').attr('id');
            checkForMatchup('opponent', id);
        });
    }

    function setChampionImage(id)
    {
        var newID;

        var champion = document.getElementById(id).value;

        if(id.includes('champion'))
            newID = id.replace('champion', 'teamImg');
        else
            newID = id.replace('opponent', 'oppImg');

        if(champions.hasOwnProperty(champion.toLowerCase())){
            var image = document.getElementById(newID);
            console.log("updating champion image");
            image.src = "/resources/images/champion/"+champion+".png";
        }
    }

    function checkForMatchup(team, id){
        var opponent = getOpponent(id);
        var role = getRole(id);
        var league = "gold";//Find a way to get this later.
        setChampionImage(id);

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
                    $.get('matchup/champions?c1=' + c1 + '&c2=' + c2 + '&role=' + role + '&league=' + league, function(data) {
                        console.log("data returned: " + data);
                        if(data == 'null%')//No data on the matchup.
                            data = "?";
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

                        getScore(role);
                    });
                }
            }

        }
        else {//Don't check for matchup info. The opponent is empty.
            console.log('No opponent.');
        }
    }

    function getScore(role)
    {
        if(role == 'Top')
            role = '1';
        else if(role == 'Jungle')
            role = '2';
        else if(role == 'Middle')
            role = '3';
        else if(role == 'ADC')
            role = '4';
        else if(role == 'Support')
            role = '5';

        var mastery = document.getElementById('mastery'+role).src;
        mastery = mastery.replace(location.port, '').replace(/\D/g,'');//get mastery number w/o port number.
        var matchup = document.getElementById('percentage'+role).value;
        matchup = matchup.replace("%", "");
        console.log('getting lane score with mastery: ' + mastery + ' matchup: ' + matchup);
        var score = document.getElementById('score'+role);
        $.get('matchup/score?mastery=' + mastery + '&matchup=' + matchup, function(data) {
            score.innerText = data;
            updateTotalScore();
            checkScoreDone();

        });
    }

    function checkScoreDone()
    {
        var p1 = document.getElementById('percentage1').value;
        var p2 = document.getElementById('percentage2').value;
        var p3 = document.getElementById('percentage3').value;
        var p4 = document.getElementById('percentage4').value;
        var p5 = document.getElementById('percentage5').value;

        console.log(p1 + " " + p2 + " " + p3 +" " + p4 + " " + p5);

        if(p1 != '' && p2 != '' && p3 != '' && p4 != '' && p5 != '')
            document.getElementById('userMessage').style.opacity = '1';

    }

    function updateTotalScore()
    {
        var score1 = document.getElementById('score1').innerText;
        var score2 = document.getElementById('score2').innerText;
        var score3 = document.getElementById('score3').innerText;
        var score4 = document.getElementById('score4').innerText;
        var score5 = document.getElementById('score5').innerText;

        var total = 0;
        total = parseFloat(score1) + parseFloat(score2) + parseFloat(score3) + parseFloat(score4) + parseFloat(score5);
        var totalScore = document.getElementById('totalScore');
        totalScore.innerText = "Total Score: "+total;

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

    var prevSelected;

    function populateSummonerNames() {
        var lines = document.getElementById("textBox").value.split('\n');
        for(var i = 0;i < lines.length;i++){
            lines[i] = lines[i].replace(" joined the lobby", "");
        }
        document.getElementById("summoner1").value = lines[0];
        document.getElementById("summoner2").value = lines[1];
        document.getElementById("summoner3").value = lines[2];
        document.getElementById("summoner4").value = lines[3];
        document.getElementById("summoner5").value = lines[4];
    }

    function markForSwap(id) {
        var summoners = document.getElementsByName("summoners");
        for(var i = 0; i < 5; i++)
        {
            if(summoners[i] != prevSelected)
                summoners[i].style.backgroundColor = "white";

        }
        var box  = document.getElementById(id);
        prevSelected = box;

        if(box.value != "")
            box.style.backgroundColor = "yellow";

    }

    function swapRoles() {
        var summoners = document.getElementsByName("summoners");
        var summoner1 = null;
        var summoner2 = null;
        var mastery1 = null;
        var mastery2 = null;
        var champion1 = null;
        var champion2 = null;
        for(var i = 0; i < 5; i++)
        {
            if(summoners[i].style.backgroundColor == "yellow") {
                summoners[i].style.backgroundColor = "white";
                if (summoner1 == null) {
                    summoner1 = summoners[i];
                    mastery1 = $('#mastery' + (i + 1));
                    champion1 = $('#champion' + (i + 1));
                }
                else {
                    summoner2 = summoners[i];
                    mastery2 = $('#mastery' + (i + 1));
                    champion2 = $('#champion' + (i + 1));
                }
            }
        }
        if(summoner1 != null && summoner2 != null)
        {
            var masteryTemp = mastery1.val();
            mastery1.val(mastery2.val());
            mastery2.val(masteryTemp);

            var championTemp = champion1.val();
            champion1.val(champion2.val());
            champion2.val(championTemp);

            var summonerTemp = summoner1.value;
            summoner1.value = summoner2.value;
            summoner2.value = summonerTemp;
        }
    }

    return {
        init: init,
        populateSummonerNames: populateSummonerNames,
        markForSwap: markForSwap,
        swapRoles: swapRoles,
        //TODO remove later - these are just for testing
        getChampions: getChampions,
        getSummonerIds: getSummonerIds
        //----------------------------------------------
    };
}(window.jQuery));
