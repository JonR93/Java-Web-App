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
@WebServlet(urlPatterns = {"/SubmitVoteServlet"})
public class SubmitVoteServlet extends HttpServlet {
    
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
            //get array of selected music types
            String[] FavMusicTypes = request.getParameterValues("musictype");
            
            
            System.out.println(FavMusicTypes.length);
            
                //process vote for each music type selected
                for(int i=0; i<FavMusicTypes.length; i++)
                {
                    // Connect to database
                    Connection connection = dataSource.getConnection();
                    int numOfVotes;
                    //get the current number of votes for the selected music type
                    String selectSQL = "select numvotes from votes where musictype = ?";
                    PreparedStatement selectNumVotes = connection.prepareStatement(selectSQL);
                    selectNumVotes.setString(1,FavMusicTypes[i]);
                    ResultSet resultSet = selectNumVotes.executeQuery();
                    
                    resultSet.next();
                    numOfVotes = resultSet.getInt("numvotes");
                    
                    resultSet.close();
                    selectNumVotes.close();

                    //increment vote count
                    numOfVotes += 1;

                    // Update the number of votes for the music type in the DB
                    String updateSQL = "update votes set numvotes = ? where musictype = ?";
                    PreparedStatement updateVotes = connection.prepareStatement(updateSQL);
                    updateVotes.setInt(1, numOfVotes);
                    updateVotes.setString(2, FavMusicTypes[i]);
                    int recordsAffected = updateVotes.executeUpdate();
                    
                    updateVotes.close();
                    if (recordsAffected == 0) 
                    {
                        //the new music type was not added to the DB
                        out.println("Insert failed");
                    }
                    //close database connection
                    connection.close();
                }
                //increment the session value for number of times voted
                session.setAttribute("timesVoted", (int)session.getAttribute("timesVoted") +1);
                //redirect to the results page
                response.sendRedirect("QueryVotingResultsServlet");
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
