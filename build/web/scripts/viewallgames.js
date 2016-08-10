/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function(){
    var gamesTemplate = Handlebars.compile($("#gamesTemplate").html());
    var promise = $.getJSON("http://localhost:8080/uno/api/games/all")
    promise.done(function(result){
         $("#all-games").append(gamesTemplate({games: result}));
    })
    
    $("#all-games").on("click", "tr", function() {
        gid = $(this).find("h4").text();
        var promise = $.getJSON("http://localhost:8080/uno/api/games/start/"+gid)
            promise.done(function(result) {

                
            })
        console.log("startgame")
    });
});
