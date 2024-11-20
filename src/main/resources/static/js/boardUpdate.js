$(document).ready(function () {
    const token = localStorage.getItem("token");
    const boardNo = window.location.pathname.match(/\/board\/(\d+)/)?.[1];
    const updateForm = $(".update-form");
    console.log(boardNo);

    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;

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
            url: `/board/${boardNo}`,
            type: "PATCH",
            contentType: "application/json",
            data: JSON.stringify(updatedBoard),
            success: function (res) {
                alert("게시글이 수정되었습니다.");

                window.location.href = `/board/${boardNo}`;
            },
            error: function (xhr, status, error) {
                alert("수정 중 오류가 발생했습니다.");

                console.error("Error:", xhr.responseText);
            }
        });
    });
});
