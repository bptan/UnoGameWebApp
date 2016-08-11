/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function(){
    var playersTemplate = Handlebars.compile($("#playersTemplate").html());
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

        var game = userData.getUserDataFromSession();
        var cardUrl = $('<p class = "discarded">');
        var img = $("<img>").attr("src","Images/"+game.topCardOfDiscardPile+".png");
        cardUrl.append(img);
        $("#discardPile").append(cardUrl);
        console.log(game);
    
    $("#all-players").append(playersTemplate({players: game.playerNames}));




});
