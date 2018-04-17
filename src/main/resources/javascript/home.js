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
        // $.get('riot/champions', function(data) {
        //     champions = swapMap(data.keys);
        // });
        champions = {"89":"Leona","110":"Varus","111":"Nautilus","112":"Viktor","113":"Sejuani","114":"Fiora",
            "236":"Lucian","115":"Ziggs","117":"Lulu","90":"Malzahar","238":"Zed","91":"Talon","119":"Draven",
            "92":"Riven","516":"Ornn","96":"KogMaw","10":"Kayle","98":"Shen","99":"Lux","11":"MasterYi",
            "12":"Alistar","13":"Ryze","14":"Sion","15":"Sivir","16":"Soraka","17":"Teemo","18":"Tristana",
            "19":"Warwick","240":"Kled","120":"Hecarim","121":"Khazix","1":"Annie","122":"Darius","2":"Olaf",
            "245":"Ekko","3":"Galio","4":"TwistedFate","126":"Jayce","5":"XinZhao","127":"Lissandra","6":"Urgot",
            "7":"Leblanc","8":"Vladimir","9":"Fiddlesticks","20":"Nunu","21":"MissFortune","22":"Ashe",
            "23":"Tryndamere","24":"Jax","25":"Morgana","26":"Zilean","27":"Singed","28":"Evelynn","29":"Twitch",
            "131":"Diana","133":"Quinn","254":"Vi","497":"Rakan","134":"Syndra","498":"Xayah","136":"AurelionSol",
            "412":"Thresh","30":"Karthus","31":"Chogath","32":"Amumu","33":"Rammus","34":"Anivia","35":"Shaco",
            "36":"DrMundo","37":"Sona","38":"Kassadin","39":"Irelia","141":"Kayn","142":"Zoe","143":"Zyra",
            "266":"Aatrox","420":"Illaoi","145":"Kaisa","267":"Nami","421":"RekSai","268":"Azir","427":"Ivern",
            "429":"Kalista","40":"Janna","41":"Gangplank","42":"Corki","43":"Karma","44":"Taric","45":"Veigar",
            "48":"Trundle","150":"Gnar","154":"Zac","432":"Bard","157":"Yasuo","50":"Swain","51":"Caitlyn",
            "53":"Blitzcrank","54":"Malphite","55":"Katarina","56":"Nocturne","57":"Maokai","58":"Renekton",
            "59":"JarvanIV","161":"Velkoz","163":"Taliyah","164":"Camille","201":"Braum","202":"Jhin","203":"Kindred",
            "60":"Elise","61":"Orianna","62":"MonkeyKing","63":"Brand","64":"LeeSin","67":"Vayne","68":"Rumble",
            "69":"Cassiopeia","72":"Skarner","74":"Heimerdinger","75":"Nasus","76":"Nidalee","77":"Udyr","78":"Poppy",
            "79":"Gragas","222":"Jinx","101":"Xerath","102":"Shyvana","223":"TahmKench","103":"Ahri","104":"Graves","105":"Fizz",
            "106":"Volibear","80":"Pantheon","107":"Rengar","81":"Ezreal","82":"Mordekaiser","83":"Yorick",
            "84":"Akali","85":"Kennen","86":"Garen"};
        champions = swapMap(champions);
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
