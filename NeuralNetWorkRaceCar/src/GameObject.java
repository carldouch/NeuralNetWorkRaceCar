import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
	public double x;
	public double y;
	public double rad;
	public ID id;
	public Rectangle bound;
	
	public GameObject(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.rad = 10;
		this.bound = new Rectangle((int)(x-rad/2), (int)(y-rad/2), (int)rad, (int)rad);
	}

	public  void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval((int)x-3, (int)y-3, 6, 6);
		g.setColor(Color.pink);
		g.drawRect(bound.x, bound.y, bound.width, bound.height);
		if(this.id == ID.Car) {
			g.setColor(Color.BLUE);
			g.drawOval((int)x-100, (int)y-100, 200, 200);
		}
	}
	
	//GETTER/SETTER
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public ID getId() {
		return id;
	}
	public void setId(ID id) {
		this.id = id;
	}
	public double getRad() {
		return rad;
	}
	public void setRad(double rad) {
		this.rad = rad;
	}
	public Rectangle getBound() {
		return bound;
	}
}	

