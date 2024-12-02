<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="styleSheet" value="/resources/css/boardList.css"/>
<%@ include file="layout/header.jsp" %>
<div id="global-wrap">
    <main>
        <div class="main-container" id="board-list-wrap">
            <h1 class="board-title">게시판</h1>
            <div class="board-top">
                <div class="search">
                    <div class="dropdown">
                        <button class="dropbtn">
                            <span class="dropbtn_content">${filter}</span> <!-- 기본값 -->
                            <i class="fas fa-chevron-down"></i>
                        </button>
                        <div class="dropdown-content">
                            <a href="#" data-value="subject">제목</a>
                            <a href="#" data-value="contentText">내용</a>
                            <a href="#" data-value="all">제목 + 내용</a>
                            <a href="#" data-value="writer">작성자명</a>
                        </div>
                    </div>
                    <form action="/board/list" method="get" style="display: inline;">
                        <input type="hidden" name="filter" value="${filter}"> <!-- 기본값 설정 -->
                        <input class="search-input" name="keyword" value="${keyword}" placeholder="검색어 입력">
                        <button id="search-btn" class="search-btn">
                            <i class="fas fa-search"></i>
                        </button>
                    </form>
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
                <c:choose>
                    <c:when test="${empty boardList}">
                        <tr>
                            <td class="no-data" colspan="6">
                                <span>조회된 게시글이 없습니다.</span>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="board" items="${boardList}">
                            <tr>
                                <td class="row board-num">${board.boardNo}</td>
                                <td class="row subject">
                                    <c:choose>
                                        <c:when test="${board.deletedYn == 'Y'}">
                                            <c:if test="${board.groupDep > 0}">
                                                <span style="margin-left: ${board.groupDep * 30}px;">  </span>
                                            </c:if>
                                            <span class="deleted-board">${board.subject}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${board.groupDep > 0}">
                                                <span class="reply-prefix"
                                                      style="margin-left: ${board.groupDep * 30}px;">RE: </span>
                                            </c:if>
                                            <a href="/board/${board.boardNo}?page=${currentPage}&filter=${filter}&keyword=${keyword}">${board.subject}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="row writer">${board.createUserName}(@${board.createUserId})</td>
                                <td class="row write-date">
                                    <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td class="row update-date">
                                    <c:choose>
                                        <c:when test="${not empty board.updatedAt}">
                                            <fmt:formatDate value="${board.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="row view-count">${board.viewCnt}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <%-- 페이지네이션 --%>
            <div class="pagination">
                <%-- 이전 버튼 --%>
                <c:if test="${currentPage > 1}">
                    <a href="?page=${currentPage - 1}&filter=${filter}&keyword=${keyword}" class="page-link prev-btn">
                        <i class="fas fa-chevron-left"></i>
                    </a>
                </c:if>
                <%-- 페이지 번호 --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="?page=${i}&filter=${filter}&keyword=${keyword}"
                       class="page-link ${currentPage == i ? 'active page-btn' : 'page-btn'}">${i}</a>
                </c:forEach>
                <%-- 다음 버튼 --%>
                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}&filter=${filter}&keyword=${keyword}" class="page-link next-btn">
                        <i class="fas fa-chevron-right"></i>
                    </a>
                </c:if>
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
<script type="module" src="/resources/js/boardList.js"></script>