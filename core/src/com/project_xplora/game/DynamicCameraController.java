package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.utils.IntIntMap;

public class DynamicCameraController extends PlayerCameraController {
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

	btPairCachingGhostObject ghostObject;
	btConvexShape ghostShape;
	btKinematicCharacterController characterController;
	Matrix4 characterTransform;
	Vector3 characterDirection = new Vector3();
	Vector3 walkDirection = new Vector3();
	AssetManager assets;
	Model ballModel;
	ModelInstance ball;

	public DynamicCameraController(Camera camera, Settings settings) {
		super(camera, settings);
		// Create the physics representation of the character
		ghostObject = new btPairCachingGhostObject();
		ghostObject.setWorldTransform(characterTransform);
		ghostShape = new btCapsuleShape(2f, 2f);
		ghostObject.setCollisionShape(ghostShape);
		ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
		characterController = new btKinematicCharacterController(ghostObject, ghostShape, .35f);

		characterTransform = ghostObject.getWorldTransform();

		assets = new AssetManager();
		assets.load("Ball.g3db", Model.class);
		assets.finishLoading();

		ballModel = assets.get("Ball.g3db", Model.class);
		ball = new ModelInstance(ballModel);
		ball.transform.translate(0, 0, 5);

	}

	@Override
	public void update(float deltaTime) {
		if (!lockedPosition) {
			if (keys.containsKey(Keys.SHIFT_LEFT)) {
				velocity = 50;
			} else {
				velocity = 5;
			}

			if (keys.containsKey(STRAFE_LEFT)) {
				characterTransform.rotate(0, 1, 0, 5f);
				ghostObject.setWorldTransform(characterTransform);
			}
			if (keys.containsKey(STRAFE_RIGHT)) {
				characterTransform.rotate(0, 1, 0, -5f);
				ghostObject.setWorldTransform(characterTransform);
			}
			// Fetch which direction the character is facing now
			characterDirection.set(-1, 0, 0).rot(characterTransform).nor();
			// Set the walking direction accordingly (either forward or
			// backward)
			walkDirection.set(0, 0, 0);

			if (keys.containsKey(FORWARD)) {
				walkDirection.add(characterDirection);
			}
			if (keys.containsKey(BACKWARD)) {
				walkDirection.add(-characterDirection.x, -characterDirection.y, -characterDirection.z);
			}
			walkDirection.scl(4f * Gdx.graphics.getDeltaTime());
			// And update the character controller
			characterController.setWalkDirection(walkDirection);
			// Now we can update the world as normally

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

	public void ghostObjectGetWorldTransform() {
		// And fetch the new transformation of the character (this will make the
		// model be rendered correctly)
		ghostObject.getWorldTransform(characterTransform);
	}
}
