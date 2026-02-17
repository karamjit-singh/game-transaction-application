<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Redirecting...</title>
</head>
<body>
    <% response.sendRedirect(request.getContextPath() + "/transaction/search"); %>
</body>
</html>
