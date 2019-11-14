import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Car extends GameObject{
	
	public static final int FOVDIVISIONS = 8;
	public static final int FIRSTSTAGESIZE = FOVDIVISIONS * 2;
	public static final int MAXIMUMSPEED = 1;
	public static final double maximumSightDistance = 100;
	public static final double maximumAngularSpeed = Math.PI / 32d;
	public static final double fieldOfView = Math.PI * 2 / 3;
	public static final boolean isNNSymmetric = false;
	public static final int stageSizes[] = new int[] { FIRSTSTAGESIZE, 16, 16, 2 };
	
	public NeuralNet brainNet;
	
	private double vx;
	private double vy;
	public boolean isAlive;
	public double angle;
	public Handler handler; 
	
	//TODO : ADD DNA CLASS
	//TODO remove that angleIncr
	private double angleIncr;

	public Car(int x, int y, Handler handler) {
		super(x,y);
		this.x = Math.random() * (400 - 200);
		this.y = Math.random() * (400 - 300);
		this.id = ID.Car;
		this.isAlive = true;
		this.angle = Math.atan2(640 / 2 - y, (640/16*9) / 2 - x);
		this.handler = handler;
		this.angleIncr = 0;
		
		
		brainNet = new NeuralNet(stageSizes);
	}

	public void tick() {
		
		//TJ = à 0
		double angleIncr = thinking();
		System.out.println(angleIncr);
		angle+=angleIncr;
		angle = MathUtil.doubleModulo(angle, Math.PI * 2);
		
		//double rad = Math.toRadians(angle);
		vx = MAXIMUMSPEED * Math.cos(angle);
		vy = MAXIMUMSPEED * Math.sin(angle);

		x+=vx;
		y+=vy;
		
		x = Game.clamp(x, rad/2, Game.WIDTH-(rad/2));
		y = Game.clamp(y, rad/2, Game.HEIGHT-(rad/2));

		updateBound();
		testObstacleCollision();
	}
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.drawOval((int)(x-rad/2), (int)(y-rad/2), (int)rad, (int)rad);
		g.setColor(Color.BLUE);
		g.drawLine((int)x, (int)y, (int)(x + 100*vx),(int) (y + 100*vy));
		for(int i = 20; i<=80 ; i+=20) {
			g.setColor(Color.GREEN);
			g.drawLine((int)x, (int)y, (int)(x + 100*(MAXIMUMSPEED*Math.cos(angle+Math.toRadians(i)))),(int) (y + 100*(MAXIMUMSPEED*Math.sin(angle+Math.toRadians(i)))));
			g.setColor(Color.RED);
			g.drawLine((int)x, (int)y, (int)(x + 100*(MAXIMUMSPEED*Math.cos(angle+Math.toRadians(-i)))),(int) (y + 100*(MAXIMUMSPEED*Math.sin(angle+Math.toRadians(-i)))));
		}
		super.render(g);
	}
	
	private double thinking() {
		//
		Double input[] = new Double[FOVDIVISIONS*2];
		for (int i = 0; i < input.length; i++) {
			input[i] = maximumSightDistance;
		}
		input = updateVisionInput(input, handler.getList());
		double stageA[] = new double[FIRSTSTAGESIZE]; 
		
		if (isNNSymmetric) {
			//AMONAVIS : for (int i = 0; i < FOVDIVISIONS/2; i++) {
			for (int i = 0; i < FOVDIVISIONS; i++) {
				stageA[i] 						= Stage.signalMultiplier * (maximumSightDistance - input[i]) / maximumSightDistance;
				stageA[FIRSTSTAGESIZE - 1 - i] 	= Stage.signalMultiplier * (maximumSightDistance - input[i + FOVDIVISIONS]) / maximumSightDistance;
			}
		} else {
			//AMONAVIS : for (int i = 0; i < FOVDIVISIONS/2; i++) {
			for (int i = 0; i < FOVDIVISIONS; i++) {
				stageA[i] 						= Stage.signalMultiplier * (maximumSightDistance - input[i]) / maximumSightDistance;
				stageA[FIRSTSTAGESIZE - 1 - i] 	= Stage.signalMultiplier * (maximumSightDistance - input[i + FOVDIVISIONS]) / maximumSightDistance;
			}
		}
		double output[] = brainNet.calc(stageA);
		double delta = output[0] - output[1];
		double angleIncrement = 10 * maximumAngularSpeed / Stage.signalMultiplier * delta;
		if (angleIncrement > maximumAngularSpeed)
			angleIncrement = maximumAngularSpeed;
		if (angleIncrement < -maximumAngularSpeed)
			angleIncrement = -maximumAngularSpeed;
		
		return angleIncrement;
	}
	private Double[] updateVisionInput(Double input[], List<GameObject> listGameObject) {
		for(GameObject go : listGameObject) {
			if(go.id == ID.Obstacle) {
				double calcAngle = MathUtil.signedDoubleModulo(getAngle(go)-angle, Math.PI*2);
				double d = getDistanceTo(go);
				if ( (calcAngle >= 0) && (calcAngle < fieldOfView) ) {
					if (d < input[(int) (calcAngle * FOVDIVISIONS / fieldOfView)]) {
//						System.err.println("OBJECT DETECTED DROITE");
						input[(int) (calcAngle * FOVDIVISIONS / fieldOfView)] = d;
					}
				}else if ( (calcAngle <= 0) && (-calcAngle < fieldOfView) ) {
					if (d < input[(int) (-calcAngle * FOVDIVISIONS / fieldOfView) + FOVDIVISIONS]) {
//						System.err.println("OBJECT DETECTED GAUCHE");
						input[(int) (-calcAngle * FOVDIVISIONS / fieldOfView) + FOVDIVISIONS] = d;
					}
				}
			}
			
		}
		return input;
	}
	
	public void testObstacleCollision() {
		for (int i = 0; i < handler.getList().size(); i++) {
			GameObject buffer = handler.getList().get(i);
			if(buffer.getId()==ID.Obstacle) {
				if(this.bound.intersects(buffer.getBound())){
					System.err.println("collision");
					setAlive(false);
				}
			}
		}
	}
	public double getAngle(GameObject go) {
		double diffx = go.x - this.x;
		double diffy = go.y - this.y;
		return Math.atan2(diffy,diffx);
	}
	public double getDistanceTo(GameObject go) {
//		System.err.println(Math.sqrt((this.x - go.x) * (this.x - go.x) + (this.y - go.y) * (this.y - go.y)) - (this.rad - go.rad) / 2);
		return Math.sqrt((this.x - go.x) * (this.x - go.x) + (this.y - go.y) * (this.y - go.y)) - (this.rad - go.rad) / 2;
	}
	public void setAlive(boolean bool) {
		this.isAlive = bool;
	}
	public void updateBound() {
		bound.x = (int)(this.x-rad/2);
		bound.y = (int)(this.y-rad/2);
	}
	public void setAngleIcr(double in) {
		this.angleIncr = in;
	}
}
