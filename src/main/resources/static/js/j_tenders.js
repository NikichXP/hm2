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
    
    if (urlArray[1] != null) {

        for (var i = 0; i < urlArray.length; i++) { 
            paramArray = urlArray[i].split('=');

            switch (paramArray[0]) {
                //case 'date': sendData.date = paramArray[1]; $('span#search-innertext__date').html(paramArray[1]); break;
                case 'group': sendData.group = paramArray[1]; $('span#search-innertext__group').html(paramArray[1]); break;
                //case 'genre': sendData.genre = paramArray[1]; $('span#search-innertext__genre').html(paramArray[1]); break;
                case 'city': sendData.city = paramArray[1]; $('span#search-innertext__city').html(paramArray[1]); break;
                case 'price': {
                    sendData.price = paramArray[1]; 
                    var priceStr = paramArray[1].split('-');
                    $('#input-price__lower').val(priceStr[0]); 
                    $('#input-price__upper').val(priceStr[1]); 
                    break;
                }
            }
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
    
       
    //End of filling dropdowns
    
    
    //Loading tenders from server
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/tenders/list/all',
        data: sendData,
        success: function(resData) { 
            $('div.tenders').html("");
            
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
            
            for (var i = 1; i < resData.length; i++)
            {     
                var expDateArr = resData[i].deadlineString.split('-');
                var expDateString = "" + expDateArr[2] + "." + expDateArr[1] - 1 + "." + expDateArr[0];
                var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
                var daysLeft = daysBetween(curDate, expDate);
                $('div.tenders').append("<a class='tender-link' href='tender.html?id=" 
                                        + resData[i].id 
                                        + "'><div class='col-md-12 col-sm-12 tender-container' id='tender-" + i + "'></div></a>");
                $('div#tender-' + i).append("<div class='user-pic'>" 
                                                + "<img class='user-pic__img thumb' src='" + currentSite + "/file/getimg/100?img=" + resData[i].creator.userImg + "'>"
                                              + "</div>"
                                              + "<div class='tender-name'>"
                                                + "<a href='profile.html?id=" + resData[i].creator.id + "'><div class='user-name'>" + resData[i].creator.name + "</div></a>"
                                                + "<div class='tender-title'>Мне нужен: " 
                                                + resData[i].group 
                                                + " (" 
                                                + resData[i].genre 
                                                + ")/ " 
                                                + resData[i].city
                                                + "</div>"
                                              + "</div>"
                                              + "<div class='tender-info'>"
                                                + "<div class='tender-price'>Цена: <span class='tender-price__red'>" + resData[i].price + " грн</span></div>"
                                                + "<div class='tender-date'>" + expDateString + "</div>"
                                                + "<div class='tender-days-left'>Дней осталось: " + Math.floor(daysLeft) + "</div>"
                                                + "<div class='tender-time'>Pабочих часов: " + resData[i].workingHours + "</div>"
                                              + "</div>"
                                              + "<div class='tender-text'>" + resData[i].description + "</div>"
                                              + "<div class='tender-bidders'>"
                                                + "<div class='tender-bidders__count'>Oтветов:<p>" + resData[i].bidders.length + "</p></div>");
                for (var j = 0; j < resData[i].bidders.length; j++) {
                    $('div#tender-' + i + ' div.tender-bidders').append("<div class='tender-bidders__block'>"
                                                    + "<a href='profile.html?id=" + resData[i].bidders[j].userId + "'><div class='tender-bidders__bidder'>"
                                                    + "<img src='" + currentSite + "/file/getimg/80?img=" + resData[i].bidders[j].userImg + "'>"
                                                    + "<p>pro</p>"
                                                    + "</div></a>" 
                                                  + "</div>");  
                    if (j == 9) break;
                }
                
                $('div#tender-' + i).append("</div>");  	
            };                
                
        },
    });
                           
    //End of loading tenders from server                    
                        
 
    
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
    
    $('body').on('click', '#search-open', function() {	   
              
        $('.part-search').slideToggle();   
    });
     
    
    $('body').on('click', '#offer-search', function() {	   
        
        
        //var url = 'offers.html?page=1';
        var url = 'tenders.html?page=1';
        
        if ($('#search-innertext__city').html() != 'Город') {
            url += '&city=' + $('#search-innertext__city').html();
        }
//        if ($('#search-innertext__genre').html() != 'Жанр') {
//            url += '&genre=' + $('#search-innertext__genre').html();
//        }
        if ($('#search-innertext__group').html() != 'Услуга') {
            url += '&group=' + $('#search-innertext__group').html();
        }
        if ($('#input-price__lower').val() != '') {
            if ($('#input-price__upper').val() != '') {
                if ($('#input-price__upper').val() > 1000000)
                    url += '&price=' + $('#input-price__lower').val() + "-1000000"; 
                else 
                    url += '&price=' + $('#input-price__lower').val() + "-" + $('#input-price__upper').val(); 
            }
            else {
                url += '&price=' + $('#input-price__lower').val() + "-1000000"; 
            }
        }
        else {
            if ($('#input-price__upper').val() != '') {
                url += '&price=' + "0-" + $('#input-price__upper').val(); 
            }
        }
//        if ($('#search-innertext__date').html() != 'Дата') {
//            var dateStr = $('#search-innertext__date').html().split('. ');
//            url += '&date=' + dateStr[2] + '-' + dateStr[1] + '-' + dateStr[0];
//        }
        
        window.location.replace(url);
         
    });
    
    $('body').on('click', '#offer-search-cancel', function() {	   
              
        //var url = 'tenders.html?page=1'; 
        var url = 'tenders.html'; 
        window.location.replace(url);
        
    });
    
    //End of search buttons
    
    
    
    /*Calendar

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


    //End of Calendar*/
    
    

});




