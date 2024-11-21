$(document).ready(function () {
    let currentPage = 1; // 현재 페이지 번호
    const tableBody = $('.board-table tbody');
    const pagination = $('.pagination');
    loadBoardList(currentPage);

    function loadBoardList(pageNumber) {
        $.ajax({
            url: "/api/board/list",
            type: "GET",
            data: {
                page: pageNumber
            },
            contentType: "application/json",
            success: function (res) {
                console.log('게시글 목록 데이터: ', res);
                const boardList = res.data.boardList;

                // 게시판 데이터 렌더링
                tableBody.empty();
                boardList.forEach(function (board) {
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

                renderPagination(res.data.totalPages);
            },
            error: function (xhr) {
                alert("게시글 목록을 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

    function renderPagination(totalPages) {
        pagination.empty();

        // 이전 버튼 추가
        const prevBtn = $('<button class="page-btn prev-btn"><i class="fas fa-chevron-left"></i></button>')
            .prop('disabled', currentPage === 1) // 첫 페이지에서는 비활성화
            .data('page', currentPage - 1);

        pagination.append(prevBtn);

        // 페이지 번호 버튼 추가
        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = $('<button class="page-btn"></button>')
                .text(i)
                .data('page', i);

            if (i === currentPage) {
                pageBtn.addClass('current');
            }

            pagination.append(pageBtn);
        }

        // 다음 버튼 추가
        const nextBtn = $('<button class="page-btn next-btn"><i class="fas fa-chevron-right"></i></button>')
            .prop('disabled', currentPage === totalPages) // 마지막 페이지에서는 비활성화
            .data('page', currentPage + 1);

        pagination.append(nextBtn);
    }

    // 페이지 버튼 클릭 이벤트 처리
    $(document).on('click', '.page-btn', function () {
        const pageNumber = $(this).data('page'); // 페이지 번호
        if (!pageNumber || $(this).prop('disabled')) return; // 비활성화된 버튼은 동작하지 않음

        currentPage = pageNumber; // 현재 페이지 번호 업데이트
        loadBoardList(pageNumber);
    });

    // 날짜 형식 변환 함수
    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0') + ' ' +
            date.getHours().toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0') + ':' + date.getSeconds().toString().padStart(2, '0');
    }
});

