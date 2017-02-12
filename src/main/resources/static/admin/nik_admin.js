// "use strict";

let currentSite = "http://localhost:8080/";
let users = null;

$(function () {

    $.ajax({
        type: 'GET',
        url: currentSite + 'api/admin/user/users',
        success: function (response) {
            // alert('i work');
            users = response;
            sort('id');
            print();
        }
    });

    $('#usertable.userid').on('click', function () {
        sort('id');
    });

    let sort = function (arg) {
        if (arg == 'id') {
            users = users.sort((x1, x2) => x1.id - x2.id);
        } else {
            users = users.sort((x1, x2) => x1[arg].localeCompare(x2[arg]));
        }
        print();
    };

    let print = function () {
        // $('#user-table-data').innerText;
        users.forEach(x => {
            $('#user-table-data').append(`<tr>
                <td>${x.id}</td>
                <td>${x.name}</td>
                <td>${x.mail}</td>
                <td>${x.entityClassName}</td>
                <td>${(x.profession != null) ? x.profession : '----'}</td>
                <td>${x.regdate}</td>
                </tr>`);
        });
    };

});