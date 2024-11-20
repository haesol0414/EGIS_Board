$(document).ready(function () {
    let currentPage = 1; // 현재 페이지 번호, 처음에는 1페이지
    const tableBody = $('.board-table tbody');
    const pagination = $('.pagination');
    loadBoardList(currentPage);

    function loadBoardList(pageNumber) {
        $.ajax({
            url: "/board/list",
            type: "GET",
            data: {
                page: pageNumber
            },
            contentType: "application/json",
            success: function (res) {
                console.log('게시글 목록 데이터: ', res);
                const boardList = res.data.boardList;

                tableBody.empty();
                boardList.forEach(function(board) {
                    let row = $('<tr></tr>');

                    row.append(`
                        <td class="row board-num">${board.boardNo}</td>
                        <td class="row subject"><a href="/board/${board.boardNo}">${board.subject}</a></td>
                        <td class="row writer">${board.createUserName}(@${board.createUserId})</td>
                        <td class="row write-date">${formatDate(board.createdAt)}</td>
                        <td class="row update-date">${board.updatedAt ? formatDate(board.updatedAt) : '-'}</td>
                        <td class="row view-count">${board.viewCnt}</td>
                    `);
                    tableBody.append(row);
                });

                // 페이지네이션 처리
                const totalPages = res.data.totalPages;
                pagination.empty();

                for (let i = 1; i <= totalPages; i++) {
                    const pageBtn = $('<button class="page-btn"></button>')
                        .text(i)
                        .data('page', i);

                    if (i === currentPage) {
                        pageBtn.addClass('current');
                    }

                    pagination.append(pageBtn);
                }
            },
            error: function (xhr) {
                alert("error", xhr);
            }
        });
    }

    // 페이지 버튼 클릭 이벤트 처리
    $(document).on('click', '.page-btn', function () {
        const pageNumber = $(this).data('page'); // 페이지 번호
        currentPage = pageNumber;  // 현재 페이지 번호 업데이트
        loadBoardList(pageNumber);

        // 클릭된 버튼에만 'current' 클래스 추가하고, 다른 버튼에서 클래스 제거
        $('.page-btn').removeClass('current');
        $(this).addClass('current');
    });

    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0') + ' ' +
            date.getHours().toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0') + ':' + date.getSeconds().toString().padStart(2, '0');
    }
});
