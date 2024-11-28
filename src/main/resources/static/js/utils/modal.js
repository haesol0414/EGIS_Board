export let $modalMsg, $alertModal, $closeBtn;

// DOM 로드 후 요소 초기화
export function initializeModalElements() {
    $modalMsg = $("#modal-msg");
    $alertModal = $("#alert-modal");
    $closeBtn = $("#close-btn");
}

// 알림 모달
export function openAlertModal(msg, redirectUrl) {
    $modalMsg.text(msg);
    $alertModal.show();

    $closeBtn.off("click").on("click", function () {
        $alertModal.hide();

        window.location.href = redirectUrl;
    });

    $(document).on("keydown", function (event) {
        if (event.key === "Enter" && $alertModal.is(":visible")) {
            $alertModal.hide();

            window.location.href = redirectUrl;
        }
    });
}