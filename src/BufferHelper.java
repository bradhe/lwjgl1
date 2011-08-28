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
	
	public static FloatBuffer floatBuffer(List<Vertex> verts) {
		// 4 color, 3 normal, 3 position
		float[] floats = new float[verts.size() * 10];
		int offset = 0;
		for(Vertex v : verts) {
			floats[offset] = ((float)v.color.getRed()/255.0f);
			floats[offset+1] = ((float)v.color.getGreen()/255.0f);
			floats[offset+2] = ((float)v.color.getBlue()/255.0f);
			floats[offset+3] = ((float)v.color.getAlpha()/255.0f);
			floats[offset+4] = v.normal.x;
			floats[offset+5] = v.normal.y;
			floats[offset+6] = v.normal.z;
			floats[offset+7] = v.position.x;
			floats[offset+8] = v.position.y;
			floats[offset+9] = v.position.z;
			
			offset += 10;
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
