import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Hospital extends JPanel implements Runnable{
    Thread thread = new Thread(this);
    LinkedList<Patient> waitingRoom = new LinkedList<>();
    LinkedList<Doctor> DoctorRoom = new LinkedList<>();
    TreatmentRoom[] treatmentRoom = new TreatmentRoom[15];

    static int waitingRoomSize = 10;
    static int patientCount = 0;
    static int treatedPatients = 0;
    static int DoctorRoomSize = 10;
    static int DoctorCount = 0;
    static int time = 0;

    public Hospital(){
        setLayout(null);
        setSize(1000,700);
        setVisible(true);
        thread.start();
        spawnDoctor();
        spawnTreatmentRoom();
    }


    public void paint(Graphics g){
        super.paint(g);
        g.drawString("Time: " + time, 950, 120);
        g.drawString("Patients Treated: " + treatedPatients, 950 - 62, 140);
        if(time != 0) g.drawString("Treatment Rate: " +  String.format("%.2f", (double)treatedPatients / (double)time), 950 - 68, 160);

        waitingRoom(g);
        DoctorRoom(g);
        paintTreatmentRoom(g);
        paintWaitingRoom(g);
        paintDoctorRoom(g);
    }
    public void waitingRoom(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(0,0,1000,50);
        g.setColor(Color.BLACK); // Set color for text

        String label = "Waiting Room";
        Font font = new Font("Arial", Font.BOLD, 20); // Create new font
        g.setFont(font); // Set the font

        FontMetrics fm = g.getFontMetrics();
        int x = (1000 - fm.stringWidth(label)) / 2;
        int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(label, x, y); // Draw the label
    }
    public void DoctorRoom(Graphics g){
        g.setColor(Color.CYAN);
        g.fillRect(0,50,1000,50);
        g.setColor(Color.BLACK); // Set color for text

        String label = "Doctor Room";
        Font font = new Font("Arial", Font.BOLD, 20); // Create new font
        g.setFont(font); // Set the font

        FontMetrics fm = g.getFontMetrics();
        int x = (1000 - fm.stringWidth(label)) / 2;
        int y = ((150 - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(label, x, y); // Draw the label
    }

    public void paintWaitingRoom(Graphics g){
        LinkedList<Patient> copyOfWaitingRoom = new LinkedList<>(waitingRoom);
        for (Patient patient : copyOfWaitingRoom) {
            patient.paint(g);
        }
    }
    public void movePatient(){
        Iterator<Patient> iterator = waitingRoom.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Patient patient = iterator.next();
            patient.setX(980 - count * 50);
            count ++;
        }
    }

    public void moveDoctor(){
        Iterator<Doctor> iterator = DoctorRoom.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Doctor doctor = iterator.next();
            doctor.setX(980 - count * 50);
            count ++;
        }
    }
    public void paintDoctorRoom(Graphics g){
        Iterator<Doctor> iterator = DoctorRoom.iterator();
        while (iterator.hasNext()) {
            Doctor doctor = iterator.next();
            doctor.paint(g);
        }
    }

    public void paintTreatmentRoom(Graphics g){
        for(int i = 0; i < treatmentRoom.length; i++){
            treatmentRoom[i].paint(g);
        }
    }

    public void update(){
        movePatient();
        moveDoctor();
        fillTreatmentRoom();

        emptyRoom();
    }

    public void spawnPatient(){
        if(waitingRoom.size() < waitingRoomSize){
            waitingRoom.add(new Patient(patientCount));
            patientCount++;
        }
        System.out.println(waitingRoom.getLast());
    }

    public void spawnDoctor(){
        for(int i = 0; i < DoctorRoomSize; i++){
            DoctorRoom.add(new Doctor(i));
        }
    }

    public void spawnTreatmentRoom(){
        int x = 1;
        int y = 1;
        for(int i = 0; i < treatmentRoom.length; i++){

            treatmentRoom[i] = new TreatmentRoom( x * 100 - 50, 200* y);
            x++;
            if(i != 0 && i %6==0){
                y++;
                x = 1;
            }
        }
    }
    public void fillTreatmentRoom() {
        for(int i = 0; i < treatmentRoom.length; i++) {
            if (treatmentRoom[i].patient == null && treatmentRoom[i].doctor == null) {
                if (!waitingRoom.isEmpty() && !DoctorRoom.isEmpty()) {
                    treatmentRoom[i].patient = waitingRoom.removeFirst();
                    treatmentRoom[i].doctor = DoctorRoom.removeFirst();
                }
            }
        }
    }
    public void emptyRoom(){
        for(int i = 0; i < treatmentRoom.length; i++){
            if(treatmentRoom[i].loadingBar > 50){
                treatmentRoom[i].patient = null;
                treatmentRoom[i].doctor.setY(60);
                treatmentRoom[i].doctor.setX(10000);
                DoctorRoom.add(treatmentRoom[i].doctor);
                treatmentRoom[i].doctor = null;
                treatmentRoom[i].loadingBar = 1;
                treatedPatients++;
            }
        }
    }

    public void startTreatment(){
        for(int i = 0; i < treatmentRoom.length; i++){
            if(treatmentRoom[i].patient != null && treatmentRoom[i].doctor != null){
                treatmentRoom[i].load();
            }
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0;
        int updatesCounter = 0;
        int framesCounter = 0;


        final int UPS_CAP = 60;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / (double) (1000000000 / UPS_CAP);
            lastTime = now;

            while (delta >= 1) {
                repaint();
                updatesCounter++;
                delta--;
                update();
            }

            framesCounter++;

            if (System.currentTimeMillis() - timer > 1000) {
                time++;
                timer += 1000;
                startTreatment();
                spawnPatient();
                //System.out.println("Updates per second: " + updatesCounter + ", Frames per second: " + framesCounter);
                updatesCounter = 0;
                framesCounter = 0;
            }
        }
    }
}
