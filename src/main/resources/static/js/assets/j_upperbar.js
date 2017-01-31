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
    
    
    //Authbutton
    
    $('body').on('click', '#auth-button__auth', function() {
        if ($('#auth-button__auth span').html() == 'Вход') $('.auth-menu').fadeToggle();
        else $('.profile-menu').fadeToggle();
    });   
    
    
    
    $('body').on('click', '#form-button__auth', function() {
        
        $('.auth-menu__warn').html('');
        
        if ($('#form-input__email').val() != '') {
            
            var authEmail = $('#form-input__email').val(); 
            
            if ($('#form-input__pass').val() != '') {
                
                var authPass = $('#form-input__pass').val(); 
                
                $.ajax({
                    type: 'GET',
                    url: currentSite + '/auth/auth',
                    data: {login: authEmail, pass: authPass},
                    success: function(resData) {          
                        setCookie('sessionId', resData.sessionID);  
                        setCookie('userId', resData.user.id);
                        setCookie('username', resData.user.name); 
                        setCookie('accessLevel', resData.user.entityClassName); 
                        window.location.reload();
                    },
                    error: function(resData) {          
                        $('.auth-menu__warn').html("Вы ввели неверные данные");        
                    },
                });
                
            }
            else {
                $('.auth-menu__warn').append("Заполните оба поля");    
            }
        }
        else {
            $('.auth-menu__warn').append("Заполните оба поля");    
        }
       
    });
    

    
    //authd menu
    
    $('body').on('click', '#profile-menu__logout', function() {
        deleteCookie('sessionId');
        deleteCookie('username'); 
        deleteCookie('accessLevel'); 
        window.location.reload();
    }); 
    
    $('body').on('click', '#profile-menu__profile', function() {        
        window.location.replace('profile.html?id=' + getCookie('userId'));
    });
    
       
});


        

