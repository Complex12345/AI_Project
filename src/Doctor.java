import java.awt.*;

public class Doctor {
    int Id;
    boolean isAvailable;
    int x,y;

    public Doctor(int Id){
        this.Id = Id;
        this.isAvailable = true;
        this.x = 0;
        this.y = 60;
    }

    public void paint(Graphics g){
        g.setColor(Color.BLUE);
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

        g.drawString("D"+Id,x,y);

    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
