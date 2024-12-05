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
                <table class="board-detail-table">
                    <tr>
                        <th class="writer">작성자</th>
                        <td id="writer"
                            data-writerid="${board.createUserId}">${board.createUserName}(@${board.createUserId})
                        </td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td id="subject" class="${board.isNotice == 'Y' ? 'bold' : ''}">
                            <c:if test="${board.groupDep > 0}">
                                <span class="reply-prefix">RE: </span>
                            </c:if>
                            <c:if test="${board.isNotice == 'Y'}">
                                <span class="notice-prefix">※공지사항※</span>
                            </c:if>
                            ${board.subject}
                            <c:if test="${board.deletedYn == 'Y'}">
                                <span class="deleted-subject">(삭제된 게시글)</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="date">작성일 (최근 수정일)</th>
                        <td id="date">
                            <fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            <c:if test="${not empty board.updatedAt and not empty board.updateUserName}">
                                <span class="update-date"> (<fmt:formatDate value="${board.updatedAt}"
                                                                            pattern="yyyy-MM-dd HH:mm:ss"/>
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
                            <c:when test="${not empty files}">
                            <ul class="file-list">
                                <c:forEach var="file" items="${files}">
                                    <li>
                                        <span class="file-name">${file.originFileName}</span>
                                        <button class="file-download" data-file-id="${file.attachmentId}">
                                            <i class="fas fa-download"></i>
                                        </button>
                                    </li>
                                </c:forEach>
                            </ul>
                            </c:when>
                            <c:otherwise>
                            <span>-</span>
                            </c:otherwise>
                            </c:choose>
                    </tr>
                </table>
            </div>
            <div class="btns-box">
                <c:if test="${board.deletedYn == 'N'}">
                    <a href="/board/reply/${board.boardNo}"
                       class="btn reply-link"
                       style="${board.isNotice == 'Y' ? 'visibility: hidden;' : ''}">
                        답글 달기
                    </a>
                    <div class="writer-btns">
                        <button class="btn" id="delete-btn">삭제하기</button>
                        <button class="btn" id="modify-btn" onclick="location.href='/board/edit/${board.boardNo}'">
                            수정하기
                        </button>
                    </div>
                </c:if>
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
<script type="module" src="/resources/js/boardDetail.js"></script>
