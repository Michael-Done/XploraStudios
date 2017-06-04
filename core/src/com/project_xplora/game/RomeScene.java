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
import com.badlogic.gdx.math.Matrix4;
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
import com.badlogic.gdx.utils.Array;
import com.project_xplora.collision_util.CollisionCircle;
import com.project_xplora.collision_util.CollisionRect;
import com.project_xplora.collision_util.CollisionShape;
import com.project_xplora.game.BritishColombiaScene.GroundObjectData;

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

	private Array<GroundObjectData> groundObjDataList;

	public RomeScene(Settings settings) {
		super(settings);
		groundObjDataList = new Array<GroundObjectData>();
		initalizeGroundObjectData();
		initalizeCollisionWorld();
		initalize();
	}

	class GroundObjectData {
		/** The location of the groundObject */
		public Vector3 location;
		/**
		 * The axis that it is meant to rotate around will be set to 1. The
		 * others will be set to 0
		 */
		public Vector3 rotateAround;
		/** The rotation around the axis in degrees */
		public float rotation;
		/** The dimensions of the box */
		public Vector3 dimensions;

		public GroundObjectData(Vector3 location, Vector3 rotateAround, float rotation, Vector3 dimensions) {
			this.location = location;
			this.rotateAround = rotateAround;
			this.rotation = rotation;
			this.dimensions = dimensions;
		}

		public btCollisionObject construct() {
			btCollisionObject out = new btCollisionObject();
			btBoxShape shape = new btBoxShape(new Vector3(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2));
			out.setCollisionShape(shape);
			Matrix4 transform = new Matrix4();
			transform.setTranslation(location);
			transform.rotate(rotateAround.x, rotateAround.y, rotateAround.z, rotation);
			out.setWorldTransform(transform);
			return out;
		}

		public ModelInstance constructModel() {
			ModelBuilder mb = new ModelBuilder();
			mb.begin();
			mb.node().id = "ground";
			mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
					new Material(ColorAttribute.createDiffuse(Color.RED)))
					.box(dimensions.x, dimensions.y, dimensions.z);
			Model a = mb.end();
			ModelInstance testObj = new ModelInstance(a);
			testObj.transform.translate(location);
			testObj.transform.rotate(rotateAround, rotation);
			return testObj;
		}
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
		assets.load("ColosseumMiddle.g3db", Model.class);
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
		Model colosseumCenter = assets.get("ColosseumMiddle.g3db", Model.class);

        objects.add(new GameObject(colosseumCenter));
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

		for (GroundObjectData i : groundObjDataList) {
			collisionWorld.addCollisionObject(i.construct());
		}
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
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1f));
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
		cameraController.addCollision(new CollisionRect(new Vector2(-1, 3), new Vector2(1, 5)));
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
		initalizeCollisionShapes();
	}

	private void initalizeGroundObjectData() {
		groundObjDataList.add(new GroundObjectData(new Vector3(35.57417f, 0f, 1.1453f), new Vector3(0, 0, 0), 0f,
				new Vector3(3.757f, 5.173f, 0.17f)));
		groundObjDataList.add(new GroundObjectData(new Vector3(-35.57417f, 0f, 1.1453f), new Vector3(0, 0, 0), 0f,
				new Vector3(3.757f, 5.173f, 0.17f)));
		groundObjDataList.add(new GroundObjectData(new Vector3(32.42646f, 0f, 0.62864f), new Vector3(0, 1, 0), -21.721f,
				new Vector3(2.745f, 3.225f, 0.17f)));
		groundObjDataList.add(new GroundObjectData(new Vector3(-32.42646f, 0f, 0.62864f), new Vector3(0, 1, 0), 21.721f,
				new Vector3(2.745f, 3.225f, 0.17f)));
		groundObjDataList.add(new GroundObjectData(new Vector3(0f, 0f, 0f), new Vector3(0, 0, 0), 0f,
				new Vector3(145.056f, 104.201f, 0.242f)));
	}

	private void initalizeCollisionShapes() {
		cameraController.addCollision(new CollisionRect(new Vector2(-29.826101821899414f, 7.16373787689209f),
				new Vector2(-26.338101821899414f, 17.14773787689209f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-29.826101821899414f, -17.14773787689209f),
				new Vector2(-26.338101821899414f, -7.16373787689209f)));
		cameraController.addCollision(new CollisionRect(new Vector2(26.338101821899414f, -17.14773787689209f),
				new Vector2(29.826101821899414f, -7.16373787689209f)));
		cameraController.addCollision(new CollisionRect(new Vector2(26.338101821899414f, 7.16373787689209f),
				new Vector2(29.826101821899414f, 17.14773787689209f)));
		cameraController.addCollision(new CollisionRect(new Vector2(37.91359768676758f, 12.413180786132813f),
				new Vector2(47.897597686767575f, 15.901180786132812f)));
		cameraController.addCollision(new CollisionRect(new Vector2(51.44937573242188f, 12.413180786132813f),
				new Vector2(61.43337573242187f, 15.901180786132812f)));
		cameraController.addCollision(new CollisionRect(new Vector2(51.44937573242188f, 31.758769470214844f),
				new Vector2(61.43337573242187f, 35.24676947021484f)));
		cameraController.addCollision(new CollisionRect(new Vector2(37.91359768676758f, 31.758769470214844f),
				new Vector2(47.897597686767575f, 35.24676947021484f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-47.897597686767575f, 31.758769470214844f),
				new Vector2(-37.91359768676758f, 35.24676947021484f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-61.43337573242187f, 31.758769470214844f),
				new Vector2(-51.44937573242188f, 35.24676947021484f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-61.43337573242187f, 12.413180786132813f),
				new Vector2(-51.44937573242188f, 15.901180786132812f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-47.897597686767575f, 12.413180786132813f),
				new Vector2(-37.91359768676758f, 15.901180786132812f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-47.897597686767575f, -15.901180786132812f),
				new Vector2(-37.91359768676758f, -12.413180786132813f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-61.43337573242187f, -15.901180786132812f),
				new Vector2(-51.44937573242188f, -12.413180786132813f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-61.43337573242187f, -35.24676947021484f),
				new Vector2(-51.44937573242188f, -31.758769470214844f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-47.897597686767575f, -35.24676947021484f),
				new Vector2(-37.91359768676758f, -31.758769470214844f)));
		cameraController.addCollision(new CollisionRect(new Vector2(37.91359768676758f, -35.24676947021484f),
				new Vector2(47.897597686767575f, -31.758769470214844f)));
		cameraController.addCollision(new CollisionRect(new Vector2(51.44937573242188f, -35.24676947021484f),
				new Vector2(61.43337573242187f, -31.758769470214844f)));
		cameraController.addCollision(new CollisionRect(new Vector2(51.44937573242188f, -15.901180786132812f),
				new Vector2(61.43337573242187f, -12.413180786132813f)));
		cameraController.addCollision(new CollisionRect(new Vector2(37.91359768676758f, -15.901180786132812f),
				new Vector2(47.897597686767575f, -12.413180786132813f)));
		cameraController.addCollision(new CollisionRect(new Vector2(33.604382141113284f, -6.120766204833984f),
				new Vector2(43.58838214111328f, -2.6327662048339846f)));
		cameraController.addCollision(new CollisionRect(new Vector2(33.604382141113284f, 2.6327662048339846f),
				new Vector2(43.58838214111328f, 6.120766204833984f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-43.58838214111328f, 2.6327662048339846f),
				new Vector2(-33.604382141113284f, 6.120766204833984f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-43.58838214111328f, -6.120766204833984f),
				new Vector2(-33.604382141113284f, -2.6327662048339846f)));

		cameraController.addCollision(new CollisionCircle(new Vector2(34.128379821777344f, 0.0f), 0.362667977809906f));
		cameraController.addCollision(new CollisionCircle(new Vector2(-34.12837600708008f, 0.0f), 0.362667977809906f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(1.1591228246688843f, 15.122767448425293f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(3.403750419616699f, 14.775103569030762f), 0.4461558163166046f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(5.676482200622559f, 14.05940055847168f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(7.809022426605225f, 12.996475219726562f), 0.4461558163166046f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(9.74870491027832f, 11.612577438354492f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(11.447625160217285f, 9.941884994506836f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(12.863824844360352f, 8.025659561157227f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(13.96232795715332f, 5.911225318908691f), 0.4461558163166046f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(14.7160062789917f, 3.6508028507232666f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(15.106244087219238f, 1.3002169132232666f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(15.123406410217285f, -1.0824801921844482f), 0.4461558163166046f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(14.767067909240723f, -3.438443422317505f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(14.046028137207031f, -5.7094879150390625f), 0.4461558759212494f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(12.978096961975098f, -7.8395256996154785f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(11.58964729309082f, -9.775951385498047f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(9.914970397949219f, -11.470942497253418f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(7.995424747467041f, -12.882637977600098f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(5.878416538238525f, -13.97617244720459f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(3.616230010986328f, -14.724538803100586f), 0.4461558759212494f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(1.2647342681884766f, -15.109254837036133f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-1.1179962158203125f, -15.120820045471191f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-3.4731156826019287f, -14.758949279785156f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-5.742460250854492f, -14.032578468322754f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-7.869983673095703f, -12.959647178649902f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-9.803143501281738f, -11.56665325164795f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-11.494196891784668f, -9.887999534606934f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-12.901378631591797f, -7.965143203735352f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-13.989936828613281f, -5.845573425292969f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-14.732987403869629f, -3.5816359519958496f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-15.11217975616455f, -1.2292437553405762f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-15.118149757385254f, 1.1535067558288574f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-14.750747680664062f, 3.5077693462371826f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-14.019048690795898f, 5.7754011154174805f), 0.4461558163166046f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-12.941123962402344f, 7.900398254394531f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-11.543593406677246f, 9.830280303955078f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-9.86097240447998f, 11.517385482788086f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-7.934817314147949f, 12.92004680633545f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-5.812697410583496f, 14.003623962402344f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-3.5470218658447266f, 14.741355895996094f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-1.1937460899353027f, 15.115022659301758f), 0.446155846118927f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-49.673484802246094f, 23.829975128173828f), 4.645557880401611f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(49.673484802246094f, 23.829975128173828f), 4.645557880401611f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(49.673484802246094f, -23.829975128173828f), 4.645557880401611f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-49.673484802246094f, -23.829975128173828f), 4.645557880401611f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(7.38139192435483e-07f, 4.3903237383347005e-06f), 13.983842086791992f));
	}
}
