<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="resources/css/common.css">
    <link rel="stylesheet" href="resources/css/boardList.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="resources/js/logout.js"></script>
    <script src="resources/js/register.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="board-list-wrap">
            <div class="board-top">
                <h1 class="board-title">게시글 목록</h1>
                <div class="board-right">
                    <input class="search-input"></input>
                    <a class="search-btn">
                        <i class="fas fa-search"></i>
                    </a>
                    <a href="/write" class="write-btn">
                        <i class="fas fa-pencil-alt"></i>
                    </a>
                </div>
            </div>
            <table class="board-table">
                <thead>
                <tr>
                    <th class="col board-num">No.</th>
                    <th class="col subject">제목</th>
                    <th class="col writer">작성자</th>
                    <th class="col write-date">작성일</th>
                    <th class="col update-date">최근 수정일</th>
                    <th class="col view-count">조회수</th>
                </tr>
                </thead>
                <tbody>
                <!-- 10개의 행을 반복문으로 출력 -->
                <c:forEach var="row" begin="1" end="10">
                    <tr>
                        <c:forEach var="col" begin="1" end="6">
                            <td class="row"></td>
                        </c:forEach>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <%-- 페이지네이션 필요 --%>
            <div class="pagination">
                <a href="#" class="prev">
                    <i class="fas fa-chevron-left"></i> <!-- 이전 화살표 -->
                </a>
                <span class="current-page">1</span>
                <a href="#">2</a>
                <a href="#" class="next">
                    <i class="fas fa-chevron-right"></i> <!-- 다음 화살표 -->
                </a>
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
</body>
</html>
