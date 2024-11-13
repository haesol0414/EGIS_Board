<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="resources/css/common.css">
    <link rel="stylesheet" href="resources/css/boardDetail.css">
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
                        <td id="writer">회원명 (@test123)</td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td id="subject">게시글 제목</td>
                    </tr>
                    <tr>
                        <th class="date">작성일 (최근 수정일)</th>
                        <td id="date">2024-11-04 05:02:32 (2024-11-06 14:49:34 관리자에 의해 수정)</td>
                    </tr>
                    <tr>
                        <th class="view_cnt">조회수</th>
                        <td id="view_cnt">0</td>
                    </tr>
                    <tr>
                        <th class="content">내용</th>
                        <td id="content">게시글 내용</td>
                    </tr>
                </table>
            </div>
            <div class="btns-box">
                <button class="btn" id="reply-btn">답글 달기</button>
                <div class="right-btns">
                    <button class="btn" id="delete-btn">삭제하기</button>
                    <button class="btn" id="modify-btn">수정하기</button>
                </div>
            </div>
        </div>
        <!-- 삭제 확인 모달 -->
        <div id="deleteModal" class="modal">
            <div class="modal-content">
                <span class="close-btn">&times;</span>
                <p>삭제하시겠습니까?</p>
                <button id="delete-confirm-btn">예</button>
                <button id="delete-deny-btn">아니오</button>
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
</body>
</html>
