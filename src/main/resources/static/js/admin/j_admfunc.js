function getData(m) {
    
    var d = $.ajax({
              type: 'GET',
              url: currentSite + '/admin/user/' + m,
              async: false,
              dataType: "json",
           }).responseJSON;
    
    d.sort((e1, e2) => e1.id - e2.id);
    
    tableGen(d, m);
    
    return d;
}

function tableGen(d, type) {
    
    $('.table-users tbody').html("")
    
    switch (type) {
        case 'clients' : {
            
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='user-" + i + "'></tr>")

                $("tr#user-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].name + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].city + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].mail + "</td>");

                $("tr#user-" + i).append("<td>" + d[i].phone + "</td>");

                if (d[i].banned) $("tr#user-" + i).append("<td>Заблокирован</td>");
                else $("tr#user-" + i).append("<td>Активен</td>");

                $("tr#user-" + i).append("<td class='td-desc'>" + d[i].description + "</td>");     
            } 
            break;
        }
            
        case 'moderators': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='user-" + i + "'></tr>")

                $("tr#user-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].name + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].city + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].mail + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].phone + "</td>");

                if (d[i].banned) $("tr#user-" + i).append("<td>Заблокирован</td>");
                else $("tr#user-" + i).append("<td>Активен</td>");

                $("tr#user-" + i).append("<td class='td-desc'>" + d[i].description + "</td>");       
            }
            break;
        }
            
        case 'workers': {
        
            for (var i = 0; i < d.length; i++) {
                $('.table-users tbody').append("<tr class='tr-user' id='user-" + i + "'></tr>")

                $("tr#user-" + i).append("<td class='td-id'>" + d[i].id + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].name + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].city + "</td>");
                $("tr#user-" + i).append("<td>" + d[i].mail + "</td>");

                $("tr#user-" + i).append("<td>" + d[i].profession + "</td>");

                if (d[i].pro) $("tr#user-" + i).append("<td>PRO</td>");
                else $("tr#user-" + i).append("<td>Basic</td>");

                $("tr#user-" + i).append("<td>" + d[i].phone + "</td>");

                if (d[i].banned) $("tr#user-" + i).append("<td>Заблокирован</td>");
                else $("tr#user-" + i).append("<td>Активен</td>");

                $("tr#user-" + i).append("<td class='td-desc'>" + d[i].description + "</td>");       
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


