$(document).ready(function () {
    const token = localStorage.getItem("token");
    // URL에서 boardNo 추출
    const pathSegments = window.location.pathname.split('/');
    const boardNo = pathSegments[pathSegments.length - 1];
    const modifyBtn = $("#modify-btn");
    const deleteBtn = $("#delete-btn");
    const replyBtn = $("#reply-btn");
    const deleteModal = $("#deleteModal");
    const confirmDeleteBtn = $("#delete-confirm-btn");
    const denyDeleteBtn = $("#delete-deny-btn");
    const closeBtn = $("#close-btn");
    const writerBtnDiv = $(".writer-btns");

    if (token) {
        const decodedToken = jwt_decode(token);

        const loggedInUserId = decodedToken.sub;
        const writerId = $("#writer").data("writerid");

        if (loggedInUserId === writerId) {
            writerBtnDiv.show();
        } else {
            writerBtnDiv.hide();
        }
    } else {
        writerBtnDiv.hide();
        replyBtn.hide();
    }

    // 수정 폼 불러오기 버튼
    modifyBtn.on("click", function (event) {
        event.preventDefault();
        event.stopPropagation();

        if (boardNo) {
            $.ajax({
                url: `/board/${boardNo}/edit`,
                type: "GET",
                success: function (res) {
                },
                error: function (xhr, status, error) {
                    alert("수정 페이지를 불러오는 데 실패했습니다.");
                    console.error("Error: ", error);
                },
            });
        } else {
            alert("게시글 번호를 찾을 수 없습니다.");
        }
    });

    // 삭제 버튼
    deleteBtn.on("click", function (event) {
        event.preventDefault();
        event.stopPropagation();

        if (boardNo) {
            openModal();

            confirmDeleteBtn.off("click").on("click", function () {
                closeModal();

                $.ajax({
                    url: `/board/${boardNo}`,
                    type: 'DELETE',
                    success: function (res) {
                        alert(res);
                        window.location.href = "/";
                    },
                    error: function (xhr, status, error) {
                        alert("삭제 중 에러가 발생했습니다: " + xhr.responseText);
                    }
                });
            });
        } else {
            alert("게시글 번호를 찾을 수 없습니다.");
        }
    });

    // '아니오', 'x' 버튼 클릭 시 모달 닫기
    denyDeleteBtn.add(closeBtn).on("click", function () {
        closeModal();
    });

    // 삭제 모달 열기
    const openModal = () => {
        deleteModal.show();
    };

    // 삭제 모달 닫기
    const closeModal = () => {
        deleteModal.hide();
    };

    // 답글달기 버튼
    // replyBtn.on("click", function (event) {
    //     event.preventDefault();
    // });
});
