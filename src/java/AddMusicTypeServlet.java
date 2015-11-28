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
import java.sql.Types;
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
@WebServlet(name = "AddMusicTypeServlet", urlPatterns = {"/AddMusicTypeServlet"})
public class AddMusicTypeServlet extends HttpServlet {
    
    @Resource(name = "jdbc/HW2DB")
    private DataSource dataSource;
    //create a session object
    HttpSession session;
    
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
        //declare session object
        //to keep track of how many times the user votes
        session = request.getSession(true);
	if (session.getAttribute("timesVoted") == null) {
	    session.setAttribute("timesVoted", 0);
	}
        PrintWriter out = response.getWriter();
        try {
            String musicType = request.getParameter("musicType");
            
            // Connect to database
            Connection connection = dataSource.getConnection();

            // Add the new music type to the DB
            String sql = "insert into votes (musictype,numvotes) values (?,?)";
            PreparedStatement insertMusicType = connection.prepareStatement(sql);
            insertMusicType.setString(1, musicType);
            insertMusicType.setInt(2, 1);
            int recordsAffected = insertMusicType.executeUpdate();
            insertMusicType.close();
            connection.close();
            if (recordsAffected == 0) {
                //the new music type was not added to the DB
                out.println("Insert failed");
            } else {
                //the new music type was added to the DB
                
                //increment the session value for number of times voted
                session.setAttribute("timesVoted", (int)session.getAttribute("timesVoted") +1);
                
                //redirect to servlet to query database for the voting results
                //ultimately will forward to a jsp to display the voting results
                response.sendRedirect("QueryVotingResultsServlet");
            }
        } catch (Exception e) {
            out.println("Error Occurred "+e.getMessage());
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
