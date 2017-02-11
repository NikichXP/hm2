$(function(){
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
        
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/categories',
        //data: offerData,
        success: function(resData) {
            $('.categories-list').html("");
            $('.categories-list__mob').html('<option selected disabled>Услуги</option>');
            
            var k = 0;
            
            for (var i = 0; i < resData.length; i++) 
                for (var j = 0; j < resData[i].groups.length; j++) 
                    k++; 
            
            k = k / 15;
            $('.categories-list').css('column-count', Math.round(k+1));
            
            for (var i = 0; i < resData.length; i++)
            {  
                $('.categories-list').append("<li class='categories-list__item categories-list__cat'>"
                                             + resData[i].name
                                             + "</li>");  	
                for (var j = 0; j < resData[i].groups.length; j++) {
                                    $('.categories-list').append("<li class='categories-list__item categories-list__group'>"
                                                + resData[i].groups[j].name 
                                                + " <span>("
                                                + resData[i].groups[j].executors 
                                                + ")</span></li>");  	    
                }
                
                $('.categories-list__mob').append("<option disabled class='categories-list__mob-item categories-list__mob-cat'>"
                                             + resData[i].name
                                             + "</option>");  	
                for (var j = 0; j < resData[i].groups.length; j++) {
                                    $('.categories-list__mob').append("<option class='categories-list__mob-item __mob-group'>"
                                                + resData[i].groups[j].name 
                                                + " <span>("
                                                + resData[i].groups[j].executors 
                                                + ")</span></option>");  	    
                }
            
            };           
        },
    });
    

    $.ajax({
        type: 'GET',
        url: currentSite + '/config/list/city',
        //data: offerData,
        success: function(resData) {
            //get city from cookies 
            if (getCookie('city') != null) {
                $('.city-list__mob').html('<option selected disabled>' + getCookie('city') + '</option>');   
                $('#menu-button__city').html(getCookie('city'));     
            }    
            else {
                $('.city-list__mob').html('<option selected disabled>Выберите город</option>');  
                $('#menu-button__city').html('Выберите город'); 
            }
            //fill mobile city list
            for (var i = 0; i < resData.length; i++)
            {  
                $('.city-list__mob').append("<option class='categories-list__mob-item'>"
                                    + resData[i] 
                                    + "</option>");  	    
            };  
            //fill desktop city list            
            for (var i = 0; i < resData.length; i++) {
                
                if (resData[i] == getCookie('city')) 
                    $('.city-list').append("<li class='city-list__item city-list__current'>"
                                    + resData[i]
                                    + "</li>");      
                else 
                    $('.city-list').append("<li class='city-list__item'>"
                                    + resData[i]
                                    + "</li>");  	    
            }
        },
    });
    
    $('body').on('click', 'li.city-list__item', function() {	
        $('#menu-button__city').html($(this).html());
        setCookie('city', $(this).html());
        window.location.reload();
    });
    
    $('body').on('change', '.city-list__mob' , function() { 
        setCookie('city', $(this).val());
        window.location.reload();
    });
    
    
    
     
    $('body').on('click', '#propositions', function() {	

        $('.categories-container').slideToggle();
    });   
    
    $('body').on('click', '#menu-button__city', function() {	

        $('.city-container').slideToggle();
    }); 
    
    $('body').on('click', '#navbar-auth-button', function() {
        if ($('#navbar-auth-button span').html() == 'ВХОД') $('.auth-menu-mob').fadeToggle();
        else $('.profile-menu-mob').fadeToggle();
    });
    
    $('body').on('click', '.auth-menu-mob #form-button__auth', function() {
        
        $('.auth-menu-mob .auth-menu__warn').html('');
        
        if ($('.auth-menu-mob #form-input__email').val() != '') {
            
            var authEmail = $('.auth-menu-mob #form-input__email').val(); 
            
            if ($('.auth-menu-mob #form-input__pass').val() != '') {
                
                var authPass = $('.auth-menu-mob #form-input__pass').val(); 
                
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
                        else $('.auth-menu-mob .auth-menu__warn').html("Вы ввели неверные данные");  
                    },
                    error: function(resData) {          
                        $('.auth-menu-mob .auth-menu__warn').html("Вы ввели неверные данные");        
                    },
                });
                
            }
            else {
                $('.auth-menu-mob .auth-menu__warn').append("Заполните оба поля");    
            }
        }
        else {
            $('.auth-menu-mob .auth-menu__warn').append("Заполните оба поля");    
        }
       
    });
        
});

