<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Game Transaction Report - Search</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f5f5; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h1 { color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
        input:focus, select:focus { outline: none; border-color: #667eea; box-shadow: 0 0 0 3px rgba(102,126,234,0.1); }
        .button-group { margin-top: 20px; display: flex; gap: 10px; }
        button { flex: 1; padding: 10px; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; }
        .btn-primary { background-color: #667eea; color: white; }
        .btn-primary:hover { background-color: #5568d3; }
        .btn-secondary { background-color: #f0f0f0; color: #333; }
        .alert { padding: 12px; margin-bottom: 20px; border-radius: 4px; }
        .alert-error { background-color: #fee; color: #c00; border: 1px solid #fcc; }
    </style>
</head>

<body>
    <div class="container">
        <h1>Search Transactions</h1>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <form action="<c:url value='/transaction/report' />" method="GET">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                <div class="form-group">
                    <label for="startDateTime">Start Date & Time *</label>
                    <input type="datetime-local" id="startDateTime" name="startDateTime" required>
                </div>
                <div class="form-group">
                    <label for="endDateTime">End Date & Time *</label>
                    <input type="datetime-local" id="endDateTime" name="endDateTime" required>
                </div>
            </div>
            <div class="form-group">
                <label for="accountId">Account ID (Optional)</label>
                <input type="number" id="accountId" name="accountId">
            </div>
            <div class="button-group">
                <button type="submit" class="btn-primary">Generate Report</button>
                <button type="reset" class="btn-secondary">Clear</button>
            </div>
        </form>
    </div>
</body>
</html>
