/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

/**
 * @author Michael
 *
 */
public class RomeScene extends CollisionObjectController {
	/**
	 * @param settings
	 */
	GameObject ground;
	GameObject box;

	public RomeScene(Settings settings) {
		super(settings);
	}

	@Override
	public void loadAssets() {
		assets = new AssetManager();
		assets.load("GroundPlane.g3db", Model.class);
		assets.load("Box.g3db", Model.class);
		assets.load("Ball.g3db", Model.class);
		assets.finishLoading();
	}

	@Override
	public void loadModelInstances() {
		ground = new GameObject(assets.get("GroundPlane.g3db", Model.class),
				new btBoxShape(new Vector3(50f, 50f, 0.1f)), 0f);
		objects.add(ground);
		dynamicsWorld.addRigidBody(ground.body, GROUND_FLAG, ALL_FLAG);

		box = new GameObject(assets.get("Box.g3db", Model.class), new btBoxShape(new Vector3(2f, 2f, 2f)), 0f);
		box.transform.rotate(1, 1, 0, 45);
		box.body.setWorldTransform(box.transform);
		objects.add(box);
		dynamicsWorld.addRigidBody(box.body, GROUND_FLAG, ALL_FLAG);

		for (int x = -10; x <= 10; x++) {
			for (int y = -10; y <= 10; y++) {
				GameObject obj = new GameObject(assets.get("Ball.g3db", Model.class), new btSphereShape(0.5f), 1f,
						new Vector3(x, y, 10f));
				obj.body.setWorldTransform(obj.transform);
				obj.body.setUserValue(objects.size);
				obj.body.setCollisionFlags(
						obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
				objects.add(obj);
				dynamicsWorld.addRigidBody(obj.body, OBJECT_FLAG, GROUND_FLAG);
			}
		}

	}
}
