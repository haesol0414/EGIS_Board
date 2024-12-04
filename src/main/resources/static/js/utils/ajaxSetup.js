import {checkTokenValidity, getCurrentUserFromStorage} from './authUtils.js';

$.ajaxSetup({
    beforeSend: () => {
        checkTokenValidity();
        const user = getCurrentUserFromStorage();
        if (!user) {
            alert("세션이 만료되었습니다. 다시 로그인해주세요.");
            window.location.href = "/login";

            return false; // 요청 중단
        }
    },
});