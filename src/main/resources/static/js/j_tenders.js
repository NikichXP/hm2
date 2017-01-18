$(function(){
    
    
    for (var i = 1; i < 10; i++) {
    
       $('.page-navigation__list').append("<li class='page-navigation__page'>" +
                        i +
                        "</li>");	     
    }
    
    $('.page-navigation__list').append("<li class='page-navigation__page'>" +
                        "Следующая" +
                        "</li>");
    $('.page-navigation__list').append("<li class='page-navigation__page'>" +
                        "Последняя" +
                        "</li>");
    
    
    $('#auth-button_search').on('click', function(){			
        $.ajax({
            type: 'GET',
            url: 'https://hm2.herokuapp.com/product/offer',
            //data: {  },
            success: function(resData) {
                for (var i = 0; i < resData.length; i++)
                {
                    $('.upper-bar').append("<p>" +
                        resData[i] +
                        "</p>");	   	
                }; 
                
            },
        });

    });
    
    
    
    

});



