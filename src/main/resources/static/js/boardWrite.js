import * as Modal from "./utils/modal.js";

$(document).ready(function () {
    Modal.initializeModalElements();

    const token = sessionStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const loggedInUserId = decodedToken.sub;
    const userName = decodedToken.userName;

    const $fileList = $('.file-list'); // 파일 리스트 컨테이너
    const $writerEl = $("#writer");
    const $writeSubmitBtn = $("#write-submit");
    const $replySubmitBtn = $("#reply-submit");


    let filesArr = []; // 파일 배열
    let fileNo = 0; // 파일 고유 ID
    const maxFiles = 5; // 최대 파일 개수

    // 작성자 정보 초기화
    const initializeWriterInfo = () => {
        $writerEl.text(`${userName} (@${loggedInUserId})`);
    };

    // 첨부파일 추가
    const addFile = ($inputElement) => {
        const attFileCnt = $fileList.find('.filebox').length; // 현재 첨부파일 개수
        const remainFileCnt = maxFiles - attFileCnt; // 남은 파일 추가 가능 개수
        const curFileCnt = $inputElement[0].files.length; // 현재 선택된 파일 개수

        if (curFileCnt > remainFileCnt) {
            alert(`첨부파일은 최대 ${maxFiles}개까지 가능합니다.`);
            return;
        }

        for (let i = 0; i < Math.min(curFileCnt, remainFileCnt); i++) {
            const file = $inputElement[0].files[i];
            if (validateFile(file)) {
                filesArr.push(file);

                const $fileBox = $(`
                    <div id="file${fileNo}" class="filebox">
                        <p class="name">${file.name}</p>
                        <button type="button" class="clear-file" data-file-id="${fileNo}">×</button>
                    </div>
                `);

                $fileList.append($fileBox);
                fileNo++;
            }
        }
        $inputElement.val(''); // 파일 입력 초기화
    };

    // 첨부파일 검증
    const validateFile = (file) => {
        if (file.name.length > 100) {
            alert("파일명이 100자를 초과했습니다.");
            return false;
        }
        if (file.size > 100 * 1024 * 1024) {
            alert("파일 크기는 최대 100MB까지 가능합니다.");
            return false;
        }

        return true;
    };

    // 첨부파일 삭제
    const deleteFile = (fileId) => {
        filesArr = filesArr.filter((_, index) => index !== fileId);
        $(`#file${fileId}`).remove();
    };

    // 폼 데이터 생성
    const getFormData = (dtoKey) => {
        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();

        if (!subject || !contentText) {
            alert("제목과 내용을 입력해주세요.");
            return null;
        }

        const formData = new FormData();

        formData.append(dtoKey, new Blob([JSON.stringify({subject, contentText})], {type: "application/json"}));

        filesArr.forEach((file) => {
            if (file) {
                formData.append("files", file);
            }
        });

        return formData;
    };

    // 작성 API 요청
    const handleFormSubmit = (url, dtoKey) => {
        const formData = getFormData(dtoKey);

        if (formData) {
            $.ajax({
                url: url,
                type: "POST",
                processData: false,
                contentType: false,
                headers: {Authorization: `Bearer ${token}`},
                data: formData,
                success: function (res) {
                    Modal.openAlertModal(res.message, `/board/${res.boardNo}`);
                },
                error: function (xhr) {
                    alert(`요청 실패: ${xhr.status} - ${xhr.statusText}`);
                },
            });
        }
    };

    // 이벤트 바인딩
    $fileList.on("change", "input[type='file']", function () {
        addFile($(this));
    });

    $fileList.on("click", ".clear-file", function () {
        const fileId = $(this).data('file-id');
        deleteFile(fileId);
    });

    $writeSubmitBtn.on("click", function (event) {
        event.preventDefault();
        handleFormSubmit("/api/board/write", "boardCreateDTO");
    });

    $replySubmitBtn.on("click", function (event) {
        event.preventDefault();
        // const parentBoardNo = $("#parent-data").data("board-no");
        // 현재 URL 경로에서 boardNo 추출
        const url = window.location.pathname; // "/board/reply/12345"
        const parentBoardNo = url.split("/").pop(); // URL의 마지막 부분 추출

        console.log(parentBoardNo);
        handleFormSubmit(`/api/board/reply/${parentBoardNo}`, "boardReplyDTO");
    });

    initializeWriterInfo();
});
