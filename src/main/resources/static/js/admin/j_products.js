var data=[]; //json data

var tableType = 'products';

$(function(){
    
    
    data = getData(tableType);
    
    
    //table headers onclicks
    
    $('body').on('click', 'th', function() {	   
        
        switch ($(this).attr('class').split('-')[1]) {
            case 'id': data.sort((e1, e2) => e1.id - e2.id); tableGen(data, tableType); break; 
            case 'creator': data.sort((e1, e2) => e1.workerEntity.name.localeCompare(e2.workerEntity.name)); tableGen(data, tableType); break; 
            case 'name': data.sort((e1, e2) => e1.title.localeCompare(e2.title)); tableGen(data, tableType); break; 
            case 'cat': data.sort((e1, e2) => e1.groupName.localeCompare(e2.groupName)); tableGen(data, tableType); break; 
            case 'genre': data.sort((e1, e2) => e1.genreName.localeCompare(e2.genreName)); tableGen(data, tableType); break; 
            case 'price': data.sort((e1, e2) => e1.price - e2.price); tableGen(data, tableType); break; 
            case 'disc': data.sort((e1, e2) => e1.discount - e2.discount); tableGen(data, tableType); break;
            case 'mod': data.sort((e1, e2) => (e2.validated) ? (e2.validated == e1.validated) ? 0 : 1 : (e2.validated == e1.validated) ? 0 : -1); tableGen(data, tableType); break;
    
                //case 'acc': data.sort((e1, e2) => (e2.banned) ? (e2.banned == e1.banned) ? 0 : 1 : (e2.banned == e1.banned) ? 0 : -1); tableGen(data, tableType); break; 
                //case 'comm': data.sort((e1, e2) => e1.description - e2.description); tableGen(data, tableType); break; 
        }  
 
    });
    
    //end of table headers onclicks
    
    //id onclicks
    
//    $('body').on('click', 'td.td-id', function() {	
//        loginAsUser($(this).html());
//    
//    });
    
    //end of id onclicks
    
});




