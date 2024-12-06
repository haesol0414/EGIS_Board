<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="styleSheet" value="/resources/css/boardDetail.css"/>
<%@ include file="layout/header.jsp" %>
<div id="global-wrap">
    <main>
        <div class="main-container" id="board-write-wrap">
            <div class="board-top">
                <h1 class="board-title">게시글 상세</h1>
            </div>
            <%
                String referer = request.getHeader("Referer");
                String boardListUrl = referer != null && referer.contains("/admin/board")
                        ? "/admin/board"
                        : "/board";
            %>
            <a href="<%= boardListUrl %>?page=${currentPage}&filter=${filter}&keyword=${keyword}"
               class="board-list-link">
                <i class="fas fa-arrow-left"></i>게시글 목록
            </a>
            <div>
                <div class="deleted-board">
                    <i class="fas fa-trash-alt"></i>
                    <p>삭제된 게시글입니다.</p>
                </div>
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
