$(document).ready(function () {
    const token = sessionStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;
    const userName = decodedToken.userName;

    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");
    const $writeEl = $("#writer");
    const $writeForm = $(".write-form");

    const initializeWriterInfo = () => {
        $writeEl.text(`${userName} (@${loggedInUserId})`);
    };

    const getNewBoardData = () => {
        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();

        if (!subject || !contentText) {
            alert("제목과 내용을 모두 입력해주세요.");
            return null;
        }

        return {
            subject: subject,
            contentText: contentText,
        };
    };

    // 서버 요청 처리
    const writeBoard = (newBoard) => {
        $.ajax({
            url: "/api/board/write",
            type: "POST",
            contentType: "application/json",
            headers: {
                Authorization: `Bearer ${token}`,
            },
            data: JSON.stringify(newBoard),
            success: function (res) {
                openAlertModal(res);
            },
            error: function (xhr) {
                openAlertModal(`게시글 작성 실패: ${xhr.status} ${xhr.statusText}`);
            },
        });
    };

    const openAlertModal = (msg) => {
        $modalMsg.text(msg);
        $alertModal.show();
    };

    const closeAlertModal = () => {
        $alertModal.hide();
    };

    // 글 작성 폼 제출
    $writeForm.on("submit", function (event) {
        event.preventDefault();

        const newBoard = getNewBoardData();
        if (newBoard) {
            writeBoard(newBoard);
        }
    });

    $closeBtn.on("click", function () {
        closeAlertModal();
        window.location.href = "/";
    });

    initializeWriterInfo();
});
