

$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    

    var curDate = new Date();
    
    var url = window.location;
    url = decodeURIComponent(url);
    
    var offerPage = parseInt(/page=(\d+)/.exec(url)[1]); //get number of the page from url
        
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
            //case 'date': sendData.date = paramArray[1]; $('span#search-innertext__date').html(paramArray[1]); break;
            case 'group': sendData.group = paramArray[1]; $('span#search-innertext__group').html(paramArray[1]); break;
            //case 'genre': sendData.genre = paramArray[1]; $('span#search-innertext__genre').html(paramArray[1]); break;
            //case 'city': sendData.city = paramArray[1]; $('span#search-innertext__city').html(paramArray[1]); break;
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
            
    $.ajax({
        type: 'GET',
        url: currentSite + '/config/list/offer/genre/' + $('#search-innertext__group').html(),
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

       
    //End of filling dropdowns
    
    
    //Loading offers from server
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/user/search',
        data: sendData,
        success: function(resData) {
            $('.offers-container').html("");
            
            maxPages = Math.ceil(resData[0]/itemsLimit);
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-first'><<</li>");		
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-prev'><</li>");
            
            var k = 0;
            
            for (var i = offerPage - 10; i < offerPage + 10; i++) {
                if (i > 0 && i <= maxPages) {
                    if (i == offerPage) 
                        $('.page-navigation__list').append("<li class='page-navigation__page page-navigation__page-num page-navigation__current'>" 
                                        + i
                                        + "</li>");	
                    else 
                        $('.page-navigation__list').append("<li class='page-navigation__page page-navigation__page-num'>" 
                                        + i
                                        + "</li>");	
                    k++;
                }
            }
        
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-next'>></li>");
            $('.page-navigation__list').append("<li class='page-navigation__page' id='page-navigation__page-last'>>></li>");
            
            for (var i = 0; i < resData[1].length; i++)
            {       
                $('.offers-container').append("<a href='profile.html?id=" + resData[1][i].id + "'><div class='col-lg-12 profile-container' id='user-" + i + "'>" 
                                            + "<div class='profile-container__desc'>"
                                                + "<span class='profile-container__name'>" 
                                                    + resData[1][i].name.split(' ')[0] 
                                                    + ' ' 
                                                    + resData[1][i].id
                                                + "</span>"
                                                + "<span class='profile-container__pro'>pro</span>"
                                                + "<span class='profile-container__city'>г. " + resData[1][i].city + "</span>"
                                                + "<span class='profile-container__price'>Стоимость от " + resData[1][i].minPrice + " грн</span>"
                                            + "</div>"
                                            + "<div class='profile-container__photos'>"
                                            + "</div>"
                                            + "</div></a>");
                
                getPhotos(i, resData[1][i].id);                                           	
            }                   
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
        
        var url = 'services.html?page=1';
        
        url += '&group=' + $('#search-innertext__group').html();

        window.location.replace(url);
    });
    
    $('body').on('click', 'ul.dropdown-menu__genre li a.dropdown-menu__item', function() {	   
        $('span#search-innertext__genre').html($(this).html());
    });
        
    
    $('body').on('click', '#offer-search', function() {	   
        
        
        var url = 'services.html?page=1';
        
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
            var dateStr = $('#search-innertext__date').html().split('. ');
            url += '&date=' + dateStr[2] + '-' + dateStr[1] + '-' + dateStr[0];
        }
        
        window.location.replace(url);
        
    });
    
    $('body').on('click', '#search-open', function() {	   
              
        $('.part-search').slideToggle();   
    });
    
    $('body').on('click', '#offer-search-cancel', function() {	   
              
        var url = 'services.html?page=1'; 
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

function getPhotos (i, id) {
    $.ajax({
        type: 'GET',
        url: currentSite + '/user/portfolio/' + id,
        success: function(resData) {
            for (var j = 0; j < resData.length; j++) {
                if (j == 4) break;
                //console.log(i);
                $('#user-' + i + ' .profile-container__photos').append("<img src='" + currentSite + "/file/getimg/250?img=" + resData[j] + "'>");
                //console.log(resData[j]);
            } 
        }
    });
}




