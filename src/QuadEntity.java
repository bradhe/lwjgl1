import org.lwjgl.opengl.GL11;


public class QuadEntity extends Entity {

	@Override
	public void renderVertices() {
		GL11.glColor3f(0.5f, 0.5f, 0.5f);
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(100,100);
		    GL11.glVertex2f(100+200,100);
		    GL11.glVertex2f(100+200,100+200);
		    GL11.glVertex2f(100,100+200);
		GL11.glEnd();
	}
}
