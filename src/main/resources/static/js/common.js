$(document).ready(function() {
    const token = localStorage.getItem('token');
    const logoutBtn = $("#logout-btn");
    const username = $("#username");
    const loginLink = $("#login-link");
    const signupLink = $("#signup-link");
    const writeBtn = $("#write-btn");

    if (token) {
        try {
            // 로그인 상태일 때
            const decodedToken = jwt_decode(token);
            console.log("디코딩된 토큰:", decodedToken);

            username.text(decodedToken.userName + "님").show();
            logoutBtn.show();
            writeBtn.show();
            loginLink.hide();
            signupLink.hide();

            // 로그아웃 버튼 클릭 시 처리
            logoutBtn.on("click", function() {
                localStorage.removeItem("token");
                alert("로그아웃 성공");
                window.location.href = "/";
            });
        } catch (error) {
            console.error("토큰 디코딩 오류:", error);
            alert("토큰 디코딩에 실패했습니다. 다시 로그인 해주세요.");
        }
    } else {
        // 로그아웃 상태일 때
        loginLink.show();
        signupLink.show();

        console.log("토큰이 없습니다.");
    }
});
