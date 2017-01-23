$(function(){
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
    var url = window.location;
    var offerPage = /page=(\d+)/.exec(url)[1]; //get number of the page from url
    
    offerPage = 0 + offerPage;
    
    var pageOffset = 0;
    var itemsLimit = 20;
    
    for (var i = 1; i < offerPage; i++) { 
        pageOffset += itemsLimit;    
    }
    

    var maxPages;  // get number of pages
    
    
    //Filling search dropdowns
    $.ajax({
        type: 'GET',
        url: currentSite + '/config/list/city',
        success: function(resData) {
            $('.dropdown-menu__city').html("");
            
            for (var i = 0; i < resData.length; i++)
            {          
                $('.dropdown-menu__city').append("<li><a class='dropdown-menu__item' href='#'>" 
                                            + resData[i]  
                                            + "</a></li>");  	
            };                
                
        },
    });
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/config/list/offer/group',
        success: function(resData) {
            $('.dropdown-menu__group').html("");
            
            for (var i = 0; i < resData.length; i++)
            {          
                $('.dropdown-menu__group').append("<li><a class='dropdown-menu__item' href='#'>" 
                                            + resData[i]  
                                            + "</a></li>");  	
            };                
                
        },
    });
    
    $('body').on('click', 'a.dropdown-menu__item', function() {	
        var cat = $(this).html();
        
        $.ajax({
            type: 'GET',
            url: currentSite + '/config/list/offer/genre/' + cat,
            success: function(resData) {
                $('.dropdown-menu__genre').html("");

                for (var i = 0; i < resData.length; i++)
                {          
                    $('.dropdown-menu__genre').append("<li><a class='dropdown-menu__item' href='#'>" 
                                                + resData[i]  
                                                + "</a></li>");  	
                };                

            },
        });

    });
       
    
    
    
    //End of filling search dropdowns
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/offer',
        data: { limit: itemsLimit, offset: pageOffset },
        success: function(resData) {
            $('.offers-container').html("");
            
            maxPages = Math.ceil(resData[0]/itemsLimit);
                    
            for (var i = 1; i < Math.ceil(resData[0]/itemsLimit) + 1; i++) { 
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
    
    
    $('body').on('click', 'ul.dropdown-menu__city li a.dropdown-menu__item', function() {	   
        $('span#search-innertext__city').html($(this).html());
    });
    
    $('body').on('click', 'ul.dropdown-menu__group li a.dropdown-menu__item', function() {	   
        $('span#search-innertext__group').html($(this).html());
    });
    
    $('body').on('click', 'ul.dropdown-menu__genre li a.dropdown-menu__item', function() {	   
        $('span#search-innertext__genre').html($(this).html());
    });
    
    
    
    $('body').on('click', '#offer-search', function() {	   
        
        var sendData = { limit: itemsLimit, 
                        offset: pageOffset, 
                      }
        if ($('#search-innertext__city').html() != 'Город') sendData.city = $('#search-innertext__city').html();
        if ($('#search-innertext__genre').html() != 'Жанр') sendData.genre = $('#search-innertext__genre').html();
        if ($('#search-innertext__group').html() != 'Услуга') sendData.group = $('#search-innertext__group').html();
        
        $.ajax({
            type: 'GET',
            url: currentSite + '/product/offer',
            data: sendData,
            success: function(resData) {
                $('.offers-container').html("");
                $('.page-navigation__list').html("");

                maxPages = Math.ceil(resData[0]/itemsLimit);
                $('.page-navigation__list').append("<li class='page-navigation__page page-navigation__page-first'>Первая</li>");		
                $('.page-navigation__list').append("<li class='page-navigation__page page-navigation__page-prev'>Предыдущая</li>");

                for (var i = 1; i < Math.ceil(resData[0]/itemsLimit) + 1; i++) { 
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
        
    });
    
    
    
    //Calendar
    

    $('.button-date').dcalendarpicker({
        format: 'dd-mm-yyyy'
    });
    
    $('body').on('click', 'td.date', function() {	   
        var day = $(this).children('span').html();
        var mmyy = $('th#currM').html();
        $('.button-date').html(day + ' ' + mmyy);
    });


    //End of Calendar
    
    

});



