var championSelectModal = (function($) {

    var selectFor;
    var currentDataFilter = '*';

    function init(selectedFunction) {
        getChampionsWithTags();

        $('.champPic').on('click', function(){
            //var id = $(this).attr.id;
            selectFor = $(this).children(':first').attr('id');
            home.populateDataIfCookie(selectFor);
            //selectFor = $(this).children[1].attr('id');
            $('#championSelectModal').modal('show');
        });

        setupFilters();
        setupSearch();
        championSelected(selectedFunction);
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

    function formatWR(winRate){
        return Math.round((1-parseFloat(winRate.toString())) * 10000)/100;
    }

    function championSelected(func) {
        $('#selectChampBtn').on('click', function() {
            var selected = getSelected();
            //func(selected, selectFor);//selected is champion name and SelectFor is src
            if(selectFor.toString().includes("teamImg"))//this updates everything
                func(selected, 'champion'+selectFor.toString().replace("teamImg", ""));
            else {
                func(selected, 'opponent' + selectFor.toString().replace("oppImg", ""));
                setOpponentMatchups(selected, home.findAvgRank(), home.getRole(selectFor.toString()))
            }
            $('.filter-options').children(':first').click();
            $('.selected-champion').first().removeClass('selected-champion');
            $('#championSearch').val('');
            $('#championSelectModal').modal('hide');
        });
        $("#championSelectModal").on('hide.bs.modal', function() {
            // set modal to default state
            $('.filter-options').children(':first').click();
            $('.selected-champion').first().removeClass('selected-champion');
            $('#championSearch').val('');
        });
    }

    function setupFilters() {
        $('.filter-options a').on('click', function() {
            $('.filter-options .current-filter').removeClass('current-filter');
            $(this).addClass('current-filter');
            var selector = $(this).attr('data-filter');
            currentDataFilter = selector;
            if(selector === '*')
                $('#championImages > div').show();
            else {
                var $el = $('.' + selector).show();
                $('#championImages > div').not($el).hide();
                if(selector == "counters")
                {
                    $('.'+ (home.getRole(selectFor).toLowerCase())).show();
                }
            }
            $('#championSearch').trigger('keyup');
        });
    }

    function formatChampionName(name) {
        return name.toLowerCase().replace(/[^a-z]/g, "");
    }

    function setupSearch() {
        $('#championSearch').on('keyup', function() {
            var value = formatChampionName($(this).val());
            if(currentDataFilter !== '*') {
                $('#championImages div.' + currentDataFilter).filter(function () {
                    $(this).toggle($(this).attr('id').indexOf(value) > -1)
                });
                if(currentDataFilter == "counters")
                {
                    $('#championImages div.' + home.getRole(selectFor).toLowerCase()).filter(function () {
                        $(this).toggle($(this).attr('id').indexOf(value) > -1)
                    });
                }
            } else {
                $('#championImages div').filter(function () {
                    $(this).toggle($(this).attr('id').indexOf(value) > -1)
                });
            }
        });
    }

    function championsContainsIndex(champions, contains)
    {
        for(var index in champions)
            if(champions[index][0].toLowerCase() == contains)
                return index;
        return -1;
    }

    function clearOldRoleAndToolTip(role){
        var championContainer = $('#championImages');
        for(var i = 0; i < championContainer[0].children.length; i++)
        {
            if(championContainer[0].childNodes[i].attributes[1].nodeValue.includes(role.toLowerCase())) {
                championContainer[0].childNodes[i].attributes[1].nodeValue = championContainer[0].childNodes[i].attributes[1].nodeValue.replace(" " + role.toLowerCase(), "");
                var el = $('div#'+championContainer[0].childNodes[i].id).children()[0];
                var champName = "";
                for(var i = 0; i < $(el).attr("data-original-title").split(" ").length; i++)
                {
                    var s = $(el).attr("data-original-title").split(" ")[i]+" ";
                    if(s.includes("("))
                        break;
                    champName += s;
                }
                $(el).tooltip("disable");
                $(el).attr("data-original-title",  champName);
                $(el).tooltip("enable");
            }
        }
    }

    function setupChampionCounters(champions, role){
        clearOldRoleAndToolTip(role);
        var championContainer = $('#championImages');
        for(var i = 0; i < championContainer[0].children.length; i++)
        {
            var index = championsContainsIndex(champions, championContainer[0].childNodes[i].id)
            if(index != -1) {
                championContainer[0].childNodes[i].attributes[1].nodeValue = championContainer[0].childNodes[i].attributes[1].nodeValue + " " + role.toLowerCase();
                var el = $('div#'+championContainer[0].childNodes[i].id).children()[0];
                $(el).tooltip("disable");
                $(el).attr("data-original-title",  $(el).attr("data-original-title") + " (" + role + ": " + champions[index][1] + "%)\n");
                $(el).tooltip("enable");
            }
        }

    }

    function setupChampionImages(champions) {
        var championContainer = $('#championImages');
        for(var name in champions) {
            var formattedName = formatChampionName(name);
            championContainer.append('' +
                '<div id="' + formattedName + '" class="champ-img ' + champions[name].toLowerCase() + '">' +
                '<img src="/resources/images/champion/' + formattedName + '.png" data-toggle="tooltip" data-html="true" data-placement="bottom" title="' + name + '">' +
                '</div>');
        }
        $('[data-toggle="tooltip"]').tooltip();

        $('.champ-img').on('click', function() {
            if($(this).hasClass('selected-champion'))
                $(this).removeClass('selected-champion');
            else {
                $('.selected-champion').removeClass('selected-champion');
                $(this).addClass('selected-champion');
            }
        });
    }

    function getChampionsWithTags() {
        $.get('riot/champion-tags', function(data) {
            setupChampionImages(setupTagMap(data));
        });
    }

    function setupTagMap(data) {
        var i, champTagMap;
        champTagMap = {};
        for(i = 0; i < data.length; i++) {
            champTagMap[data[i][0]] = data[i][1];
        }
        return champTagMap;
    }

    function getSelected() {
        return $('.selected-champion').first().attr('id');
    }

    return {
        init: init,
        setOpponentMatchups: setOpponentMatchups
    };
}(window.jQuery));