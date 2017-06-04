/**
 *  XploraStudios
 *  Ms. Krasteva
 *  PlayerCameraController.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
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
 * panning.
 * 
 * @author Michael Done, CyrusGandevia
 */
public class PlayerCameraController extends InputAdapter {
	private final Camera camera;
	private final IntIntMap keys = new IntIntMap();
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

	public PlayerCameraController(Camera camera) {
		this.camera = camera;
		collisions = new Array<CollisionShape>();
	}

	public PlayerCameraController(Camera camera, Settings settings) {
		this(camera);
		updateSettings(settings);

	}

	public void updateSettings(Settings settings) {
		FORWARD = settings.getForward();
		BACKWARD = settings.getBackward();
		STRAFE_LEFT = settings.getStrafe_left();
		STRAFE_RIGHT = settings.getStrafe_right();
		degreesPerPixel = (((float) settings.getMouseSens()) / 100) / 2;
		PAUSE = settings.getPause();
	}

	public void lockPosition() {
		lockedPosition = true;
	}

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
		// camera.up.rotate(tmp, deltaY);
		return true;
	}

	public void update() {
		update(Gdx.graphics.getDeltaTime());
	}

	public void update(float deltaTime) {
		if (keys.containsKey(PAUSE) && !paused) {
			pause();
		}
		rayFrom.set(camera.position);
		rayTo.set(camera.position.x, camera.position.y, -20f);
		Vector2 old = new Vector2(camera.position.x, camera.position.y);
		if (!lockedPosition && !paused) {
			if (keys.containsKey(Keys.SHIFT_LEFT)) {
				velocity = 50;
			} else {
				velocity = 5;
			}
			if (keys.containsKey(FORWARD)) {
				tmp.set(camera.direction).nor().scl(deltaTime * velocity);
				// tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(BACKWARD)) {
				tmp.set(camera.direction).nor().scl(-deltaTime * velocity);
				// tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(STRAFE_LEFT)) {
				tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity);
				// tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(STRAFE_RIGHT)) {
				tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity);
				// tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(UP)) {
				tmp.set(camera.up).nor().scl(deltaTime * velocity);
				// tmp.z = 0;
				camera.position.add(tmp);
			}
			if (keys.containsKey(DOWN)) {
				tmp.set(camera.up).nor().scl(-deltaTime * velocity);
				// tmp.z = 0;
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

	public float getXYAngle() {
		float x = camera.direction.x;
		float y = camera.direction.y;
		float z = camera.direction.z;
		float hypotenuse = (float) Math.sqrt((x * x) + (y * y));
		// System.out.println("(" + x + ", " + y + ", " + z + ") " +
		// hypotenuse);
		return (float) (Math.acos(y / hypotenuse) * (180 / Math.PI)) * Math.signum(camera.direction.x);
	}

	public float getYZAngle() {
		float x = camera.direction.x;
		float y = camera.direction.y;
		float z = camera.direction.z;
		float hypotenuse = (float) Math.sqrt((z * z) + (y * y));
		// System.out.println("(" + x + ", " + y + ", " + z + ") " +
		// hypotenuse);
		return (float) (Math.acos(y / hypotenuse) * (180 / Math.PI)) * Math.signum(camera.direction.z);
	}

	public void setZ(float z) {
		camera.position.z = z;
	}

	public Vector3 getRayFrom() {
		return rayFrom;
	}

	public Vector3 getRayTo() {
		return rayTo;
	}

	public void addCollision(CollisionShape c) {
		collisions.add(c);
	}

	public void pause() {
		paused = true;
	}

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
