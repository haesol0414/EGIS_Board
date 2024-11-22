$(document).ready(function () {
    const token = sessionStorage.getItem("token");
    const boardNo = window.location.pathname.match(/\/board\/(\d+)/)?.[1];
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;

    const $updateForm = $(".update-form");
    const $cancelBtn = $("#cancel-btn");
    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");

    // 게시글 수정 데이터 생성
    const getUpdatedBoardData = () => {
        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();

        if (!subject || !contentText) {
            alert("제목과 내용을 모두 입력하세요.");
            return null;
        }

        return {
            boardNo: boardNo,
            subject: subject,
            contentText: contentText,
            updatedAt: new Date().toISOString(),
        };
    };

    // 서버 요청 처리
    const updateBoard = (updatedBoard) => {
        $.ajax({
            url: `/api/board/${boardNo}`,
            type: "PATCH",
            contentType: "application/json",
            headers: {
                Authorization: `Bearer ${token}`,
            },
            data: JSON.stringify(updatedBoard),
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
});
