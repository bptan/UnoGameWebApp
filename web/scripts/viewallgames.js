/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function(){
    var gamesTemplate = Handlebars.compile($("#gamesTemplate").html());
    var promise = $.getJSON("http://localhost:8080/uno/api/games/all")
    var gid;
    var userData = {
 storeUserDataInSession: function(userData) {
     var userObjectString = JSON.stringify(userData);
     window.sessionStorage.setItem('userObject',userObjectString)
 },
 getUserDataFromSession: function() {
     var userData = window.sessionStorage.getItem('userObject')
     return JSON.parse(userData);
 }
}
    
    promise.done(function(result){
         $("#all-games").append(gamesTemplate({games: result}));
    })
    
   
    $("#all-games").on("click", "[data-gameId]", function() {
        gid = $(this).attr("data-gameId");
        var promise = $.getJSON("http://localhost:8080/uno/api/games/start/"+gid)
            promise.done(function(result) {
                console.log("button clicked");
                window.location.replace("/uno/gametableview.html");
                userData.storeUserDataInSession(result);
            })
        console.log("startgame")
    });
    
    $("#btnCreateMore").on("click",function(){
        window.location.replace("/uno/index.html");
    });
    
    
   
});
