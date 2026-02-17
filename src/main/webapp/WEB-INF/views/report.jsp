<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction Report</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; padding: 20px; color: #333; }
        .container { max-width: 1400px; margin: 0 auto; }
        header { background: white; padding: 30px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h1 { font-size: 28px; margin-bottom: 20px; }
        .summary { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 15px; margin-top: 20px; }
        .summary-item { background: #f9f9f9; padding: 12px; border-radius: 4px; border-left: 4px solid #667eea; }
        .summary-label { font-size: 12px; color: #666; font-weight: 600; text-transform: uppercase; margin-bottom: 5px; }
        .summary-value { font-size: 16px; color: #333; font-weight: 500; }
        .btn { padding: 10px 20px; background: #667eea; color: white; text-decoration: none; border-radius: 4px; display: inline-block; }
        .btn:hover { background: #5568d3; }
        .report-section { background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }
        .table-header { padding: 20px 30px; display: flex; justify-content: space-between; align-items: center; }
        table { width: 100%; border-collapse: collapse; }
        thead { background-color: #f8f9fa; }
        th { padding: 15px; text-align: left; font-weight: 600; font-size: 13px; color: #333; border-bottom: 2px solid #eee; }
        th a { color: #667eea; text-decoration: none; display: flex; align-items: center; gap: 5px; cursor: pointer; }
        th a:hover { text-decoration: underline; }
        td { padding: 15px; border-bottom: 1px solid #eee; font-size: 13px; }
        tbody tr:hover { background-color: #f9f9f9; }
        .text-right { text-align: right; }
        .amount-positive { color: #27ae60; }
        .amount-negative { color: #e74c3c; }
        .pagination { padding: 20px 30px; border-top: 1px solid #eee; display: flex; justify-content: center; align-items: center; gap: 10px; flex-wrap: wrap; }
        .pagination a, .pagination span { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; text-decoration: none; color: #667eea; font-size: 13px; font-weight: 600; transition: all 0.3s; }
        .pagination a:hover { background-color: #667eea; color: white; }
        .pagination .current { background-color: #667eea; color: white; border-color: #667eea; }
        .pagination .disabled { color: #999; border-color: #ddd; cursor: not-allowed; }
        .empty-message { padding: 40px; text-align: center; color: #999; }
        .footer-info { padding: 20px 30px; background-color: #f9f9f9; border-top: 1px solid #eee; font-size: 12px; color: #666; text-align: center; }
    </style>
</head>

<body>
    <div class="container">
        <header>
            <h1>Transaction Report</h1>
            <div class="summary">
                <div class="summary-item">
                    <div class="summary-label">Date Range</div>
                    <div class="summary-value">
                        ${startDateFormatted} - ${endDateFormatted}
                    </div>
                </div>
                <c:if test="${searchCriteria.accountId != null}">
                    <div class="summary-item">
                        <div class="summary-label">Account ID</div>
                        <div class="summary-value">${searchCriteria.accountId}</div>
                    </div>
                </c:if>
            </div>
            <div style="margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/transaction/search" class="btn">New Search</a>
            </div>
        </header>

        <div class="report-section">
            <div class="table-header">
                <h2>Transaction Details</h2>
                <div style="font-size: 14px; color: #666; font-weight: 600;">Showing <span style="color: #667eea;">${fn:length(transactions)}</span> of <span style="color: #667eea;">${totalElements}</span> records</div>
            </div>
           <div style="display: flex; justify-content: flex-end;">
               <button style="margin-right:25px; margin-bottom:10px; padding:5px; width:250px;" type="button" onclick="filterTransactions()"><b>Filter rows</b></button>
           </div>
            <table>
                <thead>
                    <tr>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=id&sortOrder=${sortByField=='id' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                ID
                            </a>
                             <c:if test="${sortByField == 'id'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                             </c:if>
                           </th>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=accountId&sortOrder=${sortByField=='accountId' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Account
                            </a>
                            <c:if test="${sortByField == 'accountId'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>
                            <input style="width: 90px; height: 20px; font-size: 11px;" type="text" value="${param.accountId}" id="accountId" name="accountId" />
                         </th>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=dateTime&sortOrder=${sortByField=='dateTime' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Date & Time
                            </a>
                            <c:if test="${sortByField == 'dateTime'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>
                        </th>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=tranType&sortOrder=${sortByField=='tranType' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Transaction Type
                            </a>
                             <c:if test="${sortByField == 'tranType'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>
                            <select style="width: 90px; height: 20px; font-size: 11px;" id="tranType" name="tranType">
                                <option value="">-- Select --</option>
                                <option value="BONUS_REL"  ${param.tranType == 'BONUS_REL'  ? 'selected' : ''}>BONUS_REL</option>
                                <option value="CANC_BONUS" ${param.tranType == 'CANC_BONUS' ? 'selected' : ''}>CANC_BONUS</option>
                                <option value="CASHOUT"    ${param.tranType == 'CASHOUT'    ? 'selected' : ''}>CASHOUT</option>
                                <option value="CRE_BONUS"  ${param.tranType == 'CRE_BONUS'  ? 'selected' : ''}>CRE_BONUS</option>
                                <option value="DEPOSIT"    ${param.tranType == 'DEPOSIT'    ? 'selected' : ''}>DEPOSIT</option>
                                <option value="EXP_BONUS"  ${param.tranType == 'EXP_BONUS'  ? 'selected' : ''}>EXP_BONUS</option>
                                <option value="GAME_BET"   ${param.tranType == 'GAME_BET'   ? 'selected' : ''}>GAME_BET</option>
                                <option value="GAME_WIN"   ${param.tranType == 'GAME_WIN'   ? 'selected' : ''}>GAME_WIN</option>
                                <option value="MAN_ADJUST" ${param.tranType == 'MAN_ADJUST' ? 'selected' : ''}>MAN_ADJUST</option>
                                <option value="PLTFRM_BON" ${param.tranType == 'PLTFRM_BON' ? 'selected' : ''}>PLTFRM_BON</option>
                                <option value="PLTFRM_PRO" ${param.tranType == 'PLTFRM_PRO' ? 'selected' : ''}>PLTFRM_PRO</option>
                                <option value="ROLLBACK"   ${param.tranType == 'ROLLBACK'   ? 'selected' : ''}>ROLLBACK</option>
                                <option value="TIPS"       ${param.tranType == 'TIPS'       ? 'selected' : ''}>TIPS</option>
                            </select>
                        </th>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=platformTranId&sortOrder=${sortByField=='platformTranId' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Platform Tran ID
                            </a>
                            <c:if test="${sortByField == 'platformTranId'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>
                            <input style="width: 90px; height: 20px; font-size: 11px;" type="text" value="${param.platformTranId}" id="platformTranId" name="platformTranId" />
                        </th>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=gameTranId&sortOrder=${sortByField=='gameTranId' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Game Tran ID
                            </a>
                            <c:if test="${sortByField == 'gameTranId'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>

                            <input style="width: 90px; height: 20px; font-size: 11px;" type="text" value="${param.gameTranId}" id="gameTranId" name="gameTranId" />

                        </th>
                        <th>
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=gameId&sortOrder=${sortByField=='gameId' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Game ID
                            </a>
                            <c:if test="${sortByField == 'gameId'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>

                             <input style="width: 90px; height: 20px; font-size: 11px;" type="text" value="${param.gameId}" id="gameId" name="gameId" />

                         </th>
                        <th class="text-right">
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=amount&sortOrder=${sortByField=='amount' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Amount
                            </a>
                            <c:if test="${sortByField == 'amount'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>
                        </th>
                        <th class="text-right">
                            <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=balance&sortOrder=${sortByField=='balance' && currentSortOrder=='ASC'?'DESC':'ASC'}&pageSize=${pageSize}"
                            style="display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                                Balance
                            </a>
                            <c:if test="${sortByField == 'balance'}">
                                 <span style="font-size: 10px;">
                                    ${currentSortOrder == 'ASC' ? '&#9650;' : '&#9660;'}
                                 </span>
                            </c:if>
                        </th>
                    </tr>
                </thead>
                <tbody>
                       <c:choose>
                            <c:when test="${empty transactions}">
                                <tr><td colspan="9"><div class="empty-message">No transactions found for the specified criteria.</div></td><tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${transactions}" var="tran">
                                    <tr>
                                        <td>${tran.id}</td>
                                        <td>${tran.accountId}</td>
                                        <td>${tran.dateTime}</td>
                                        <td>${tran.tranType}</td>
                                        <td>${tran.platformTranId}</td>
                                        <td>${tran.gameTranId}</td>
                                        <td>${tran.gameId}</td>
                                        <td class="text-right"><span class="${tran.totalAmount.signum() >= 0 ? 'amount-positive' : 'amount-negative'}"><fmt:formatNumber value="${tran.totalAmount}" type="currency" currencySymbol="$"/></span></td>
                                        <td class="text-right"><fmt:formatNumber value="${tran.totalBalance}" type="currency" currencySymbol="$"/></td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                       </c:choose>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${!isFirst}">
                        <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=${sortByField}&sortOrder=${currentSortOrder}&pageNumber=0&pageSize=${pageSize}">First</a>
                        <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=${sortByField}&sortOrder=${currentSortOrder}&pageNumber=${currentPage-1}&pageSize=${pageSize}">Previous</a>
                    </c:if>

                    <c:set var="startPage" value="${Math.max(0, currentPage - 2)}"/>
                    <c:set var="endPage" value="${Math.min(totalPages - 1, currentPage + 2)}"/>
                    <c:forEach begin="${startPage}" end="${endPage}" var="p">
                        <c:choose>
                            <c:when test="${p == currentPage}">
                                <span class="current">${p + 1}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=${sortByField}&sortOrder=${currentSortOrder}&pageNumber=${p}&pageSize=${pageSize}">${p + 1}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${!isLast}">
                        <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=${sortByField}&sortOrder=${currentSortOrder}&pageNumber=${currentPage+1}&pageSize=${pageSize}">Next</a>
                        <a href="${pageContext.request.contextPath}/transaction/report?startDateTime=${param.startDateTime}&endDateTime=${param.endDateTime}&accountId=${param.accountId}&tranType=${param.tranType}&platformTranId=${param.platformTranId}&gameTranId=${param.gameTranId}&gameId=${param.gameId}&sortBy=${sortByField}&sortOrder=${currentSortOrder}&pageNumber=${totalPages-1}&pageSize=${pageSize}">Last</a>
                    </c:if>
                </div>
            </c:if>

            <div class="footer-info">Page ${currentPage + 1} of ${totalPages} | ${pageSize} records per page | Total: ${totalElements} records</div>
        </div>
    </div>

    <script>
        var Math = window.Math;

        function filterTransactions() {
            var tranType = document.getElementById("tranType").value;
            var platformTranId = document.getElementById("platformTranId").value;
            var accountId = document.getElementById("accountId").value;
            var gameTranId = document.getElementById("gameTranId").value;
            var gameId = document.getElementById("gameId").value;

            var url = '${pageContext.request.contextPath}/transaction/report' +
                    '?startDateTime=${param.startDateTime}' +
                    '&endDateTime=${param.endDateTime}' +
                    '&accountId=' + accountId +
                    '&tranType=' + tranType +
                    '&platformTranId=' + platformTranId +
                    '&gameTranId=' + gameTranId +
                    '&gameId=' + gameId +
                    '&sortBy=${sortByField}' +
                    '&sortOrder=${currentSortOrder}' +
                    '&pageNumber=${currentPage}' +
                    '&pageSize=${pageSize}';

                window.location.href = url;

            }
    </script>
</body>
</html>
