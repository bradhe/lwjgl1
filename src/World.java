import org.lwjgl.opengl.GL11;


public class World {
	private Texture texture;
	
	public World() {
		
	}
	
	public Texture getTexture() {
		if(texture == null) {
			texture = Texture.load("grass.jpg");
		}
		
		return texture;
	}
	
	public void render() {
		GL11.glPushMatrix();
		
		//getTexture().bind();
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, BufferHelper.floatBuffer(new float[] { 1.0f, 0.0f, 0.0f, 1.0f }));
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(-1000.0f, -10.0f, -1000.0f);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1000.0f, -10.0f, -1000.0f);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1000.0f, -10.0f, -1.0f);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-1000.0f, -10.0f, -1.0f);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glEnd();		
		
		GL11.glPopMatrix();
	}
}
