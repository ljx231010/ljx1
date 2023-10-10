<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <!-- 这是定义移动端显示的属性 -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 这里用到一个第三方的UI库semantic -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/me.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <title>Enzyme_Recommend_Show</title>
</head>
<body>
<!-- 导航界面 -->
<%@include file="nav.jsp" %>

<div class="ui aligned container" style="margin-top: 5em; width: 80%; ">
    <h3 class="ui header" style="text-align: center">
        Recommend exogenous enzyme for reaction <b>${requestScope.get("reactionId")}</b> in <b>${requestScope.get("speciesName")}</b></h3>
    <div class="ui attached segment" style="margin-top: 10px;">
        <div class="ui floating labeled icon dropdown button">
            <i class="filter icon"></i>
            <span class="text">Filter Tags</span>
            <div class="menu">
                <div class="header">
                    Filter by tag
                </div>
                <div class="divider"></div>
                <div class="item" onclick="func2(1)">
                    <!-- <span class="description">2 new</span> -->
                    <span class="text">only phylogenetic distance</span>
                </div>
                <div class="item" onclick="func2(2)">
                    <!-- <span class="description">10 new</span> -->
                    <span class="text">only Km</span>
                </div>
                <div class="item" onclick="func2(3)">
                    <!-- <span class="description">5 new</span> -->
                    <span class="text">combination</span>
                </div>
            </div>
        </div>
        <table class="ui celled table" id="table1" hidden="hidden" style="text-align: center;">
            <thead>
            <tr>
                <th>Rank <span title="Enzyme score ranking" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Potential Enzyme <span title="Recommended Enzyme EC Number" style="cursor: help;"
                                           data-toggle="tooltip"
                                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Source Species <span title="Source of recommended enzyme" style="cursor: help;"
                                         data-toggle="tooltip"
                                         data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Distance <span title="Phylogenetic distance" style="cursor: help;" data-toggle="tooltip"
                                   data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>View <span title="Related links" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="enzyme" items="${requestScope.get('distanceEnzymes')}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enzyme.getEcNumber()}</td>
                    <td>${enzyme.getSourceOrganismId()}(${enzyme.getSourceOrganismName()})</td>
                    <td>${enzyme.getDistance()}</td>
                    <td>
                        <c:forEach var="url" items="${enzyme.getUrls()}">
                            <a href="${url.getWebSite()}" target="_blank">${url.getName()}</a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestScope.get('distanceEnzymes')}">
                <tr>
                    <td colspan="5">No Results</td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <table class="ui celled table" id="table2" hidden="hidden" style="text-align: center;">
            <thead>
            <tr>
                <th>Rank <span title="Enzyme score ranking" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Potential Enzyme <span title="Recommended Enzyme EC Number" style="cursor: help;"
                                           data-toggle="tooltip"
                                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Source Species <span title="Source of recommended enzyme" style="cursor: help;"
                                         data-toggle="tooltip"
                                         data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Km<span title="KM values" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>View <span title="Related links" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="enzyme" items="${requestScope.get('dynamicsEnzymes')}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enzyme.getEcNumber()}</td>
                    <td>${enzyme.getSourceOrganismName()}</td>
                    <td>${enzyme.getKm()}</td>
                    <td>
                        <c:forEach var="url" items="${enzyme.getUrls()}">
                            <a href="${url.getWebSite()}" target="_blank">${url.getName()}</a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestScope.get('dynamicsEnzymes')}">
                <tr>
                    <td colspan="5">No Results</td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <table class="ui celled table" id="table3" style="text-align: center;">
            <thead>
            <tr>
                <th>Rank <span title="Enzyme score ranking" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Potential Enzyme <span title="Recommended Enzyme EC Number" style="cursor: help;"
                                           data-toggle="tooltip"
                                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Source Species <span title="Source of recommended enzyme" style="cursor: help;"
                                         data-toggle="tooltip"
                                         data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Score <span title="Enzyme score" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>View <span title="Related links" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="enzyme" items="${requestScope.get('collect')}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enzyme.getEcNumber()}</td>
                    <td>
                        <c:if test="${enzyme.getSourceOrganismId()!=null}">
                            ${enzyme.getSourceOrganismId()}(${enzyme.getSourceOrganismName()})
                        </c:if>
                        <c:if test="${enzyme.getSourceOrganismId()==null}">
                            ${enzyme.getSourceOrganismTaxId()}(${enzyme.getSourceOrganismTaxName()})
                        </c:if>
                    </td>
                    <td>${enzyme.getScore()}</td>
                    <td>
                        <c:forEach var="url" items="${enzyme.getUrls()}">
                            <a href="${url.getWebSite()}" target="_blank">${url.getName()}</a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestScope.get('collect')}">
                <tr>
                    <td colspan="5">No Results</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
</body>

<!-- 脚注部分 -->
<footer class="ui inverted vertical segment">
    <div class="ui center aligned container">
        <p class="" style="margin-bottom: 0px">©Guangxi University, Nanning, China</p>
        <p>
            Physical and Biological Computing Group · Department of Computer Science · Rice University</p>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/semantic.min.js"></script>
<script>
    $('.ui.dropdown').dropdown();
</script>
<script>
    function func2(value) {
        console.log("func2");
        if (value === 1) {
            console.log("value===1");
            document.getElementById("table1").removeAttribute("hidden");
            document.getElementById("table2").hidden = "hidden";
            document.getElementById("table3").hidden = "hidden";
        } else if (value === 2) {
            console.log("value===2");
            document.getElementById("table2").removeAttribute("hidden");
            document.getElementById("table1").hidden = "hidden";
            document.getElementById("table3").hidden = "hidden";
        } else if (value === 3) {
            console.log("value===3");
            document.getElementById("table3").removeAttribute("hidden");
            document.getElementById("table1").hidden = "hidden";
            document.getElementById("table2").hidden = "hidden";
        }
    }
</script>
</html>

