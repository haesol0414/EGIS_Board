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
    <link rel="stylesheet" href="resources/css/boardUpdate.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
    <script src="resources/js/common.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="board-update-wrap">
            <div class="board-top">
                <h1 class="board-title">게시글 수정</h1>
            </div>
            <form class="update-table">
                <table>
                    <tr>
                        <th class="writer">작성자</th>
                        <td id="writer">회원명 @(test123)</td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td><input type="text" name="subject" id="subject" maxlength=20></td>
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
                        <td><textarea name="content" id="content" maxlength=2000></textarea></td>
                    </tr>
                </table>
                <p><input type="submit" value="수정하기" class="update-btn"></p>
            </form>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
</body>
</html>
