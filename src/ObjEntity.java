import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

//import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;


public class ObjEntity extends Entity {
	private List<Vector> vertexes;
	private List<Integer> indicies;
	private Map<Vector, ColorInfo> vertexColors;
	private Vector[] normals;
	private int geometryType;
	
	public ObjEntity() {
		vertexes = new ArrayList<Vector>();
		indicies = new ArrayList<Integer>();
		vertexColors = new HashMap<Vector, ColorInfo>();
		geometryType = GL11.GL_TRIANGLES;
	}
	
	@Override
	protected void renderVertices() {
		GL11.glColor3f(1.0f, 1.0f, 0.0f);

		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, BufferHelper.floatBuffer(new float[] { 0.0f, 1.0f, 0.0f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, BufferHelper.floatBuffer(new float[] { 1.0f, 1.0f,0.0f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, BufferHelper.floatBuffer(new float[] { 1.0f, 1.0f, 0.0f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, BufferHelper.floatBuffer(new float[] { 0.0f, 0.0f, 0.0f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, BufferHelper.floatBuffer(new float[] { 1.0f, 1.0f, 1.0f, 1.0f }));
//		
		Vector[] verts = new Vector[vertexes.size()];
		vertexes.toArray(verts);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		
		GL11.glVertexPointer(3, 0, BufferHelper.floatBuffer(verts));
		GL11.glNormalPointer(3, BufferHelper.floatBuffer(normals));
		GL11.glDrawElements(geometryType, BufferHelper.intBuffer(indicies));
		
		if(this.getDebug()) {
			// Lets draw all the normals so that we can see what they look like!
			for(int i = 0; i < verts.length; i++) {
				GL11.glBegin(GL11.GL_LINES);
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				GL11.glVertex3f(verts[i].x, verts[i].y, verts[i].z);
				GL11.glVertex3f((verts[i].x + normals[i].x), (verts[i].y + normals[i].y), (verts[i].z + normals[i].z));
				GL11.glEnd();
			}
		}
		
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
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

	public void computeVertexNormals(List<Integer[]> faces) {
		// For each triangulated face computer the normal for that face. Then
		// set each vert's normal to that face. If the vert already has a normal
		// then take the difference of that normal.
		Map<Vector, List<Vector>> normalsMap = new Hashtable<Vector, List<Vector>>();
		
		for(Integer[] faceVertices : faces) {
			Vector startVector = vertexes.get(faceVertices[0]);
			
			Vector normal = startVector.cross(vertexes.get(faceVertices[1]), vertexes.get(faceVertices[2]));
			normal.normalize();

			updateNormal(startVector, normal, normalsMap);
			updateNormal(vertexes.get(faceVertices[1]), normal, normalsMap);
			updateNormal(vertexes.get(faceVertices[2]), normal, normalsMap);
		}
		
		// Create a new list of normals for each vertice
		normals = new Vector[vertexes.size()];
		for(int i = 0; i < vertexes.size(); i++) {
			List<Vector> allNormals = normalsMap.get(vertexes.get(i));
			
			Vector[] vectorArray = new Vector[allNormals.size()];
			allNormals.toArray(vectorArray);
			
			Vector avg = Vector.average(vectorArray);
			System.out.println("Average normal vector: " + avg);
			
			avg.normalize();
			System.out.println("Normal: " + avg);
			normals[i] = avg;  
		}
	}
	
	private void updateNormal(Vector startVector, Vector normal, Map<Vector, List<Vector>> normalsMap) {
		if(normalsMap.containsKey(startVector)) {
			// Average these two vectors
			normalsMap.get(startVector).add(normal);
		}
		else {
			List<Vector> list = new ArrayList<Vector>();
			list.add(normal);
			
			normalsMap.put(startVector, list);
		}
	}

	public static ObjEntity load(String fileName) throws FileNotFoundException {
		ObjEntity entity = new ObjEntity();
		List<Vector> verts = new ArrayList<Vector>();
		List<Integer> indices = new ArrayList<Integer>();
		Map<Vector, ColorInfo> vertColors = entity.getVertexColorMap();
		

		// Save face data for later.
		List<Integer[]> faces = new ArrayList<Integer[]>();

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
						Queue<Integer> indexQueue = new LinkedList<Integer>();
						
						// The first index will always be in the queue.
						indexQueue.add(Integer.parseInt(c[1]) - 1);
						
						// Next, for each other element in the queue...
						for(int i = 2; i < c.length; i++) {
							// Add an element to the queue, then operate on it.
							indexQueue.add(Integer.parseInt(c[i]) - 1);
							
							// If it is three, take the three elements off the queue.
							// Then put the first and the last back on the queue.
							if(indexQueue.size() == 3) {
								Integer first = indexQueue.remove();
								indices.add(first);
								
								Integer middle = indexQueue.remove();
								indices.add(middle);
								
								Integer last = indexQueue.remove();
								indices.add(last);
								
								indexQueue.add(first);
								indexQueue.add(last);
								
								faces.add(new Integer[] { first, middle, last });
							}
						}
					}
					else {
						Integer[] currentFace = new Integer[3];
						for(int i = 1; i < c.length; i++) {
							if(c[i].indexOf('/') >= 0) {
								c[i] = c[i].substring(0, c[i].indexOf('/'));
							}
							
							currentFace[i-1] = Integer.parseInt(c[i]) - 1; 
							indices.add(currentFace[i-1]);
						}	
						
						faces.add(currentFace);
					}
				}
			}
			
			// Set up vertex array.
			entity.setVertextList(verts);
			entity.setIndexList(indices);
			entity.computeVertexNormals(faces);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileNotFoundException("Could not load model file " + fileName);
		}
		
		return entity;
	}
}
