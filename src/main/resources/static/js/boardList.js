$(document).ready(function () {
    const filterMap = {
        "제목": "subject",
        "내용": "contentText",
        "제목 + 내용": "all",
        "작성자명": "writer"
    };

    const defaultFilterText = "제목";
    const defaultFilterValue = filterMap[defaultFilterText];

    const $searchInput = $('.search-input');
    const $searchBtn = $('#search-btn');
    const $dropbtnContent = $('.dropbtn_content');
    const $dropdownContent = $('.dropdown-content');
    const $form = $('form');

    // 기본 드롭다운 값 설정
    $dropbtnContent.text(defaultFilterText);
    $form.find('input[name="filter"]').val(defaultFilterValue);

    // 드롭다운 토글
    $('.dropbtn').on('click', function (e) {
        e.preventDefault();
        $dropdownContent.toggleClass('show'); // 드롭다운 메뉴 표시/숨기기
    });

    // 드롭다운 옵션 선택
    $dropdownContent.find('a').on('click', function (e) {
        e.preventDefault();
        const selectedText = $(this).text(); // 선택한 옵션의 텍스트
        const selectedValue = filterMap[selectedText]; // filterMap에서 매핑된 값 가져오기

        if (!selectedValue) {
            alert("유효하지 않은 필터 값입니다.");
            return;
        }

        $dropbtnContent.text(selectedText); // 드롭다운 버튼에 선택된 텍스트 표시
        $form.find('input[name="filter"]').val(selectedValue); // 숨겨진 필터 필드에 값 설정
        $dropdownContent.removeClass('show'); // 드롭다운 닫기
    });

    // 드롭다운 외부 클릭 시 닫기
    $(window).on('click', function (e) {
        if (!$(e.target).closest('.dropdown').length) {
            $dropdownContent.removeClass('show');
        }
    });

    // 검색 처리
    $searchBtn.on('click', function () {
        const keyword = $searchInput.val();
        if (!keyword) {
            alert("검색어를 입력해주세요.");
            return;
        }
        $form.submit();
    });

    $searchInput.on('keydown', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            $form.submit();
        }
    });
});
