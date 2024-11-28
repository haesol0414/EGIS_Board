import * as Modal from './utils/modal.js';
import { getUserInfoFromToken } from "./utils/authUtils.js";

$(document).ready(function () {
    Modal.initializeModalElements();

    const token = sessionStorage.getItem("token");
    const pathSegments = window.location.pathname.split('/');
    const boardNo = pathSegments[pathSegments.length - 1];

    const $modifyBtn = $("#modify-btn");
    const $deleteBtn = $("#delete-btn");
    const $replyBtn = $(".reply-link");
    const $deleteModal = $("#deleteModal");
    const $confirmDeleteBtn = $("#confirm-btn");
    const $denyDeleteBtn = $("#deny-btn");
    const $writerBtnDiv = $(".writer-btns");
    const $fileDownloadBtn = $(".file-download");
    const $writerId = $("#writer").data("writerid");

    const userInfo = getUserInfoFromToken(token);


    // 로그인유저 == 작성자인지 확인
    const initializeUI = () => {

        if (userInfo != null) {
            if (userInfo.userId === $writerId) {
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
                Modal.openAlertModal("게시글이 삭제되었습니다.", "/");
            },
            error: function (xhr, status, error) {
                alert("삭제 중 에러가 발생했습니다: " + xhr.responseText);
            },
        });
    };

    // 파일 다운로드 버튼
    $fileDownloadBtn.on("click", function () {
        const attachmentId = $(this).data("file-id");

        // API 요청
        $.ajax({
            url: `/api/files/download/${attachmentId}`,
            type: "GET",
            xhrFields: {
                responseType: "blob",
            },
            success: function (data, status, xhr) {
                const disposition = xhr.getResponseHeader("Content-Disposition");
                let filename = "download";

                if (disposition && disposition.indexOf("attachment") !== -1) {
                    const matches = disposition.match(/filename="(.+)"/);
                    if (matches != null && matches[1]) {
                        filename = decodeURIComponent(matches[1]);
                    }
                }

                const blob = new Blob([data]);
                const url = window.URL.createObjectURL(blob);

                // 다운로드 링크 생성 및 클릭
                const a = document.createElement("a");
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                a.remove();

                window.URL.revokeObjectURL(url);
            },
            error: function (xhr) {
                alert("파일 다운로드에 실패했습니다.");
                console.error(xhr);
            },
        });
    });

    // 삭제 모달 열기/닫기
    const openDeleteModal = () => $deleteModal.show();
    const closeDeleteModal = () => $deleteModal.hide();

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

    // 삭제 취소
    $denyDeleteBtn.on("click", closeDeleteModal);

    // 초기화 실행
    initializeUI();
});
