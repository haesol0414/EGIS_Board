<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="/resources/css/common.css">
    <link rel="stylesheet" href="${styleSheet}">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="module" src="/resources/js/header.js"></script>
</head>
<header>
    <div class="header-container">
        <div class="header-group">
            <div class="logo">
                <a href="/">
                    <span>Board</span>
                </a>
            </div>
            <div class="menu-group">
                <%--    로그인 상태일 때     --%>
                <div class="login-user">
                    <i class="fas fa-user-circle"></i>
                    <div id="username"></div>
                </div>
                <button id="logout-btn">Logout</button>
                <%--    로그아웃 상태일 때     --%>
                <div id="login-link">
                    <a href="/login">Login</a>
                </div>
                <div id="signup-link">
                    <a href="/register">SignUp</a>
                </div>
            </div>
        </div>
        <div id="alert-modal" class="modal">
            <div class="modal-content">
                <p id="modal-msg"></p>
                <button id="close-btn">확인</button>
            </div>
        </div>
    </div>
</header>
<body>
