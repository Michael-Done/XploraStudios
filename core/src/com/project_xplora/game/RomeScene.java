/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
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
import com.project_xplora.collision_util.CollisionCircle;
import com.project_xplora.collision_util.CollisionRect;
import com.project_xplora.collision_util.CollisionShape;

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
		assets.load("SkyDome.g3db", Model.class);
		assets.load("Box.g3db", Model.class);
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
		Model sky = assets.get("SkyDome.g3db", Model.class);
		Model box = assets.get("Box.g3db", Model.class);

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ball";
		mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
		Model a = mb.end();
		CollisionShape c = new CollisionRect(new Vector2(-1, 3), new Vector2(1, 5));
		for (float i = -10; i <= 10; i += 0.2) {
			for (float z = -10; z <= 10; z += 0.2) {
				if (c.isInside(i, z)) {
					objects.add(new GameObject(a, new Vector3(i, z, 0.5f)));
				}
			}
		}
		// objects.add(new GameObject(box, new Vector3(0, 4, 0)));
		// Sky dome
		GameObject sky_inst = new GameObject(sky);
		sky_inst.transform.scale(10, 10, 10);
		objects.add(sky_inst);
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

	@Override
	public void environmentSetup() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 2f, 2f, 2f, 1f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -0.8f, -1f, -0.8f));
		environment.add(new DirectionalLight().set(0.1f, 0.1f, 0.05f, 0.8f, 1f, 0.8f));
		// environment.add(new PointLight().set(1f, 1f, 1f, new Vector3(0, 10,
		// 1), 1000f));
	}

	@Override
	public void camSetup() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		// camera = new PerspectiveCamera(70, screenWidth, screenHeight);
		float playerHeight = 3f;
		ProjectXploraGame.camera.position.set(0f, -30f, playerHeight);
		ProjectXploraGame.camera.lookAt(0f, 0f, playerHeight);
		ProjectXploraGame.camera.near = 0.1f;
		ProjectXploraGame.camera.far = 3000f;
		ProjectXploraGame.camera.update();
		cameraController = new PlayerCameraController(ProjectXploraGame.camera, settings);
		cameraController.addCollision( new CollisionRect(new Vector2(-1, 3), new Vector2(1, 5)));
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}
}
