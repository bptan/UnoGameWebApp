/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function(){
    
    $("#btnCreate").on("click",function(){
        console.log("before click")
        var promise = $.post("api/games",
            {title:$("#txtTitle").val()});
        
        promise.done(function(result){
            var status = result;
            console.log(status);
            window.location.replace("/uno/viewallgames.html");         
        })
    })
    
    
});
