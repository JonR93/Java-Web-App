<%-- 
    Document   : ResultsPage
    Created on : Oct 9, 2015, 2:49:19 PM
    Author     : Jon
--%>


<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Results Page</title>
    </head>
    <body>
        
        <%
            if (request.getAttribute("votingResults") != null){
                List<String> results = (ArrayList<String>) request.getAttribute("votingResults");
        %>
        
        <!-- Display voting results in table -->
        <table border='1'>
            <tr><th>Music Type</th><th>Number of Votes</th></tr>
            
            <% for (String result : results) {%>

            <tr>
                <%= result%>
            </tr>
            <% }
          }%>
            
        </table>
        
        <br>
        <form action='StartPageServlet'>
            <input type='submit' value='Go vote'>
        </form>
        <br>
        <form action='index.html'>
            <input type='submit' value='Home'>
        </form> 
    </body>
</html>
