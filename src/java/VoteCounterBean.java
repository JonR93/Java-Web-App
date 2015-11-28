
import java.io.Serializable;
import javax.faces.bean.SessionScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jon
 */
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@SessionScoped //look into this@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
public class VoteCounterBean implements Serializable {
    
    private int numOfVotes;
    
    public VoteCounterBean()
    {
    }
    
    public int getNumOfVotes() {
        return numOfVotes;
    }

    public void setNumOfVotes(int numOfVotes) {
        this.numOfVotes = numOfVotes;
    }
}
