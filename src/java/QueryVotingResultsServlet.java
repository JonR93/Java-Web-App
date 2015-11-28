/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Jon
 */
@WebServlet(name = "QueryVotingResultsServlet",urlPatterns = {"/QueryVotingResultsServlet"})
public class QueryVotingResultsServlet extends HttpServlet {
    
    @Resource(name="jdbc/HW2DB")
    private DataSource dataSource;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String musicType;
        int numOfVotes;
        PrintWriter out = response.getWriter();
        try {
            //query database to select all music types and their number of votes
            Connection connection = dataSource.getConnection();
            String selectSQL = "select * from votes";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = selectStatement.executeQuery();
            List<String> votingResults = new ArrayList<String>();
            //loop through query results
            while(resultSet.next()){
                musicType = resultSet.getString("musictype");
                numOfVotes = resultSet.getInt("numvotes");
                //formating results as table rows
                votingResults.add("<td>"+musicType+"</td><td>"+numOfVotes+"</td>");
            }

            //close database connection
            resultSet.close();
            selectStatement.close();
            connection.close();
            
            //set request to be the list of musictypes and number of votes
            request.setAttribute("votingResults", votingResults);
            //forward the request to the DisplayResults page
            getServletContext().getRequestDispatcher("/ResultsPage.jsp").forward(request, response);
        }catch(Exception e){
            out.println("Exception occurred "+e.getMessage());
            e.printStackTrace();
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
