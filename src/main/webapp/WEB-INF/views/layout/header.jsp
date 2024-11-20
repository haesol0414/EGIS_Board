<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="/resources/css/common.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<script src="/resources/js/common.js"></script>
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
    </div>
</header>