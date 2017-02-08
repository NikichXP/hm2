$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    var curDate = new Date();
    
    var url = window.location;
    url = decodeURIComponent(url);
    
    var offerPage = /page=(\d+)/.exec(url)[1]; //get number of the page from url
    
    offerPage = 0 + offerPage;
    
    var pageOffset = 0;
    var itemsLimit = 10;
    
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
    if (url.slice(-1) == '#') {
            url = url.substring(0, url.length - 1);      
        }
    var urlArray = url.split('&');
    var paramArray;

    for (var i = 0; i < urlArray.length; i++) { 
        
        paramArray = urlArray[i].split('=');
        
        switch (paramArray[0]) {
            case 'date': sendData.date = paramArray[1]; $('span#search-innertext__date').html(paramArray[1]); break;
            case 'group': sendData.group = paramArray[1]; $('span#search-innertext__group').html(paramArray[1]); break;
            case 'genre': sendData.genre = paramArray[1]; $('span#search-innertext__genre').html(paramArray[1]); break;
            case 'city': sendData.city = paramArray[1]; $('span#search-innertext__city').html(paramArray[1]); break;
        }

    }
    
    //End of getting search params from url
    
    
    //Filling dropdowns
    
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
       
    //End of filling dropdowns
    
    
    //Loading offers from server
    
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
                var expDateArr = resData[i].expirationDateString.split('-');
                var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
                var daysLeft = daysBetween(curDate, expDate);
                $('.offers-container').append("<div class='col-md-6 col-sm-12 offers-container__offer'>" 
                                            //+ "<img src='" + currentSite + "/file/get/" + resData[i].validImage + "' alt=''>" 
                                            + "<div class='offer-image' style='background: url(" + currentSite + "/file/get?file=" + resData[i].image + ") 0px 0px no-repeat; background-size: cover; background-position: center;'></div>" 
                                            + "<div class='prob-block-bg'>"
                                            + "</div>"
                                            + "<div class='prob-block-desc'>"
                                                + "<h5>г. " + resData[i].city + "</h5>"
                                                + "<h4>" + resData[i].title + "</h4>"
                                            + "</div>"
                                            + "<a href='profile.html?id=" + resData[i].workerEntity.id + "'>"
                                            + "<div class='prob-block-user'>"
                                                + "<img class='user-pic__img thumb' src='" + currentSite + "/file/get?file=" + resData[i].workerEntity.userImg + "'>"
                                                + "<div class='user-name'>" + resData[i].workerEntity.name + "</div>"
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
                           
    //End of loading offers from server                    
                        
     
    
    //Navigation buttons onclicks  
            
    $('body').on('click', 'li#page-navigation__page-next', function() {	   
        
        if (offerPage < maxPages) {
            offerPage++;    
            window.location.replace(urlChangePage(offerPage));
        }

    });
    
    $('body').on('click', 'li#page-navigation__page-prev', function() {	
        
        if (offerPage > 1) {
            offerPage--;
            window.location.replace(urlChangePage(offerPage));
        }

    });
    
    $('body').on('click', 'li#page-navigation__page-last', function() {
        
        if (offerPage < maxPages) {
            window.location.replace(urlChangePage(maxPages));
        }
    });
    
    $('body').on('click', 'li#page-navigation__page-first', function() {
        
        if (offerPage > 1) {
            window.location.replace(urlChangePage(1));
        }
    });
    
    $('body').on('click', 'li.page-navigation__page-num', function() {	
        
        if (offerPage != $(this).html()) {
            offerPage = $(this).html();
            window.location.replace(urlChangePage(offerPage));
            
        }
    });
    
    //End of navigation buttons onclicks 
    
    
    
    //Search buttons 
    
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
        if ($('#search-innertext__date').html() != 'Дата окончания') {
            var dateStr = $('#search-innertext__date').html().split('. ');
            url += '&date=' + dateStr[2] + '-' + dateStr[1] + '-' + dateStr[0];
        }
        
        window.location.replace(url);
        
    });
    
    $('body').on('click', '#offer-search-cancel', function() {	   
              
        var url = 'offers.html?page=1'; 
        window.location.replace(url);
        
    });
    
    //End of search buttons
    
    
    
    //Calendar

    $('.button-date').dcalendarpicker({
        format: 'dd-mm-yyyy'
    });
    
    $('body').on('click', 'td.date', function() {	   
        var day = $(this).children('span').html();
        if (day < 10) day = '0' + day;
        var mmyy = $('th#currM').html().split(' ');
        switch (mmyy[0]) {
                case 'Январь' : mmyy[0] = '01'; break;
                case 'Февраль' : mmyy[0] = '02'; break;
                case 'Март' : mmyy[0] = '03'; break;
                case 'Апрель' : mmyy[0] = '04'; break;
                case 'Май' : mmyy[0] = '05'; break;
                case 'Июнь' : mmyy[0] = '06'; break;
                case 'Июль' : mmyy[0] = '07'; break;
                case 'Август' : mmyy[0] = '08'; break;
                case 'Сентябрь' : mmyy[0] = '09'; break;
                case 'Октябрь' : mmyy[0] = '10'; break;
                case 'Ноябрь' : mmyy[0] = '11'; break;
                case 'Декабрь' : mmyy[0] = '12'; break;
        }
        $('#search-innertext__date').html(day + '. ' + mmyy[0] + '. ' + mmyy[1]);
    });


    //End of Calendar
    
    

});




