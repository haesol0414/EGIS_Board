<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="styleSheet" value="/resources/css/boardList.css" />
<%@ include file="layout/header.jsp" %>
<div id="global-wrap">
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
<script type="module" src="resources/js/boardList.js"></script>
