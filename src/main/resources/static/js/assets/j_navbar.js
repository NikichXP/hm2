$(function(){
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/categories',
        //data: offerData,
        success: function(resData) {
            $('.categories-list').html("");	
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
            
            };           
        },
    });
    
    
    
    $('body').on('click', '#propositions', function() {	
        $('.categories-container').slideToggle();
    });   
    
});

