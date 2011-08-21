
public class Vector {
	public float x;
	public float y;
	public float z;
	
	public Vector() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
	
	public boolean equals(Object obj) {
		if(obj.getClass().equals(Vector.class)) {
			Vector v = (Vector)obj;
			return (v.x == x) && (v.y == y) && (v.z == z); 
		}
		else {
			return super.equals(obj);
		}
	}
}
