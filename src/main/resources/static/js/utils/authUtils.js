// 로컬스토리지에서 사용자 정보 가져오기
export const getCurrentUserFromStorage = () => {
    const currentUser = localStorage.getItem("currentUser");
    if (!currentUser) return null; // 데이터가 없으면 null 반환

    const { isLoggedIn, username, userId, role } = JSON.parse(currentUser);
    return { isLoggedIn, username, userId, role }; // 필요한 값 반환
};