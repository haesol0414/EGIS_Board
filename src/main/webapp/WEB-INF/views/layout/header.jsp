<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                <div id="username"></div>
                <button id="logout-btn">Logout</button>
                <%--    로그아웃 상태일 때     --%>
                <div id="login-link">
                    <a href="/login">Login</a>
                </div>
                <div id="signup-link">
                    <a href="/register">SignUp</a>
                </div>
<%--                 나중에 지우기 --%>
<%--                <div>--%>
<%--                    <a href="/detail">Detail</a>--%>
<%--                </div>--%>
<%--                <div>--%>
<%--                    <a href="/update">Update</a>--%>
<%--                </div>--%>
            </div>
        </div>
    </div>
</header>