$(function(){
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
    var url = window.location;
    url = decodeURIComponent(url);
    var offerPage = /page=(\d+)/.exec(url)[1]; //get number of the page from url
    
    offerPage = 0 + offerPage;
    
    var pageOffset = 0;
    var itemsLimit = 20;
    
    for (var i = 1; i < offerPage; i++) { 
        pageOffset += itemsLimit;    
    }
    

    var maxPages;  // get number of pages
    
    //Getting search params from url
    
    var sendData = { 
        limit: itemsLimit, 
        offset: pageOffset 
    }
    
    url = '' + url;
    var urlArray = url.split('&');
    var paramArray;

    for (var i = 0; i < urlArray.length; i++) { 
        
        paramArray = urlArray[i].split('=');
        
        switch (paramArray[0]) {
            case 'date': sendData.date = paramArray[1]; break;
            case 'group': sendData.group = paramArray[1]; break;
            case 'genre': sendData.genre = paramArray[1]; break;
            case 'city': sendData.city = paramArray[1]; break;
        }

    }
    
    //End of getting search params from url
    
    
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
        data: sendData,
        success: function(resData) {
            $('.offers-container').html("");
            
            maxPages = Math.ceil(resData[0]/itemsLimit);
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-first'>Первая</li>");		
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-prev'>Предыдущая</li>");
                    
            for (var i = 1; i < Math.ceil(resData[0]/itemsLimit) + 1; i++) { 
                $('.page-navigation__list').append("<li class='page-navigation__page page-navigation__page-num'>" 
                                                + i
                                             + "</li>");	    
            }
            
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-next'>Следующая</li>");
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-last'>Последняя</li>");
            
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
                                            + "<div class='prob-block-user'>"
                                                + "<img class='user-pic__img' src='" + currentSite + "/file/get/" + resData[i].workerEntity.validUserImg + "'>"
                                                + "<div class='user-name'>" + resData[i].workerEntity.name + "</div>"
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
            //var url = 'offers.html?page=' + offerPage;
            //window.location.replace(url);    
            window.location.replace(urlChangePage(offerPage));
        }

    });
    
    $('body').on('click', 'li#page-navigation__page-prev', function() {	
        
        if (offerPage > 1) {
            offerPage--;
            //var url = 'offers.html?page=' + offerPage;
            //window.location.replace(url);  
            window.location.replace(urlChangePage(offerPage));
        }

    });
    
    $('body').on('click', 'li#page-navigation__page-last', function() {
        
        if (offerPage < maxPages) {
            //var url = 'offers.html?page=' + maxPages;
            //window.location.replace(url);
            window.location.replace(urlChangePage(maxPages));
        }
    });
    
    $('body').on('click', 'li#page-navigation__page-first', function() {
        
        if (offerPage > 1) {
            //var url = 'offers.html?page=' + 1;
            window.location.replace(urlChangePage(1));
        }
    });
    
    $('body').on('click', 'li.page-navigation__page-num', function() {	
        
        if (offerPage != $(this).html()) {
            offerPage = $(this).html();
            //var url = 'offers.html?page=' + offerPage;
            window.location.replace(urlChangePage(offerPage));
            
        }
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
        
        
        var url = 'offers.html?page=1';
        
        if ($('#search-innertext__city').html() != 'Город') {
            url += '&city=' + $('#search-innertext__city').html();
        }
        if ($('#search-innertext__genre').html() != 'Жанр') {
            url += '&genre=' + $('#search-innertext__genre').html();
        }
        if ($('#search-innertext__group').html() != 'Услуга') {
            url += '&group=' + $('#search-innertext__group').html();
        }
        if ($('#search-innertext__date').html() != 'Дата') {
            url += '&date=' + $('#search-innertext__date').html();
        }
        
        window.location.replace(url);
        
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


function urlChangePage(pageNum) {
    var url = window.location;
    url = decodeURIComponent(url);
    
    url = '' + url;
    var urlArray = url.split('?');
    
    var urlMain = urlArray[0];
    urlArray = urlArray[1].split('&');
    
    for (var i = 0; i < urlArray.length; i++) 
        if (urlArray[i].includes('page=')) 
            urlArray[i] = 'page=' + pageNum;    

    url = urlMain + '?';
    
    for (var i = 0; i < urlArray.length; i++) 
        
        if (i == urlArray.length - 1) url += urlArray[i];  
        else url += urlArray[i] + '&';  
    
    return url;    
}



