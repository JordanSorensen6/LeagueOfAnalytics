<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Player Search</title>

    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/bootstrap/css/bootstrap-theme.css"/>
    <link rel="stylesheet" href="resources/css/styles.css"/>
    <link rel="stylesheet" href="resources/css/layout.css"/>

    <script src="https://d3js.org/d3.v4.js"></script>
    <script src="resources/javascript/jquery-3.3.1.min.js"></script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

<jsp:include page="navbar.jsp"/>

<div class="row">
    <div class="col-lg-4 offset-4">
        <div class="input-group">
            <input id="username" type="text" class="form-control" placeholder="Search for...">
            <span class="input-group-btn">
                <button class="btn btn-secondary" type="button" id="search">Go!</button>
            </span>
        </div>
    </div>
</div>
</body>

<script>
    $('#nav-search').addClass('active');
    $('#username').on('keypress', function(e) {
        if(e.which == 13)
            if($(this).val() !== '')
                window.location = '/search/user?name=' + $(this).val();
    });
    $('#search').on('click', function() {
        if($('#username').val() !== '')
            window.location = '/search/user?name=' + $('#username').val();
    });
</script>
</html>
