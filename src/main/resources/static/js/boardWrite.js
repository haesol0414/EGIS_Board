$(document).ready(function() {
    // 사용자의 토큰을 통해 정보 추출
    const token = localStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;
    const userName = decodedToken.userName;
    const closeBtn = $("#close-btn");
    const alertModal = $("#alert-modal");
    const modalMsg = $("#modal-msg");

    const writeEl = $('#writer');
    writeEl.text(`${userName} (@${loggedInUserId})`);
    const writeForm = $(".write-form");

    writeForm.on("submit", function(event) {
        event.preventDefault();

        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();

        if (!subject || !contentText) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        const newBoard = {
            createUserId: loggedInUserId,
            subject: subject,
            contentText: contentText
        };
        console.log(newBoard);

        $.ajax({
            url: "/api/board/write",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(newBoard),
            success: function (res) {
                openAlertModal(res);
            },
            error: function (xhr) {
                openAlertModal('게시글 작성 실패: ' + xhr.status + " " + xhr.statusText);
            }
        });
    });

    const openAlertModal = (msg) => {
        alertModal.show();
        modalMsg.text(msg);
    }
    const closeAlertModal = () => {
        alertModal.hide();
    }

    closeBtn.on("click", function () {
        closeAlertModal();

        window.location.href = "/";
    });
});
