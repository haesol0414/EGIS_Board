$(document).ready(function() {
    $("form").on("submit", function(event) {
        event.preventDefault();

        const userId = $("#form-id").val().trim();
        const password = $("#form-pw").val().trim();

        if (userId === "" || password === "") {
            alert("아이디 또는 비밀번호를 입력해주세요.");
            return;
        }

        const loginData = {
            userId, password
        }

        $.ajax({
            url: "/api/users/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: function (res) {
                alert(`로그인 성공`)
                console.log(res)

                localStorage.setItem('token', res);
                window.location.href = "/";
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
                } else {
                    alert("서버 오류 발생");
                }
            }
        });
    });
});
