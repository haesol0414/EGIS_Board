$(document).ready(function () {
    const token = sessionStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;
    const userName = decodedToken.userName;

    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");
    const $writerEl = $("#writer");
    const $writeSubmitBtn = $("#write-submit");
    const $replySubmitBtn = $("#reply-submit");


    const initializeWriterInfo = () => {
        $writerEl.text(`${userName} (@${loggedInUserId})`);
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

    const openAlertModal = (msg) => {
        $modalMsg.text(msg);
        $alertModal.show();
    };

    const closeAlertModal = () => {
        $alertModal.hide();
    };

    // 게시글 작성 API
    $writeSubmitBtn.on("click", function (event) {
        event.preventDefault();

        const newBoard = getNewBoardData();

        if (newBoard) {
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
        }
    });

    // 답글 작성 API
    $replySubmitBtn.on("click", function (event) {
        event.preventDefault();
        const parentBoardNo = $("#parent-data").data("board-no");
        const newBoard = getNewBoardData();

        if (newBoard) {
            $.ajax({
                url: `/api/board/reply/${parentBoardNo}`,
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
        }
    });

    $closeBtn.on("click", function () {
        closeAlertModal();
        window.location.href = "/";
    });

    initializeWriterInfo();
});
