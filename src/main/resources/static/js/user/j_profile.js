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
            $('.profile-pic').attr("src", currentSite + "/file/get?file=" + resData.userImg);  
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
    
    
    
    
    $('body').on('click', '.table-props tr.table-props__option td.td-price', function() {
        
        $(this).parent('tr').next().css('display', 'table-row');            

    });
    
    $('body').on('click', '.td-desc__close', function() {
           
        $(this).parent('td').parent('tr').css('display', 'none');            
    
    });
    
});




