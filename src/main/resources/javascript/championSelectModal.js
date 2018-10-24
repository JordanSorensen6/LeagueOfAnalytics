var championSelectModal = (function($) {

    var selectFor;
    var currentDataFilter = '*';

    function init(selectedFunction) {
        getChampionsWithTags();

        $('.champPic').on('click', function(){
            selectFor = $(this).children(':first').attr('id');
            $('#championSelectModal').modal('show');
        });

        setupFilters();
        setupSearch();
        championSelected(selectedFunction)
    }

    function championSelected(func) {
        $('#selectChampBtn').on('click', function() {
            var selected = getSelected();
            //func(selected, selectFor);//selected is champion name and SelectFor is src
            if(selectFor.toString().includes("teamImg"))//this updates everything
                func(selected, 'champion'+selectFor.toString().replace("teamImg", ""));
            else
                func(selected, 'opponent'+selectFor.toString().replace("oppImg", ""));
            $('.filter-options').children(':first').click();
            $('.selected-champion').first().removeClass('selected-champion');
            $('#championSearch').val('');
            $('#championSelectModal').modal('hide');
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
            } else {
                $('#championImages div').filter(function () {
                    $(this).toggle($(this).attr('id').indexOf(value) > -1)
                });
            }
        });
    }

    function setupChampionImages(champions) {
        var championContainer = $('#championImages');
        for(var name in champions) {
            var formattedName = formatChampionName(name);
            championContainer.append('' +
                '<div id="' + formattedName + '" class="champ-img ' + champions[name].toLowerCase() + '">' +
                '<img src="/resources/images/champion/' + formattedName + '.png" data-toggle="tooltip" data-placement="bottom" title="' + name + '">' +
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
        init: init
    };
}(window.jQuery));