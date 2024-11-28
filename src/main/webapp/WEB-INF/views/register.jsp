<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="styleSheet" value="/resources/css/register.css" />
<%@ include file="layout/header.jsp" %>
<div id="global-wrap">
    <main>
        <div class="main-container" id="signup-wrap">
            <h1 class="title">회원 가입</h1>
            <div class="signup-card">
                <form>
                    <div>
                        <div>
                            <label for="form-id">아이디</label>
                        </div>
                        <div class="signup-id">
                            <input type="text" id="form-id" class="form-id" name="form-id" maxlength="10" required
                                   placeholder="Id"/>
                            <button type="button" id="id-check-btn">중복확인</button>
                        </div>
                        <div id="error-msg" class="error-msg"></div>
                        <div id="positive-msg" class="positive-msg"></div>
                    </div>
                    <div>
                        <label for="form-name">이름</label>
                        <input type="text" id="form-name" class="form-name" name="form-name" maxlength="5" required
                               placeholder="Name"/>
                    </div>
                    <div>
                        <label for="form-pw">비밀번호</label>
                        <input type="password" id="form-pw" class="form-pw" name="form-pw" maxlength="10"
                               pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$" required
                               placeholder="영문 숫자 특수문자 조합 8글자를 입력해 주세요."/>
                    </div>
                    <div>
                        <label for="form-pwCheck">비밀번호 확인</label>
                        <input type="password" id="form-pwCheck" class="form-pwCheck" name="form-pwCheck" minlength="8"
                               required placeholder="비밀번호와 동일한 문자를 입력해 주세요."/>
                    </div>
                    <button type="submit" class="form-submit">Register</button>
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
<script type="module" src="resources/js/register.js"></script>
</body>
</html>
