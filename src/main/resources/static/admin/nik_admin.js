"use strict";

let currentSite = "http://localhost:8080/";

$(function () {
    $.ajax({
        type: 'GET',
        url: currentSite + 'api/admin/user/users',
        success: function (response) {
            response
                .sort((x1, x2) => x1.name.localeCompare(x2.name))
                .forEach(x => {
                $('#user-table-data')
            .append(`<tr>
                <td>${x.id}</td>
                <td>${x.name}</td>
                <td>${x.mail}</td>
                <td>${x.entityClassName}</td>
                <td>${(x.profession != null) ? x.profession : '----'}</td>
                </tr>`);
        });
        }
    });
});