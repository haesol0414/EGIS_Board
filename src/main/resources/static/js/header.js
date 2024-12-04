import * as Modal from './utils/modal.js';
import {getCurrentUserFromStorage} from './utils/authUtils.js';

$(document).ready(function () {
    Modal.initializeModalElements();

    const user = getCurrentUserFromStorage();

    const $logoutBtn = $("#logout-btn");
    const $username = $("#username");
    const $loginLink = $("#login-link");
    const $signupLink = $("#signup-link");
    const $writeBtn = $("#write-btn");
    const $loggedInUserDiv = $('.login-user');

    const fetchCurrentUser = () => {
        if (user && user.isLoggedIn) {
            // 회원 상태일 경우 UI 설정
            setUserUI(user.username);
        } else {
            // 비회원 상태일 경우 UI 설정
            setGuestUI();
        }
    }

    // 사용자 UI 설정
    const setUserUI = (username) => {
        $username.text(`${username}님`).show();
        $logoutBtn.show();
        $loginLink.hide();
        $signupLink.hide();
        $writeBtn.show();
        $loggedInUserDiv.show();
        console.log("로그인 상태입니다.");
    };

    // 비회원 UI 설정
    const setGuestUI = () => {
        $username.hide();
        $logoutBtn.hide();
        $loginLink.show();
        $signupLink.show();
        $writeBtn.hide();
        $loggedInUserDiv.hide();
        console.log("비회원입니다.");
    };

    // 로그아웃 처리
    const handleLogout = () => {
        $.ajax({
            url: "/api/users/logout",
            type: "POST",
            success: function () {
                localStorage.removeItem("currentUser");
                Modal.openAlertModal("로그아웃 성공", "/");
            },
            error: function () {
                alert("로그아웃 실패");
            }
        });
    };

    $logoutBtn.on("click", handleLogout);

    // 페이지 로드 시 사용자 정보 확인
    fetchCurrentUser();
});
