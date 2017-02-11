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
    var paramArray = urlArray[1].split('=');
        
    var userId = paramArray[1]; 

    
    $.ajax({
        type: 'GET',
        url: currentSite + '/user/getUser',
        data: {id: userId},
        success: function(resData) {   
            $('.profile-pic').attr("src", currentSite + "/file/getimg/200?img=" + resData.userImg);  
            var name = resData.name.split(' ');   
            
            $('.profile-info__name').html(name[0] + " / " + resData.city); 
            if (resData.pro == true) $('.profile-info__pro').css('display', 'inline-block');
            
            $('.profile-info__group').html(resData.profession); 
            
            var expDateArr = resData.regDate.split('-');
            var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
            var daysLeft = daysBetween(expDate, curDate);
            
            $('.profile-info__days').html("На сайте " + Math.floor(daysLeft) + " дней"); 
            
            $('.profile-desc__text').html(resData.description); 
            
        },
    });
    
    $('body').on('click', '#content-options__port', function() {
        
        $('.content-options__option').children('span').css('border-bottom','2px solid #9c9c9c');  
        $('.content-options__option').children('span').css('color','#585963');
        
        $(this).children('span').css('border-bottom','2px solid #90b651');  
        $(this).children('span').css('color','#90b651');  
        
        $('.table-props').css('display', 'none');  
        $('.container-portfolio').css('display', 'block'); 
    });
    
    $('body').on('click', '#content-options__price', function() {
        
        $('.content-options__option').children('span').css('border-bottom','2px solid #9c9c9c');  
        $('.content-options__option').children('span').css('color','#585963');
        
        $(this).children('span').css('border-bottom','2px solid #90b651');  
        $(this).children('span').css('color','#90b651');  
        
        $('.table-props').css('display', 'block'); 
        $('.container-portfolio').css('display', 'none'); 
    });
    
    $('body').on('click', '#content-options__calend', function() {
        
        $('.content-options__option').children('span').css('border-bottom','2px solid #9c9c9c');  
        $('.content-options__option').children('span').css('color','#585963');
        
        $(this).children('span').css('border-bottom','2px solid #90b651');  
        $(this).children('span').css('color','#90b651');  
        
        $('.table-props').css('display', 'none'); 
        $('.container-portfolio').css('display', 'none'); 
    });
    
    
    
    
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/getUserProducts/arr/' + userId,
        success: function(resData) {  
            $('.table-props tbody').html(""); 
            for (var i = 0; i < resData.length; i++) {
                $('.table-props tbody').append("<tr class='table-props__option' id='prop-option__" + i + "'></tr>");
                
                $('.table-props tr#prop-option__' + i).append("<td class='td-name'>" + resData[i][0].genreName + "</td>");  
                
                for (var j = 0; j < 5; j++) {
                    if (j < resData[i].length)
                        $('.table-props tr#prop-option__' + i).append("<td class='td-price' id='" + resData[i][j].id + "'>" 
                                                        + resData[i][j].finalPrice
                                                        + "</td>");  
                    else $('.table-props tr#prop-option__' + i).append("<td class='td-price'>...</td>"); 
                }
                
                $('.table-props tbody').append("<tr class='table-props__desc' id='prop__desc__" + i + "'></tr>");
                $('.table-props tr#prop__desc__' + i).append("<td colspan='6' class='td-desc'></td>"); 
                $('.table-props tr#prop__desc__' + i + ' td.td-desc').append("<div class='td-desc__title'>Описание услуги:</div>"); 
                $('.table-props tr#prop__desc__' + i + ' td.td-desc').append("<div class='td-desc__text'></div>"); 
                $('.table-props tr#prop__desc__' + i + ' td.td-desc').append("<span class='td-desc__close'>Закрыть</span>"); 
                $('.table-props tr#prop__desc__' + i + ' td.td-desc').append("<button class='btn btn-default btn-send' type='submit'>Заказать</button>"); 
            }  
        },
    });
    
    
    
    
    
//    $('.table-props tr#prop__desc__' + i + 'td.td-desc').append("<div class='td-desc__text'>" 
//                                                                            + resData[i][j].description
//                                                                            + "</div>"); 
    

                
    
    $('body').on('click', '.table-props tr.table-props__option td.td-price', function() {
        
        $('.table-props tr.table-props__desc').css('display', 'none'); 
        var curId = $(this).parent('tr').attr('id').split('__'); 
        
        if ($(this).attr('id') != null) {
            $.ajax({
                type: 'GET',
                url: currentSite + '/product/' + $(this).attr('id'),
                success: function(resData) {  
                    //$(this).parent('tr').next('tr td.td-desc div.td-desc__text').html(""); 
                    $('.table-props tr#prop__desc__' + curId[1] + ' td.td-desc div.td-desc__text').html("");   
                    $('.table-props tr#prop__desc__' + curId[1] + ' td.td-desc div.td-desc__text').append(resData.description); 
                    $('.table-props tr#prop__desc__' + curId[1]).css('display', 'table-row'); 
                    //$(this).parent('tr').next('tr').css('display', 'table-row'); 
                },
            });
            
            
              
        }

    });
    
    $('body').on('click', '.td-desc__close', function() {
           
        $(this).parent('td').parent('tr').css('display', 'none');            
    
    });
    
});




