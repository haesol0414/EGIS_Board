<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${parent != null ? "답글 작성" : "게시글 작성"}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="/resources/css/boardWrite.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
    <script src="/resources/js/boardWrite.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
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
                            <label for="file-input" class="custom-file-input">파일 선택</label>
                            <input type="file" id="file-input" name="file" />
                            <span id="file-name">선택된 파일 없음</span>
                            <button id="clear-file" class="clear-file" type="button">×</button>
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
</body>
</html>
