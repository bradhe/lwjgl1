import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;


public class ObjEntity extends Entity {
	private List<Vector> vertexes;
	private List<Integer> indicies;
	private Map<Vector, ColorInfo> vertexColors;
	private int geometryType;
	
	public ObjEntity() {
		vertexes = new ArrayList<Vector>();
		indicies = new ArrayList<Integer>();
		vertexColors = new HashMap<Vector, ColorInfo>();
		geometryType = GL11.GL_TRIANGLES;
	}
	
	@Override
	protected void renderVertices() {
		GL11.glColor3f(0.8f, 0.8f, 0.8f);
		//GL11.glBegin(GL11.GL_POLYGON);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, BufferHelper.floatBuffer(new float[] { 1.0f, 0.0f, 0.0f, 1.0f }));
		
		Vector[] v = new Vector[vertexes.size()];
		vertexes.toArray(v);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glVertexPointer(3, 0, BufferHelper.floatBuffer(v));
		
		GL11.glDrawElements(geometryType, BufferHelper.intBuffer(indicies));
		//GL11.glDrawArrays(GL11.GL_POINTS, 0, v.length);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
	
	public List<Vector> getVertexList() {
		return vertexes;
	}
	
	public void setVertextList(List<Vector> vertices) {
		vertexes = vertices;
	}
	
	public Map<Vector, ColorInfo> getVertexColorMap() {
		return vertexColors;
	}
	
	public void setGeometryType(int geometryType) {
		this.geometryType = geometryType;
	}
	
	public void setIndexList(List<Integer> indices) {
		this.indicies = indices;
	}

	public static ObjEntity load(String fileName) throws FileNotFoundException {
		ObjEntity entity = new ObjEntity();
		List<Vector> verts = new ArrayList<Vector>();
		List<Integer> indices = new ArrayList<Integer>();
		Map<Vector, ColorInfo> vertColors = entity.getVertexColorMap();

		try {
			InputStream fis;
			fis = new FileInputStream(fileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
			
			String line;
			ColorInfo currentColorInfo = null;
						
			while((line = bufferedReader.readLine()) != null) {
				// Vertex
				if(line.startsWith("v ")) {
					String[] c = line.split(" ");
					
					// Don't do anything if there aren't enough components.
					if(c.length < 4) {
						continue;
					}
					
					Vector vertex = new Vector(Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
					if(verts.contains(vertex)) {
						continue;
					}
					verts.add(vertex);

					// If there is a color info, add it to this map.
					if(currentColorInfo != null) {
						vertColors.put(vertex, currentColorInfo);
						
						// Start over!
						currentColorInfo = null;
					}
				}
				else if(line.startsWith("Ka ")) {
					if(currentColorInfo == null) {
						currentColorInfo = new ColorInfo();
					}
					
					String[] c = line.split(" ");
					
					// Don't do anything if there aren't enough components.
					if(c.length < 4) {
						continue;
					}
					
					currentColorInfo.ambient = new Vector(Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
				}
				else if(line.startsWith("f ")) {
					String[] c = line.split(" ");
					
					if(c.length > 4) {
						// If it's greater than 4 (a quad) whine and continue.
						if(c.length > 5) {
							System.err.println("Error: More than 5 indices. Fix this. Indices: " + c.length);
							continue;
						}
						
						// Insert the first 3 indices
						indices.add(Integer.parseInt(c[1]) - 1);
						indices.add(Integer.parseInt(c[2]) - 1);
						indices.add(Integer.parseInt(c[3]) - 1);
						
						// then the next 3 indices
						indices.add(Integer.parseInt(c[1]) - 1);
						indices.add(Integer.parseInt(c[3]) - 1);
						indices.add(Integer.parseInt(c[4]) - 1);
					}
					else {
						for(int i = 1; i < c.length; i++) {
							indices.add(Integer.parseInt(c[i]) - 1);
						}	
					}
				}
			}
			
			// Set up vertex array.
			entity.setVertextList(verts);
			entity.setIndexList(indices);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileNotFoundException("Could not load model file " + fileName);
		}
		
		return entity;
	}
}
