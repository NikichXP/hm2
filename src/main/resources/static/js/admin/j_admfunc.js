function getData(m) {
     
    switch (m) {
        case 'clients':
        case 'moderators':
        case 'workers': {
            var d = $.ajax({
                      type: 'GET',
                      url: currentSite + '/admin/user/' + m,
                      async: false,
                      dataType: "json",
                   }).responseJSON;

            d.sort((e1, e2) => e1.id - e2.id);
            break;
        }
        case 'tenders':
        case 'products': {
            var d = $.ajax({
                      type: 'GET',
                      url: currentSite + '/admin/crm/' + m,
                      async: false,
                      dataType: "json",
                   }).responseJSON;

            d.sort((e1, e2) => e1.id - e2.id);
            break;    
        }
        case 'genre':     
        case 'city': {
            var d = $.ajax({
                      type: 'GET',
                      url: currentSite + '/config/list/' + m,
                      async: false,
                      dataType: "json",
                   }).responseJSON;

            //d.sort((e1, e2) => e1.id - e2.id);
            break;      
        }
        case 'categories': {
            var d = $.ajax({
                      type: 'GET',
                      url: currentSite + '/product/' + m,
                      async: false,
                      dataType: "json",
                   }).responseJSON;
            break;
        }
    }
    tableGen(d, m);
    
    return d;
}

function tableGen(d, type) {
     
    $('.table-users tbody').html("")
    
    switch (type) {
        case 'clients' : {
            
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='row-" + i + "'></tr>")

                $("tr#row-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].name + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].city + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].mail + "</td>");

                $("tr#row-" + i).append("<td>" + d[i].phone + "</td>");

                if (d[i].banned) $("tr#row-" + i).append("<td>Заблокирован</td>");
                else $("tr#row-" + i).append("<td>Активен</td>");

                $("tr#row-" + i).append("<td class='td-desc'>" + d[i].comment + "</td>");     
            } 
            break;
        }
            
        case 'moderators': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='row-" + i + "'></tr>")

                $("tr#row-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].name + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].city + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].mail + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].phone + "</td>");

                if (d[i].banned) $("tr#row-" + i).append("<td>Заблокирован</td>");
                else $("tr#row-" + i).append("<td>Активен</td>");

                $("tr#row-" + i).append("<td class='td-desc'>" + d[i].comment + "</td>");       
            }
            break;
        }
            
        case 'workers': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='row-" + i + "'></tr>")

                $("tr#row-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].name + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].city + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].mail + "</td>");

                $("tr#row-" + i).append("<td>" + d[i].profession + "</td>");

                if (d[i].pro) $("tr#row-" + i).append("<td>PRO</td>");
                else $("tr#row-" + i).append("<td>Basic</td>");

                $("tr#row-" + i).append("<td>" + d[i].phone + "</td>");

                if (d[i].banned) $("tr#row-" + i).append("<td>Заблокирован</td>");
                else $("tr#row-" + i).append("<td>Активен</td>");

                $("tr#row-" + i).append("<td class='td-desc'>" + d[i].comment + "</td>");       
            }
            break;
        }
            
        case 'tenders': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='row-" + i + "'></tr>")

                $("tr#row-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].creator.name + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].deadlineString + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].city + "</td>");

                $("tr#row-" + i).append("<td>" + d[i].creator.phone + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].price + "</td>");

                if (d[i].assignedUser != null) $("tr#row-" + i).append("<td>" + d[i].assignedUser + "</td>");
                else $("tr#row-" + i).append("<td>Нет</td>");
                
                if (d[i].paid) $("tr#row-" + i).append("<td>Да</td>");
                else $("tr#row-" + i).append("<td>Нет</td>");
                
                if (d[i].active) $("tr#row-" + i).append("<td>Да</td>");
                else $("tr#row-" + i).append("<td>Нет</td>");

                if (d[i].validated) $("tr#row-" + i).append("<td>Да</td>");
                else $("tr#row-" + i).append("<td>Нет</td>");

                $("tr#row-" + i).append("<td class='td-desc'>" + d[i].comment + "</td>");       
            }
            break;
        }
            
        case 'products': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='row-" + i + "'></tr>")

                $("tr#row-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].workerEntity.name + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].title + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].groupName + "</td>");

                $("tr#row-" + i).append("<td>" + d[i].genreName + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].price + "</td>");

                if (d[i].discount > 0) $("tr#row-" + i).append("<td>Да</td>");
                else $("tr#row-" + i).append("<td>Нет</td>");

                if (d[i].validated) $("tr#row-" + i).append("<td>Да</td>");
                else $("tr#row-" + i).append("<td>Нет</td>");

                $("tr#row-" + i).append("<td class='td-desc'>" + d[i].comment + "</td>");       
            }
            break;
        }
            
        case 'city': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='row-" + i + "'></tr>")

                $("tr#row-" + i).append("<td class='td-id'>" + i + "</td>");
                $("tr#row-" + i).append("<td>" + d[i] + "</td>");      
            }
            break;
        }
            
        case 'categories': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-cat' id='row-" + i + "'></tr>")
                $("tr#row-" + i).append("<td class='td-cat'>" + d[i].name + "</td>");
                $("tr#row-" + i).append("<td>" + d[i].id + "</td>");
                
                
                for (var j = 0; j < d[i].groups.length; j++) { 
                    $('.table-users tbody').append("<tr class='tr-gr' id='row-" + i + "-" + j + "'></tr>")
                    $("#row-" + i + "-" + j).append("<td>" + d[i].groups[j].name + "</td>"); 
                    $("#row-" + i + "-" + j).append("<td>" + d[i].groups[j].id + "</td>");
                     
                }
                
                    
            }
            break;
        }
            
        case 'genre': {
        
            for (var i = 0; i < d.length; i++) {
//                $('.table-users tbody').append("<tr class='tr-cat' id='row-" + i + "'></tr>")
                
                for (var j = 0; j < d[i].groups.length; j++) { 
                    
//                    $("tr#row-" + i).append("<td class='td-cat'>" + d[i].groups[j].name + "</td>");
//                    $("tr#row-" + i).append("<td>" + d[i].groups[j].id + "</td>");
                    
//                    if (d[i].groups[j].genres.length > 0) {
                        for (var k = 0; k < d[i].groups[j].genres.length; k++) { 
                            $('.table-users tbody').append("<tr class='tr-gr' id='row-" + i + "-" + j + "-" + k + "'></tr>")
                            $("#row-" + i + "-" + j + "-" + k).append("<td>" + d[i].groups[j].genres[k].name + "</td>"); 
                            $("#row-" + i + "-" + j + "-" + k).append("<td>" + d[i].groups[j].genres[k].id + "</td>");

                        }    
//                    }
//                    else {
//                        $('.table-users tbody').append("<tr class='tr-gr' id='row-" + i + "-" + j + "-" + k + "'></tr>")
//                        $("#row-" + i + "-" + j + "-" + 0).append("<td>" + d[i].groups[j].genres.name + "</td>"); 
//                        $("#row-" + i + "-" + j + "-" + 0).append("<td>" + d[i].groups[j].genres.name + "</td>");        
//                    }
                    
                    
                }
                
                
                
                
                    
            }
            break;
        }
        
    }       
}

function loginAsUser(id) {
    
    $.ajax({
        type: 'GET',
        url: currentSite + '/admin/user/loginasother',
        data: {token: getCookie('sessionId'), target: id},
        success: function(resData) {
            setCookie('sessionIdAdm', getCookie('sessionId'));  
            setCookie('userIdAdm', getCookie('userId'));
            setCookie('usernameAdm', getCookie('username')); 
            setCookie('accessLevelAdm', getCookie('accessLevel')); 
            
            setCookie('sessionId', resData.sessionID);  
            setCookie('userId', resData.user.id);
            setCookie('username', resData.user.name); 
            setCookie('accessLevel', resData.user.entityClassName); 
            
            window.location.href = '../profile.html?id=' + resData.user.id;           
        },
    });   
     
}


