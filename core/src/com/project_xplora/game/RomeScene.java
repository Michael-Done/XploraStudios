/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
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

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btCollisionWorld collisionWorld;

	public RomeScene(Settings settings) {
		super(settings);
		initalizeCollisionWorld();
		initalize();
	}

	@Override
	public void loadAssets() {
		assets = new AssetManager();
		assets.load("RomeGround.g3db", Model.class);
		assets.load("Plaza.g3db", Model.class);
		assets.load("RomanGardenfbx.g3db", Model.class);
		assets.load("RomanTemple.g3db", Model.class);
		assets.load("LongBuilding.g3db", Model.class);
		assets.load("Fountain.g3db", Model.class);
		assets.load("ColosseumLayer.g3db", Model.class);
		assets.finishLoading();
	}

	@Override
	public void loadModelInstances() {
		Model ground = assets.get("RomeGround.g3db", Model.class);
		Model plaza = assets.get("Plaza.g3db", Model.class);
		Model garden = assets.get("RomanGardenfbx.g3db", Model.class);
		Model temple = assets.get("RomanTemple.g3db", Model.class);
		Model longBuilding = assets.get("LongBuilding.g3db", Model.class);
		Model fountain = assets.get("Fountain.g3db", Model.class);
		Model colosseum = assets.get("ColosseumLayer.g3db", Model.class);
		// Ground
		objects.add(new GameObject(ground));
		// Gardens
		objects.add(new GameObject(garden, new Vector3(42.90559768676758f, 14.157180786132812f, 6.370274604705628e-06f),
				new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(56.441375732421875f, 14.157180786132812f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(56.441375732421875f, 33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(new GameObject(garden, new Vector3(42.90559768676758f, 33.502769470214844f, 6.258487701416016e-06f),
				new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-42.90559768676758f, 33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-56.441375732421875f, 33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-56.441375732421875f, 14.157180786132812f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-42.90559768676758f, 14.157180786132812f, 6.370274604705628e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-42.90559768676758f, -14.157180786132812f, 6.370274604705628e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-56.441375732421875f, -14.157180786132812f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-56.441375732421875f, -33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-42.90559768676758f, -33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(42.90559768676758f, -33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(56.441375732421875f, -33.502769470214844f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(56.441375732421875f, -14.157180786132812f, 6.258487701416016e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(42.90559768676758f, -14.157180786132812f, 6.370274604705628e-06f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(new GameObject(garden, new Vector3(38.59638214111328f, -4.376766204833984f, 2.927790774265304e-07f),
				new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(new GameObject(garden, new Vector3(38.59638214111328f, 4.376766204833984f, 2.927790774265304e-07f),
				new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(new GameObject(garden, new Vector3(-38.59638214111328f, 4.376766204833984f, 2.927790774265304e-07f),
				new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(
				new GameObject(garden, new Vector3(-38.59638214111328f, -4.376766204833984f, 2.927790774265304e-07f),
						new Vector3(0.0f, -0.0f, -89.999995674289f)));
		objects.add(new GameObject(garden, new Vector3(-28.008010864257812f, 12.059791564941406f, 0.0f),
				new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(new GameObject(garden, new Vector3(-28.008010864257812f, -12.059791564941406f, 0.0f),
				new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(new GameObject(garden, new Vector3(28.008010864257812f, -12.059791564941406f, 0.0f),
				new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(new GameObject(garden, new Vector3(28.008010864257812f, 12.059791564941406f, 0.0f),
				new Vector3(0.0f, 0.0f, 0.0f)));
		// Plazas
		objects.add(new GameObject(plaza, new Vector3(-46.04918670654297f, 23.829975128173828f, 1.25f),
				new Vector3(0.0f, -0.0f, -90.00000250447816f)));
		objects.add(new GameObject(plaza, new Vector3(-46.04918670654297f, -23.829975128173828f, 1.25f),
				new Vector3(0.0f, -0.0f, -90.00000250447816f)));
		objects.add(new GameObject(plaza, new Vector3(46.04918670654297f, -23.829975128173828f, 1.25f),
				new Vector3(0.0f, -0.0f, 90.00000250447816f)));
		objects.add(new GameObject(plaza, new Vector3(46.04918670654297f, 23.829975128173828f, 1.25f),
				new Vector3(0.0f, -0.0f, 90.00000250447816f)));
		// Fountains
		objects.add(
				new GameObject(fountain, new Vector3(-49.673484802246094f, 23.829975128173828f, 2.8255856037139893f),
						new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(new GameObject(fountain, new Vector3(49.673484802246094f, 23.829975128173828f, 2.8255856037139893f),
				new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(
				new GameObject(fountain, new Vector3(49.673484802246094f, -23.829975128173828f, 2.8255856037139893f),
						new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(
				new GameObject(fountain, new Vector3(-49.673484802246094f, -23.829975128173828f, 2.8255856037139893f),
						new Vector3(0.0f, 0.0f, 0.0f)));
		// Temples
		objects.add(new GameObject(temple, new Vector3(37.96327590942383f, 1.0471412679180503e-05f, 2.512864589691162f),
				new Vector3(0.0f, 0.0f, -359.999982697156f)));
		objects.add(
				new GameObject(temple, new Vector3(-37.96327590942383f, -7.152557373046875e-06f, 2.512864589691162f),
						new Vector3(0.0f, -0.0f, -179.999991348578f)));
		// Colosseum
		objects.add(
				new GameObject(colosseum, new Vector3(0.0f, 0.0f, 9.530657768249512f), new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(
				new GameObject(colosseum, new Vector3(0.0f, 0.0f, 6.453100204467773f), new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(
				new GameObject(colosseum, new Vector3(0.0f, 0.0f, 3.3507978916168213f), new Vector3(0.0f, 0.0f, 0.0f)));
		objects.add(new GameObject(colosseum, new Vector3(0.0f, 0.0f, 0.24637416005134583f),
				new Vector3(0.0f, 0.0f, 0.0f)));
		// Long buildings
		objects.add(
				new GameObject(longBuilding, new Vector3(0.03594597801566124f, -38.07497024536133f, 3.259446620941162f),
						new Vector3(0.0f, -0.0f, -90.00000250447816f)));
		objects.add(
				new GameObject(longBuilding, new Vector3(0.03594597801566124f, 38.07497024536133f, 3.259446620941162f),
						new Vector3(0.0f, -0.0f, 90.00000250447816f)));
	}

	public void initalizeCollisionWorld() {
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
	}

	@Override
	public void update() {
		super.update();

	}

	@Override
	public void updateCamera() {
		super.updateCamera();
		Vector3 intersectLocation = GroundCollisionDetector.rayTest(collisionWorld, cameraController.getRayFrom(),
				cameraController.getRayTo());
		if (intersectLocation != null) {
			cameraController.setZ(intersectLocation.z + 1);
		}
	}
}
