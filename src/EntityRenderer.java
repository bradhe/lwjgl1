import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


public class EntityRenderer {
	private Game game;
	private List<Entity> entities;
	private World world;
	
	public EntityRenderer(Game game) {
		this.game = game;
		this.world = new World();
		this.entities = new ArrayList<Entity>();
	}
	
	public void init() {
		//this.entities.add(new QuadEntity());
		try {
			//Entity entity = ObjEntity.load("C:\\Users\\brad.heller\\Desktop\\pyramid.obj");
			//Entity entity = ObjEntity.load("C:\\Users\\brad.heller\\Desktop\\untitled.obj");
			//Entity entity = ObjEntity.load("C:\\Users\\brad.heller\\Desktop\\ak47.obj");
			Entity entity = ObjEntity.load("C:\\Users\\brad.heller\\Desktop\\plane.obj");
			entity.changePosition(new Vector(0.0f, 0.0f, -75.0f));
			this.entities.add(entity);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setupPerspective();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, BufferHelper.floatBuffer(new float[] {1.0f, 0.0f, 0.2f, 1.0f}));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, BufferHelper.floatBuffer(new float[] {0.0f, 10.0f, 0.0f, 0.0f}));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, BufferHelper.floatBuffer(new float[] {1.0f, 0.0f, 0.0f, 1.0f}));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, BufferHelper.floatBuffer(new float[] { 1.0f, 0.0f, 0.0f, 1.0f }));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, BufferHelper.floatBuffer(new float[] { 1.0f, 1.0f, 1.0f, 0.0f }));
		GL11.glEnable(GL11.GL_LIGHT1);
		
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearDepth(1.0f);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	public void renderAll() {
		//setupPerspective();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glClearColor(0.3f, 0.3f, 0.3f, 0.3f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		
		this.world.render();
		
		Iterator<Entity> it = this.entities.iterator();
		while(it.hasNext()) {
			Entity entity = it.next();
			entity.render();
		}
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	private void setupPerspective() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glViewport(0, 0, game.getGameWindow().width(), game.getGameWindow().height());
		GL11.glLoadIdentity();
				
		float aspect = (float)game.getGameWindow().width()/(float)game.getGameWindow().height();
		GLU.gluPerspective(45.0f, aspect, 1.0f, 1000.0f);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		//GL11.glOrtho(0, game.getGameWindow().width(), game.getGameWindow().height(), 0, -1.0f, 1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);	
	}
}
