import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{

	public static final int WIDTH = 640, HEIGHT = WIDTH/16*9;
	
	private Boolean running = false;
	private Thread thread;
	private Handler handler;
	
	public synchronized void start() {
		//System.err.println("Class Game : Start function");
		thread = new Thread(this);
		running = true;
		thread.start();
		
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		//System.err.println("Class Game : Run function");
		long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;
	        while(delta >=1)
	            {
	                tick();
	                delta--;
	            }
	            if(running) {
	            	render();
	            }
	            frames++;
	            
	            if(System.currentTimeMillis() - timer > 1000)
	            {
	                timer += 1000;
	                //System.out.println("FPS: "+ frames);
	                frames = 0;
	            }
        }
        stop();
	}
	public void tick() {
		handler.tick();
	}
	public void render() {
		//System.err.println("Class Game : Render function");
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bufferStrategy.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		g.dispose();
		bufferStrategy.show();
	}
	
	public static double clamp(double val, double min, double max) {
		if(val>max) {
			System.err.println("YEET WALL");
			return max;
		}
		if(val<min) {
			System.err.println("YEET WALL");
			return min;
		}
		else return val;
	}
	
	//CONSTRUCTOR
	public Game() {
		
		handler = new Handler();
		mapCreator(handler);
		handler.addObject(new Car(600, HEIGHT/2, handler));

		KeyInput keyInput = new KeyInput(handler,this);
		this.addKeyListener(keyInput);
		Window window = new Window(WIDTH, HEIGHT, "Race Car",this);
	}
	
	public void mapCreator(Handler handler) {
		
		handler.addObject(new Obstacle(100, 100, 10));
		
		for(int i=0;i<WIDTH;i+=10) {
			handler.addObject(new Obstacle(i, 0, 10));
			handler.addObject(new Obstacle(i, HEIGHT-30, 10));
			if(i<HEIGHT-30) {
				handler.addObject(new Obstacle(0, i, 10));
				handler.addObject(new Obstacle(WIDTH, i, 10));
			}
		}
	}
	public void setRunning() {
		running = !running;
		if(running) this.start();
		else this.stop();
	}
	//Main Method
	public static void main(String[] args) {
		Game game = new Game();
	}
}
