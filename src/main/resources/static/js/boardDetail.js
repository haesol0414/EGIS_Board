$(document).ready(function () {
    const token = sessionStorage.getItem("token");
    const pathSegments = window.location.pathname.split('/');
    const boardNo = pathSegments[pathSegments.length - 1];

    const $modifyBtn = $("#modify-btn");
    const $deleteBtn = $("#delete-btn");
    const $replyBtn = $("#reply-btn");
    const $deleteModal = $("#deleteModal");
    const $confirmDeleteBtn = $("#confirm-btn");
    const $denyDeleteBtn = $("#deny-btn");
    const $writerBtnDiv = $(".writer-btns");
    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");

    // 로그인유저 == 작성자인지 확인
    const initializeUI = () => {
        if (token) {
            const decodedToken = jwt_decode(token);
            const loggedInUserId = decodedToken.sub;
            const writerId = $("#writer").data("writerid");

            if (loggedInUserId === writerId) {
                $writerBtnDiv.show();
            } else {
                $writerBtnDiv.hide();
            }
        } else {
            $writerBtnDiv.hide();
            $replyBtn.hide();
        }
    };

    // 수정 폼 로드
    const loadEditForm = () => {
        if (!boardNo) {
            alert("게시글 번호를 찾을 수 없습니다.");
            return;
        }

        $.ajax({
            url: `/board/${boardNo}/edit`,
            type: "GET",
            success: function (res) {
                // 추가 작업 필요 시 작성
            },
            error: function (xhr, status, error) {
                alert("수정 페이지를 불러오는 데 실패했습니다.");
                console.error("Error: ", error);
            },
        });
    };

    // 게시글 삭제
    const deleteBoard = () => {
        if (!boardNo) {
            alert("게시글 번호를 찾을 수 없습니다.");
            return;
        }

        $.ajax({
            url: `/api/board/${boardNo}`,
            type: 'DELETE',
            headers: {
                "Authorization": `Bearer ${token}`,
            },
            success: function (res) {
                openAlertModal("게시글이 삭제되었습니다.");
            },
            error: function (xhr, status, error) {
                alert("삭제 중 에러가 발생했습니다: " + xhr.responseText);
            },
        });
    };

    // 삭제 모달 열기/닫기
    const openDeleteModal = () => $deleteModal.show();
    const closeDeleteModal = () => $deleteModal.hide();

    // 알림 모달 열기/닫기
    const openAlertModal = (msg) => {
        $modalMsg.text(msg);
        $alertModal.show();
    };
    const closeAlertModal = () => $alertModal.hide();

    // 수정하기 버튼
    $modifyBtn.on("click", function (event) {
        event.preventDefault();
        loadEditForm();
    });

    // 삭제하기 버튼
    $deleteBtn.on("click", function (event) {
        event.preventDefault();
        openDeleteModal();

        $confirmDeleteBtn.off("click").on("click", function () {
            closeDeleteModal();
            deleteBoard();
        });
    });

    $denyDeleteBtn.on("click", closeDeleteModal);

    // 답글 버튼
    $replyBtn.on("click", function (event) {
        event.preventDefault();
    });

    $closeBtn.on("click", function () {
        closeAlertModal();
        window.location.href = "/";
    });

    // 초기화 실행
    initializeUI();
});
