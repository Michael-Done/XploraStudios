/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

/**
 * @author Michael
 *
 */
public class RomeScene extends GameObjectController {
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

	}
}
