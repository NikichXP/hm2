$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
		
	$('#auth-button_search').on('click', function(){			
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
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/offer',
        data: { limit: '4', shuffle: true },
        success: function(resData) {
            $('.offers-container').html("");	
            for (var i = 1; i < resData.length; i++)
            {          
                $('.offers-container').append("<div class='col-md-3 col-sm-6 hero-feature'>" 
                                            + "<img src='" + currentSite + "/file/get/" + resData[i].validImage + "' alt=''>" 
                                            + "<div class='prob-block-bg'>"
                                            + "</div>"
                                            + "<div class='prob-block-desc'>"
                                                + "<h5>г. " + resData[i].city + "</h5>"
                                                + "<h4>" + resData[i].title + "</h4>"
                                            + "</div>"
                                            + "<div class='prob-block-price'> "
                                                + "<div class='price-new'>" + resData[i].finalPrice + " грн</div>"
                                                + "<div class='price-old'>" + resData[i].price + " грн</div>"
                                                + "<div class='price-to'>" + i + " дней</div>"
                                            + "</div>"
                                            + "<div class='prob-block-dis'>-" + resData[i].discount + "%</div>"
                                            + "</div>");  	
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
        window.location.replace(urlChangePage(1, $(this).html()));
    });
    
    
});



