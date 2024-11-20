$(document).ready(function () {
    const token = localStorage.getItem("token");
    const pathSegments = window.location.pathname.split('/');
    const boardNo = pathSegments[pathSegments.length - 1];
    const modifyBtn = $("#modify-btn");
    const deleteBtn = $("#delete-btn");
    const replyBtn = $("#reply-btn");
    const deleteModal = $("#deleteModal");
    const confirmDeleteBtn = $("#confirm-btn");
    const denyDeleteBtn = $("#deny-btn");
    const writerBtnDiv = $(".writer-btns");
    const closeBtn = $("#close-btn");
    const alertModal = $("#alert-modal");
    const modalMsg = $("#modal-msg");

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
            openDeleteModal();

            confirmDeleteBtn.off("click").on("click", function () {
                closeDeleteModal();

                $.ajax({
                    url: `/board/${boardNo}`,
                    type: 'DELETE',
                    success: function (res) {
                        openAlertModal("게시글이 삭제되었습니다.");
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

    // '아니오' 버튼 클릭 시 모달 닫기
    denyDeleteBtn.on("click", function () {
        closeDeleteModal();
    });

    // 답글달기 버튼
    replyBtn.on("click", function (event) {
        event.preventDefault();
    });


    // 삭제 모달 열기, 닫기
    const openDeleteModal = () => {
        deleteModal.show();
    };
    const closeDeleteModal = () => {
        deleteModal.hide();
    };

    // 알림창 열기, 닫기
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
