<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="styleSheet" value="/resources/css/login.css" />
<%@ include file="layout/header.jsp" %>
<div id="global-wrap">
    <main>
        <div class="main-container" id="login-wrap">
            <h1 class="title">로그인</h1>
            <div class="login-card">
                <form>
                    <input class="form-id" id="form-id" type="text" placeholder="Id" required/>
                    <input class="form-pw" id="form-pw" type="password" placeholder="Password" required/>
                    <div id="error-msg" class="error-msg"></div>
                    <button class="form-submit" type="submit">Login</button>
                    <%-- 회원가입 페이지 이동 버튼 --%>
                    <div id="signup-link" class="btn-link">
                        <a href="/register" class="outline-btn">SignUp</a>
                    </div>
                </form>
            </div>
        </div>
        <!-- 메세지 모달 -->
        <div id="alert-modal" class="modal">
            <div class="modal-content">
                <p id="modal-msg"></p>
                <button id="close-btn">확인</button>
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
<script type="module" src="resources/js/login.js"></script>
