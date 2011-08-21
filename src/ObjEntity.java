import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;


public class ObjEntity extends Entity {
	private List<Vector> vertexes;
	private Map<Vector, ColorInfo> vertexColors;
	
	public ObjEntity() {
		vertexes = new ArrayList<Vector>();
		vertexColors = new HashMap<Vector, ColorInfo>();
	}
	
	@Override
	protected void renderVertices() {
		GL11.glColor3f(0.8f, 0.8f, 0.8f);
		//GL11.glBegin(GL11.GL_POLYGON);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, BufferHelper.floatBuffer(new float[] { 1.0f, 0.0f, 0.0f, 1.0f }));
		GL11.glBegin(GL11.GL_POINTS);

		Iterator<Vector> it = vertexes.iterator();
		while(it.hasNext()) {
			Vector v = it.next();
			
			// Set the color if there is one to set.
			if(vertexColors.containsKey(v)) {
				ColorInfo vertexColor = vertexColors.get(v);
				GL11.glColor3f(vertexColor.ambient.x, vertexColor.ambient.y, vertexColor.ambient.z);
			}
					
			GL11.glVertex3f(v.x, v.y, v.z);
		}

		GL11.glEnd();
	}
	
	public List<Vector> getVertexList() {
		return vertexes;
	}
	
	public Map<Vector, ColorInfo> getVertexColorMap() {
		return vertexColors;
	}

	public static ObjEntity load(String fileName) throws FileNotFoundException {
		ObjEntity entity = new ObjEntity();
		List<Vector> verts = entity.getVertexList();
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
						System.err.println("Duplicate detected! " + vertex);
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
			}
			
			// Set up vertex array.
		} catch (Exception e) {
			throw new FileNotFoundException("Could not load model file " + fileName);
		}
		
		return entity;
	}
}
