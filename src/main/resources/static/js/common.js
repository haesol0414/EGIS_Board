$(document).ready(function () {
    // 공통 변수 선언
    const token = sessionStorage.getItem('token');
    const $logoutBtn = $("#logout-btn");
    const $username = $("#username");
    const $loginLink = $("#login-link");
    const $signupLink = $("#signup-link");
    const $writeBtn = $("#write-btn");
    const $loggedInUserDiv = $('.login-user');
    const $closeBtn = $("#close-btn");
    const $alertModal = $("#alert-modal");
    const $modalMsg = $("#modal-msg");

    // 토큰 유효성 확인 및 처리
    const handleToken = () => {
        if (!token) {
            setGuestUI();
            return;
        }

        try {
            const decodedToken = jwt_decode(token);
            console.log("회원 - 디코딩된 토큰:", decodedToken);

            const now = Math.floor(Date.now() / 1000);
            const exp = decodedToken.exp;

            if (exp > now) {
                setUserUI(decodedToken.userName);
                setTokenExpirationHandler(exp - now);
            } else {
                handleTokenExpired();
            }

            $logoutBtn.on("click", handleLogout);
        } catch (error) {
            console.error("토큰 디코딩 오류:", error);
            handleTokenError();
        }
    };

    // 유효한 토큰일 때 UI 업데이트
    const setUserUI = (userName) => {
        $username.text(`${userName}님`).show();
        $logoutBtn.show();
        $loginLink.hide();
        $signupLink.hide();
        console.log("로그인 상태입니다.");
    };

    // 비회원일 때 UI 설정
    const setGuestUI = () => {
        $loginLink.show();
        $signupLink.show();
        $writeBtn.hide();
        $loggedInUserDiv.hide();
        console.log("비회원입니다.");
    };

    // 토큰 만료 처리
    const setTokenExpirationHandler = (timeUntilExpiration) => {
        console.log(`토큰 만료까지 남은 시간: ${(timeUntilExpiration / 60).toFixed(0)}분`);
        setTimeout(() => {
            alert("토큰이 만료되었습니다. 다시 로그인 해주세요.");
            sessionStorage.removeItem("token");
            window.location.href = "/login";
        }, timeUntilExpiration * 1000);
    };

    // 토큰 만료 또는 디코딩 실패 시 처리
    const handleTokenExpired = () => {
        alert("토큰이 만료되었습니다. 다시 로그인 해주세요.");
        sessionStorage.removeItem("token");
        window.location.href = "/login";
    };

    const handleTokenError = () => {
        alert("토큰 디코딩에 실패했습니다. 다시 로그인 해주세요.");
        sessionStorage.removeItem("token");
        window.location.href = "/login";
    };

    // 로그아웃 처리
    const handleLogout = () => {
        sessionStorage.removeItem("token");
        openAlertModal("로그아웃 성공");
    };

    // 알림 모달 열기
    const openAlertModal = (msg) => {
        $modalMsg.text(msg);
        $alertModal.show();
    };

    // 알림 모달 닫기
    const closeAlertModal = () => {
        $alertModal.hide();

        window.location.href = "/";
    };

    // 알림 모달 닫기 버튼
    $closeBtn.on("click", closeAlertModal);

    // Enter 키로 모달 닫기
    $(document).on("keydown", function (event) {
        if (event.key === "Enter" && $alertModal.is(":visible")) {
            closeAlertModal();
        }
    });

    // 실행
    handleToken();
});
