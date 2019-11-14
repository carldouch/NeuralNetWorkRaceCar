import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	private LinkedList<GameObject> list;
	
	public Handler() {
		list = new LinkedList();
	}
	
	public void render(Graphics g) {
		for(int i = 0; i<list.size();i++) {
			GameObject buffer = list.get(i);
			buffer.render(g);
		}
	}
	public void tick() {
		for (int i = 0; i < list.size(); i++) {
			GameObject buffer = list.get(i);
			if(buffer.id == ID.Car) {
				Car car = (Car)buffer;
				car.tick();
				if(car.isAlive == false) {
					System.out.println("Car removed");
					list.remove(i);
				}
			}
		}
	}

	//GETTER/ADDER/REMOVER
	public int getSize() {
		return this.list.size();
	}
	public LinkedList<GameObject> getList(){
		return this.list;
	}
	public GameObject getGameObject(ID id) {
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getId() == id) {
				return list.get(i);
			}
		}
		return null;
	}
	public void addObject(GameObject gameObject) {
		list.add(gameObject);
	}
	public void removeObject(GameObject gameObject) {
		list.remove(gameObject);
	}
}
