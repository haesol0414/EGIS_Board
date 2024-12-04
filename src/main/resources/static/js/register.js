import * as Modal from './utils/modal.js';

$(document).ready(function () {
    Modal.initializeModalElements();

    const idPattern = /^[a-z0-9]+$/;
    let isUserIdChecked = false;

    const $signUpFrom = $("form")
    const $formId = $("#form-id");
    const $formName = $("#form-name");
    const $formPw = $("#form-pw");
    const $formPwCheck = $("#form-pwCheck");
    const $idCheckBtn = $("#id-check-btn");
    const $positiveMsg = $("#positive-msg");
    const $errorMsg = $("#error-msg");

    // 아이디 중복 체크 API
    const checkUserId = () => {
        const userId = $formId.val().trim();
        resetMessages();

        if (!validateUserId(userId)) {
            return;
        }

        $.ajax({
            url: "/api/users/check-id",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ userId }),
            success: (res) => {
                $positiveMsg.text(`* ${res}`).show();
                isUserIdChecked = true;
            },
            error: (xhr) => {
                if (xhr.status === 400) {
                    $errorMsg.text(`* ${xhr.responseText}`).show();
                } else {
                    alert("서버와의 연결이 실패했습니다.");
                }
                isUserIdChecked = false;
            },
        });
    };

    // 회원가입 API
    const registerUser = (userData) => {
        $.ajax({
            url: "/api/users/signup",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(userData),
            success: () => {
                Modal.openAlertModal("회원가입이 완료되었습니다.", "/login");
            },
            error: () => {
                alert("회원가입 중 오류가 발생했습니다.");
            },
        });
    };

    // 회원가입 데이터 생성
    const handleSignupForm = (event) => {
        event.preventDefault();

        const userId = $formId.val().trim();
        const userName = $formName.val().trim();
        const password = $formPw.val().trim();
        const pwCheck = $formPwCheck.val().trim();

        if (!userId || !userName || !password || !pwCheck) {
            alert("모든 필드를 채워주세요.");
            return false;
        }

        if (password !== pwCheck) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }

        if (!isUserIdChecked) {
            alert("아이디 중복 체크를 해주세요.");
            return false;
        }

        const userData = { userId, userName, password };

        registerUser(userData);
    };

    // 아이디 유효성 검사
    const validateUserId = (userId) => {
        if (!idPattern.test(userId)) {
            $errorMsg.text("* 아이디는 영문 소문자와 숫자만 입력 가능합니다.").show();
            return false;
        }

        if (userId === "") {
            alert("아이디를 입력해 주세요.");
            return false;
        }

        return true;
    };

    // 메시지 초기화
    const resetMessages = () => {
        $errorMsg.hide();
        $positiveMsg.hide();
    };

    // 아이디 중복체크 버튼
    $idCheckBtn.on("click", checkUserId);

    // 회원가입 버튼
    $signUpFrom.on("submit", handleSignupForm);
});
