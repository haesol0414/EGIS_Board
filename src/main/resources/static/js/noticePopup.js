$(document).ready(function() {
    // 팝업
    // const noticeClosed = localStorage.getItem('noticeClosed');
    //
    // // 닫기 상태가 없을 때만 팝업 표시
    // if (!noticeClosed) {
    //     // 공지사항 데이터 호출
    //     $.get('/api/notice', function(notices) {
    //         if (notices && notices.length > 0) {
    //             const popupContent = notices.map(notice =>
    //                 <div>
    //                     <h3>${notice.subject}</h3>
    //                     <p>${notice.contentText}</p>
    //                 </div>
    //             ).join('');
    //
    //             const popup =
    //                 <div class="popup">
    //                     <h2>📢 공지사항</h2>
    //                     ${popupContent}
    //                     <button id="closePopup">닫기</button>
    //                 </div>
    //             ;
    //             $('body').append(popup);
    //
    //             // 팝업 닫기 버튼 이벤트
    //             $('#closePopup').on('click', function() {
    //                 $('.popup').remove();
    //
    //                 // 로컬 스토리지에 팝업 닫기 상태 저장
    //                 localStorage.setItem('noticeClosed', 'true');
    //             });
    //         }
    //     });
    // }
});