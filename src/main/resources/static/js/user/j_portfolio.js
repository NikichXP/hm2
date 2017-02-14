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
    var paramArray = urlArray[1].split('&');
    
    var valArray = new Array();
    
    valArray[0] = paramArray[0].split('=')[1]; 
    valArray[1] = paramArray[1].split('=')[1]; 
    
    var ID = valArray[0];
    var genre = valArray[1];
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/user/getUser',
        data: {id : ID},
        success: function(resData) {   
            $('.user-link').html("<a href='profile.html?id=" + ID + "'>" + resData.name.split(' ')[0] + ' id' + ID + '</a> - <span>' + genre + '</span>');             
        },
    });
     
    $.ajax({
        type: 'GET',
        url: currentSite + '/user/portfolio/' + ID + '?genre=' + genre,
        success: function(resData) {   
             
            $('.container-portfolio').html(""); 
            var arr = new Array();
            for (var i = 0; i < resData.length; i++) {
                $('.container-portfolio').append("<div class='col-lg-4 col-xs-4 portfolio'>" 
                                                + "<img class='portfolio-album' id='photo-" + i + "' src='" 
                                                + currentSite 
                                                + "/file/getimg/350?img=" 
                                                + resData[i] 
                                                + "'>" 
                                            + "</div>");
            }          
        },
    });
    
    $('body').on('click', '.portfolio-album', function() {
        
        $('.portfolio-gallery').html('');
        $('.portfolio-gallery').append('<div class="fotorama" data-auto="false" data-nav="thumbs" data-width="100%" data-height="80%"></div>');
        $('.portfolio-gallery').append("<div class='portfolio-gallery__close'>Закрыть</div>");
        
        var thisImage = $(this).attr('id').split('-')[1];
        
        thisImage--;
        
        var arr = new Array();
        
        $.ajax({
            type: 'GET',
            url: currentSite + '/user/portfolio/' + ID + '?genre=' + genre,
            success: function(resData) {  

                for (var i = 1; i < resData.length; i++) {
                    arr[i] = { img: currentSite + "/file/get?file=" + resData[i], thumb: currentSite + "/file/getimg/64?img=" + resData[i] };
                } 

                $('.fotorama').fotorama({
                    startindex: thisImage,
                    data: arr
                });

            },
        });
        
        $(".portfolio-gallery").fadeToggle();            
    });
    
    $('body').on('click', '.portfolio-gallery__close', function() {    
        $(".portfolio-gallery").fadeToggle();            
    });
    
    
    
    
    
});




