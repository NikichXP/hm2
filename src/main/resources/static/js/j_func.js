//api path 
var currentSite = "https://hm2.herokuapp.com/api";
//var currentSite = "https://hm2.herokuapp.com";
//var currentSite = "https://07962c19.eu.ngrok.io";

//Change page number or city without losing search params in url

function urlChangePage(pageNum, city) {
        
    var url = window.location;
    url = decodeURIComponent(url);
    
    url = '' + url;
    var urlArray = url.split('?');
    
    var urlMain = urlArray[0];
    
    if (urlArray[1] != null) {
        
        urlArray = urlArray[1].split('&');

        if (pageNum != null) {
            for (var i = 0; i < urlArray.length; i++) 
                if (urlArray[i].includes('page=')) 
                    urlArray[i] = 'page=' + pageNum;
        }


        if (city != null) {
            var cityFound = false;
            for (var i = 0; i < urlArray.length; i++) {
                if (urlArray[i].includes('city=')) {
                    urlArray[i] = 'city=' + city;
                    cityFound = true;
                }       
            }
            if (!cityFound) urlArray.push('city=' + city);        
        }  

        url = urlMain + '?';

        for (var i = 0; i < urlArray.length; i++) 
            if (i == urlArray.length - 1) url += urlArray[i];  
            else url += urlArray[i] + '&';  
    }
    else {
        if (urlMain.slice(-1) == '#') {
            url = urlMain.substring(0, urlMain.length - 1) + '?city=' + city;      
        }
        else {
            url = urlMain + '?city=' + city;
        }
          
    }
    
    return url;    
}

//shit with dates

function treatAsUTC(date) {
    var result = new Date(date);
    result.setMinutes(result.getMinutes() - result.getTimezoneOffset());
    return result;
}

function daysBetween(startDate, endDate) {
    var millisecondsPerDay = 24 * 60 * 60 * 1000;
    return (treatAsUTC(endDate) - treatAsUTC(startDate)) / millisecondsPerDay;
}
