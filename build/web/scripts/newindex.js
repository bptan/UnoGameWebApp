/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function () {
    var playersTemplate = Handlebars.compile($("#playersTemplate").html());
    var gamesTemplate = Handlebars.compile($("#gamesTemplate").html());
    var socket;
    $("#btnCreate").on("singletap", function () {

        var promise = $.post("api/games",
                {title: $("#txtTitle").val()});

        promise.done(function (result) {
            var status = result;
            console.log(status);
            $.UIGoToArticle("#listGames");
        });
    });

    $("#btnCreateMore").on("singletap", function () {
        $.UIGoToArticle("#createGame");
    });

    $("#btnReload").on("singletap", function () {
        console.log("reload clicked");
        var promise = $.getJSON("api/games/all");

        promise.done(function (result) {
            $("#all-games").append(gamesTemplate({games: result}));
        });
    });

    $("#all-games").on("singletap", "li", function () {
        gid = $(this).find("h4").text();
        socket = new WebSocket("ws://localhost:8080/uno/games/" + gid + "/verysecrettable");
        socket.onopen = function () {
            console.log("Websocket is connected");
            $.UIGoToArticle("#selectedGame");
        };
        socket.onmessage = function (msg) {

            var jsonObj = JSON.parse(msg.data);//why must parse? String->Json
            /*
            if((jsonObj.command === "yourcards")&&(jsonObj.pname==="verysecrettable")){
                var message = {};
                message.command = "startgame";
                message.gid = gid;
                
                socket.send();
            }*/
            var cardUrl = $('<p class = "discarded">');
            var img = $("<img>").attr("src","Images/"+jsonObj.hand.image+".png");
            cardUrl.append(img);
            $("#discardPile").append(cardUrl);  
            $("#all-players").append(playersTemplate({players: jsonObj.pname}));               
            console.log(jsonObj);
        };      
    });
    $("#btnBackInsideSelectedGame").on("singletap", function () {
            $.UIGoBack();
        });
});
