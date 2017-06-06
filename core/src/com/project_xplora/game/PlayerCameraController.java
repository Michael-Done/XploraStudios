
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.project_xplora.collision_util.CollisionShape;

/**
 * Takes a {@link Camera} instance and controls it via w,a,s,d and mouse
 * panning. Also controls all collisions with the player. /** The treasure chest
 * class. This class is used in all 3 of the levels in order to help organize
 * and animate the treasure chests.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * private int <b> *all private ints in theis class* </b> - The keys used to
 * controll the movement
 * <p>
 * private float <b> velocity </b> - The player speed
 * <p>
 * private float <b> degreesPerPixel </b> - The mouseSensitivity
 * <p>
 * final IntIntMap <b> keys </b> - The list of keys being pressed
 * <p>
 * private final Camera <b> camera </b> - The camera
 * <p>
 * private Array<CollisionShapes <b> collisions </b> - The walls
 * <p>
 * Time taken to complete: 3 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class PlayerCameraController extends InputAdapter {
	private final Camera camera;
	final IntIntMap keys = new IntIntMap();
	private int STRAFE_LEFT = Keys.A;
	private int STRAFE_RIGHT = Keys.D;
	private int FORWARD = Keys.W;
	private int BACKWARD = Keys.S;
	private int UP = Keys.Q;
	private int DOWN = Keys.E;
	private int PAUSE = Keys.ESCAPE;
	private float velocity = 5;
	private float degreesPerPixel = 0.5f;
	private final Vector3 tmp = new Vector3();
	private boolean lockedPosition = false;
	private Vector3 rayFrom = new Vector3();
	private Vector3 rayTo = new Vector3();
	private Vector2 lastUnstuckPosition;
	public boolean paused = false;

	private Array<CollisionShape> collisions;
	/**
	 * Contructs a new camera controller given the camera
	 * @param camera
	 */
	public PlayerCameraController(Camera camera) {
		this.camera = camera;
		collisions = new Array<CollisionShape>();
	}
	/**
	 * Contructs a new camera controller given the camera and settings
	 * @param camera
	 */
	public PlayerCameraController(Camera camera, Settings settings) {
		this(camera);
		updateSettings(settings);

	}
	/**
	 * updates the settings
	 * @param settings
	 */
	public void updateSettings(Settings settings) {
		FORWARD = settings.getForward();
		BACKWARD = settings.getBackward();
		STRAFE_LEFT = settings.getStrafe_left();
		STRAFE_RIGHT = settings.getStrafe_right();
		degreesPerPixel = (((float) settings.getMouseSens()) / 100) / 2;
		PAUSE = settings.getPause();
	}
	/**
	 * locks the position of the camera
	 */
	public void lockPosition() {
		lockedPosition = true;
	}
	/**
	 * unlocks the position of the camera
	 */
	public void unlockPosition() {
		lockedPosition = false;
	}
	@Override
	public boolean keyDown(int keycode) {
		keys.put(keycode, keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys.remove(keycode, 0);
		return true;
	}

	/**
	 * Sets the velocity in units per second for moving forward, backward and
	 * strafing left/right.
	 * 
	 * @param velocity
	 *            the velocity in units per second
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * Sets how many degrees to rotate per pixel the mouse moved.
	 * 
	 * @param degreesPerPixel
	 */
	public void setDegreesPerPixel(float degreesPerPixel) {
		this.degreesPerPixel = degreesPerPixel;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
		float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;
		camera.direction.rotate(camera.up, deltaX);
		tmp.set(camera.direction).crs(camera.up).nor();
		camera.direction.rotate(tmp, deltaY);
		return true;
	}
	/** 
	 * updates the camera controller
	 */
	public void update() {
		update(Gdx.graphics.getDeltaTime());
	}
	/** updates the camera controller */
	public void update(float deltaTime) {
		if (keys.containsKey(PAUSE) && !paused) {
			pause();
		}
		rayFrom.set(camera.position);
		rayTo.set(camera.position.x, camera.position.y, -20f);
		Vector2 old = new Vector2(camera.position.x, camera.position.y);
		if (!lockedPosition && !paused) {
			if (keys.containsKey(FORWARD)) {
				tmp.set(camera.direction).nor().scl(deltaTime * velocity);
				tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(BACKWARD)) {
				tmp.set(camera.direction).nor().scl(-deltaTime * velocity);
				tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(STRAFE_LEFT)) {
				tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity);
				tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(STRAFE_RIGHT)) {
				tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity);
				tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(UP)) {
				tmp.set(camera.up).nor().scl(deltaTime * velocity);
				tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(DOWN)) {
				tmp.set(camera.up).nor().scl(-deltaTime * velocity);
				tmp.z = 0;
				camera.position.add(tmp);
			}
		}
		Vector2 current = new Vector2(camera.position.x, camera.position.y);
		Vector2 newPos = new Vector2(current);
		for (CollisionShape i : collisions) {
			if (i.isInside(current)) {
				newPos = i.newPointCaclulation(newPos, old);
			}
		}
		if (newPos == null) {
			camera.position.set(lastUnstuckPosition, camera.position.z);
		} else {
			camera.position.set(newPos, camera.position.z);
			lastUnstuckPosition = old;
		}
		camera.update(true);
	}
	/**
	 * 
	 * @return the X Y angle of the camera
	 */
	public float getXYAngle() {
		float x = camera.direction.x;
		float y = camera.direction.y;
		float hypotenuse = (float) Math.sqrt((x * x) + (y * y));
		// System.out.println("(" + x + ", " + y + ", " + z + ") " +
		// hypotenuse);
		return (float) (Math.acos(y / hypotenuse) * (180 / Math.PI)) * Math.signum(camera.direction.x);
	}
	/**
	 * sets the z location of the camera
	 * @param z
	 */
	public void setZ(float z) {
		camera.position.z = z;
	}
	/**
	 * 
	 * @return the start of the collision ray
	 */
	public Vector3 getRayFrom() {
		return rayFrom;
	}
	/**
	 * 
	 * @return the end of the collision ray
	 */
	public Vector3 getRayTo() {
		return rayTo;
	}
	/**
	 * adds a new collision object to the world
	 * @param c
	 */
	public void addCollision(CollisionShape c) {
		collisions.add(c);
	}
	/**
	 * paused the camera controller
	 */
	public void pause() {
		paused = true;
	}
	/**
	 * unpauses the camera controller
	 */
	public void unPause() {
		paused = false;
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
