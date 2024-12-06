import * as Modal from "./utils/modal.js";
import {getCurrentUserFromStorage} from './utils/authUtils.js';

$(document).ready(function () {
    Modal.initializeModalElements();

    const user = getCurrentUserFromStorage();
    const maxFiles = 5; // 최대 파일 개수
    let filesArr = []; // 파일 배열
    let fileNo = 0; // 파일 고유 ID

    const $fileList = $('.file-list');
    const $writerEl = $("#writer");
    const $writeSubmitBtn = $("#write-submit");
    const $replySubmitBtn = $("#reply-submit");
    const $noticeCheckBox = $("#is-notice");

    // 공지사항 체크박스 클릭 시 날짜 입력 필드 토글
    $noticeCheckBox.on('change', function () {
        if ($(this).is(':checked')) {
            $('#date-fields').show();
        } else {
            $('#date-fields').hide();
        }
    });

    // 작성자 정보 초기화
    const initializeWriterInfo = () => {
        $writerEl.text(`${user.username} (@${user.userId})`);
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

                const fileURL = URL.createObjectURL(file); // 파일 URL 생성

                const $fileBox = $(`
                    <div id="file${fileNo}" class="filebox">
                        ${file.type.startsWith('image/') ? `<img src="${fileURL}" alt="${file.name}" class="thumbnail" />` : ''}
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

        // 게시글 데이터 생성
        const boardData = {
            subject: subject,
            contentText: contentText,
        };

        // 관리자 - 공지사항 관련 데이터 추가 (체크된 경우에만)
        if (user.role === "ADMIN" && $noticeCheckBox.is(":checked")) {
            const startDate = $("#start-date").val();
            const endDate = $("#end-date").val();

            if (!startDate || !endDate) {
                alert("공지 시작일과 종료일을 입력해주세요.");
                return null;
            }

            if (new Date(startDate) >= new Date(endDate)) {
                alert("공지 종료일은 시작일보다 늦어야 합니다.");
                return null;
            }

            // 공지사항 데이터 추가
            boardData.isNotice = "Y";
            boardData.startDate = startDate;
            boardData.endDate = endDate;
        } else {
            boardData.isNotice = "N";
        }

        // `boardCreateDTO`로 데이터 전송
        formData.append(dtoKey, new Blob([JSON.stringify(boardData)], {type: "application/json"}));

        // 첨부파일 추가
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

    /* 이벤트 바인딩 */
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
        const url = window.location.pathname;
        const parentBoardNo = url.split("/").pop();

        console.log(parentBoardNo);
        handleFormSubmit(`/api/board/reply/${parentBoardNo}`, "boardReplyDTO");
    });

    initializeWriterInfo();
});
