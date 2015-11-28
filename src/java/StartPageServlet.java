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
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author Jon
 */
@WebServlet(name = "StartPageServlet", urlPatterns = {"/StartPageServlet"})
public class StartPageServlet extends HttpServlet {

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
        
        String musicType = "";
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            
            //html head
            out.println("<head>");
            out.println("<title>Vote</title>");  
            out.println("</head>");
            
            //html body
            out.println("<body>");
            
            //display form to vote for music types in the database
            out.println("<form action='SubmitVoteServlet'>");
            out.println("<fieldset>");
                out.println("<legend>Vote what your favorite type of music is</legend>");
            
            //query database to select all music types and their number of votes
            Connection connection = dataSource.getConnection();
            String selectSQL = "select * from votes";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = selectStatement.executeQuery();
            
            //print query results to web page
            while(resultSet.next()){
                musicType = resultSet.getString("musictype");
                out.println("<input type='checkbox' name='musictype' value='"+musicType+"'>"+musicType+"<br>");
            }
            
            //close database connection
            resultSet.close();
            selectStatement.close();
            connection.close();
            
            //close form to vote for music types in the database
            out.println("<br><br>");
                out.println("<input type='submit' value='Submit vote'>");
            out.println("</fieldset>");
            out.println("</form>");
            
            //display form to add new music type
            out.println("<form action='AddMusicTypeServlet'>");
            out.println("<fieldset>");
                out.println("<legend>Or add a new one</legend>");
                out.println("New music type:<br>");
                out.println("<input type='text' name='musicType' value=''>");
                out.println("<br><br>");
                out.println("<input type='submit' value='Add type and vote'>");
            out.println("</fieldset>");
            out.println("</form>");
            
            //close body/html
            out.println("</body>");
            out.println("</html>");
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
