$(document).ready(function() {
    $("#logout-btn").on("click", function(event) {
        event.preventDefault();

        $.ajax({
            url: "/logout",  // 로그아웃 요청 URL
            type: "POST",
            success: function (res) {
                alert("로그아웃 성공");

                window.location.href = "/";
            },
            error: function (xhr) {
                alert("로그아웃 실패");
            }
        });
    });
});
