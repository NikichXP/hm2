var data=[]; //json data

var tableType = 'categories';

$(function(){
    
    data = getData(tableType);
    
//    $('body').on('click', '.button-city', function() {	   
//        
//        var city = prompt('Введите название города', 'Город');
//        
//        if (city != null) {
//            
//            $.ajax({
//                type: 'GET',
//                url: currentSite + '/config/list/city/add/' + city,
//            });
//            
//            //window.location.reload();
//        }
//        
//
//
//
//    });
        
});