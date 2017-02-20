$(function(){
    

    if (getCookie('sessionId') != null) {
        $.ajax({
            type: 'GET',
            url: currentSite + '/auth/check/' + getCookie('sessionId'),
            error: function() {
                deleteCookie('sessionId');  
                deleteCookie('userId');
                deleteCookie('username'); 
                deleteCookie('accessLevel'); 
            }
        });   
    }
    
    
    //Authbutton
    
    
    
    $('body').on('click', '#form-button__reg', function() {
        window.location.href = 'register.html';
    }); 
    
    $('body').on('click', '#auth-button__auth', function() {
        if ($('#auth-button__auth span').html() == 'Вход') $('.auth-menu').fadeToggle();
        else $('.profile-menu').fadeToggle();
    });   
    
    
    
    $('body').on('click', '.auth-menu #form-button__auth', function() {
        
        $('.auth-menu .auth-menu__warn').html('');
        
        if ($('.auth-menu #form-input__email').val() != '') {
            
            var authEmail = $('.auth-menu #form-input__email').val(); 
            
            if ($('.auth-menu #form-input__pass').val() != '') {
                
                var authPass = $('.auth-menu #form-input__pass').val(); 
                
                $.ajax({
                    type: 'GET',
                    url: currentSite + '/auth/auth',
                    data: {login: authEmail, pass: authPass},
                    success: function(resData) {  
                        
                        if (resData != '') {
                            setCookie('sessionId', resData.sessionID);  
                            setCookie('userId', resData.user.id);
                            setCookie('username', resData.user.name); 
                            setCookie('accessLevel', resData.user.entityClassName); 
                            window.location.reload();
                        }
                        else $('.auth-menu .auth-menu__warn').html("Вы ввели неверные данные");  
                    },
                    error: function(resData) {          
                        $('.auth-menu .auth-menu__warn').html("Вы ввели неверные данные");        
                    },
                });
                
            }
            else {
                $('.auth-menu .auth-menu__warn').append("Заполните оба поля");    
            }
        }
        else {
            $('.auth-menu .auth-menu__warn').append("Заполните оба поля");    
        }
       
    });
    

    
    //authd menu
    
    $('body').on('click', '#profile-menu__logout', function() {
        deleteCookie('sessionId');
        deleteCookie('userId');
        deleteCookie('username'); 
        deleteCookie('accessLevel'); 
        window.location.reload();
    }); 
    
    $('body').on('click', '#profile-menu__profile', function() {        
        window.location.replace('profile.html?id=' + getCookie('userId'));
    });
    
    
    $('body').on('click', '.back', function() {
            
        setCookie('sessionId', getCookie('sessionIdAdm'));  
        setCookie('userId', getCookie('userIdAdm'));
        setCookie('username', getCookie('usernameAdm')); 
        setCookie('accessLevel', getCookie('accessLevelAdm')); 
        
        deleteCookie('sessionIdAdm');
        deleteCookie('userIdAdm');
        deleteCookie('usernameAdm');
        deleteCookie('accessLevelAdm');
        
        window.location.href = 'admin/users.html';  
        
    });
    
    $('body').on('click', '.logout', function() {
            
        deleteCookie('sessionId');
        deleteCookie('userId');
        deleteCookie('username'); 
        deleteCookie('accessLevel'); 
        
        deleteCookie('sessionIdAdm');
        deleteCookie('userIdAdm');
        deleteCookie('usernameAdm');
        deleteCookie('accessLevelAdm');
        
        window.location.reload();  
        
    });
    
       
});


        

