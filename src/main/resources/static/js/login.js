$(document).ready(function() {
    // 로그인
    $("form").on("submit", function(event) {
        event.preventDefault();

        const userId = $("#form-id").val().trim();
        const password = $("#form-pw").val().trim();

        // 빈 값 체크
        if (userId === "" || password === "") {
            alert("아이디 또는 비밀번호를 입력해주세요.");
            return;
        }

        const loginData = {
            userId,
            password
        };

        $.ajax({
            url: "/users/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: function(response) {
                alert("회원가입이 완료되었습니다.");
                window.location.href = "/login";
            },
            error: function(xhr) {
                alert("회원가입 중 오류가 발생했습니다.");
            }
        });
    });
});
