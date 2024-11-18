$(document).ready(function () {
    $.ajax({
        url: "/board/list",
        type: "GET",
        contentType: "application/json",
        success: function (res) {
            console.log(res)
            const tableBody = $('.board-table tbody');
            tableBody.empty();

            res.forEach(function(board) {
                let row = $('<tr></tr>');

                row.append(`
                    <td class="row board-num">${board.boardNo}</td>
                    <td class="row subject"><a href="/board/${board.boardNo}">${board.subject}</a></td>
                    <td class="row writer">${board.createUserName}(@${board.createUserId})</td>
                    <td class="row write-date">${formatDate(board.createdAt)}</td>
                    <td class="row update-date">${board.updatedAt ? formatDate(board.updatedAt) : ''}</td>
                    <td class="row view-count">${board.viewCnt}</td>
                `);
                tableBody.append(row);
            });
        },
        error: function (xhr) {
            alert("error", xhr);
        }
    });

    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0') + ' ' +
            date.getHours().toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0') + ':' + date.getSeconds().toString().padStart(2, '0');
    }
});
