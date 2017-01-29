$(function(){
    
    //Load elements from files
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    
    var curDate = new Date();
   
    
    //End of load elements from files
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
    
	$('body').on('click', '#auth-button_search', function() {		
        $.ajax({
            type: 'GET',
            url: 'https://hm2.herokuapp.com/test/getMappings',
            //data: {  },
            success: function(resData) {
                for (var i = 0; i < resData.length; i++)
                {
                    $('.upper-bar').append("<p>" +
                        resData[i] +
                        "</p>");	   	
                }; 
                
            },
        });
    });
    
    var offerData = { limit: '4', shuffle: true }
    
    if (getCookie('city') != null) {
        offerData.city = getCookie('city');
        var proUserData = {city: getCookie('city')};
    }
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/offer',
        data: offerData,
        success: function(resData) {
            $('.offers-container').html("");	
            for (var i = 1; i < resData.length; i++)
            {  
                var expDateArr = resData[i].expirationDateString.split('-');
                var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
                var daysLeft = daysBetween(curDate, expDate);
                $('.offers-container').append("<div class='col-md-3 col-sm-6 hero-feature'>" 
                                            + "<img src='" + currentSite + "/file/get?file=" + resData[i].image + "' alt=''>" 
                                            + "<div class='prob-block-bg'>"
                                            + "</div>"
                                            + "<div class='prob-block-desc'>"
                                                + "<h5>г. " + resData[i].city + "</h5>"
                                                + "<h4>" + resData[i].title + "</h4>"
                                            + "</div>"
                                            + "<div class='prob-block-price'> "
                                                + "<div class='price-new'>" + resData[i].finalPrice + " грн</div>"
                                                + "<div class='price-old'>" + resData[i].price + " грн</div>"
                                                + "<div class='price-to'>" + Math.floor(daysLeft) + " дней</div>"
                                            + "</div>"
                                            + "<div class='prob-block-dis'>-" + resData[i].discount + "%</div>"
                                            + "</div>");  	
            };           
        },
    });
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/user/getProUsers',
        data: proUserData,
        success: function(resData) {
            $('.rec-container').html("");	
            for (var i = 0; i < resData.length; i++)
            {          
                $('.rec-container').append("<a href='#'><div class='col-md-3 col-sm-6 rec-block'>" 
                                            + "<div class='rec-pic' style='background: url(" + currentSite + "/file/get?file=" + resData[i].userImg + ") 0px 0px no-repeat; background-size: cover; background-position: center;'></div>" 
                                            + "<div class='rec-block-desc'>"
                                                + "<div class='rec-name'>" + resData[i].name + "</div>"
                                                + "<div class='rec-desc'>" + resData[i].profession + "</div>"
                                            + "</div>"
                                            + "<div class='rec-block-city-block'>"
                                                + "<div class='rec-city'>г. " + resData[i].city + "</div>"
                                                + "<div class='rec-city-bg'></div>"
                                            + "</div>"
                                        + "</div></a>");  	
            };           
        },
    });
    
    
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/config/list/city',
        success: function(resData) {
            $('.dropdown-upper-menu__city').html("");
            
            for (var i = 0; i < resData.length; i++)
            {          
                $('.dropdown-upper-menu__city').append("<li><a class='dropdown-menu__item' href='#'>" 
                                            + resData[i]  
                                            + "</a></li>");  	
            };                
                
        },
    });
    
    $('body').on('click', 'ul.dropdown-upper-menu__city li a.dropdown-menu__item', function() {	
        $('span#upper-innertext__city').html($(this).html());
        setCookie('city', $(this).html());
        window.location.reload();
    });
       
});



