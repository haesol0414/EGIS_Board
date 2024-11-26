$(document).ready(function () {
    const token = sessionStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;
    const userName = decodedToken.userName;
    let boardNo;

    const $fileInput = $('#file-input');
    const $fileName = $('#file-name');
    const $clearFileBtn = $('#clear-file');
    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");
    const $writerEl = $("#writer");
    const $writeSubmitBtn = $("#write-submit");
    const $replySubmitBtn = $("#reply-submit");

    const initializeWriterInfo = () => {
        $writerEl.text(`${userName} (@${loggedInUserId})`);
    };

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

    const getNewBoardData = (dtoKey) => {
        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();
        const files = $fileInput[0].files;
        const formData = new FormData();

        if (!subject || !contentText) {
            alert("제목과 내용을 모두 입력해주세요.");
            return null;
        }

        // JSON 데이터
        const newBoard = {
            subject: subject,
            contentText: contentText,
        };

        // JSON 데이터를 "boardCreateDTO" key로 추가
        formData.append(dtoKey, new Blob([JSON.stringify(newBoard)], {
            type: "application/json",
        }));

        // 파일 데이터를 "file" key로 추가
        if (files.length > 0) {
            formData.append("file", files[0]);
        }

        return formData;
    };

    // 글 작성 API
    const handleFormSubmit = (url, dtoKey) => {
        const formData = getNewBoardData(dtoKey);

        if (formData) {
            $.ajax({
                url: url,
                type: "POST",
                processData: false,
                contentType: false,
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                data: formData,
                success: function (res) {
                    openAlertModal(res.message, res.boardNo);
                },
                error: function (xhr) {
                    openAlertModal(`요청 실패: ${xhr.status} ${xhr.statusText}`);
                },
            });
        }
    };

    const openAlertModal = (msg, boardNo) => {
        $modalMsg.text(msg);
        $alertModal.show();

        $closeBtn.on("click", function () {
            closeAlertModal();

            window.location.href = `/board/${boardNo}`;
        });
    };

    const closeAlertModal = () => {
        $alertModal.hide();
    };

    // 이벤트 핸들러 등록
    $fileInput.on('change', updateFileName);

    $writeSubmitBtn.on("click", function (event) {
        event.preventDefault();
        handleFormSubmit("/api/board/write", "boardCreateDTO");
    });

    $replySubmitBtn.on("click", function (event) {
        event.preventDefault();
        const parentBoardNo = $("#parent-data").data("board-no");
        handleFormSubmit(`/api/board/reply/${parentBoardNo}`, "boardReplyDTO");
    });

    $fileInput.on('change', updateFileName);

    initializeWriterInfo();
});
