import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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
    Random randomSpawn = new Random();

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
        if(randomSpawn.nextInt(1000) < 100){
            spawnPatient();
        }

        emptyRoom();
    }

    public void spawnPatient(){
        Random rand = new Random();
        if(waitingRoom.size() < waitingRoomSize){
            int sick_level = rand.nextInt(3) + 1;
            waitingRoom.add(new Patient(patientCount , sick_level));
            patientCount++;
        }
        System.out.println(waitingRoom.getLast());
    }

    public void spawnDoctor() {
        Random rand = new Random();

        DoctorRoom.clear(); 
        for (int i = 0; i < DoctorRoomSize; i++) {
            int treat_level = rand.nextInt(3) + 1; 
            DoctorRoom.add(new Doctor(i, treat_level)); 
        }
        boolean isLevel1 = false, isLevel2 = false, isLevel3 = false;
        for (Doctor doctor : DoctorRoom) {
            switch (doctor.treat_level) {
                case 1: isLevel1 = true; break;
                case 2: isLevel2 = true; break;
                case 3: isLevel3 = true; break;
            }
        }
        if (!isLevel1) DoctorRoom.add(new Doctor(DoctorRoomSize++, 1));
        if (!isLevel2) DoctorRoom.add(new Doctor(DoctorRoomSize++, 2));
        if (!isLevel3) DoctorRoom.add(new Doctor(DoctorRoomSize++, 3));
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
                if (!waitingRoom.isEmpty()) {
                    Patient potentialPatient = waitingRoom.peekFirst(); 
                    boolean suitableDoctorFound = false;
                    LinkedList<Doctor> tempDoctorList = new LinkedList<>();
                    // Check if there is a doctor that can treat the patient
                    while (!DoctorRoom.isEmpty() && !suitableDoctorFound) {
                        Doctor potentialDoctor = DoctorRoom.removeFirst(); 
                        if(potentialPatient.sickness_level <= potentialDoctor.treat_level) {
                            System.out.println("Filling Treatment Room");
                            treatmentRoom[i].patient = waitingRoom.removeFirst();
                            treatmentRoom[i].doctor = potentialDoctor;
                            suitableDoctorFound = true;
                        } else {
                            tempDoctorList.add(potentialDoctor); 
                        }
                    }
                    DoctorRoom.addAll(tempDoctorList);
                    if (!suitableDoctorFound) {
                        System.out.println("No suitable doctor found for the patient. Trying next patient.");
                        waitingRoom.addLast(waitingRoom.removeFirst());
                    }
                }
            }
        }
    }
    
    
    public void emptyRoom(){
        for(int i = 0; i < treatmentRoom.length; i++){
            if(treatmentRoom[i].loadingBar >= 50){
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
                //System.out.println("Updates per second: " + updatesCounter + ", Frames per second: " + framesCounter);
                updatesCounter = 0;
                framesCounter = 0;
            }
        }
    }
}
