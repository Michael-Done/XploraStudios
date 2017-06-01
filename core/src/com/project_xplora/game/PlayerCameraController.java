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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.utils.IntIntMap;

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
	private float velocity = 5;
	private float degreesPerPixel = 0.5f;
	private final Vector3 tmp = new Vector3();
	private boolean lockedPosition = false;
	private Ray verticalSpring; // The ray used to keep the camera at a constant distance above ground

	public PlayerCameraController(Camera camera) {
		this.camera = camera;
	}

	public PlayerCameraController(Camera camera, Settings settings) {
		this.camera = camera;
		updateSettings(settings);

	}

	public void updateSettings(Settings settings) {
		FORWARD = settings.getForward();
		BACKWARD = settings.getBackward();
		STRAFE_LEFT = settings.getStrafe_left();
		STRAFE_RIGHT = settings.getStrafe_right();
		degreesPerPixel = (((float) settings.getMouseSens()) / 100) / 2;
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
		setVerticalSpring(new Ray(camera.position, new Vector3(camera.position.x, camera.position.y, -20)));
		if (!lockedPosition) {
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

	public Ray getVerticalSpring() {
		return verticalSpring;
	}

	public void setVerticalSpring(Ray verticalSpring) {
		this.verticalSpring = verticalSpring;
	}
}
