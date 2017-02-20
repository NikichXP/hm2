$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    
    if (getCookie('sessionId') != null) {
        
        var url = window.location;
        url = decodeURIComponent(url);

        url = '' + url;
        var urlArray = url.split('.');

        var urlMain = urlArray[0].substr(0, urlArray[0].length-8);
        window.location.replace(urlMain + 'index.html');  
    }
    
 
    var curDate = new Date();
    
    
    $('body').on('change', '.optionsRadios', function() {	
    
        switch ($(this).attr('id')) {
            case 'optionsRadios1': {
                $('.alert-title span').html('ВНИМАНИЕ');
                $('.alert-text span').html('В случае указания Вами своих контактных данных (номеров телефонов, ссылок на посторонние ресурсы) в любом из разделов, доступных для просмотра посетителям сайта, Ваш профиль не пройдет модерацию<br>');
                $('.btn-send').html('Я согласен');
                $('.register-alert').css('display','block');
                break;
            } 
            case 'optionsRadios2': {
                $('.alert-title span').html('HappyMoments.ua');
                
                $('.alert-text span').html('HappyMoments.ua – это информативный и простой в использовании сайт с гарантией лучшей цены. <br>Мы стремимся обеспечить удобный, экономичный и эффективный способ поиска и заказа различных услуг для тех, кто хочет сделать свой праздник качественным и запоминающимся. <br>Услуги компании HappyMoments.ua предоставляются бесплатно. <br>Мы не взымаем плату за подбор и заказ учасников<br>');
                $('.btn-send').html('Продолжить');
                $('.register-alert').css('display','block');
                break;
            }
        }
    });  
    

});




