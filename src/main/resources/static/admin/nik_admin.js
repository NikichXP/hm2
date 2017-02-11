"use strict";

let currentSite = "http://localhost:8080/";
let users = null;

$(function () {

    $.ajax({
        type: 'GET',
        url: currentSite + 'api/admin/user/users',
        success: function (response) {
            users = response;
            print();
        }
    });

    $('#usertable.userid').on('click', function () {
        alert('lol');
        sort('id');
    });

    let sort = function (arg) {
        users = users.sort((x1, x2) => x1.arg.localeCompare(x2.arg));
        print();
    };

    let print = function () {
        $('#user-table-data').remove();
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