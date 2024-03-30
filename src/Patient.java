import java.awt.*;

public class Patient {
    int x,y;
    int Id;
    int vx;

    public Patient(int Id){
        this.Id = Id;
        this.x = 10;
        this.y = 10;
    }

    public int getId() {
        return Id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(){
        x += 2;
    }
    public Rectangle gethitbox(){
        return new Rectangle(x-5,y,20,35);
    }


    public void paint(Graphics g){
        g.setColor(Color.RED);
        // Draw the head
        g.drawOval(x, y, 10, 10); // reduced size
        // Draw the body
        g.drawLine(x + 5, y + 10, x + 5, y + 25); // reduced length
        // Draw the arms
        g.drawLine(x - 5, y + 15, x + 15, y + 15); // reduced length
        // Draw the legs
        g.drawLine(x + 5, y + 25, x - 5, y + 35); // reduced length
        g.drawLine(x + 5, y + 25, x + 15, y + 35); // reduced length
        g.drawRect(x-5, y, 20, 35);
        Font font = new Font("Arial", Font.PLAIN, 10); // Create new font with smaller size
        g.setFont(font); // Set the font
        g.drawString("P"+Id,x,y);
    }
}
