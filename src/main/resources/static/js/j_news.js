$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    

    var curDate = new Date();
    
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/news/getNews',
        success: function(resData) {
            $('.container-news').html("");
            
            for (var i = 0; i < resData.length; i++)
            {          
                var trimmedText = resData[i].text.substring(0, 100) + "...";
                
                $('.container-news').append("<a href='newspost.html?id=" + resData[i].id + "'><div class='col-lg-6 col-sm-6 container-news__post'>" 
                                            + "<div class='container-news__img' style='background: url(" + currentSite + "/file/getimg/274?img=" + resData[i].img + ") no-repeat center;'></div>" 
                                            + "<div class='container-news__text'>" 
                                            + "<h3>" + resData[i].title + "</h3>" 
                                            + "<h5>" + trimmedText + "</h5>" 
                                            + " </div></div></a>");  	
            };                
                
        },
    });
    

    
    
    
            
                
                
                    
                    
               
            
        
});




