import * as Modal from './utils/modal.js';

$(document).ready(function () {
    Modal.initializeModalElements();

    const $form = $("form");
    const $userIdInput = $("#form-id");
    const $passwordInput = $("#form-pw");
    const $errorMsg = $("#error-msg");

    // 로그인 처리
    const handleLogin = (event) => {
        event.preventDefault();

        const userId = $userIdInput.val().trim();
        const password = $passwordInput.val().trim();

        if (!userId || !password) {
            alert("아이디 또는 비밀번호를 입력해주세요.");
            return;
        }

        const loginData = { userId, password };

        sendLoginRequest(loginData);
    };

    // 로그인 요청 및 처리
    const sendLoginRequest = (loginData) => {
        $.ajax({
            url: "/api/users/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: (token) => {
                sessionStorage.setItem("token", token);
                Modal.openAlertModal("로그인 성공", "/");
            },
            error: (xhr) => {
                if (xhr.status === 401) {
                    $errorMsg.text(xhr.responseText).show();
                } else {
                    alert("서버 오류 발생");
                }
            },
        });
    };

    // 로그인 버튼
    $form.on("submit", handleLogin);
});
