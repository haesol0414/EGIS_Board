<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="/resources/css/boardDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/resources/js/boardDetail.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="board-write-wrap">
            <div class="board-top">
                <h1 class="board-title">게시글 상세</h1>
            </div>
            <div class="board-detail-table">
                <table>
                    <tr>
                        <th class="writer">작성자</th>
                        <td id="writer" data-writerid="${board.createUserId}">${board.createUserName}(@${board.createUserId})</td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td id="subject">
                        <c:if test="${board.groupDep > 0}">
                                <span class="reply-prefix">RE: </span>
                        </c:if>
                        ${board.subject}</td>
                    </tr>
                    <tr>
                        <th class="date">작성일 (최근 수정일)</th>
                        <td id="date">
                            <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                            <c:if test="${not empty board.updatedAt and not empty board.updateUserName}">
                                <span class="update-date"> (<fmt:formatDate value="${board.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                                ${board.updateUserName}님에 의해 수정)</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="view_cnt">조회수</th>
                        <td id="view_cnt">${board.viewCnt}</td>
                    </tr>
                    <tr>
                        <th class="content">내용</th>
                        <td id="content"><c:out value="${board.contentText}"/></td>
                    </tr>
                        <tr>
                            <th class="file">첨부파일</th>
                            <td id="file" class="file-name">
                                <c:choose>
                                    <c:when test="${file != null}">
                                        <span id="file-name">${file.originFileName}</span>
                                        <a href="#" class="file-download" download>
                                            <i class="fas fa-download"></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <span>-</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                </table>
            </div>
            <div class="btns-box">
                <a href="/board/reply/${board.boardNo}" class="btn reply-link">답글 달기</a>
                <div class="writer-btns">
                    <button class="btn" id="delete-btn">삭제하기</button>
                    <button class="btn" id="modify-btn" onclick="location.href='/board/${board.boardNo}/edit'">수정하기
                    </button>
                </div>
            </div>
        </div>
        <!-- 삭제 확인 모달 -->
        <div id="deleteModal" class="modal">
            <div class="modal-content">
                <p>삭제하시겠습니까?</p>
                <button id="confirm-btn">예</button>
                <button id="deny-btn">아니오</button>
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
</body>
</html>
