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
    if (url.slice(-1) == '#') {
            url = url.substring(0, url.length - 1);      
        }
    var urlArray = url.split('?');
    
    var arrID = urlArray[1].split('=');
    var tenderId = arrID[1];
    
        
    //End of getting search params from url
    
    
    
    
    //Loading tender from server
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/tenders/' + tenderId,
        success: function(resData) { 
            $('div.tenders').html("");
            
  
            var expDateArr = resData.deadlineString.split('-');
            var expDateString = "" + expDateArr[2] + "." + expDateArr[1] - 1 + "." + expDateArr[0];
            var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
            var daysLeft = daysBetween(curDate, expDate);
            $('div.tenders').append("<div class='col-md-12 col-sm-12 tender-container'><div class='user-pic'>" 
                                                + "<img class='user-pic__img thumb' src='" + currentSite + "/file/getimg/100?img=" + resData.creator.userImg + "'>"
                                              + "</div>"
                                              + "<div class='tender-name'>"
                                                + "<a href='profile.html?id=" + resData.creator.id + "'><div class='user-name'>" + resData.creator.name + "</div></a>"
                                                + "<div class='tender-title'>Мне нужен: " 
                                                + resData.group 
                                                + " (" 
                                                + resData.genre 
                                                + ")/ " 
                                                + resData.city
                                                + "</div>"
                                              + "</div>"
                                              + "<div class='tender-info'>"
                                                + "<div class='tender-price'>Цена: <span class='tender-price__red'>" + resData.price + " грн</span></div>"
                                                + "<div class='tender-date'>" + expDateString + "</div>"
                                                + "<div class='tender-days-left'>Дней осталось: " + Math.floor(daysLeft) + "</div>"
                                                + "<div class='tender-time'>Pабочих часов: " + resData.workingHours + "</div>"
                                              + "</div>"
                                              + "<div class='tender-text'>" + resData.description + "</div>"
                                              + "<div class='tender-bidders'>"
                                                + "<div class='tender-bidders__count'>Oтветов:<p>" + resData.bidders.length + "</p></div>");
            for (var j = 0; j < resData.bidders.length; j++) {
                $('div.tender-container div.tender-bidders').append("<div class='tender-bidders__block'>"
                                                    + "<a href='profile.html?id=" + resData.bidders[j].userId + "'><div class='tender-bidders__bidder'>"
                                                    + "<img src='" + currentSite + "/file/getimg/80?img=" + resData.bidders[j].userImg + "'>"
                                                    + "<p>pro</p>"
                                                    + "</div></a>" 
                                                  + "</div>");  
                if (j == 9) break;
            }
                
            $('div.tenders').append("</div></div>"); 
            
            for (var i = 0; i < resData.bidders.length; i++) {
                $('div.participants').append("<div class='participant-user' id='user-" + i + "'></div>"); 
                $('div#user-' + i).append("<div class='participant-user__pic'>" 
                                          + "<img class='participant-pic__img thumb' src='" 
                                          + currentSite 
                                          + "/file/getimg/100?img=" 
                                          + resData.bidders[i].userImg 
                                          + "'>" 
                                        + "</div>"); 
                $('div#user-' + i).append("<div class='participant-user__name'>" 
                                          + resData.bidders[i].userName.split(' ')[0] + ' id' + resData.bidders[i].userId
                                        + "</div>"); 
            }
                
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




