$(function(){
    
    
		
	$('#auth-button_search').on('click', function(){			
        $.ajax({
            type: 'GET',
            url: 'https://hm2.herokuapp.com/test/getMappings',
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



