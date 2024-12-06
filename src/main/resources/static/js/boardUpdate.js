import * as Modal from './utils/modal.js';

$(document).ready(function () {
    Modal.initializeModalElements();

    const boardNo = window.location.pathname.match(/\/board\/edit\/(\d+)/)?.[1];
    const maxFiles = 5; // 최대 첨부파일 개수
    let newFiles = []; // 새로 추가된 파일 저장
    let retainedFileIds = []; // 유지할 기존 파일 ID 저장
    let removedFileIds = []; // 삭제할 파일 ID 저장

    const $updateForm = $(".update-form");
    const $fileInput = $("#file-input");
    const $fileList = $(".file-list");
    const $cancelBtn = $("#cancel-btn");
    const $clearFileBtn = $("#clear-btn");

    // 게시글 수정 데이터 생성
    const getUpdatedBoardData = () => {
        const subject = $("#subject").val().trim();
        const contentText = $("#content").val().trim();
        const formData = new FormData();

        if (!subject || !contentText) {
            alert("제목과 내용을 입력하세요.");
            return null;
        }

        const boardUpdateDTO = {
            boardNo: boardNo,
            subject: subject,
            contentText: contentText,
            removedFileIds: removedFileIds,
        };

        // 제목, 내용, 삭제할 파일 ID 리스트 추가
        formData.append(
            "boardUpdateDTO",
            new Blob([JSON.stringify(boardUpdateDTO)], {type: "application/json"})
        );

        // 새로 추가된 파일
        newFiles.forEach((file) => {
            if (file) formData.append("files", file);
        });

        return formData;
    };

    // 게시글 수정 API 요청
    const updateBoard = (formData) => {
        $.ajax({
            url: `/api/board/${boardNo}`,
            type: "PATCH",
            processData: false,
            contentType: false,
            data: formData,
            success: function (res) {
                Modal.openAlertModal("게시글이 수정되었습니다.", `/board/${boardNo}`);
            },
            error: function (xhr) {
                if (xhr.status === 403) {
                    alert('작성자 또는 관리자만 수정할 수 있습니다');
                } else if (xhr.status === 500) {
                    alert('서버 오류가 발생했습니다.');
                } else {
                    alert('요청 처리 중 문제가 발생했습니다.');
                }
            },
        });
    };

    // 기존 파일 ID 수집
    $fileList.find("li").each(function () {
        const fileId = $(this).data("file-id");
        if (fileId) retainedFileIds.push(fileId);

        console.log(fileId);
    });


    // 첨부파일 추가
    $fileInput.off("change").on("change", function () {
        const currentFiles = Array.from($fileInput[0].files);
        // 현재 추가된 새 파일의 개수
        const existingNewFileCount = newFiles.filter((f) => f !== null).length;
        // 기존 파일의 개수
        const existingFileCount = retainedFileIds.length;
        // 총 파일 개수 계산
        const totalFileCount = existingNewFileCount + existingFileCount;
        // 추가 가능 파일 수 계산
        const remainFileCount = maxFiles - totalFileCount;

        if (currentFiles.length > remainFileCount) {
            alert(`첨부파일은 최대 ${maxFiles}개까지 가능합니다.`);
            $fileInput.val(""); // 입력 초기화
            return;
        }

        currentFiles.forEach((file) => {
            newFiles.push(file); // 파일 추가
            const fileIndex = newFiles.filter((f) => f !== null).length - 1;

            // 파일 URL 생성 (이미지일 경우 썸네일 생성)
            const fileURL = file.type.startsWith("image/") ? URL.createObjectURL(file) : null;

            // 파일 리스트 요소 추가
            const fileElement = `
            <li data-new-file-index="${fileIndex}" class="file-box">
                ${fileURL ? `<img src="${fileURL}" alt="${file.name}" class="thumbnail" />` : ''}
                <span class="file-name">${file.name}</span>
                <button class="clear-file" type="button" data-new-file-index="${fileIndex}">×</button>
            </li>
        `;
            $fileList.append(fileElement);
        });

        $fileInput.val(""); // 파일 입력 초기화
    });


    // 첨부파일 삭제
    $fileList.on("click", ".clear-file", function () {
        const $li = $(this).closest("li");
        const fileId = $li.data("file-id");              // li에서 파일 ID 가져오기
        const newFileIndex = $li.data("new-file-index"); // 새 파일일 경우 인덱스 가져오기

        if (fileId) {
            // 기존 파일 삭제 처리
            removedFileIds.push(fileId);
            retainedFileIds = retainedFileIds.filter((id) => id !== fileId);
        } else if (newFileIndex !== undefined) {
            // 파일 추가 취소 처리
            newFiles[newFileIndex] = null;
        }

        $li.remove(); // li 요소 삭제
    });

    // 수정하기 버튼
    $updateForm.on("submit", function (e) {
        e.preventDefault();

        if (!boardNo) {
            alert("게시글 번호를 찾을 수 없습니다.");
            return;
        }

        const formData = getUpdatedBoardData();

        if (formData) updateBoard(formData);
    });

    // 수정 취소 버튼 클릭
    $cancelBtn.on("click", function () {
        window.location.href = `/board/${boardNo}`;
    });
});
