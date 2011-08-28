import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;


public class Game {
	private GameWindow gameWindow;
	private EntityRenderer entityRenderer;
	
	public static void main(String[] args) {
		new Game().play();
	}
	
	public Game() {
		gameWindow = new GameWindow(this);
		entityRenderer = new EntityRenderer(this);
	}
	
	public void play() {
		gameWindow.start();
	}

	public GameWindow getGameWindow() {
		return gameWindow;
	}

	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	public EntityRenderer getEntityRenderer() {
		return entityRenderer;
	}

	public void setEntityRenderer(EntityRenderer entityRenderer) {
		this.entityRenderer = entityRenderer;
	}
	
	public void updateTicks() {
		
	}
	
	public void checkKeyboard() {
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).getPosition().x -= 0.1f;
			System.out.println("Position is " + entities.get(0).getPosition());
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).getPosition().x += 0.1f;
			System.out.println("Position is " + entities.get(0).getPosition());
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).getPosition().y += 0.1f;
			System.out.println("Position is " + entities.get(0).getPosition());
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).getPosition().y -= 0.1f;
			System.out.println("Position is " + entities.get(0).getPosition());
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).getPosition().z += 0.1f;
			System.out.println("Position is " + entities.get(0).getPosition());
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).getPosition().z -= 0.1f;
			System.out.println("Position is " + entities.get(0).getPosition());
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).setRotation(entities.get(0).getRotation() + 0.5f);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).setRotation(entities.get(0).getRotation() - 0.5f);
		}
		
//		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
//			entityRenderer.moveCamera(new Vector(0.0f, 0.0f, 1.0f));
//		}
//		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
//			entityRenderer.moveCamera(new Vector(0.0f, 0.0f, -1.0f));
//		}
//		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			entityRenderer.rotateCamera(-1.15f);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			entityRenderer.rotateCamera(1.15f);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_N)){
			List<Entity> entities = entityRenderer.getEntities();
			entities.get(0).toggleDebug();
		}
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	gameWindow.setDisplayMode(800, 600, !Display.isFullscreen());
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	Display.setVSyncEnabled(gameWindow.toggleVsync());
		        }
		    }
		}
	}
}
