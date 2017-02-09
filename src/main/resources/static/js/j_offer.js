$(function(){
    
    $('.main-navbar').load('assets/navbar.html');
    $('.upper-bar').load('assets/upperbar.html');
    $('.container-footer').load('assets/footer.html');
    
    var currentSite = "https://hm2.herokuapp.com";
    //var currentSite = "https://07962c19.eu.ngrok.io";
    var curDate = new Date();
    
    var url = window.location;
    url = decodeURIComponent(url);
    
    url = '' + url;

    var urlArray = url.split('?');
    var paramArray = urlArray[1].split('=');
        
    var offerId = paramArray[1]; 
    var sendData = { 
        limit: 2, 
        shuffle: true, 
    }
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/' + offerId,
        success: function(resData) {
            
            sendData.genre = resData.groupName;
            
            $('.container-offer h1').html(resData.title);
            $('.offer-pic').attr('src', currentSite + "/file/get?file=" + resData.image);
            $('.offer-disc__price').html("-" + resData.discount + "%");
            $('.offer-desc__about').append(resData.description);
            $('.offer-desc__info').append(resData.condition);
            $('.offer-user__link').attr('href', 'profile.html?id=' + resData.workerEntity.id);
            
            $('.offer-user__pic').attr('src', currentSite + "/file/get?file=" + resData.workerEntity.userImg);
            $('.offer-user__info').append(resData.workerEntity.name);
            
            var expDateArr = resData.expirationDateString.split('-');
            var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
            var daysLeft = daysBetween(curDate, expDate);
            
            $('.offer-info__price span').append(resData.finalPrice);
            $('.offer-info__old span').append(resData.price);
            //$('.offer-info__date span').append('' + expDateArr[0] + '-' + expDateArr[1] - 1 + '-' + expDateArr[2]);
            ;
            $('.offer-info__date span').append(expDateArr[2] + '.' + expDateArr[1]-- + '.' + expDateArr[0]);
            $('.offer-info__days span').append(Math.floor(daysLeft));
            
        }
    });
    
    
    
    //sendData.genre = 2;
    
    //Similar offers
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/product/offer',
        data: sendData,
        success: function(resData) {
            $('.offer-similar').html("<h3 class='offer-similar__header'>Похожие акции</h3>");
            
            for (var i = 1; i < resData.length; i++) {          
                var expDateArr = resData[i].expirationDateString.split('-');
                var expDate = new Date(expDateArr[0], expDateArr[1] - 1, expDateArr[2]);
                var daysLeft = daysBetween(curDate, expDate);
                $('.offer-similar').append("<a href='offer.html?id=" + resData[i].id + "'><div class='offers-container__offer'>" 
                                            + "<div class='offer-image' style='background: url(" + currentSite + "/file/get?file=" + resData[i].image + ") 0px 0px no-repeat; background-size: cover; background-position: center;'></div>" 
                                            + "<div class='prob-block-bg'>"
                                            + "</div>"
                                            + "<div class='prob-block-desc'>"
                                                + "<h5>г. " + resData[i].city + "</h5>"
                                                + "<h4>" + resData[i].title + "</h4>"
                                            + "</div>"
                                            + "<a href='profile.html?id=" + resData[i].workerEntity.id + "'>"
                                            + "<div class='prob-block-user'>"
                                                + "<img class='user-pic__img thumb' src='" + currentSite + "/file/get?file=" + resData[i].workerEntity.userImg + "'>"
                                                + "<div class='user-name'>" + resData[i].workerEntity.name + "</div>"
                                            + "</div></a>"
                                            + "<div class='prob-block-price'> "
                                                + "<div class='price-new'>" + resData[i].finalPrice + " грн</div>"
                                                + "<div class='price-old'>" + resData[i].price + " грн</div>"
                                                + "<div class='price-to'>" + Math.floor(daysLeft) + " дней</div>"
                                            + "</div>"
                                            + "<div class='prob-block-dis'>-" + resData[i].discount + "%</div>"
                                        + "</div></a>");  	
            };                
                
        },
    });
    

    
 
    
});




