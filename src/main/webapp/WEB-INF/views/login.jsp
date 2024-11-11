<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="resources/css/common.css">
    <link rel="stylesheet" href="resources/css/login.css">
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="login-wrap">
            <h1>로그인</h1>
            <div class="login-card">
                <form>
                    <input class="form-id" type="email" placeholder="Id" required/>
                    <input class="form-pw" type="password" placeholder="Password"/>
                    <button class="form-submit" type="submit">Login</button>
                    <div class="btn-link">
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
