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
            if (getCookie('city') != null) $('.city-list__mob').html('<option selected disabled>' + getCookie('city') + '</option>');     
            else $('.city-list__mob').html('<option selected disabled>Выберите город</option>');  
            
            
            for (var i = 0; i < resData.length; i++)
            {  
                	

                $('.city-list__mob').append("<option class='categories-list__mob-item __mob-group'>"
                                    + resData[i] 
                                    + "</option>");  	    
            
            };           
        },
    });
    
    
     
    $('body').on('click', '#propositions', function() {	
//        var position = $(this).offset();
//        $('.categories-container').css('top', position.top + 68);
//        $('.categories-container').css('top', position.left);
        $('.categories-container').slideToggle();
    });   
    
});

