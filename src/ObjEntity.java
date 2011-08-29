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
	private List<Vertex> vertexes;
	private List<Integer> indicies;
	private Vector[] normals;
	private int geometryType;
	
	public ObjEntity() {
		vertexes = new ArrayList<Vertex>();
		geometryType = GL11.GL_TRIANGLES;
	}
	
	@Override
	protected void renderVertices() {
		GL11.glColor3f(1.0f, 1.0f, 0.0f);

		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, BufferHelper.floatBuffer(new float[] { 0.0f, 1.0f, 0.0f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, BufferHelper.floatBuffer(new float[] { 1.0f, 1.0f, 1.0f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, BufferHelper.floatBuffer(new float[] { 0.6f, 0.6f, 0.6f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, BufferHelper.floatBuffer(new float[] { 0.2f, 0.2f, 0.2f, 1.0f }));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, BufferHelper.floatBuffer(new float[] { 0.0f, 0.0f, 0.0f, 1.0f }));

		GL11.glInterleavedArrays(GL11.GL_C4F_N3F_V3F, 0, BufferHelper.floatBuffer(this.vertexes));
		GL11.glDrawElements(geometryType, BufferHelper.intBuffer(indicies));
		
		if(this.getDebug()) {
			// Lets draw all the normals so that we can see what they look like!
			for(Vertex v : this.vertexes) {
				GL11.glPushMatrix();

				GL11.glTranslatef(v.position.x, v.position.y, v.position.z);
				
				// Find the rotation we want.
				float dot = v.position.dot(v.normal);
				Vector axis = v.position.cross(v.normal);
				axis.normalize();

				float angle = (float)((180.0f * Math.acos(dot/v.position.length()))/Math.PI);
				//System.out.format("Angle: %.4f\n", angle);
				GL11.glRotatef(angle, axis.x, axis.y, axis.z);
				GL11.glBegin(GL11.GL_LINES);
				
				GL11.glColor3f(1, 0, 0);
				GL11.glVertex3f(0, 0, 0);
				GL11.glVertex3f(1, 0, 0);
				
				GL11.glColor3f(0, 1, 0);
				GL11.glVertex3f(0, 0, 0);
				GL11.glVertex3f(0, 1, 0);
				
				GL11.glColor3f(0, 0, 1);
				GL11.glVertex3f(0, 0, 0);
				GL11.glVertex3f(0, 0, 1);
				
				GL11.glEnd();
				GL11.glPopMatrix();
			}
		}
	}
	
	public List<Vertex> getVertexList() {
		return vertexes;
	}
	
	public void setVertextList(List<Vertex> vertices) {
		vertexes = vertices;
	}

	public void setGeometryType(int geometryType) {
		this.geometryType = geometryType;
	}
	
	public void setIndexList(List<Integer> indices) {
		this.indicies = indices;
	}

	public void computeVertexNormals(List<Integer[]> faces) {
		// Thread safe what?
		int[] counts = new int[vertexes.size()];
		
		for(Integer[] faceVertices : faces) {
			Vertex vert = vertexes.get(faceVertices[0]);
			
			Vector normal = vert.position.cross(vertexes.get(faceVertices[1]).position, vertexes.get(faceVertices[2]).position);

			vert.normal = vert.normal.add(normal);
			vertexes.get(faceVertices[1]).normal = vertexes.get(faceVertices[1]).normal.add(normal);
			vertexes.get(faceVertices[2]).normal = vertexes.get(faceVertices[2]).normal.add(normal);
			
			counts[faceVertices[0]] += 1;
			counts[faceVertices[1]] += 1;
			counts[faceVertices[1]] += 1;
		}
		
		// Now do the reduction.
		for(int i = 0; i < counts.length; i++) {
			Vertex vertex = vertexes.get(i);
			vertex.normal.divide(counts[i]);
			
			if(!GL11.glIsEnabled(GL11.GL_NORMALIZE)) {
				vertex.normal.normalize();
			}			
		}
		
		for(Vertex v : vertexes) {
			System.out.format("Position: %s, Normal: %s\n", v.position, v.normal);
		}
	}

	public static ObjEntity load(String fileName) throws FileNotFoundException {
		ObjEntity entity = new ObjEntity();
		List<Vertex> verts = new ArrayList<Vertex>();
		List<Integer> indices = new ArrayList<Integer>();

		// Save face data for later.
		List<Integer[]> faces = new ArrayList<Integer[]>();

		try {
			InputStream fis;
			fis = new FileInputStream(fileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
			
			String line;
//			ColorInfo currentColorInfo = null;
									
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
					verts.add(new Vertex(vertex));

					// If there is a color info, add it to this map.
//					if(currentColorInfo != null) {
//						//vertColors.put(vertex, currentColorInfo);
//						
//						// Start over!
//						currentColorInfo = null;
//					}
				}
//				else if(line.startsWith("Ka ")) {
//					if(currentColorInfo == null) {
//						currentColorInfo = new ColorInfo();
//					}
//					
//					String[] c = line.split(" ");
//					
//					// Don't do anything if there aren't enough components.
//					if(c.length < 4) {
//						continue;
//					}
//					
//					currentColorInfo.ambient = new Vector(Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
//				}
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
