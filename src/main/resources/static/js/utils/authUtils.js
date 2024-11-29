export const getUserInfoFromToken = (token) => {
    try {
        if (!token) {
            return null;
        }

        const decodedToken = jwt_decode(token);
        if(decodedToken) {
            console.log('디코딩된 토큰: ', decodedToken);
            const userId = decodedToken.sub;
            const userName = decodedToken.userName;
            const role = decodedToken.role;
            const exp = decodedToken.exp;

            return { userId, userName, role, exp };
        }
    } catch (error) {
        console.error("토큰 디코딩 중 오류 발생:", error.message);
        return null;
    }
};