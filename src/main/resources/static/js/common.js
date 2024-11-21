$(document).ready(function() {
    const token = sessionStorage.getItem('token');
    const logoutBtn = $("#logout-btn");
    const username = $("#username");
    const loginLink = $("#login-link");
    const signupLink = $("#signup-link");
    const writeBtn = $("#write-btn");
    const loggedInUserDiv = $('.login-user');
    const closeBtn = $("#close-btn");
    const alertModal = $("#alert-modal");
    const modalMsg = $("#modal-msg");

    if (token) {
        try {
            const decodedToken = jwt_decode(token);
            console.log("(common) 디코딩된 토큰:", decodedToken);
            const now = Math.floor(Date.now() / 1000);
            const exp = decodedToken.exp;

            if (exp > now) {
                username.text(decodedToken.userName + "님").show();
                logoutBtn.show();
                loginLink.hide();
                signupLink.hide();

                const timeUntilExpiration = (exp - now) * 1000;
                console.log(`토큰 만료까지 남은 시간: ${(timeUntilExpiration / 1000 / 60).toFixed(0)}분`);

                setTimeout(() => {
                    alert("토큰이 만료되었습니다. 다시 로그인 해주세요.");
                    sessionStorage.removeItem("token");
                    window.location.href = "/login";
                }, timeUntilExpiration);
            } else {
                alert("토큰이 만료되었습니다. 다시 로그인 해주세요.");
                sessionStorage.removeItem("token");
                window.location.href = "/login";
            }

            logoutBtn.on("click", function() {
                sessionStorage.removeItem("token");
                openAlertModal("로그아웃 성공");
            });
        } catch (error) {
            console.error("토큰 디코딩 오류:", error);
            alert("토큰 디코딩에 실패했습니다. 다시 로그인 해주세요.");
            sessionStorage.removeItem("token");
            window.location.href = "/login";
        }
    } else {
        loginLink.show();
        signupLink.show();
        writeBtn.hide();
        loggedInUserDiv.hide();

        console.log("비회원 입니다.");
    }

    const openAlertModal = (msg) => {
        alertModal.show();
        modalMsg.text(msg);
    }
    const closeAlertModal = () => {
        alertModal.hide();
    }
    closeBtn.on("click", function () {
        closeAlertModal();
    });
    $(document).on("keydown", function (event) {
        if (event.key === "Enter" && alertModal.is(":visible")) {
            closeAlertModal();
            window.location.href = "/";
        }
    });
});
