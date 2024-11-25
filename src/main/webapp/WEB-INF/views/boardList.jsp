<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="resources/css/boardList.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="resources/js/boardList.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="board-list-wrap">
            <h1 class="board-title">게시판</h1>
            <div class="board-top">
                <div class="search">
                    <div class="dropdown">
                        <button class="dropbtn">
                            <span class="dropbtn_content">제목</span>
                            <i class="fas fa-chevron-down"></i>
                        </button>
                        <div class="dropdown-content">
                            <a href="#" data-value="제목">제목</a>
                            <a href="#" data-value="내용">내용</a>
                            <a href="#" data-value="제목 + 내용">제목 + 내용</a>
                            <a href="#" data-value="작성자명">작성자명</a>
                        </div>
                    </div>
                    <input class="search-input">
                    <a id="search-btn" class="search-btn">
                        <i class="fas fa-search"></i>
                    </a>
                </div>
                <div class="board-right">
                    <a href="/board/write" id="write-btn" class="write-btn">
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
                </tbody>
            </table>
            <%-- 페이지네이션 --%>
            <div class="pagination">
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
</body>
</html>
