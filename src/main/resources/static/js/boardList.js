$(document).ready(function () {
    let currentPage = 1;
    const filterMap = {
        "제목": "subject",
        "내용": "contentText",
        "제목 + 내용": "all",
        "작성자명": "writer"
    };

    const $tableBody = $('.board-table tbody');
    const $pagination = $('.pagination');
    const $searchBtn = $('#search-btn');
    const $searchInput = $('.search-input');
    const $dropbtnContent = $('.dropbtn_content');
    const $dropdown = $('.dropdown');
    const $dropdownContent = $('.dropdown-content');
    const $dropbtn = $('.dropbtn');

    // 게시글 목록 로드
    function loadBoardList(pageNumber, filter, keyword) {
        $.ajax({
            url: "/api/board/list",
            type: "GET",
            data: { page: pageNumber, filter, keyword },
            success: function (res) {
                console.log(res);
                renderBoardList(res.data.boardList);
                renderPagination(res.data.totalPages, filter, keyword);
            },
            error: function () {
                alert("게시글 목록을 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

    // 게시글 목록 렌더링
    function renderBoardList(boardList) {
        $tableBody.empty();

        if (boardList.length === 0) {
            // 게시글이 없을 경우
            const noDataRow = $(`
            <tr>
                <td class="no-data" colspan="6">조회된 게시글이 없습니다.</td>
            </tr>
        `);
            $tableBody.append(noDataRow);
            return;
        }

        boardList.forEach(function (board) {
            const row = $(`
            <tr>
                <td class="row board-num">${board.boardNo}</td>
                <td class="row subject"><a href="/board/${board.boardNo}">${board.subject}</a></td>
                <td class="row writer">${board.createUserName}(@${board.createUserId})</td>
                <td class="row write-date">${formatDate(board.createdAt)}</td>
                <td class="row update-date">${board.updatedAt ? formatDate(board.updatedAt) : '-'}</td>
                <td class="row view-count">${board.viewCnt}</td>
            </tr>
        `);
            $tableBody.append(row);
        });
    }

    // 페이지네이션 렌더링
    function renderPagination(totalPages, filter, keyword) {
        $pagination.empty();

        // 이전 버튼
        const prevBtn = $('<button class="page-btn prev-btn"><i class="fas fa-chevron-left"></i></button>')
            .prop('disabled', currentPage === 1)
            .on('click', function () {
                currentPage -= 1;
                loadBoardList(currentPage, filter, keyword);
            });
        $pagination.append(prevBtn);

        // 페이지 번호 버튼
        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = $('<button class="page-btn"></button>')
                .text(i)
                .toggleClass('current', i === currentPage)
                .on('click', function () {
                    currentPage = i;
                    loadBoardList(currentPage, filter, keyword);
                });
            $pagination.append(pageBtn);
        }

        // 다음 버튼
        const nextBtn = $('<button class="page-btn next-btn"><i class="fas fa-chevron-right"></i></button>')
            .prop('disabled', currentPage === totalPages)
            .on('click', function () {
                currentPage += 1;
                loadBoardList(currentPage, filter, keyword);
            });
        $pagination.append(nextBtn);
    }

    // 드롭다운 초기화
    function initializeDropdown() {
        // 드롭다운 버튼 클릭
        $dropbtn.on('click', function () {
            $dropdownContent.toggleClass('show');
        });

        // 드롭다운 옵션 선택
        $dropdownContent.find('a').on('click', function () {
            const value = $(this).data('value');
            $dropbtnContent.text(value);
            $dropdownContent.removeClass('show');
        });

        // 드롭다운 외부 클릭 시 닫기
        $(window).on('click', function (e) {
            if (!$(e.target).closest($dropdown).length) {
                $dropdownContent.removeClass('show');
            }
        });
    }

    // 검색 처리
    $searchBtn.on('click', function () {
        const filterText = $dropbtnContent.text();
        const filter = filterMap[filterText];
        const keyword = $searchInput.val();

        if (!keyword) {
            alert("검색어를 입력해주세요.");
            return;
        }

        currentPage = 1;
        loadBoardList(currentPage, filter, keyword);
    });

    // 날짜 포맷팅
    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ` +
            `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`;
    }

    // 초기화 함수 실행
    initializeDropdown();
    loadBoardList(currentPage);
});
