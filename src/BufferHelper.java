import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class BufferHelper {
	public static FloatBuffer floatBuffer(float[] floats) {
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		return (FloatBuffer)temp.asFloatBuffer().put(new float[] {1.0f, 1.0f, 1.0f, 1.0f}).flip();
	}
}
