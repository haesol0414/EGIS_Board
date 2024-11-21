$(document).ready(function () {
    const token = sessionStorage.getItem("token");
    const boardNo = window.location.pathname.match(/\/board\/(\d+)/)?.[1];
    const updateForm = $(".update-form");
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;
    const cancelBtn = $('#cancel-btn');
    const closeBtn = $("#close-btn");
    const alertModal = $("#alert-modal");
    const modalMsg = $("#modal-msg");

    updateForm.on("submit", function (event) {
        event.preventDefault();

        if (!boardNo) {
            alert("게시글 번호를 찾을 수 없습니다.");
            return;
        }

        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();

        if (!subject || !contentText) {
            alert("제목과 내용을 모두 입력하세요.");
            return;
        }

        const updatedBoard = {
            boardNo: boardNo,
            updateUserId: loggedInUserId,
            subject: subject,
            contentText: contentText,
            updatedAt: new Date().toISOString()
        }

        console.log(updatedBoard);

        $.ajax({
            url: `/api/board/${boardNo}`,
            type: "PATCH",
            contentType: "application/json",
            data: JSON.stringify(updatedBoard),
            success: function (res) {
                openAlertModal("게시글이 수정되었습니다.");
            },
            error: function (xhr, status, error) {
                alert("수정 중 오류가 발생했습니다.");

                console.error("Error:", xhr.responseText);
            }
        });
    });

    cancelBtn.on('click', function () {
        window.location.href = `/board/${boardNo}`;
    })

    const openAlertModal = (msg) => {
        alertModal.show();
        modalMsg.text(msg);
    }
    const closeAlertModal = () => {
        alertModal.hide();
    }

    closeBtn.on("click", function () {
        closeAlertModal();
        window.location.href = `/board/${boardNo}`;
    });
});
