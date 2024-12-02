<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="styleSheet" value="/resources/css/boardWrite.css" />
<%@ include file="layout/header.jsp" %>
<div id="global-wrap">
    <main>
        <div class="main-container" id="board-write-wrap">
            <div class="board-top">
                <h1 class="board-title">${parent != null ? "답글 작성" : "게시글 작성"}</h1>
            </div>
            <form class="write-form">
                <table>
                    <tr>
                        <c:if test="${parent != null}">
                            <th>원글</th>
                            <td class="parent">
                                <c:if test="${parent.groupDep > 0}">
                                    <span class="reply-prefix">RE: </span>
                                </c:if>
                                <a href="/board/${parent.boardNo}" id="parent">${parent.subject}</a>
                            </td>
                        </c:if>
                    </tr>
                    <tr>
                        <th class="writer">작성자</th>
                        <td id="writer"></td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td><input type="text" name="subject" id="subject" minlength="2" maxlength="20"></td>
                    </tr>
                    <tr>
                        <th class="content">내용</th>
                        <td><textarea name="content" id="content" maxlength="2000" wrap="hard"></textarea></td>
                    </tr>
                    <tr>
                        <th class="file-upload">첨부파일</th>
                        <td>
                            <div class="file-list">
                                <label for="file-input-0" class="custom-file-input">파일 선택</label>
                                <input type="file" id="file-input-0" name="file" class="file-input" multiple />
                                <span class="file-alert">파일은 최대 5개까지 첨부 가능합니다.</span>
                            </div>
                        </td>
                    </tr>
                </table>
                <c:choose>
                    <c:when test="${parent != null}">
                        <div id="parent-data" data-board-no="${parent.boardNo}"></div>
                        <button id="reply-submit" class="write_btn">답글 작성</button>
                    </c:when>
                    <c:otherwise>
                        <button id="write-submit" class="write_btn">작성하기</button>
                    </c:otherwise>
                </c:choose>
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
<script type="module" src="/resources/js/boardWrite.js"></script>
