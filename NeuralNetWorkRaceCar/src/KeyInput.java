import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//For test only.

public class KeyInput implements KeyListener{
	
	private Handler handler;
	private Game game;
	
	public KeyInput(Handler handler, Game game) {
		super();
		this.handler = handler;
		this.game = game;
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			Car car = (Car)handler.getGameObject(ID.Car);
			if(car!=null) {
				car.setAngleIcr(-0.10);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Car car = (Car)handler.getGameObject(ID.Car);
			if(car!=null) {
				car.setAngleIcr(+0.10);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			game.setRunning();
		}
		if(e.getKeyCode() == KeyEvent.VK_R) {
			game.setRunning();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			Car car = (Car)handler.getGameObject(ID.Car);
			if(car!=null) {
				car.setAngleIcr(0);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Car car = (Car)handler.getGameObject(ID.Car);
			if(car!=null) {
				car.setAngleIcr(0);
			}
		}
	}

}
