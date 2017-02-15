$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    

    var curDate = new Date();
    
    var url = window.location;
    url = decodeURIComponent(url);
    
    url = '' + url;
    if (url.slice(-1) == '#') {
            url = url.substring(0, url.length - 1);      
        }
    var urlArray = url.split('?');
    var paramArray;

    for (var i = 0; i < urlArray.length; i++) { 
        
        paramArray = urlArray[i].split('=');
        
        if (paramArray[0] == 'id') {
            $.ajax({
                type: 'GET',
                url: currentSite + '/news/getNews',
                data: {id: paramArray[1]},
                success: function(resData) {
                    $('.title-container h1').html(resData.title);
                    $('.container-news__img').css('background', "url(" + currentSite + "/file/getimg/300?img=" + resData.img + ") no-repeat center");
                    $('.container-news__text').html("<h5>" + resData.text + "</h5>" );           
                },
            }); 
            
        }

    }
    
    
    

});




