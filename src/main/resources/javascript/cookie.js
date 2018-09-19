

    function setCookies() {
            var allElements = document.querySelectorAll('*[id]');
            var pair = "";
            for (var i = 4, n = allElements.length; i < n; i++) {//4-13, 29-33, 46

                if (allElements[i] instanceof HTMLInputElement) {
                    console.log(i + " " + allElements[i].id);
                    pair = allElements[i].id + "=" + getValue(allElements[i].id);
                    document.cookie = pair;//Set the cookies
                }
                else if (allElements[i] instanceof HTMLTextAreaElement) {
                    pair = allElements[i].id + "=" + getValue(allElements[i].id);
                    document.cookie = pair;
                }
                else if (allElements[i] instanceof HTMLImageElement) {
                    pair = allElements[i].id + "=" + getSrc(allElements[i].id);
                    document.cookie = pair;
                }
                else {//This screws things up.
                    pair = allElements[i].id + "=" + getText(allElements[i].id).replace(/["']/g, "").replace(/[%]/g, "");
                    document.cookie = pair;
                }
            }
    }

    function getValue(id) {
        return document.getElementById(id).value;
    }

    function getSrc(id){
        return document.getElementById(id).src;
    }

    function getText(id){
        return JSON.stringify(document.getElementById(id).innerText);
    }


    function getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    function restoreValue(key, val){
        document.getElementById(key).value = val;
    }

    function restoreSrc(key, val){
        document.getElementById(key).src = val;
    }

    function restoreText(key, val){
        document.getElementById(key).innerHTML = val;
    }

    // function deleteCookies(){
    //     for(var i = 1; i < 6; i++) {
    //         eraseCookie("champion"+i);
    //         eraseCookie("summoner"+i);
    //         eraseCookie("opponent"+i);
    //         eraseCookie("teamImg"+i);
    //         eraseCookie("mastery"+i);
    //         eraseCookie("oppImg"+i);
    //         eraseCookie("percentage"+i);
    //         eraseCookie("score"+i);
    //     }
    //     eraseCookie("totalScore");
    //     eraseCookie("userMessage");
    //
    //     setNoCookies();
    // }

    // function eraseCookie(name) {
    //     document.cookie = name + '=; Max-Age=0'
    // }

    function checkCookies() {
        for(var i = 1; i < 6; i++)//restore champions
        {
            restoreValue("champion" + i, getCookie("champion" + i));
            restoreValue("summoner"+i, getCookie("summoner"+i));
            restoreValue("opponent"+i, getCookie("opponent"+i));
            if(getCookie("teamImg"+i) != getCookie("mastery"+i))
            {//Pulls the wrong src initially for some reason.
                restoreSrc("teamImg" + i, getCookie("teamImg" + i));
                restoreSrc("mastery" + i, getCookie("mastery" + i));
                restoreSrc("oppImg" + i, getCookie("oppImg" + i))
            }
            if(getCookie("percentage" + i) != getCookie("score" + i))
            {
                restoreText("percentage" + i, "<b>"+getCookie("percentage" + i)+"%"+"</b>");
                restoreText("score" + i, getCookie("score" + i));
            }

        }
        if(getCookie("totalScore") != getCookie("userMessage"))
        {
            restoreText("totalScore", getCookie("totalScore"));
            restoreText("userMessage", "<b>"+getCookie("userMessage")+"</b>");
            if(getCookie("userMessage").valueOf() != "Based on (number) games, players with this score win (some percentage) of their games.")
                document.getElementById('userMessage').style.opacity = '1';//Make the message visible.
        }
    }


