$(document).ready(function () {
    let currentPage = 1;

    const $tableBody = $('.board-table tbody');
    const $pagination = $('.pagination');
    const $dropdown = $('.dropdown');
    const $dropbtn = $('.dropbtn');
    const $dropdownContent = $('.dropdown-content');
    const $dropbtnContent = $('.dropbtn_content');
    const $searchInput = $('.search-input');
    const $searchBtn = $('#search-btn');

    // 초기 실행
    loadBoardList(currentPage);

    // 게시글 목록 로드
    function loadBoardList(pageNumber) {
        $.ajax({
            url: "/api/board/list",
            type: "GET",
            data: { page: pageNumber },
            contentType: "application/json",
            success: function (res) {
                renderBoardList(res.data.boardList);
                renderPagination(res.data.totalPages);
            },
            error: function () {
                alert("게시글 목록을 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

    // 게시글 목록 렌더링
    function renderBoardList(boardList) {
        $tableBody.empty();

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
    function renderPagination(totalPages) {
        $pagination.empty();

        // 이전 버튼
        const prevBtn = $('<button class="page-btn prev-btn"><i class="fas fa-chevron-left"></i></button>')
            .prop('disabled', currentPage === 1)
            .data('page', currentPage - 1);
        $pagination.append(prevBtn);

        // 페이지 번호 버튼
        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = $('<button class="page-btn"></button>')
                .text(i)
                .data('page', i)
                .toggleClass('current', i === currentPage);
            $pagination.append(pageBtn);
        }

        // 다음 버튼
        const nextBtn = $('<button class="page-btn next-btn"><i class="fas fa-chevron-right"></i></button>')
            .prop('disabled', currentPage === totalPages)
            .data('page', currentPage + 1);
        $pagination.append(nextBtn);
    }

    // 날짜 형식 변환
    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return date.getFullYear() + '-' +
            (date.getMonth() + 1).toString().padStart(2, '0') + '-' +
            date.getDate().toString().padStart(2, '0') + ' ' +
            date.getHours().toString().padStart(2, '0') + ':' +
            date.getMinutes().toString().padStart(2, '0') + ':' +
            date.getSeconds().toString().padStart(2, '0');
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

    // 검색 이벤트 처리
    function initializeSearch() {
        $searchBtn.on('click', function () {
            const filter = $dropbtnContent.text();
            const keyword = $searchInput.val();
            console.log('검색 기준:', filter, '검색어:', keyword);

            $.ajax({
                url: '/api/board/search',
                method: 'GET',
                data: { filter, keyword },
                success: function (res) {
                    renderBoardList(res.data.boardList);
                    renderPagination(res.data.totalPages);
                },
                error: function () {
                    alert('검색 중 오류가 발생했습니다.');
                }
            });
        });
    }

    // 페이지네이션 버튼 클릭 이벤트
    $(document).on('click', '.page-btn', function () {
        const pageNumber = $(this).data('page');
        if (!pageNumber || $(this).prop('disabled')) return;

        currentPage = pageNumber;
        loadBoardList(pageNumber);
    });

    // 초기화 함수 실행
    initializeDropdown();
    initializeSearch();
});
