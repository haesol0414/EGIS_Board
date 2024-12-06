<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ERROR</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f8f9fa;
        }

        .error-container {
            text-align: center;
            background: #ffffff;
            border: 1px solid #f1f1f1;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
        }

        .error-container h1 {
            color: #e15c68;
            font-size: 24px;
        }

        .error-container p {
            font-size: 18px;
            color: #767676;
            margin: 20px 0;
            font-weight: bold;
        }

        .error-container a {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            color: #fff;
            font-weight: bold;
            background-color: #A5D6A7;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
        }

        .error-container a:hover {
            background-color: #e2eeda;
        }
    </style>
</head>
<body>
<div class="error-container">
    <h1>ERROR</h1>
    <p>${errorMessage}</p>
    <a href="/">홈으로 돌아가기</a>
</div>
</body>
</html>
