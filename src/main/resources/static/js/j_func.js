//Change page number or city without losing search params in url

function urlChangePage(pageNum, city) {
        
    var url = window.location;
    url = decodeURIComponent(url);
    
    url = '' + url;
    var urlArray = url.split('?');
    
    var urlMain = urlArray[0];
    urlArray = urlArray[1].split('&');
    
    if (pageNum !== undefined) {
        for (var i = 0; i < urlArray.length; i++) 
            if (urlArray[i].includes('page=')) 
                urlArray[i] = 'page=' + pageNum;
    }
    
    
    if (city !== undefined) {
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
    
    return url;    
}