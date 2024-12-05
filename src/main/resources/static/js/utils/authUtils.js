import * as Modal from './modal.js';

// 로컬스토리지에서 사용자 정보 가져오기
export const getCurrentUserFromStorage = () => {
    const currentUser = localStorage.getItem("currentUser");
    if (!currentUser) return null; // 데이터가 없으면 null 반환

    const {isLoggedIn, username, userId, role, expiresAt} = JSON.parse(currentUser);
    return {isLoggedIn, username, userId, role, expiresAt};
};

// 토큰 유효성 확인
export const checkTokenValidity = () => {
    const user = getCurrentUserFromStorage();

    if (user && user.expiresAt) {
        const now = new Date().getTime();
        const remainingTimeInMs = user.expiresAt - now;
        const remainingMinutes = Math.floor(remainingTimeInMs / (1000 * 60));

        console.log(`토큰 만료까지 남은 시간 : ${remainingMinutes}분`);

        if (!user.expiresAt || now > user.expiresAt) {
            // 만료된 경우 로컬 스토리지 초기화 (쿠키는 서버에서 자동 삭제)
            localStorage.removeItem("currentUser");

            // 알림 표시 및 리다이렉트
            Modal.openAlertModal("세션이 만료되었습니다.<br>다시 로그인해주세요.", "/login");
        }
    }
};

// 초기화 시 호출
document.addEventListener("DOMContentLoaded", checkTokenValidity);

// 주기적 세션 확인 (5분)
setInterval(checkTokenValidity, 5 * 60 * 1000);