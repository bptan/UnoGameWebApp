
$(function () {
    var rootURL = "http://localhost:8080/uno/api";
    var gamesTemplate = Handlebars.compile($("#gamesTemplate").html());

    var gid;
    var userData = {
        storeUserDataInSession: function (userData) {
            var userObjectString = JSON.stringify(userData);
            window.sessionStorage.setItem('userObject', userObjectString)
        },
        getUserDataFromSession: function () {
            var userData = window.sessionStorage.getItem('userObject')
            return JSON.parse(userData);
        }
    };

    var promise = $.getJSON(rootURL + "/games/all");

    promise.done(function (result) {
        $("#all-games").append(gamesTemplate({games: result}));
    });
    var socket;





    $("#all-games").on("click", "[data-gameId]", function () {
        gid = $(this).attr("data-gameId");
        socket = new WebSocket("ws://localhost:8080/uno/games/" + gid + "/verysecrettable");
        socket.onopen = function () {
            console.log("Websocket is connected");
        };
        socket.onmessage = function (msg) {
            console.log(msg.data);
        }
        /*
         var promise = $.ajax({
         type:'PUT',
         url:rootURL+"/games/"+gid 
         });
         promise.done(function(result) {
         console.log("button clicked");
         window.location.replace("/uno/gametableview.html");
         userData.storeUserDataInSession(result);
         })
         console.log("startgame")*/
    });

    $("#btnCreateMore").on("click", function () {
        //window.location.replace("/uno/index.html");
        socket.send("hello");
    });



});
