$(document).ready(function() {
    let isUserIdChecked = false; // 아이디 중복 체크 상태 변수

    // 아이디 중복체크
    $("#id-check-btn").on("click", function() {
        const userId = $("#form-id").val().trim();

        if (userId === "") {
            alert("아이디를 입력해 주세요.");
            return;
        }

        $.ajax({
            url: "/api/users/check-id",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ userId: userId }),
            success: function(res) {
                alert(res);
                isUserIdChecked = true;
            },
            error: function(xhr) {
                alert(xhr.status === 400 ? xhr.responseText : "서버와의 연결이 실패했습니다.");
                isUserIdChecked = false;
            }
        });
    });

    // 회원가입
    $("form").on("submit", function(event) {
        event.preventDefault();

        const userId = $("#form-id").val().trim();
        const userName = $("#form-name").val().trim();
        const password = $("#form-pw").val().trim();
        const pwCheck = $("#form-pwCheck").val().trim();

        // 빈 값 체크
        if (userId === "" || userName === "" || password === "" || pwCheck === "") {
            alert("모든 필드를 채워주세요.");
            return;
        }

        // 비밀번호 불일치 체크
        if (password !== pwCheck) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        // 아이디 중복 체크가 안 되었다면
        if (!isUserIdChecked) {
            alert("아이디 중복 체크를 해주세요.");
            return;
        }

        const userData = {
            userId,
            userName,
            password
        };

        $.ajax({
            url: "/api/users/signup",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(userData),
            success: function (res) {
                alert("회원가입이 완료되었습니다.");
                window.location.href = "/login";
            },
            error: function (xhr) {
                alert("회원가입 중 오류가 발생했습니다.");
            }
        });
    });
});
