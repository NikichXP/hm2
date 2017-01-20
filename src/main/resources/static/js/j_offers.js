$(function(){
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
    var url = window.location;
    var offerPage = /page=(\d+)/.exec(url)[1]; //get number of the page from url
    
    offerPage = 0 + offerPage;
    
    var pageOffset = 0;
    
    for (var i = 1; i < offerPage; i++) { 
        pageOffset += 20;    
    }
    

    var maxPages;  // get number of pages
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/offer',
        data: { limit: '20', offset: pageOffset },
        success: function(resData) {
            $('.offers-container').html("");
            
            maxPages = Math.ceil(resData[0]/20);
                    
            for (var i = 1; i < Math.ceil(resData[0]/20) + 1; i++) { 
                $('.page-navigation__list').append("<li class='page-navigation__page page-navigation__page-num'>" 
                                                + i
                                             + "</li>");	    
            }
            
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-next'>" +
                        "Следующая" +
                        "</li>");
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-last'>" +
                        "Последняя" +
                        "</li>");
            
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
    
        
            
    $('body').on('click', 'li#page-navigation__page-next', function() {	   
        
        if (offerPage < maxPages) {
            offerPage++;
            var url = 'offers.html?page=' + offerPage;
            window.location.replace(url);    
        }

    });
    
    $('body').on('click', 'li#page-navigation__page-prev', function() {	
               
        if (offerPage > 1) {
            offerPage--;
            var url = 'offers.html?page=' + offerPage;
            window.location.replace(url);  
        }

    });
    
    $('body').on('click', 'li#page-navigation__page-last', function() {	
        var url = 'offers.html?page=' + maxPages;
        window.location.replace(url);

    });
    
    $('body').on('click', 'li#page-navigation__page-first', function() {	
        var url = 'offers.html?page=' + 1;
        window.location.replace(url);

    });
    
    $('body').on('click', 'li.page-navigation__page-num', function() {	
        offerPage = $(this).html();
        var url = 'offers.html?page=' + offerPage;
        window.location.replace(url);

    });
    
    
    

    
    
    
    

});



