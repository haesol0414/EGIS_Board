<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="resources/css/login.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="resources/js/login.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="login-wrap">
            <h1>로그인</h1>
            <div class="login-card">
                <form>
                    <input class="form-id" id="form-id" type="text" placeholder="Id" required/>
                    <input class="form-pw" id="form-pw" type="password" placeholder="Password" required/>
                    <button class="form-submit" type="submit">Login</button>
                    <%-- 회원가입 페이지 이동 버튼 --%>
                    <div id="signup-link" class="btn-link">
                        <a href="/register" class="outline-btn">SignUp</a>
                    </div>
                </form>
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
</body>
</html>
