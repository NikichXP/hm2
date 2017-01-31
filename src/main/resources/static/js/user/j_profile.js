$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    var curDate = new Date();
    
    var url = window.location;
    url = decodeURIComponent(url);
    
    url = '' + url;

    var urlArray = url.split('?');
    var paramArray = urlArray[1].split('=');
        
    var userId = paramArray[1]; 

    
    $.ajax({
        type: 'GET',
        url: currentSite + '/auth/getUser',
        data: {id: userId},
        success: function(resData) {
            
            alert('yay');               
                
        },
    });
    

});




