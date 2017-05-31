/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

/**
 * @author Michael
 *
 */
public class CollisionObjectController extends GameObjectController {
	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btCollisionWorld collisionWorld;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;
	btGhostPairCallback ghostPairCallback;

	/**
	 * @param settings
	 */
	public CollisionObjectController(Settings settings) {
		super(settings);
		initalize();
	}

	@Override
	public void initalize() {
		super.initalize();
		initalizeDynamics();		
	}

	public void initalizeDynamics() {
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		ghostPairCallback = new btGhostPairCallback();
		broadphase.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, 0, -10f));

		// Add it to the collision world
		dynamicsWorld.addCollisionObject(((DynamicCameraController)cameraController).ghostObject,
				(short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
				(short) (btBroadphaseProxy.CollisionFilterGroups.StaticFilter
						| btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
		dynamicsWorld.addAction(((DynamicCameraController)cameraController).characterController);

	}

	@Override
	public void update() {
		cameraController.update();
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
		dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

		for (ModelInstance obj : objects){
			try {
				((GameObject) obj).body.getWorldTransform(obj.transform);
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
		super.update();
		((DynamicCameraController)cameraController).ghostObjectGetWorldTransform();
	}

	public void camSetup() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		// camera = new PerspectiveCamera(70, screenWidth, screenHeight);
		float playerHeight = 3f;
		ProjectXploraGame.camera.position.set(0f, 0f, playerHeight);
		ProjectXploraGame.camera.lookAt(0f, 1f, playerHeight);
		ProjectXploraGame.camera.near = 0.1f;
		ProjectXploraGame.camera.far = 3000f;
		ProjectXploraGame.camera.update();
		cameraController = new DynamicCameraController(ProjectXploraGame.camera, settings);
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}

}
