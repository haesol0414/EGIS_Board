<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="/resources/css/boardUpdate.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
    <script src="/resources/js/boardUpdate.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="board-update-wrap">
            <div class="board-top">
                <h1 class="board-title">게시글 수정</h1>
            </div>
            <form class="update-form">
                <table>
                    <tr>
                        <th class="writer">작성자</th>
                        <td id="writer" data-writerid="${board.createUserId}">${board.createUserName}(@${board.createUserId})</td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td><input type="text" name="subject" id="subject" value="${board.subject}" maxlength=20 /></td>
                    </tr>
                    <tr>
                        <th class="date">작성일 (최근 수정일)</th>
                        <td id="date">
                            <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                            <c:if test="${not empty board.updatedAt and not empty board.updateUserName}">
                                (<fmt:formatDate value="${board.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                                ${board.updateUserName}님에 의해 수정)
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="view_cnt">조회수</th>
                        <td id="view_cnt">${board.viewCnt}</td>
                    </tr>
                    <tr>
                        <th class="content">내용</th>
                        <td><textarea id="content" name="content" maxlength=2000 wrap="hard">${board.contentText}</textarea></td>
                    </tr>
                </table>
                <div class="update-btns">
                    <button class="btn" id="cancel-btn">취소</button>
                    <p><input type="submit" value="수정하기" class="btn"></p>
                </div>
            </form>
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
</body>
</html>
