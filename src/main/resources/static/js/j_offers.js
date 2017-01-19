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
    
    
//    $('#auth-button_search').on('click', function(){			
//        $.ajax({
//            type: 'GET',
//            url: 'https://hm2.herokuapp.com/product/offer',
//            data: { limit: '20' },
//            success: function(resData) {
//                for (var i = 0; i < resData.length; i++)
//                {
//                    $('.upper-bar').append("<p>" +
//                        resData[i] +
//                        "</p>");	   	
//                }; 
//                
//            },
//        });
//
//    });
    
    $.ajax({
        type: 'GET',
        url: 'https://hm2.herokuapp.com/product/offer',
        data: { limit: '20' },
        success: function(resData) {
            $('.offers-container').html("");	
            for (var i = 0; i < resData.length; i++)
            {
                $('.offers-container').append("<div class='col-md-3 col-sm-6 hero-feature'>" 
                                            + "<img src='../files/" + resData[i].image + "' alt=''>"  
                                            + "<div class='prob-block-bg'>"
                                            + "</div>"
                                            + "<div class='prob-block-desc'>"
                                                + "<h5>г. " + resData[i].city + "</h5>"
                                                + "<h4>" + resData[i].title + "</h4>"
                                            + "</div>"
                                            + "<div class='prob-block-price'> "
                                                + "<div class='price-new'>" + resData[i].finalPrice + " грн</div>"
                                                + "<div class='price-old'>" + resData[i].price + " грн</div>"
                                                + "<div class='price-to'>" + i + " дней</div>"
                                            + "</div>"
                                            + "<div class='prob-block-dis'>-" + resData[i].discount * 100 + "%</div>"
                                        + "</div>");	   	
            }; 
                
        },
    });
    
    
//            <div class="col-md-3 col-sm-6 hero-feature">
//                
//                <img src="img/auth5.jpg" alt="">
//                    <div class="prob-block-bg">   
//                    </div>
//                    <div class="prob-block-desc">   
//                        <h5>г.Киев</h5>
//                        <h4>Lorem ipsum dolor sit amet, consectetur adipisicing elit.</h4>
//                    </div>
//                    <div class="prob-block-price"> 
//                        <div class='price-new'>2000 грн</div>
//                        <div class='price-old'>9000 грн</div>
//                        <div class='price-to'>10 дней</div>
//                    </div>
//                    <div class="prob-block-dis">-150%</div>
//                <div class="thumbnail prop-block">                   
//                </div>
//
//            </div>
    
    
    
    

});



