import java.awt.*;

public class TreatmentRoom {
    int x,y, loadingBar;
    boolean occupied = false;
    Patient patient;
    Doctor doctor;

    public TreatmentRoom(int x, int y){
        this.x = x;
        this.y = y;
        this.patient = null;
        this.doctor = null;
        this.loadingBar = 1;
    }
    public void occupy(){
        occupied = true;
    }
    public void open(){
        occupied = false;
    }
    public void load(){
        loadingBar +=10;
    }
    public void reset(){
        loadingBar = 1;
    }

    public void paint(Graphics g){
        g.setColor(Color.pink);
        g.fillRect(x,y,50,100);
        g.setColor(Color.black);
        g.fillRect(x-5,y,5,100);
        g.fillRect(x-5,y+100,60,5);
        g.fillRect(x+50,y,5,100);
        g.drawRect(x,y+ 110, loadingBar,5);

        if(patient != null && doctor != null){
            patient.setX(x+20);
            patient.setY(y+10);
            patient.paint(g);
            doctor.setX(x+20);
            doctor.setY(y+60);
            doctor.paint(g);
        }
    }
}
