import * as Modal from './utils/modal.js';
import { getUserInfoFromToken } from "./utils/authUtils.js";

$(document).ready(function () {
    Modal.initializeModalElements()
    const token = sessionStorage.getItem('token');

    const $logoutBtn = $("#logout-btn");
    const $username = $("#username");
    const $loginLink = $("#login-link");
    const $signupLink = $("#signup-link");
    const $writeBtn = $("#write-btn");
    const $loggedInUserDiv = $('.login-user');

    const handleHeaderUI = () => {
        if (token) {
            const userInfo = getUserInfoFromToken(token);
            if (userInfo) {
                setUserUI(userInfo.userName);
                setTokenExpirationHandler(token);
            } else {
                setGuestUI();
            }
        } else {
            setGuestUI();
        }
    };

    const setUserUI = (userName) => {
        $username.text(`${userName}님`).show();
        $logoutBtn.show();
        $loginLink.hide();
        $signupLink.hide();
    };

    const setGuestUI = () => {
        $loginLink.show();
        $signupLink.show();
        $writeBtn.hide();
        $loggedInUserDiv.hide();
        console.log("비회원입니다.");
    };

    // 토큰 만료 처리
    const setTokenExpirationHandler = (token) => {
        const userInfo = getUserInfoFromToken(token);

        if (!userInfo || !userInfo.exp) {
            console.error("유효하지 않은 토큰입니다.");
            return;
        }

        const currentTime = Math.floor(Date.now() / 1000); // 현재 시간 (초 단위)
        const timeUntilExpiration = userInfo.exp - currentTime;

        if (timeUntilExpiration <= 0) {
            alert("토큰이 이미 만료되었습니다. 다시 로그인 해주세요.");
            sessionStorage.removeItem("token");
            window.location.href = "/login";
            return;
        }

        console.log(`토큰 만료까지 남은 시간: ${(timeUntilExpiration / 60).toFixed(0)}분`);

        setTimeout(() => {
            alert("토큰이 만료되었습니다. 다시 로그인 해주세요.");
            sessionStorage.removeItem("token");
            window.location.href = "/login";
        }, timeUntilExpiration * 1000);
    };

    // 로그아웃 처리
    const handleLogout = () => {
        Modal.openAlertModal("로그아웃 성공", "/");
        sessionStorage.removeItem("token");
    };

    $logoutBtn.on("click", handleLogout);

    handleHeaderUI();
});
