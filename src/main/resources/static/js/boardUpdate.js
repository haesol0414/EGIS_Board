$(document).ready(function () {
    const token = sessionStorage.getItem("token");
    const boardNo = window.location.pathname.match(/\/board\/(\d+)/)?.[1];

    const $updateForm = $(".update-form");
    const $cancelBtn = $("#cancel-btn");
    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");

    const $fileInput = $('#file-input');
    const $fileName = $('#file-name');
    const $clearFileBtn = $('.clear-file');


    // 파일 선택
    const updateFileName = () => {
        const files = $fileInput[0].files;
        if (files.length > 0) {
            $fileName.text(files[0].name);
            $clearFileBtn.show();
        } else {
            $fileName.text("선택된 파일 없음");
            $clearFileBtn.hide();
        }
    };

    // "x" 버튼 클릭 시 파일 업로드 취소
    $clearFileBtn.on('click', function () {
        resetFileInput();
    });

    // 파일 입력 리셋
    const resetFileInput = () => {
        $fileInput.val('');
        $fileName.text('선택된 파일 없음');
        $clearFileBtn.hide()
    };

    // 게시글 수정 데이터 생성
    const getUpdatedBoardData = () => {
        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();
        const files = $fileInput[0].files;
        const formData = new FormData();

        if (!subject || !contentText) {
            alert("제목과 내용을 모두 입력하세요.");
            return null;
        }

        // JSON 데이터
        const updatedBoard = {
            subject: subject,
            contentText: contentText,
            updatedAt: new Date().toISOString()
        };

        // JSON 데이터를 "boardCreateDTO" key로 추가
        formData.append("boardUpdateDTO", new Blob([JSON.stringify(updatedBoard)], {
            type: "application/json",
        }));

        if (files.length > 0) {
            formData.append("file", files[0]);
        }

        return formData;
    };

    // 서버 요청 처리
    const updateBoard = (formData) => {
        $.ajax({
            url: `/api/board/${boardNo}`,
            type: "PATCH",
            processData: false,
            contentType: false,
            headers: {
                Authorization: `Bearer ${token}`,
            },
            data: formData,
            success: function (res) {
                openAlertModal("게시글이 수정되었습니다.");
            },
            error: function (xhr, status, error) {
                alert("수정 중 오류가 발생했습니다.");
                console.error("Error:", xhr.responseText);
            },
        });
    };

    // 알림 모달 열기
    const openAlertModal = (msg) => {
        $modalMsg.text(msg);
        $alertModal.show();
    };

    // 알림 모달 닫기
    const closeAlertModal = () => {
        $alertModal.hide();
    };

    // 수정 폼 제출
    $updateForm.on("submit", function (event) {
        event.preventDefault();

        if (!boardNo) {
            alert("게시글 번호를 찾을 수 없습니다.");
            return;
        }

        const updatedBoard = getUpdatedBoardData();
        if (updatedBoard) {
            updateBoard(updatedBoard);
        }
    });

    $cancelBtn.on("click", function () {
        window.location.href = `/board/${boardNo}`;
    });

    $closeBtn.on("click", function () {
        closeAlertModal();
        window.location.href = `/board/${boardNo}`;
    });

    $fileInput.on('change', updateFileName);
});
