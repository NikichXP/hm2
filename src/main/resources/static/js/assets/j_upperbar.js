$(function(){
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
    
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/config/list/city',
        success: function(resData) {
            
            if (getCookie('city') != null) $('#upper-innertext__city').html(getCookie('city'));     
            else $('#upper-innertext__city').html('Выбрать город');
            
            $('.dropdown-upper-menu__city').html("");
            
            for (var i = 0; i < resData.length; i++)
            {          
                $('.dropdown-upper-menu__city').append("<li><a class='dropdown-menu__item' href='#'>" 
                                            + resData[i]  
                                            + "</a></li>");  	
            };                
                
        },
    });
    
    $('body').on('click', 'ul.dropdown-upper-menu__city li a.dropdown-menu__item', function() {	
        $('span#upper-innertext__city').html($(this).html());
        setCookie('city', $(this).html());
        window.location.reload();
    });
    

     

    
});

