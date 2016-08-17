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
            $("#all-games").empty();
            $("#all-games").append(gamesTemplate({games: result}));
        });
    });

    $("#all-games").on("singletap", "li", function () {
        gid = $(this).find("h4").text();
        socket = new WebSocket("ws://localhost:8080/uno/games/" + gid + "/verysecrettable");
        $("#gametitle").empty();
        $("#gametitle").append($(this).find("h3").text());
        socket.onopen = function () {
            console.log("Websocket is connected");
            $.UIGoToArticle("#selectedGame");
            $("#discardPile").empty();
            $("#all-players").empty();
            $("#cards-remaining").empty();
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
            var jsonObj = JSON.parse(msg.data);
            if (jsonObj.command === "yourcards") {
                var cardUrl = $('<p class = "discarded">');
                var img = $("<img>").attr("src", "Images/" + jsonObj.hand.image + ".png")
                        .attr("alt",jsonObj.hand.image);
                cardUrl.append(img);

                $("#discardPile").empty();
                $("#discardPile").append(cardUrl);
                $("#all-players").empty();
                $("#all-players").append(playersTemplate({players: jsonObj.pname}));
                $("#cards-remaining").empty();
                $("#cards-remaining").append("<p>Deck Cards Remaining: " + jsonObj.deckSize + "</p>");
                console.log(jsonObj);
            }
            
        };
    });
    $("#btnBackInsideSelectedGame").on("singletap", function () {
        socket.close();
        $.UIGoBack();
    });


    $("#all-players").on("singletap", "[data-playername]", function () {
        var message = {};
        message.command = "drawcard";
        message.pname = $(this).attr("data-playername");
        console.log(message);
        socket.send(JSON.stringify(message));
    });

    $(".special").draggable();

    $("[drop-target]").droppable({
        accept: ".special",
        tolerance: "pointer",
        drop: function (_, ui) {
            if ($(ui.draggable).attr("data-draw")) {
                alert("success");
                console.log("dropped");
            }
        }
    });

});
