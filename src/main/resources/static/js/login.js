$(document).ready(function() {
    const closeBtn = $("#close-btn");
    const alertModal = $("#alert-modal");
    const modalMsg = $("#modal-msg");
    const errorMsg = $("#error-msg");

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
                openAlertModal("로그인 성공")

                sessionStorage.setItem('token', res);
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    errorMsg.text(xhr.responseText).show();
                } else {
                    alert("서버 오류 발생");
                }
            }
        });
    });

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
