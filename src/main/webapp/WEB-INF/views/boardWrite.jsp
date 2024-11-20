<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="resources/css/boardWrite.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
    <script src="resources/js/boardWrite.js"></script>
</head>
<body>
<div id="global-wrap">
    <%@ include file="layout/header.jsp" %>
    <main>
        <div class="main-container" id="board-write-wrap">
            <div class="board-top">
                <h1 class="board-title">게시글 작성</h1>
            </div>
            <form class="write-form">
                <table>
                    <tr>
                        <th class="writer">작성자</th>
                        <td id="writer"></td>
                    </tr>
                    <tr>
                        <th class="subject">제목</th>
                        <td><input type="text" name="subject" id="subject" maxlength=20></td>
                    </tr>
                    <tr>
                        <th class="content">내용</th>
                        <td><textarea name="content" id="content" maxlength=2000 wrap="hard"></textarea></td>
                    </tr>
                </table>
                <p><input type="submit" value="작성하기" class="write_btn"></p>
            </form>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</div>
</body>
</html>
