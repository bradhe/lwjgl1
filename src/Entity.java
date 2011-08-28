import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;


public abstract class Entity {
	private float rotation;
	private float scale;
	private Vector positionVector;
	private boolean debug;
	
	public Entity() {
		scale = 1.0f;
		rotation = 0.0f;
		positionVector = new Vector();
		debug = true;
	}
	
	public void render() {
		GL11.glPushMatrix();
		applyTransforms();
		renderVertices();
		GL11.glPopMatrix();
	}
	
	public Vector getPosition() {
		return positionVector;
	}
	
	public void scale(float scale) {
		if(scale < 0.25f) {
			scale = 0.25f;
		}
		
		System.out.println("Setting scale to " + scale);
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void changePosition(Vector newPosition) {
		positionVector = newPosition;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public float getRotation() {
		return this.rotation;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean getDebug() {
		return this.debug;
	}
	
	public void toggleDebug() {
		this.debug = !this.debug;
	}
	
	protected void applyTransforms() {
		GL11.glLoadIdentity();
		GL11.glTranslatef(positionVector.x, positionVector.y, positionVector.z);
		GL11.glRotatef(rotation, 0f, 1f, 0f);
		GL11.glScalef(scale, scale, scale);
	}
	
	// Abstract stuff
	protected abstract void renderVertices();
}