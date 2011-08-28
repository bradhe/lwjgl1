import java.awt.Color;


public class Vertex {
	public Vector position;
	public Vector normal;
	public Color color;
	
	public Vertex() {
		position = new Vector();
		normal = new Vector();
		color = Color.gray;
	}
	
	public Vertex(Vector position) {
		this.position = position;
		normal = new Vector();
		color = Color.gray;
	}
	
	public Vertex(Vector position, Vector normal) {
		this.position = position;
		this.normal = normal;
		color = Color.gray;
	}
}
