import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class BufferHelper {
	public static FloatBuffer floatBuffer(float[] floats) {
		ByteBuffer temp = ByteBuffer.allocateDirect(floats.length * 4);
		temp.order(ByteOrder.nativeOrder());
		return (FloatBuffer)temp.asFloatBuffer().put(floats).flip();
	}
	
	public static FloatBuffer floatBuffer(List<Float> floats) {
		float[] realFloats = new float[floats.size()];
		for(int i = 0; i < floats.size(); i++) {
			realFloats[i] = floats.get(i).floatValue();
		}
		
		return floatBuffer(realFloats);
	}
	
	public static FloatBuffer floatBuffer(Vector[] verts) {
		List<Float> floats = new ArrayList<Float>();
		for(Vector v : verts) {
			floats.add(v.x);
			floats.add(v.y);
			floats.add(v.z);
		}
		
		return floatBuffer(floats);
	}
	
	public static IntBuffer intBuffer(int[] ints) {
		ByteBuffer temp = ByteBuffer.allocateDirect(ints.length * 4);
		temp.order(ByteOrder.nativeOrder());
		return (IntBuffer)temp.asIntBuffer().put(ints).flip();
	}
	
	public static IntBuffer intBuffer(List<Integer> ints) {
		int[] integers = new int[ints.size()];
		for(int i = 0; i < ints.size(); i++) {
			integers[i] = ints.get(i).intValue();
		}
		
		return intBuffer(integers);
	}
}
