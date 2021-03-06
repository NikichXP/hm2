var data=[]; //json data

var tableType = 'clients';

$(function(){
    
    
    data = getData(tableType);
    
    
    
    //table headers onclicks
    
    $('body').on('click', 'th', function() {	   
        
        switch ($(this).attr('class').split('-')[1]) {
                case 'id': data.sort((e1, e2) => e1.id - e2.id); tableGen(data, tableType); break; 
                case 'name': data.sort((e1, e2) => e1.name.localeCompare(e2.name)); tableGen(data, tableType); break; 
                case 'city': data.sort((e1, e2) => e1.city.localeCompare(e2.city)); tableGen(data, tableType); break; 
                case 'mail': data.sort((e1, e2) => e1.mail.localeCompare(e2.mail)); tableGen(data, tableType); break; 
                case 'prop': data.sort((e1, e2) => e1.profession.localeCompare(e2.profession)); tableGen(data, tableType); break; 
                case 'type': data.sort((e1, e2) => (e2.pro) ? (e2.pro == e1.pro) ? 0 : 1 : (e2.pro == e1.pro) ? 0 : -1); tableGen(data, tableType); break; 
                case 'tel': data.sort((e1, e2) => e1.phone - e2.phone); tableGen(data, tableType); break; 
                case 'acc': data.sort((e1, e2) => (e2.banned) ? (e2.banned == e1.banned) ? 0 : 1 : (e2.banned == e1.banned) ? 0 : -1); tableGen(data, tableType); break; 
                //case 'comm': data.sort((e1, e2) => e1.description - e2.description); tableGen(data, tableType); break; 
        }
    

    });
    
    //end of table headers onclicks
    
    //id onclicks
    
    $('body').on('click', 'td.td-id', function() {	
        loginAsUser($(this).html());
    
    });
    
    //end of id onclicks
    
});




