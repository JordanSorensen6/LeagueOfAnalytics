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
    for(var i = 0; i < 5; i++)
    {
        if(summoners[i].style.backgroundColor == "yellow") {
            summoners[i].style.backgroundColor = "white";
            if (summoner1 == null)
                summoner1 = summoners[i];
            else
                summoner2 = summoners[i];
        }
    }
    if(summoner1 != null && summoner2 != null)
    {
        var temp = summoner1.value;
        summoner1.value = summoner2.value;
        summoner2.value = temp;
    }
}