var data=[]; //json data

var tableType = 'tenders';

$(function(){
    
    
    data = getData(tableType);
    
    
    //table headers onclicks
    
    $('body').on('click', 'th', function() {	   
        
        switch ($(this).attr('class').split('-')[1]) {
            case 'id': data.sort((e1, e2) => e1.id - e2.id); tableGen(data, tableType); break; 
            case 'username': data.sort((e1, e2) => e1.creator.name.localeCompare(e2.creator.name)); tableGen(data, tableType); break; 
            case 'date': data.sort((e1, e2) => e1.deadlineString.localeCompare(e2.deadlineString)); tableGen(data, tableType); break; 
            case 'city': data.sort((e1, e2) => e1.city.localeCompare(e2.city)); tableGen(data, tableType); break; 
            case 'phone': data.sort((e1, e2) => e1.creator.phone - e2.creator.phone); tableGen(data, tableType); break; 
            case 'price': data.sort((e1, e2) => e1.price - e2.price); tableGen(data, tableType); break; 
            case 'part': data.sort((e1, e2) => e1.assignedUser - e2.assignedUser); tableGen(data, tableType); break;
            case 'paid': data.sort((e1, e2) => (e2.paid) ? (e2.paid == e1.paid) ? 0 : 1 : (e2.paid == e1.paid) ? 0 : -1); tableGen(data, tableType); break;
            case 'open': data.sort((e1, e2) => (e2.active) ? (e2.active == e1.active) ? 0 : 1 : (e2.active == e1.active) ? 0 : -1); tableGen(data, tableType); break;
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




