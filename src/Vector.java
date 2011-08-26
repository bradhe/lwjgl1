
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
	
	public Vector cross(Vector v) {
		Vector u = this; // convenience
		return new Vector((u.y * v.z) - (u.z * v.y), (u.z * v.x) - (u.x * v.z), (u.x * v.y) - (u.y * v.x));
	}
	
	public Vector cross(Vector v1, Vector v2) {
		// Consider this vector the base
		Vector u = subtract(v1);
		Vector v = subtract(v2);
		return u.cross(v);
	}
	
	public Vector subtract(Vector v1) {
		return new Vector(v1.x - x, v1.y - y, v1.z - z);
	}
	
	public Vector add(Vector v) {
		return new Vector((x+v.x), (y+v.y), (z+v.z));
	}
	
	public Vector average(Vector v) {
		return new Vector((x*v.x)/2.0f, (y*v.y)/2.0f, (z*v.z)/2.0f);
	}
	
	public void normalize() {
		float len = (float)length();
		//System.out.println("Len ("+x+"): " +" (" + (x * x) + ") " + Math.sqrt((x * x) + (y * y) + (z * z)));
		x = x / len;
		y = y / len;
		z = z / len;
	}
	
	public double length() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
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
	
	public static Vector average(Vector[] vectors) {
		float x = 0.0f;
		float y = 0.0f;
		float z = 0.0f;
		
		for(Vector v : vectors) {
			x += v.x;
			y += v.y;
			z += v.z;
		}
		
		x /= vectors.length;
		y /= vectors.length;
		z /= vectors.length;
		
		return new Vector(x, y, z);
	}
}
