package com.project_xplora.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.project_xplora.collision_util.CollisionCircle;
import com.project_xplora.collision_util.CollisionRect;

/**
 * The EuropeScene class sets up Project Xplorer's second level.
 * <p>
 * Time taken to complete: 8 hours.
 * <p>
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */

public class EuropeScene extends GameObjectController {
	//Fields
	Array<Vector3> grassLocations = new Array<Vector3>();
	Array<Vector3> treeLocations = new Array<Vector3>();
	private Array<GroundObjectData> groundObjDataList;
	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btCollisionWorld collisionWorld;
	private int counter = 0;
	private int grassIndexStart, grassEndIndex;
	Model tree;
	Model wheat;
	public boolean isQuiz = false;
	int artifactsUnlocked = 0;
	public boolean moveToNext = false;
	private Array<TreasureChest> chests;
	public Stage exitStage;
	private Label exitLabel;
	private Skin exitSkin;
	public Stage hud;
	private Label time;
	private Label artifacts;

	/** Helper Class for ground objects. */
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

		/**
		 * Class Constructor.
		 * 
		 * @param location
		 *            - Location of the groundObject that is passed as a
		 *            parameter.
		 * @param rotateAround
		 *            - The axis meant to be rotated around.
		 * @param rotation
		 *            - Rotation around the axis in degrees.
		 * @param dimensions
		 *            - The dimensions of the box.
		 */
		public GroundObjectData(Vector3 location, Vector3 rotateAround, float rotation, Vector3 dimensions) {
			this.location = location;
			this.rotateAround = rotateAround;
			this.rotation = rotation;
			this.dimensions = dimensions;
		}

		/** Constructs CollisionObeject. */
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

		/** Constructs Model */
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

	/**
	 * Class Constructor: Initializes object with settings passed through
	 * parameter as well as initializes all fields.
	 * 
	 * @param settings
	 *            - Stores the current settings.
	 */
	public EuropeScene(Settings settings) {
		super(settings);
		groundObjDataList = new Array<GroundObjectData>();
		chests = new Array<TreasureChest>();
		initalizeGrassLocations();
		initalizeGroundObjectData();
		initalizeCollisionWorld();
		initalize();
		exitSkin = new Skin(Gdx.files.internal("uiskin.json"));
		exitStage = new Stage();
		exitLabel = new Label("Level Completed! Space bar to continue to next level", exitSkin);
		exitLabel.setX(Gdx.graphics.getWidth() / 2 - exitLabel.getWidth() / 2);
		exitLabel.setY(Gdx.graphics.getHeight() / 2 - exitLabel.getHeight() / 2 - 100);
		exitStage.addActor(exitLabel);
		hud = new Stage();
		time = new Label("", exitSkin);
		artifacts = new Label("", exitSkin);
		time.setX(100);
		time.setY(100);
		artifacts.setX(Gdx.graphics.getWidth() - artifacts.getWidth() - 100);
		artifacts.setY(100);
		hud.addActor(time);
		hud.addActor(artifacts);

	}

	@Override
	public void loadAssets() {
		assets = new AssetManager();
		assets.load("Fence.g3db", Model.class);
		assets.load("Rock.g3db", Model.class);
		assets.load("CrashedPlane.g3db", Model.class);
		assets.load("Tractor.g3db", Model.class);
		assets.load("BrokenTank.g3db", Model.class);
		assets.load("SkyDome.g3db", Model.class);
		assets.load("Church.g3db", Model.class);
		assets.load("EuropeGround.g3db", Model.class);
		assets.load("Tree2.g3db", Model.class);
		assets.load("Wheat.g3db", Model.class);
		assets.load("Gun.g3db", Model.class);
		assets.load("DogTags.g3db", Model.class);
		assets.load("Bomb.g3db", Model.class);
		assets.load("TankModel.g3db", Model.class);
		assets.load("Flag.g3db", Model.class);

		assets.load("RifleDescription.g3db", Model.class);
		assets.load("DogtagsDescription.g3db", Model.class);
		assets.load("BombDescription.g3db", Model.class);
		assets.load("TankDescription.g3db", Model.class);
		assets.load("FlagDescription.g3db", Model.class);
		assets.finishLoading();
	}

	@Override
	public void update() {
		super.update();
		counter = (counter + 1) % 120;
		Vector2 camLoc = new Vector2(ProjectXploraGame.camera.position.x, ProjectXploraGame.camera.position.y);
		for (int i = grassIndexStart; i < grassEndIndex; i++) {
			Vector3 grassLoc3 = new Vector3();
			objects.get(i).transform.getTranslation(grassLoc3);
			Vector2 grassLoc = new Vector2(grassLoc3.x, grassLoc3.y);
			if (grassLoc.dst(camLoc) > 25) {
				grassLoc.x -= camLoc.x;
				grassLoc.y -= camLoc.y;
				grassLoc.x *= -1;
				grassLoc.y *= -1;
				grassLoc.nor().scl(25f);
				grassLoc.x += camLoc.x;
				grassLoc.y += camLoc.y;
				// System.out.println(grassLoc.dst(camLoc));
				boolean relocate = true;
				for (TreasureChest e : chests) {
					if (e.location2().dst(grassLoc) < 7) {
						relocate = false;
					}
				}
				if (relocate)
					objects.get(i).transform.setTranslation(new Vector3(grassLoc.x, grassLoc.y, 0));
			}

		}
		for (TreasureChest t : chests) {
			t.update(ProjectXploraGame.camera.position);
			isQuiz |= t.isQuiz();
			if (!t.isQuiz() && t.unlocked() && !t.added) {
				artifactsUnlocked++;
				objects.add(t.artifact);
				objects.add(t.description);
				t.added = true;
			}
		}
		if (artifactsUnlocked >= 5) {
			exitStage.draw();
			if (Gdx.input.isKeyPressed(Keys.SPACE)) {
				moveToNext = true;
			}
		}
		if (isQuiz) {
			cameraController.keys.clear();
		}
		time.setText((ProjectXploraGame.timer.player.getEuropeTime()) + " Seconds");
		artifacts.setText(artifactsUnlocked + "/5");
	}

	/** Initializes Collision World Configurations. */
	private void initalizeCollisionWorld() {
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

		for (GroundObjectData i : groundObjDataList) {
			collisionWorld.addCollisionObject(i.construct());
		}
	}

	/** Initializes all ground object data. */
	private void initalizeGroundObjectData() {
		groundObjDataList.add(
				new GroundObjectData(new Vector3(0f, 0f, 0f), new Vector3(0, 0, 0), 0f, new Vector3(305f, 305f, 1f)));
	}

	/** Initializes all grass locations. */
	private void initalizeGrassLocations() {
		for (int i = 0; i < 300; i++) {
			float random1 = (float) Math.random();
			float random2 = (float) Math.random();
			float randomXPoint = (float) (random2 * 25 * Math.cos(Math.PI * 2 * random1 / random2));
			float randomYPoint = (float) (random2 * 25 * Math.sin(Math.PI * 2 * random1 / random2));
			grassLocations.add(new Vector3(randomXPoint, randomYPoint, 0));
		}
	}

	/** Initializes all collision shapes. */
	private void initalizeCollisionShapes() {
		// Circles
		cameraController.addCollision(
				new CollisionCircle(new Vector2(30.526680946350098f, 54.840006828308105f), 2.816077470779419f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-55.17082214355469f, -1.6269731521606445f), 3.534199893474579f));
		cameraController.addCollision(
				new CollisionCircle(new Vector2(-47.58145332336426f, -1.6523444652557373f), 4.6844998002052305f));
		// Fences
		cameraController.addCollision(new CollisionRect(new Vector2(99.7129111328125f, 88.19154660949707f),
				new Vector2(100.4299111328125f, 94.11384660949707f)));
		cameraController.addCollision(new CollisionRect(new Vector2(99.71290159606933f, 78.90683095703125f),
				new Vector2(100.42990159606934f, 84.82913095703125f)));
		cameraController.addCollision(new CollisionRect(new Vector2(99.71290159606933f, 69.16798036346435f),
				new Vector2(100.42990159606934f, 75.09028036346436f)));
		cameraController.addCollision(new CollisionRect(new Vector2(81.79947424316406f, 88.3429328704834f),
				new Vector2(82.51647424316407f, 94.2652328704834f)));
		cameraController.addCollision(new CollisionRect(new Vector2(81.79947424316406f, 78.60408704528808f),
				new Vector2(82.51647424316407f, 84.52638704528809f)));
		cameraController.addCollision(new CollisionRect(new Vector2(81.79947424316406f, 69.31938569793701f),
				new Vector2(82.51647424316407f, 75.24168569793702f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-20.50627898788452f, 1.0298187638282776f),
				new Vector2(-19.789278987884522f, 6.9521187638282775f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-20.561685081481933f, -6.394687483215332f),
				new Vector2(-19.844685081481934f, -0.4723874832153321f)));
		cameraController.addCollision(new CollisionRect(new Vector2(93.07341497192382f, 97.858634475708f),
				new Vector2(98.99571497192383f, 98.57563447570801f)));
		cameraController.addCollision(new CollisionRect(new Vector2(83.23366086730957f, 97.85865354919433f),
				new Vector2(89.15596086730957f, 98.57565354919434f)));
		cameraController.addCollision(new CollisionRect(new Vector2(83.2336513305664f, 66.62368822479247f),
				new Vector2(89.15595133056641f, 67.34068822479249f)));
		cameraController.addCollision(new CollisionRect(new Vector2(92.7706615234375f, 66.6237072982788f),
				new Vector2(98.6929615234375f, 67.34070729827882f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-55.27248937835694f, 6.956856731414795f),
				new Vector2(-49.35018937835693f, 7.673856731414795f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-46.352005789184574f, 6.956856731414795f),
				new Vector2(-40.42970578918457f, 7.673856731414795f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-37.40384895553589f, 6.956856731414795f),
				new Vector2(-31.48154895553589f, 7.673856731414795f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-28.73270113220215f, 6.956856731414795f),
				new Vector2(-22.81040113220215f, 7.673856731414795f)));
		// Other rectangles
		cameraController.addCollision(new CollisionRect(new Vector2(-120.40060937404633f, -118.99113297462463f),
				new Vector2(121.64010107517242f, -98.99112105369568f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-131.75570487976074f, -109.6879243850708f),
				new Vector2(-111.75570487976074f, 121.3231611251831f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-120.40060937404633f, 110.83854079246521f),
				new Vector2(121.64010107517242f, 130.83855271339417f)));
		cameraController.addCollision(new CollisionRect(new Vector2(108.29254150390625f, -109.6879243850708f),
				new Vector2(128.29254150390625f, 121.3231611251831f)));
		cameraController.addCollision(new CollisionRect(new Vector2(32.14639276266098f, 61.36067569255829f),
				new Vector2(38.81988912820816f, 64.5529443025589f)));
		cameraController.addCollision(new CollisionRect(new Vector2(33.223433792591095f, 56.29160165786743f),
				new Vector2(36.12490624189377f, 64.22887563705444f)));
		cameraController.addCollision(new CollisionRect(new Vector2(31.919093430042267f, 48.6991286277771f),
				new Vector2(35.21079033613205f, 56.63640260696411f)));
		cameraController.addCollision(new CollisionRect(new Vector2(33.882989287376404f, 51.2932725250721f),
				new Vector2(44.41566526889801f, 55.72522684931755f)));
		cameraController.addCollision(new CollisionRect(new Vector2(22.718040347099304f, 51.89569905400276f),
				new Vector2(33.25071632862091f, 56.327653378248215f)));
		cameraController.addCollision(new CollisionRect(new Vector2(54.488380029797554f, -47.124542370438576f),
				new Vector2(55.440018102526665f, -45.527883395552635f)));
		cameraController.addCollision(new CollisionRect(new Vector2(52.30178602039814f, -47.124542370438576f),
				new Vector2(53.25342409312725f, -45.527883395552635f)));
		cameraController.addCollision(new CollisionRect(new Vector2(52.30178602039814f, -53.94748389720917f),
				new Vector2(53.25342409312725f, -50.455601811409f)));
		cameraController.addCollision(new CollisionRect(new Vector2(54.488380029797554f, -53.94748389720917f),
				new Vector2(55.440018102526665f, -50.455601811409f)));
		cameraController.addCollision(new CollisionRect(new Vector2(52.95559659600258f, -53.589298725128174f),
				new Vector2(54.65454325079918f, -44.21845197677612f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-24.412243366241455f, -75.09800285100937f),
				new Vector2(-10.467214584350586f, -68.22482734918594f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-55.07857918739319f, -8.582870811223984f),
				new Vector2(-50.79247832298279f, -4.307706505060196f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-55.24659901857376f, -5.161173045635223f),
				new Vector2(-48.72824877500534f, 1.9072267413139343f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-48.95018517971039f, -6.3368600606918335f),
				new Vector2(-29.188843369483948f, 3.032171130180359f)));

	}

	/** Resets the isQuiz variable. */
	public void resetIsQuiz() {
		isQuiz = false;
	}

	@Override
	public void camSetup() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		float playerHeight = 1f;
		ProjectXploraGame.camera.position.set(0f, 0f, playerHeight);
		ProjectXploraGame.camera.lookAt(0f, 1f, playerHeight);
		ProjectXploraGame.camera.near = 0.1f;
		ProjectXploraGame.camera.far = 1000f;
		ProjectXploraGame.camera.update();
		cameraController = new PlayerCameraController(ProjectXploraGame.camera, settings);
		initalizeCollisionShapes();
		// cameraController.unlockPosition();
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}

	@Override
	public void updateCamera() {
		super.updateCamera();
		Vector3 intersectLocation = GroundCollisionDetector.rayTest(collisionWorld, cameraController.getRayFrom(),
				cameraController.getRayTo());
		if (intersectLocation != null) {
			cameraController.setZ(intersectLocation.z + 1.4f);
		}
	}

	@Override
	public void loadModelInstances() {
		initalizeTrees();
		initalizeGrassLocations();
		chests.add(new TreasureChest(28.5756f, 59.9431f, 0f, 180f, assets.get("Gun.g3db", Model.class),
				assets.get("RifleDescription.g3db", Model.class)));
		chests.add(new TreasureChest(-27.2791f, -2.256398f, 0f, 90f, assets.get("Flag.g3db", Model.class),
				assets.get("FlagDescription.g3db", Model.class)));
		chests.add(new TreasureChest(-17.5712f, -77.4782f, 0f, 0f, assets.get("DogTags.g3db", Model.class),
				assets.get("DogtagsDescription.g3db", Model.class)));
		chests.add(new TreasureChest(57.7517f, -49.4148f, 0f, 90f, assets.get("TankModel.g3db", Model.class),
				assets.get("TankDescription.g3db", Model.class)));
		chests.add(new TreasureChest(91.227f, 92.3701f, 0f, 0f, assets.get("Bomb.g3db", Model.class),
				assets.get("BombDescription.g3db", Model.class)));
		for (TreasureChest t : chests) {
			objects.add(t.base);
			objects.add(t.lid);
			if (t.base.transform.getRotation(new Quaternion()).nor().getRoll() == 0) {
				cameraController.addCollision(new CollisionRect(
						new Vector2(t.base.transform.getTranslation(new Vector3()).x - 1,
								t.base.transform.getTranslation(new Vector3()).y - 1),
						new Vector2(t.base.transform.getTranslation(new Vector3()).x + 1,
								t.base.transform.getTranslation(new Vector3()).y)));
			} else if (t.base.transform.getRotation(new Quaternion()).nor().getRoll() == 180) {
				cameraController.addCollision(new CollisionRect(
						new Vector2(t.base.transform.getTranslation(new Vector3()).x - 1,
								t.base.transform.getTranslation(new Vector3()).y),
						new Vector2(t.base.transform.getTranslation(new Vector3()).x + 1,
								t.base.transform.getTranslation(new Vector3()).y + 1)));
			} else if (t.base.transform.getRotation(new Quaternion()).nor().getRoll() == -90) {
				cameraController.addCollision(new CollisionRect(
						new Vector2(t.base.transform.getTranslation(new Vector3()).x - 1,
								t.base.transform.getTranslation(new Vector3()).y - 1),
						new Vector2(t.base.transform.getTranslation(new Vector3()).x,
								t.base.transform.getTranslation(new Vector3()).y + 1)));
			} else {
				cameraController.addCollision(new CollisionRect(
						new Vector2(t.base.transform.getTranslation(new Vector3()).x,
								t.base.transform.getTranslation(new Vector3()).y - 1),
						new Vector2(t.base.transform.getTranslation(new Vector3()).x + 1,
								t.base.transform.getTranslation(new Vector3()).y + 1)));
			}
		}
		// sky dome
		Model sky = assets.get("SkyDome.g3db", Model.class);
		GameObject sky_inst = new GameObject(sky);
		sky_inst.transform.scale(20, 20, 20);
		objects.add(sky_inst);
		// Ground
		Model ground = assets.get("EuropeGround.g3db", Model.class);
		objects.add(new GameObject(ground));
		// Church
		Model church = assets.get("Church.g3db", Model.class);
		objects.add(new GameObject(church, new Vector3(-43.9461f, -2.678932f, 15.0999f)));
		// Plane
		Model plane = assets.get("CrashedPlane.g3db", Model.class);
		objects.add(new GameObject(plane, new Vector3(33.8331f, 54.0714f, 2.107422f)));
		// Rock
		Model rock = assets.get("Rock.g3db", Model.class);
		objects.add(new GameObject(rock, new Vector3(30.7169f, 55.7016f, 0.861701f)));
		// Tractor
		Model tractor = assets.get("Tractor.g3db", Model.class);
		objects.add(new GameObject(tractor, new Vector3(53.8608f, -49.448f, 1.57316f), new Vector3(0, 0, -90f)));
		// Tank
		Model tank = assets.get("BrokenTank.g3db", Model.class);
		objects.add(new GameObject(tank, new Vector3(-16.9186f, -71.5672f, 1.964423f), new Vector3(0, 0, -90f)));
		// Fences
		Model fence = assets.get("Fence.g3db", Model.class);
		objects.add(new GameObject(fence, new Vector3(93.80023002624512f, -99.29638862609863f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(83.6191177368164f, -99.29638862609863f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(73.43800067901611f, -99.29638862609863f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-8.010926246643066f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-18.192039728164673f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-28.37315797805786f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-38.55427265167236f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-48.7354040145874f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-79.27876949310303f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-89.45988655090332f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-99.6410083770752f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-109.8221206665039f, -99.2963981628418f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-111.99482917785645f, -94.22174453735352f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99482917785645f, -84.0406322479248f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99483871459961f, -73.8595199584961f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99483871459961f, -63.6784029006958f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99483871459961f, -53.49729061126709f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99482917785645f, -43.3161735534668f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99482917785645f, -33.135056495666504f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99483871459961f, -22.953948974609375f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99483871459961f, -2.591722309589386f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99482917785645f, 7.589391469955444f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99483871459961f, 17.770508527755737f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 27.9516339302063f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 38.13275098800659f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 48.313870429992676f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 58.495001792907715f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 68.67610454559326f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 78.85722637176514f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 89.0383529663086f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-111.99485778808594f, 109.40059661865234f, 1.4024418592453003f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(-99.81595039367676f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-89.63483810424805f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-79.45371150970459f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-48.910369873046875f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-38.72925519943237f, 112.83123970031738f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-28.548126220703125f, 112.83123970031738f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-18.367013931274414f, 112.83123970031738f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-8.18589448928833f, 112.83123970031738f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(22.357454299926758f, 112.83123016357422f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(32.53858804702759f, 112.83123016357422f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(42.719712257385254f, 112.83123016357422f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(52.90082931518555f, 112.83123016357422f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(63.08195114135742f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(73.2630729675293f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(83.44418525695801f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(93.62530708312988f, 112.83122062683105f, 1.4024418592453003f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(108.54074478149414f, 109.64801788330078f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54074478149414f, 89.28581237792969f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54073524475098f, 79.10468101501465f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54074478149414f, 68.92357349395752f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54074478149414f, 58.74245643615723f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54073524475098f, 48.56133460998535f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54073524475098f, 38.38021993637085f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54073524475098f, 28.19910764694214f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54073524475098f, 18.01799178123474f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54073524475098f, 7.8368741273880005f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -2.344236522912979f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54072570800781f, -22.706475257873535f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54072570800781f, -32.8875994682312f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -43.06872367858887f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -53.24984073638916f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -63.43095779418945f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -73.61207485198975f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -83.79319190979004f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(108.54071617126465f, -93.97431373596191f, 1.4024418592453003f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(63.25687885284424f, -99.29638862609863f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(53.07576656341553f, -99.29638862609863f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(42.894649505615234f, -99.29638862609863f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(32.71353483200073f, -99.29638862609863f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(22.53242015838623f, -99.29638862609863f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(100.0714111328125f, 91.15269660949707f, 1.4024417102336884f),
				new Vector3(0f, 0f, -359.999982697156f)));
		objects.add(new GameObject(fence, new Vector3(100.07140159606934f, 81.86798095703125f, 1.4024417102336884f),
				new Vector3(0f, 0f, -359.999982697156f)));
		objects.add(new GameObject(fence, new Vector3(100.07140159606934f, 72.12913036346436f, 1.4024417102336884f),
				new Vector3(0f, 0f, -359.999982697156f)));
		objects.add(new GameObject(fence, new Vector3(96.03456497192383f, 98.21713447570801f, 1.4024417102336884f),
				new Vector3(0f, 0f, -270.00000068324533f)));
		objects.add(new GameObject(fence, new Vector3(86.19481086730957f, 98.21715354919434f, 1.4024417102336884f),
				new Vector3(0f, 0f, -270.00000068324533f)));
		objects.add(new GameObject(fence, new Vector3(82.15797424316406f, 91.3040828704834f, 1.4024417102336884f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(82.15797424316406f, 81.56523704528809f, 1.4024417102336884f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(82.15797424316406f, 72.28053569793701f, 1.4024417102336884f),
				new Vector3(0f, 0f, -179.999991348578f)));
		objects.add(new GameObject(fence, new Vector3(86.1948013305664f, 66.98218822479248f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(95.7318115234375f, 66.98220729827881f, 1.4024417102336884f),
				new Vector3(0f, 0f, -89.999995674289f)));
		objects.add(new GameObject(fence, new Vector3(-52.311339378356934f, 7.315356731414795f, 1.4024417102336884f),
				new Vector3(0f, 0f, 90.00000250447816f)));
		objects.add(new GameObject(fence, new Vector3(-43.39085578918457f, 7.315356731414795f, 1.4024417102336884f),
				new Vector3(0f, 0f, 90.00000250447816f)));
		objects.add(new GameObject(fence, new Vector3(-34.44269895553589f, 7.315356731414795f, 1.4024417102336884f),
				new Vector3(0f, 0f, 90.00000250447816f)));
		objects.add(new GameObject(fence, new Vector3(-25.77155113220215f, 7.315356731414795f, 1.4024417102336884f),
				new Vector3(0f, 0f, 90.00000250447816f)));
		objects.add(new GameObject(fence, new Vector3(-20.14777898788452f, 3.9909687638282776f, 1.4024417102336884f),
				new Vector3(0f, 0f, 0.0f)));
		objects.add(new GameObject(fence, new Vector3(-20.203185081481934f, -3.433537483215332f, 1.4024417102336884f),
				new Vector3(0f, 0f, 0.0f)));
		grassIndexStart = objects.size;
		wheat = assets.get("Wheat.g3db", Model.class);
		for (Vector3 i : grassLocations) {
			objects.add(new GameObject(wheat, i));
		}
		grassEndIndex = objects.size;
	}

	private void initalizeTrees() {
		tree = assets.get("Tree2.g3db", Model.class);
		objects.add(new GameObject(tree, new Vector3(112.99702644348145f, 141.84045791625977f, 4.370512664318085f)));
		objects.add(new GameObject(tree, new Vector3(120.04488945007324f, 147.21545219421387f, 4.4768401980400085f)));
		objects.add(new GameObject(tree, new Vector3(114.39632415771484f, 148.6490821838379f, 5.304628014564514f)));
		objects.add(new GameObject(tree, new Vector3(120.36574363708496f, 149.86788749694824f, 4.132022857666016f)));
		objects.add(new GameObject(tree, new Vector3(120.9603500366211f, 139.03011322021484f, 5.720040798187256f)));
		objects.add(new GameObject(tree, new Vector3(121.27993583679199f, 143.5836124420166f, 4.500797390937805f)));
		objects.add(new GameObject(tree, new Vector3(117.88217544555664f, 147.94100761413574f, 5.6220680475234985f)));
		objects.add(new GameObject(tree, new Vector3(116.88483238220215f, 148.29745292663574f, 5.007069706916809f)));
		objects.add(new GameObject(tree, new Vector3(114.04325485229492f, 142.75389671325684f, 5.469729900360107f)));
		objects.add(new GameObject(tree, new Vector3(113.94124031066895f, 141.65372848510742f, 4.18305367231369f)));
		objects.add(new GameObject(tree, new Vector3(122.75633811950684f, 143.3280372619629f, 4.874894320964813f)));
		objects.add(new GameObject(tree, new Vector3(117.89797782897949f, 145.148344039917f, 5.705786943435669f)));
		objects.add(new GameObject(tree, new Vector3(122.30170249938965f, 6.542725563049316f, 4.637922942638397f)));
		objects.add(new GameObject(tree, new Vector3(119.2941951751709f, 90.19457817077637f, 5.4667288064956665f)));
		objects.add(new GameObject(tree, new Vector3(123.27146530151367f, -4.260852634906769f, 4.610141217708588f)));
		objects.add(new GameObject(tree, new Vector3(119.60089683532715f, 90.24860382080078f, 4.862113893032074f)));
		objects.add(new GameObject(tree, new Vector3(122.47382164001465f, 2.1492165327072144f, 5.299341082572937f)));
		objects.add(new GameObject(tree, new Vector3(116.32861137390137f, 1.9588349759578705f, 5.478781461715698f)));
		objects.add(new GameObject(tree, new Vector3(115.39461135864258f, 12.11277723312378f, 4.588751494884491f)));
		objects.add(new GameObject(tree, new Vector3(113.4444808959961f, 89.48629379272461f, 5.40838360786438f)));
		objects.add(new GameObject(tree, new Vector3(123.38357925415039f, 58.975892066955566f, 4.324252605438232f)));
		objects.add(new GameObject(tree, new Vector3(119.64875221252441f, -6.852359175682068f, 4.95546817779541f)));
		objects.add(new GameObject(tree, new Vector3(115.81249237060547f, 67.37448215484619f, 5.08511483669281f)));
		objects.add(new GameObject(tree, new Vector3(116.9210433959961f, 26.60555362701416f, 4.4419920444488525f)));
		objects.add(new GameObject(tree, new Vector3(117.9045295715332f, 34.1291618347168f, 4.561378657817841f)));
		objects.add(new GameObject(tree, new Vector3(116.88611030578613f, -100.49975395202637f, 4.286293685436249f)));
		objects.add(new GameObject(tree, new Vector3(121.27854347229004f, -99.80600357055664f, 4.882799983024597f)));
		objects.add(new GameObject(tree, new Vector3(114.51544761657715f, -65.89845657348633f, 5.503127574920654f)));
		objects.add(new GameObject(tree, new Vector3(115.59813499450684f, -48.19692611694336f, 4.198223352432251f)));
		objects.add(new GameObject(tree, new Vector3(121.48037910461426f, -25.075347423553467f, 4.2044976353645325f)));
		objects.add(new GameObject(tree, new Vector3(113.31082344055176f, -82.70956039428711f, 5.342682600021362f)));
		objects.add(new GameObject(tree, new Vector3(119.2466926574707f, -39.37978982925415f, 5.602731704711914f)));
		objects.add(new GameObject(tree, new Vector3(116.69692039489746f, -103.57364654541016f, 4.191397130489349f)));
		objects.add(new GameObject(tree, new Vector3(119.94759559631348f, -113.8420581817627f, 4.509821832180023f)));
		objects.add(new GameObject(tree, new Vector3(117.39901542663574f, -50.38901329040527f, 4.954107105731964f)));
		objects.add(new GameObject(tree, new Vector3(117.19462394714355f, -38.081395626068115f, 4.570402204990387f)));
		objects.add(new GameObject(tree, new Vector3(121.0112476348877f, -94.4707202911377f, 4.198873937129974f)));
		objects.add(new GameObject(tree, new Vector3(121.26985549926758f, -61.309332847595215f, 5.457221269607544f)));
		objects.add(new GameObject(tree, new Vector3(114.09749031066895f, -99.78233337402344f, 4.585956931114197f)));
		objects.add(new GameObject(tree, new Vector3(119.57494735717773f, -78.89604568481445f, 5.281808972358704f)));
		objects.add(new GameObject(tree, new Vector3(120.3404426574707f, -115.03179550170898f, 4.928369224071503f)));
		objects.add(new GameObject(tree, new Vector3(115.78310966491699f, -33.928353786468506f, 4.976951479911804f)));
		objects.add(new GameObject(tree, new Vector3(118.63774299621582f, -114.65027809143066f, 5.471529960632324f)));
		objects.add(new GameObject(tree, new Vector3(113.44045639038086f, -35.72566747665405f, 4.671661555767059f)));
		objects.add(new GameObject(tree, new Vector3(118.54104995727539f, -125.9153938293457f, 5.323144197463989f)));
		objects.add(new GameObject(tree, new Vector3(120.75592041015625f, -131.13996505737305f, 4.2097654938697815f)));
		objects.add(new GameObject(tree, new Vector3(116.8374252319336f, -125.75915336608887f, 5.463160872459412f)));
		objects.add(new GameObject(tree, new Vector3(115.59295654296875f, -126.6104507446289f, 5.22674560546875f)));
		objects.add(new GameObject(tree, new Vector3(123.35230827331543f, -138.64116668701172f, 5.153366923332214f)));
		objects.add(new GameObject(tree, new Vector3(118.20619583129883f, -143.25510025024414f, 5.288659334182739f)));
		objects.add(new GameObject(tree, new Vector3(113.78203392028809f, -141.9509983062744f, 4.973782896995544f)));
		objects.add(new GameObject(tree, new Vector3(116.98391914367676f, -150.1368808746338f, 5.732229948043823f)));
		objects.add(new GameObject(tree, new Vector3(120.3342056274414f, -134.52046394348145f, 4.421975016593933f)));
		objects.add(new GameObject(tree, new Vector3(120.37683486938477f, -145.93267440795898f, 4.939825534820557f)));
		objects.add(new GameObject(tree, new Vector3(118.3456802368164f, -143.87276649475098f, 5.188514590263367f)));
		objects.add(new GameObject(tree, new Vector3(120.8248519897461f, -150.17746925354004f, 4.444270431995392f)));
		objects.add(new GameObject(tree, new Vector3(122.67251968383789f, -149.1982936859131f, 4.11466658115387f)));
		objects.add(new GameObject(tree, new Vector3(112.87941932678223f, -129.30514335632324f, 4.214936196804047f)));
		objects.add(new GameObject(tree, new Vector3(123.27198028564453f, -130.89509963989258f, 4.502202868461609f)));
		objects.add(new GameObject(tree, new Vector3(120.32082557678223f, -142.12542533874512f, 5.510944128036499f)));
		objects.add(new GameObject(tree, new Vector3(116.76482200622559f, -146.2121868133545f, 4.633580148220062f)));
		objects.add(new GameObject(tree, new Vector3(121.02580070495605f, -143.9311122894287f, 4.923543035984039f)));
		objects.add(new GameObject(tree, new Vector3(113.30222129821777f, -146.23624801635742f, 4.918748438358307f)));
		objects.add(new GameObject(tree, new Vector3(117.06253051757812f, 126.46706581115723f, 5.358654260635376f)));
		objects.add(new GameObject(tree, new Vector3(119.27990913391113f, 121.80968284606934f, 5.323646664619446f)));
		objects.add(new GameObject(tree, new Vector3(126.30387306213379f, 128.15468788146973f, 5.341198444366455f)));
		objects.add(new GameObject(tree, new Vector3(131.39739990234375f, 119.18380737304688f, 5.213825702667236f)));
		objects.add(new GameObject(tree, new Vector3(146.24237060546875f, 123.59953880310059f, 4.616842269897461f)));
		objects.add(new GameObject(tree, new Vector3(145.52862167358398f, 125.40724754333496f, 5.363592505455017f)));
		objects.add(new GameObject(tree, new Vector3(148.03483963012695f, 117.84270286560059f, 5.594503879547119f)));
		objects.add(new GameObject(tree, new Vector3(135.9550952911377f, 124.85043525695801f, 4.973664879798889f)));
		objects.add(new GameObject(tree, new Vector3(131.92238807678223f, 120.55960655212402f, 5.260862708091736f)));
		objects.add(new GameObject(tree, new Vector3(138.8052749633789f, 114.80500221252441f, 5.40710985660553f)));
		objects.add(new GameObject(tree, new Vector3(125.88727951049805f, 127.12162971496582f, 4.867505729198456f)));
		objects.add(new GameObject(tree, new Vector3(133.00381660461426f, 118.60095977783203f, 4.754613935947418f)));
		objects.add(new GameObject(tree, new Vector3(128.5061740875244f, 124.44156646728516f, 5.446639657020569f)));
		objects.add(new GameObject(tree, new Vector3(125.02452850341797f, 114.15308952331543f, 4.500868618488312f)));
		objects.add(new GameObject(tree, new Vector3(147.00408935546875f, 122.24241256713867f, 5.538094639778137f)));
		objects.add(new GameObject(tree, new Vector3(128.80426406860352f, 119.84359741210938f, 5.312798023223877f)));
		objects.add(new GameObject(tree, new Vector3(152.46975898742676f, 130.66232681274414f, 4.490953087806702f)));
		objects.add(new GameObject(tree, new Vector3(126.24164581298828f, 113.36411476135254f, 5.0276970863342285f)));
		objects.add(new GameObject(tree, new Vector3(140.26926040649414f, 114.49774742126465f, 5.162007808685303f)));
		objects.add(new GameObject(tree, new Vector3(-112.29143142700195f, 120.5722713470459f, 5.323038101196289f)));
		objects.add(new GameObject(tree, new Vector3(-78.66098880767822f, 130.78227996826172f, 4.122390449047089f)));
		objects.add(new GameObject(tree, new Vector3(-88.92308235168457f, 119.72783088684082f, 4.307029545307159f)));
		objects.add(new GameObject(tree, new Vector3(-111.07942581176758f, 125.83730697631836f, 4.9607715010643005f)));
		objects.add(new GameObject(tree, new Vector3(0.655757337808609f, 126.41598701477051f, 5.642642974853516f)));
		objects.add(new GameObject(tree, new Vector3(-51.47383689880371f, 120.91268539428711f, 5.554801821708679f)));
		objects.add(new GameObject(tree, new Vector3(-31.22891902923584f, 129.90236282348633f, 5.52557647228241f)));
		objects.add(new GameObject(tree, new Vector3(-29.415254592895508f, 120.53864479064941f, 4.65796023607254f)));
		objects.add(new GameObject(tree, new Vector3(-15.349689722061157f, 124.33173179626465f, 4.73644345998764f)));
		objects.add(new GameObject(tree, new Vector3(-49.534640312194824f, 126.66900634765625f, 4.297146797180176f)));
		objects.add(new GameObject(tree, new Vector3(-15.810003280639648f, 130.4621410369873f, 4.561445713043213f)));
		objects.add(new GameObject(tree, new Vector3(-18.139604330062866f, 127.90444374084473f, 4.431731402873993f)));
		objects.add(new GameObject(tree, new Vector3(24.67189311981201f, 120.03544807434082f, 5.340222716331482f)));
		objects.add(new GameObject(tree, new Vector3(81.73830986022949f, 130.49707412719727f, 4.452648460865021f)));
		objects.add(new GameObject(tree, new Vector3(66.07890129089355f, 129.75895881652832f, 5.343681573867798f)));
		objects.add(new GameObject(tree, new Vector3(51.433701515197754f, 124.08461570739746f, 4.374307096004486f)));
		objects.add(new GameObject(tree, new Vector3(89.67686653137207f, 124.86505508422852f, 4.475834369659424f)));
		objects.add(new GameObject(tree, new Vector3(53.55061054229736f, 127.91637420654297f, 4.468695521354675f)));
		objects.add(new GameObject(tree, new Vector3(92.93740272521973f, 130.9469985961914f, 5.0246453285217285f)));
		objects.add(new GameObject(tree, new Vector3(60.58095455169678f, 124.21984672546387f, 4.757068753242493f)));
		objects.add(new GameObject(tree, new Vector3(20.52241563796997f, 123.38896751403809f, 4.628448486328125f)));
		objects.add(new GameObject(tree, new Vector3(41.24239921569824f, 126.71005249023438f, 5.166273713111877f)));
		objects.add(new GameObject(tree, new Vector3(23.065872192382812f, 125.31341552734375f, 4.671353995800018f)));
		objects.add(new GameObject(tree, new Vector3(62.7492094039917f, 121.1353874206543f, 4.282243847846985f)));
		objects.add(new GameObject(tree, new Vector3(72.24298477172852f, 127.45035171508789f, 4.207203984260559f)));
		objects.add(new GameObject(tree, new Vector3(-114.9886703491211f, 129.77922439575195f, 4.303320348262787f)));
		objects.add(new GameObject(tree, new Vector3(-119.25206184387207f, 122.95500755310059f, 4.378975927829742f)));
		objects.add(new GameObject(tree, new Vector3(-127.63527870178223f, 127.87588119506836f, 4.22857403755188f)));
		objects.add(new GameObject(tree, new Vector3(-114.3105697631836f, 119.62964057922363f, 5.643168091773987f)));
		objects.add(new GameObject(tree, new Vector3(-149.73464965820312f, 129.183988571167f, 4.256817102432251f)));
		objects.add(new GameObject(tree, new Vector3(-146.37222290039062f, 119.92414474487305f, 4.113594889640808f)));
		objects.add(new GameObject(tree, new Vector3(-141.0179328918457f, 131.24503135681152f, 4.344213902950287f)));
		objects.add(new GameObject(tree, new Vector3(-152.34152793884277f, 122.41824150085449f, 5.736590027809143f)));
		objects.add(new GameObject(tree, new Vector3(-119.5025634765625f, 126.11570358276367f, 5.281718969345093f)));
		objects.add(new GameObject(tree, new Vector3(-148.4203338623047f, 128.38780403137207f, 5.374100208282471f)));
		objects.add(new GameObject(tree, new Vector3(-150.4898452758789f, 121.67821884155273f, 5.497668981552124f)));
		objects.add(new GameObject(tree, new Vector3(-146.11105918884277f, 118.65145683288574f, 5.598088502883911f)));
		objects.add(new GameObject(tree, new Vector3(-128.39728355407715f, 117.00510025024414f, 5.106459856033325f)));
		objects.add(new GameObject(tree, new Vector3(-150.56652069091797f, 113.29567909240723f, 4.819261133670807f)));
		objects.add(new GameObject(tree, new Vector3(-130.56955337524414f, 114.95756149291992f, 4.144757688045502f)));
		objects.add(new GameObject(tree, new Vector3(-141.85086250305176f, 120.58645248413086f, 4.867509305477142f)));
		objects.add(new GameObject(tree, new Vector3(-118.09733390808105f, 121.09161376953125f, 5.3747594356536865f)));
		objects.add(new GameObject(tree, new Vector3(-142.73322105407715f, 118.93648147583008f, 4.678472280502319f)));
		objects.add(new GameObject(tree, new Vector3(-138.4526252746582f, 118.39162826538086f, 4.151199162006378f)));
		objects.add(new GameObject(tree, new Vector3(-123.07754516601562f, 117.08808898925781f, 5.499876141548157f)));
		objects.add(new GameObject(tree, new Vector3(-139.9009132385254f, 128.83278846740723f, 5.3285253047943115f)));
		objects.add(new GameObject(tree, new Vector3(-128.47027778625488f, 128.59838485717773f, 4.298849701881409f)));
		objects.add(new GameObject(tree, new Vector3(-142.84598350524902f, 126.47652626037598f, 4.863021969795227f)));
		objects.add(new GameObject(tree, new Vector3(-124.17654037475586f, 129.34691429138184f, 4.244937300682068f)));
		objects.add(new GameObject(tree, new Vector3(-146.34808540344238f, 122.21494674682617f, 4.888387620449066f)));
		objects.add(new GameObject(tree, new Vector3(-144.1225814819336f, 128.11984062194824f, 4.433250427246094f)));
		objects.add(new GameObject(tree, new Vector3(-151.99270248413086f, 128.24697494506836f, 4.1199275851249695f)));
		objects.add(new GameObject(tree, new Vector3(-120.37288665771484f, 142.73200035095215f, 4.358207285404205f)));
		objects.add(new GameObject(tree, new Vector3(-118.98720741271973f, 149.45704460144043f, 5.340678095817566f)));
		objects.add(new GameObject(tree, new Vector3(-141.9327449798584f, 148.5693645477295f, 4.585320055484772f)));
		objects.add(new GameObject(tree, new Vector3(-136.45742416381836f, 137.2069549560547f, 5.217301845550537f)));
		objects.add(new GameObject(tree, new Vector3(-145.08880615234375f, 138.19920539855957f, 4.532420039176941f)));
		objects.add(new GameObject(tree, new Vector3(-120.23674011230469f, 150.25957107543945f, 4.70293402671814f)));
		objects.add(new GameObject(tree, new Vector3(-146.4344882965088f, 134.07015800476074f, 4.327249526977539f)));
		objects.add(new GameObject(tree, new Vector3(-150.21432876586914f, 150.46990394592285f, 4.548540711402893f)));
		objects.add(new GameObject(tree, new Vector3(-116.86620712280273f, 152.56304740905762f, 5.574536323547363f)));
		objects.add(new GameObject(tree, new Vector3(-140.638427734375f, 132.82769203186035f, 4.535602629184723f)));
		objects.add(new GameObject(tree, new Vector3(-120.73854446411133f, 135.67903518676758f, 5.0191885232925415f)));
		objects.add(new GameObject(tree, new Vector3(-134.79165077209473f, 138.83586883544922f, 4.780483245849609f)));
		objects.add(new GameObject(tree, new Vector3(-118.10060501098633f, 139.02715682983398f, 4.871078431606293f)));
		objects.add(new GameObject(tree, new Vector3(-132.54613876342773f, 150.5540370941162f, 4.198627173900604f)));
		objects.add(new GameObject(tree, new Vector3(-118.1287670135498f, 139.91867065429688f, 5.266679525375366f)));
		objects.add(new GameObject(tree, new Vector3(-132.72601127624512f, 144.568510055542f, 5.343160629272461f)));
		objects.add(new GameObject(tree, new Vector3(-138.7069606781006f, 141.0765552520752f, 5.238420367240906f)));
		objects.add(new GameObject(tree, new Vector3(-140.94626426696777f, 134.75101470947266f, 5.203767418861389f)));
		objects.add(new GameObject(tree, new Vector3(-152.71160125732422f, 137.39648818969727f, 4.9443623423576355f)));
		objects.add(new GameObject(tree, new Vector3(-140.22564888000488f, 140.28935432434082f, 5.554730296134949f)));
		objects.add(new GameObject(tree, new Vector3(-114.71590042114258f, 136.13457679748535f, 4.877803921699524f)));
		objects.add(new GameObject(tree, new Vector3(-151.12631797790527f, 152.5405979156494f, 5.048398375511169f)));
		objects.add(new GameObject(tree, new Vector3(-147.72777557373047f, 140.00263214111328f, 4.535862505435944f)));
		objects.add(new GameObject(tree, new Vector3(-123.71875762939453f, 138.113374710083f, 5.0635480880737305f)));
		objects.add(new GameObject(tree, new Vector3(-139.4479751586914f, 150.27161598205566f, 4.170824289321899f)));
		objects.add(new GameObject(tree, new Vector3(-141.2786865234375f, 138.69196891784668f, 4.937292635440826f)));
		objects.add(new GameObject(tree, new Vector3(-145.6304168701172f, 142.14097023010254f, 4.909269213676453f)));
		objects.add(new GameObject(tree, new Vector3(-134.85037803649902f, 131.94398880004883f, 4.6839869022369385f)));
		objects.add(new GameObject(tree, new Vector3(-150.43977737426758f, 134.7385311126709f, 5.506412982940674f)));
		objects.add(new GameObject(tree, new Vector3(-126.02198600769043f, 134.0119743347168f, 5.31633734703064f)));
		objects.add(new GameObject(tree, new Vector3(-129.63815689086914f, 140.67132949829102f, 4.475139677524567f)));
		objects.add(new GameObject(tree, new Vector3(-151.78183555603027f, 143.58233451843262f, 4.680180847644806f)));
		objects.add(new GameObject(tree, new Vector3(-143.33694458007812f, 144.02349472045898f, 5.384899377822876f)));
		objects.add(new GameObject(tree, new Vector3(-143.38262557983398f, 146.79726600646973f, 5.23892879486084f)));
		objects.add(new GameObject(tree, new Vector3(-125.08443832397461f, 141.57659530639648f, 4.113890826702118f)));
		objects.add(new GameObject(tree, new Vector3(-136.964693069458f, 132.885160446167f, 4.475294649600983f)));
		objects.add(new GameObject(tree, new Vector3(-128.3558464050293f, 145.52578926086426f, 4.5040348172187805f)));
		objects.add(new GameObject(tree, new Vector3(-148.2402515411377f, 134.14369583129883f, 5.380281209945679f)));
		objects.add(new GameObject(tree, new Vector3(-147.73571014404297f, 146.30179405212402f, 5.4593271017074585f)));
		objects.add(new GameObject(tree, new Vector3(-143.52381706237793f, 133.93712997436523f, 5.340779423713684f)));
		objects.add(new GameObject(tree, new Vector3(-125.14976501464844f, 140.69757461547852f, 4.780414700508118f)));
		objects.add(new GameObject(tree, new Vector3(-151.38476371765137f, 150.4955005645752f, 5.590717792510986f)));
		objects.add(new GameObject(tree, new Vector3(-136.74832344055176f, 11.06532335281372f, 4.159604907035828f)));
		objects.add(new GameObject(tree, new Vector3(-128.16253662109375f, 16.257691383361816f, 4.280624091625214f)));
		objects.add(new GameObject(tree, new Vector3(-127.3810863494873f, 47.41269588470459f, 4.994132220745087f)));
		objects.add(new GameObject(tree, new Vector3(-126.12960815429688f, 92.00090408325195f, 5.61551570892334f)));
		objects.add(new GameObject(tree, new Vector3(-144.49030876159668f, 82.56636619567871f, 5.437808632850647f)));
		objects.add(new GameObject(tree, new Vector3(-141.12526893615723f, 50.4998779296875f, 4.541029334068298f)));
		objects.add(new GameObject(tree, new Vector3(-137.5854206085205f, 82.02942848205566f, 5.658621788024902f)));
		objects.add(new GameObject(tree, new Vector3(-132.49016761779785f, 25.79287052154541f, 4.843294620513916f)));
		objects.add(new GameObject(tree, new Vector3(-140.54763793945312f, 6.323294639587402f, 4.579406678676605f)));
		objects.add(new GameObject(tree, new Vector3(-131.30792617797852f, 84.22965049743652f, 4.269701838493347f)));
		objects.add(new GameObject(tree, new Vector3(-122.30697631835938f, 35.706751346588135f, 5.386894345283508f)));
		objects.add(new GameObject(tree, new Vector3(-149.14236068725586f, 36.05597734451294f, 4.865821599960327f)));
		objects.add(new GameObject(tree, new Vector3(-150.1400852203369f, 82.68280029296875f, 4.7589656710624695f)));
		objects.add(new GameObject(tree, new Vector3(-150.43086051940918f, 41.52632236480713f, 5.050843954086304f)));
		objects.add(new GameObject(tree, new Vector3(-129.4822120666504f, 87.91646957397461f, 5.0784385204315186f)));
		objects.add(new GameObject(tree, new Vector3(-121.27070426940918f, 27.90302276611328f, 4.855711758136749f)));
		objects.add(new GameObject(tree, new Vector3(-138.4523868560791f, 54.88875389099121f, 4.705478847026825f)));
		objects.add(new GameObject(tree, new Vector3(-145.5348777770996f, 66.06589794158936f, 5.117422342300415f)));
		objects.add(new GameObject(tree, new Vector3(-151.2000846862793f, 26.694819927215576f, 4.76120263338089f)));
		objects.add(new GameObject(tree, new Vector3(-140.8339786529541f, 4.116345047950745f, 5.636141300201416f)));
		objects.add(new GameObject(tree, new Vector3(-147.22851753234863f, 72.92052745819092f, 5.512905120849609f)));
		objects.add(new GameObject(tree, new Vector3(-149.24799919128418f, 13.933669328689575f, 4.857434034347534f)));
		objects.add(new GameObject(tree, new Vector3(-143.66156578063965f, 76.73911094665527f, 5.092725157737732f)));
		objects.add(new GameObject(tree, new Vector3(-143.07336807250977f, -2.4428267776966095f, 5.733845233917236f)));
		objects.add(new GameObject(tree, new Vector3(-137.4575710296631f, 4.9995410442352295f, 4.248976111412048f)));
		objects.add(new GameObject(tree, new Vector3(-134.18879508972168f, -2.9504913091659546f, 4.243958294391632f)));
		objects.add(new GameObject(tree, new Vector3(-149.2189121246338f, 82.46956825256348f, 5.548000335693359f)));
		objects.add(new GameObject(tree, new Vector3(-148.12588691711426f, 85.94589233398438f, 5.227130055427551f)));
		objects.add(new GameObject(tree, new Vector3(-135.27122497558594f, 5.288447141647339f, 5.502792000770569f)));
		objects.add(new GameObject(tree, new Vector3(-149.18940544128418f, 46.78298473358154f, 5.44416606426239f)));
		objects.add(new GameObject(tree, new Vector3(-149.67824935913086f, 72.16942310333252f, 5.373539924621582f)));
		objects.add(new GameObject(tree, new Vector3(-149.65957641601562f, 43.05183410644531f, 4.424904882907867f)));
		objects.add(new GameObject(tree, new Vector3(-146.9119358062744f, 25.76080560684204f, 5.528757572174072f)));
		objects.add(new GameObject(tree, new Vector3(-151.51448249816895f, -6.390140056610107f, 5.569102168083191f)));
		objects.add(new GameObject(tree, new Vector3(-121.83554649353027f, 42.31703758239746f, 4.801470339298248f)));
		objects.add(new GameObject(tree, new Vector3(-144.09357070922852f, 48.86688232421875f, 5.4144346714019775f)));
		objects.add(new GameObject(tree, new Vector3(-140.08170127868652f, 82.68260955810547f, 4.817177355289459f)));
		objects.add(new GameObject(tree, new Vector3(-146.71890258789062f, -5.942139029502869f, 5.360388159751892f)));
		objects.add(new GameObject(tree, new Vector3(-141.55579566955566f, 76.89978122711182f, 5.325536727905273f)));
		objects.add(new GameObject(tree, new Vector3(-138.93890380859375f, 87.53495216369629f, 5.198912620544434f)));
		objects.add(new GameObject(tree, new Vector3(-124.46358680725098f, 86.55729293823242f, 4.1130200028419495f)));
		objects.add(new GameObject(tree, new Vector3(-152.60215759277344f, 0.60846246778965f, 4.409307539463043f)));
		objects.add(new GameObject(tree, new Vector3(-146.41180992126465f, 52.00841426849365f, 5.2540522813797f)));
		objects.add(new GameObject(tree, new Vector3(-146.88386917114258f, 3.785659372806549f, 5.252953767776489f)));
		objects.add(new GameObject(tree, new Vector3(-128.77141952514648f, 88.99415016174316f, 4.84784334897995f)));
		objects.add(new GameObject(tree, new Vector3(-147.77554512023926f, -2.2656548023223877f, 4.697932004928589f)));
		objects.add(new GameObject(tree, new Vector3(-151.75869941711426f, 63.61656665802002f, 4.632874429225922f)));
		objects.add(new GameObject(tree, new Vector3(-147.88923263549805f, 8.89209508895874f, 4.706464111804962f)));
		objects.add(new GameObject(tree, new Vector3(-132.23496437072754f, 19.18900966644287f, 4.963683784008026f)));
		objects.add(new GameObject(tree, new Vector3(-148.83437156677246f, 45.541934967041016f, 5.204535722732544f)));
		objects.add(new GameObject(tree, new Vector3(-151.12967491149902f, 44.98323440551758f, 4.681068062782288f)));
		objects.add(new GameObject(tree, new Vector3(-150.6764316558838f, 29.152803421020508f, 4.557463824748993f)));
		objects.add(new GameObject(tree, new Vector3(-128.03616523742676f, 78.32770824432373f, 4.255761504173279f)));
		objects.add(new GameObject(tree, new Vector3(-137.3993682861328f, 13.072710037231445f, 4.354563653469086f)));
		objects.add(new GameObject(tree, new Vector3(-142.6872444152832f, 9.857213497161865f, 4.242610931396484f)));
		objects.add(new GameObject(tree, new Vector3(-135.1589584350586f, 75.71610927581787f, 5.655895471572876f)));
		objects.add(new GameObject(tree, new Vector3(-152.38987922668457f, -4.352726340293884f, 4.918278753757477f)));
		objects.add(new GameObject(tree, new Vector3(-148.1906032562256f, 65.0066089630127f, 5.600306391716003f)));
		objects.add(new GameObject(tree, new Vector3(-124.29747581481934f, 80.147705078125f, 4.532715678215027f)));
		objects.add(new GameObject(tree, new Vector3(-135.29973030090332f, 18.497254848480225f, 5.065022706985474f)));
		objects.add(new GameObject(tree, new Vector3(-124.87855911254883f, 75.47506809234619f, 4.488059878349304f)));
		objects.add(new GameObject(tree, new Vector3(-141.70928955078125f, 81.45962715148926f, 5.415248870849609f)));
		objects.add(new GameObject(tree, new Vector3(-131.61385536193848f, 84.62613105773926f, 5.421236753463745f)));
		objects.add(new GameObject(tree, new Vector3(-127.63304710388184f, 76.06051921844482f, 5.758790969848633f)));
		objects.add(new GameObject(tree, new Vector3(-123.41024398803711f, 40.381789207458496f, 5.679665803909302f)));
		objects.add(new GameObject(tree, new Vector3(-120.70358276367188f, 47.45157241821289f, 4.714818298816681f)));
		objects.add(new GameObject(tree, new Vector3(-149.58740234375f, 65.56993961334229f, 4.180670082569122f)));
		objects.add(new GameObject(tree, new Vector3(-151.6530990600586f, 60.673203468322754f, 4.52410489320755f)));
		objects.add(new GameObject(tree, new Vector3(-144.53228950500488f, 15.295919179916382f, 5.477025508880615f)));
		objects.add(new GameObject(tree, new Vector3(-151.69477462768555f, 44.455575942993164f, 4.595103561878204f)));
		objects.add(new GameObject(tree, new Vector3(-152.51132011413574f, 2.5743407011032104f, 4.348345994949341f)));
		objects.add(new GameObject(tree, new Vector3(-148.76694679260254f, 18.1019389629364f, 4.20779287815094f)));
		objects.add(new GameObject(tree, new Vector3(-137.03279495239258f, 19.160064458847046f, 5.611735582351685f)));
		objects.add(new GameObject(tree, new Vector3(-122.83763885498047f, 29.496746063232422f, 4.498444199562073f)));
		objects.add(new GameObject(tree, new Vector3(-126.89948081970215f, 89.52813148498535f, 4.148766100406647f)));
		objects.add(new GameObject(tree, new Vector3(-142.42687225341797f, -4.453842639923096f, 4.171612560749054f)));
		objects.add(new GameObject(tree, new Vector3(-147.4809169769287f, 24.688761234283447f, 4.297720789909363f)));
		objects.add(new GameObject(tree, new Vector3(-136.0710334777832f, 19.362047910690308f, 5.117447376251221f)));
		objects.add(new GameObject(tree, new Vector3(-136.9330596923828f, 88.89529228210449f, 4.693518877029419f)));
		objects.add(new GameObject(tree, new Vector3(-151.02442741394043f, 89.86318588256836f, 4.14562463760376f)));
		objects.add(new GameObject(tree, new Vector3(-124.76255416870117f, 73.54409694671631f, 4.3287986516952515f)));
		objects.add(new GameObject(tree, new Vector3(-144.80485916137695f, 14.436606168746948f, 5.308869481086731f)));
		objects.add(new GameObject(tree, new Vector3(-128.63764762878418f, 16.697787046432495f, 5.041012763977051f)));
		objects.add(new GameObject(tree, new Vector3(-147.8233242034912f, 81.93614959716797f, 4.948101043701172f)));
		objects.add(new GameObject(tree, new Vector3(-152.03575134277344f, 40.65433979034424f, 5.699595212936401f)));
		objects.add(new GameObject(tree, new Vector3(-140.1750373840332f, -1.793212741613388f, 5.700746774673462f)));
		objects.add(new GameObject(tree, new Vector3(-136.0144805908203f, 1.689850091934204f, 4.367094933986664f)));
		objects.add(new GameObject(tree, new Vector3(-149.07882690429688f, 91.02463722229004f, 5.734871029853821f)));
		objects.add(new GameObject(tree, new Vector3(-151.39944076538086f, 8.339967131614685f, 4.796628057956696f)));
		objects.add(new GameObject(tree, new Vector3(-120.02951622009277f, 16.813753843307495f, 5.602636337280273f)));
		objects.add(new GameObject(tree, new Vector3(-120.37112236022949f, 33.03172826766968f, 4.4361114501953125f)));
		objects.add(new GameObject(tree, new Vector3(-120.98265647888184f, 38.306522369384766f, 4.965692162513733f)));
		objects.add(new GameObject(tree, new Vector3(-126.84931755065918f, -37.21599340438843f, 4.441450238227844f)));
		objects.add(new GameObject(tree, new Vector3(-135.10009765625f, -74.35959815979004f, 4.9824342131614685f)));
		objects.add(new GameObject(tree, new Vector3(-146.73215866088867f, -81.22140884399414f, 5.505106449127197f)));
		objects.add(new GameObject(tree, new Vector3(-131.87358856201172f, -63.82795333862305f, 5.51746129989624f)));
		objects.add(new GameObject(tree, new Vector3(-130.05629539489746f, -85.19139289855957f, 5.577021241188049f)));
		objects.add(new GameObject(tree, new Vector3(-124.10235404968262f, -98.63024711608887f, 5.099886059761047f)));
		objects.add(new GameObject(tree, new Vector3(-143.34484100341797f, -30.848886966705322f, 4.676614701747894f)));
		objects.add(new GameObject(tree, new Vector3(-120.82488059997559f, -86.05441093444824f, 4.906229376792908f)));
		objects.add(new GameObject(tree, new Vector3(-145.40847778320312f, -82.62166023254395f, 5.486851930618286f)));
		objects.add(new GameObject(tree, new Vector3(-128.46464157104492f, -97.70889282226562f, 5.094213485717773f)));
		objects.add(new GameObject(tree, new Vector3(-127.18826293945312f, -87.23722457885742f, 4.760201871395111f)));
		objects.add(new GameObject(tree, new Vector3(-139.09546852111816f, -110.89080810546875f, 5.598745942115784f)));
		objects.add(new GameObject(tree, new Vector3(-122.78596878051758f, -54.3989372253418f, 4.79893296957016f)));
		objects.add(new GameObject(tree, new Vector3(-137.95071601867676f, -119.60149765014648f, 5.253927111625671f)));
		objects.add(new GameObject(tree, new Vector3(-141.73439979553223f, -43.05428981781006f, 4.554315805435181f)));
		objects.add(new GameObject(tree, new Vector3(-124.72587585449219f, -76.10567569732666f, 5.617703795433044f)));
		objects.add(new GameObject(tree, new Vector3(-127.0831298828125f, -27.552402019500732f, 5.48004150390625f)));
		objects.add(new GameObject(tree, new Vector3(-120.42274475097656f, -25.8260178565979f, 5.295865535736084f)));
		objects.add(new GameObject(tree, new Vector3(-149.51740264892578f, -100.0806999206543f, 5.17369270324707f)));
		objects.add(new GameObject(tree, new Vector3(-136.8607997894287f, -104.84349250793457f, 4.869992733001709f)));
		objects.add(new GameObject(tree, new Vector3(-132.05995559692383f, -28.019371032714844f, 4.76051926612854f)));
		objects.add(new GameObject(tree, new Vector3(-126.24464988708496f, -95.24720191955566f, 5.47402024269104f)));
		objects.add(new GameObject(tree, new Vector3(-148.26997756958008f, -24.98910665512085f, 4.313948750495911f)));
		objects.add(new GameObject(tree, new Vector3(-139.39902305603027f, -83.05904388427734f, 4.335240423679352f)));
		objects.add(new GameObject(tree, new Vector3(-127.55757331848145f, -105.66287040710449f, 5.3218865394592285f)));
		objects.add(new GameObject(tree, new Vector3(-141.17176055908203f, -66.76045894622803f, 5.5006468296051025f)));
		objects.add(new GameObject(tree, new Vector3(-123.08521270751953f, -82.41734504699707f, 5.313515663146973f)));
		objects.add(new GameObject(tree, new Vector3(-138.12901496887207f, -32.01858997344971f, 4.786081612110138f)));
		objects.add(new GameObject(tree, new Vector3(-148.9866542816162f, -45.010528564453125f, 4.299136400222778f)));
		objects.add(new GameObject(tree, new Vector3(-125.98650932312012f, -96.41609191894531f, 4.743218719959259f)));
		objects.add(new GameObject(tree, new Vector3(-119.26571846008301f, -118.78920555114746f, 5.212852954864502f)));
		objects.add(new GameObject(tree, new Vector3(-136.0386562347412f, -23.82380962371826f, 4.669337272644043f)));
		objects.add(new GameObject(tree, new Vector3(-145.14063835144043f, -92.83596992492676f, 4.28059458732605f)));
		objects.add(new GameObject(tree, new Vector3(-139.97254371643066f, -48.73642444610596f, 4.316178560256958f)));
		objects.add(new GameObject(tree, new Vector3(-134.73175048828125f, -116.15211486816406f, 4.3705689907073975f)));
		objects.add(new GameObject(tree, new Vector3(-145.79011917114258f, -47.82001972198486f, 5.73805034160614f)));
		objects.add(new GameObject(tree, new Vector3(-139.4576358795166f, -24.258854389190674f, 4.847650825977325f)));
		objects.add(new GameObject(tree, new Vector3(-135.14363288879395f, -97.68661499023438f, 5.272255539894104f)));
		objects.add(new GameObject(tree, new Vector3(-150.16607284545898f, -75.98051071166992f, 4.787388741970062f)));
		objects.add(new GameObject(tree, new Vector3(-142.60775566101074f, -23.424465656280518f, 5.715411305427551f)));
		objects.add(new GameObject(tree, new Vector3(-129.63793754577637f, -41.502718925476074f, 4.717271327972412f)));
		objects.add(new GameObject(tree, new Vector3(-118.79206657409668f, -115.8786392211914f, 4.356731474399567f)));
		objects.add(new GameObject(tree, new Vector3(-151.78625106811523f, -75.2733564376831f, 4.584779143333435f)));
		objects.add(new GameObject(tree, new Vector3(-125.73051452636719f, -64.84311580657959f, 5.217100381851196f)));
		objects.add(new GameObject(tree, new Vector3(-121.64092063903809f, -67.51739978790283f, 5.527803301811218f)));
		objects.add(new GameObject(tree, new Vector3(-122.10947036743164f, -30.737266540527344f, 5.068874359130859f)));
		objects.add(new GameObject(tree, new Vector3(-144.07959938049316f, -75.8601713180542f, 5.083509683609009f)));
		objects.add(new GameObject(tree, new Vector3(-120.71846961975098f, -121.7220687866211f, 5.549068450927734f)));
		objects.add(new GameObject(tree, new Vector3(-123.90538215637207f, -83.35816383361816f, 4.580124318599701f)));
		objects.add(new GameObject(tree, new Vector3(-133.2612133026123f, -76.71339511871338f, 4.769991636276245f)));
		objects.add(new GameObject(tree, new Vector3(-130.31322479248047f, -111.21837615966797f, 5.320888161659241f)));
		objects.add(new GameObject(tree, new Vector3(-126.35293006896973f, -49.586191177368164f, 5.6651222705841064f)));
		objects.add(new GameObject(tree, new Vector3(-143.1186294555664f, -47.47297763824463f, 4.240741729736328f)));
		objects.add(new GameObject(tree, new Vector3(-128.02040100097656f, -46.612300872802734f, 4.573303759098053f)));
		objects.add(new GameObject(tree, new Vector3(-148.59034538269043f, -33.67703437805176f, 4.413851499557495f)));
		objects.add(new GameObject(tree, new Vector3(-132.71859169006348f, -101.76913261413574f, 4.240438342094421f)));
		objects.add(new GameObject(tree, new Vector3(-144.17957305908203f, -33.58873128890991f, 5.511561036109924f)));
		objects.add(new GameObject(tree, new Vector3(-148.70582580566406f, -121.6875171661377f, 4.987992346286774f)));
		objects.add(new GameObject(tree, new Vector3(-138.3012580871582f, -120.18345832824707f, 5.349981188774109f)));
		objects.add(new GameObject(tree, new Vector3(-130.22879600524902f, -37.138614654541016f, 5.5984944105148315f)));
		objects.add(new GameObject(tree, new Vector3(-130.38707733154297f, -24.577739238739014f, 4.280261993408203f)));
		objects.add(new GameObject(tree, new Vector3(-141.06231689453125f, -120.24954795837402f, 5.636888742446899f)));
		objects.add(new GameObject(tree, new Vector3(-128.4950828552246f, -35.78638792037964f, 5.179510712623596f)));
		objects.add(new GameObject(tree, new Vector3(-151.01701736450195f, -102.09898948669434f, 4.526109099388123f)));
		objects.add(new GameObject(tree, new Vector3(-135.79510688781738f, -83.90679359436035f, 4.373071193695068f)));
		objects.add(new GameObject(tree, new Vector3(-120.07260322570801f, -56.92573070526123f, 4.314239025115967f)));
		objects.add(new GameObject(tree, new Vector3(-132.39798545837402f, -37.36551523208618f, 4.84967827796936f)));
		objects.add(new GameObject(tree, new Vector3(-125.78727722167969f, -82.53034591674805f, 5.1679527759552f)));
		objects.add(new GameObject(tree, new Vector3(-134.51655387878418f, -73.57624053955078f, 4.6051013469696045f)));
		objects.add(new GameObject(tree, new Vector3(-150.03856658935547f, -36.78288459777832f, 5.531232953071594f)));
		objects.add(new GameObject(tree, new Vector3(-148.5851001739502f, -54.076857566833496f, 5.401210784912109f)));
		objects.add(new GameObject(tree, new Vector3(-124.09783363342285f, -88.15776824951172f, 5.4911768436431885f)));
		objects.add(new GameObject(tree, new Vector3(-135.50854682922363f, -74.92392539978027f, 5.1522427797317505f)));
		objects.add(new GameObject(tree, new Vector3(-131.79731369018555f, -109.87856864929199f, 5.564005374908447f)));
		objects.add(new GameObject(tree, new Vector3(-130.97485542297363f, -101.90488815307617f, 5.199868083000183f)));
		objects.add(new GameObject(tree, new Vector3(-137.34705924987793f, -63.80776405334473f, 4.36219334602356f)));
		objects.add(new GameObject(tree, new Vector3(-133.31597328186035f, -47.47092247009277f, 4.961094558238983f)));
		objects.add(new GameObject(tree, new Vector3(-119.12483215332031f, -116.49617195129395f, 5.205666422843933f)));
		objects.add(new GameObject(tree, new Vector3(-128.64044189453125f, -45.10825157165527f, 5.631945729255676f)));
		objects.add(new GameObject(tree, new Vector3(-121.28268241882324f, -94.5669174194336f, 5.461238622665405f)));
		objects.add(new GameObject(tree, new Vector3(-139.23420906066895f, -44.784016609191895f, 5.529869794845581f)));
		objects.add(new GameObject(tree, new Vector3(-151.7640209197998f, -32.031376361846924f, 4.7662705183029175f)));
		objects.add(new GameObject(tree, new Vector3(-144.28189277648926f, -70.49778938293457f, 4.261847734451294f)));
		objects.add(new GameObject(tree, new Vector3(-126.25734329223633f, -51.25661373138428f, 5.157432556152344f)));
		objects.add(new GameObject(tree, new Vector3(-145.16009330749512f, -33.835532665252686f, 5.407203435897827f)));
		objects.add(new GameObject(tree, new Vector3(-141.31278038024902f, -113.75954627990723f, 5.704537630081177f)));
		objects.add(new GameObject(tree, new Vector3(-142.26518630981445f, -67.72514343261719f, 5.4280537366867065f)));
		objects.add(new GameObject(tree, new Vector3(-128.36904525756836f, -21.557190418243408f, 4.163752198219299f)));
		objects.add(new GameObject(tree, new Vector3(-149.49463844299316f, -109.62882995605469f, 4.489729106426239f)));
		objects.add(new GameObject(tree, new Vector3(-122.87182807922363f, -102.37330436706543f, 5.236388444900513f)));
		objects.add(new GameObject(tree, new Vector3(-121.21085166931152f, -63.07303428649902f, 4.282497763633728f)));
		objects.add(new GameObject(tree, new Vector3(-138.32630157470703f, -88.9609146118164f, 5.637022256851196f)));
		objects.add(new GameObject(tree, new Vector3(-136.80583000183105f, -86.17020606994629f, 4.6524447202682495f)));
		objects.add(new GameObject(tree, new Vector3(-139.7996997833252f, -110.59807777404785f, 4.797143340110779f)));
		objects.add(new GameObject(tree, new Vector3(-134.59418296813965f, -117.49497413635254f, 4.731407463550568f)));
		objects.add(new GameObject(tree, new Vector3(-137.6517105102539f, -88.47555160522461f, 5.6681400537490845f)));
		objects.add(new GameObject(tree, new Vector3(-127.93909072875977f, -85.21241188049316f, 5.056331157684326f)));
		objects.add(new GameObject(tree, new Vector3(-150.92472076416016f, -78.57057571411133f, 5.043002367019653f)));
		objects.add(new GameObject(tree, new Vector3(-134.87727165222168f, -34.137866497039795f, 5.673614144325256f)));
		objects.add(new GameObject(tree, new Vector3(-150.65847396850586f, -40.43797492980957f, 4.897754490375519f)));
		objects.add(new GameObject(tree, new Vector3(-121.68764114379883f, -85.77787399291992f, 5.359517931938171f)));
		objects.add(new GameObject(tree, new Vector3(-147.183837890625f, -98.33159446716309f, 5.615109801292419f)));
		objects.add(new GameObject(tree, new Vector3(-125.66057205200195f, -28.868162631988525f, 4.920791983604431f)));
		objects.add(new GameObject(tree, new Vector3(-145.97599983215332f, -43.56281280517578f, 4.861203730106354f)));
		objects.add(new GameObject(tree, new Vector3(-148.6962890625f, -39.28122043609619f, 4.148050546646118f)));
		objects.add(new GameObject(tree, new Vector3(-140.00771522521973f, -107.06095695495605f, 5.45318603515625f)));
		objects.add(new GameObject(tree, new Vector3(-142.6078987121582f, -89.97139930725098f, 5.590487122535706f)));
		objects.add(new GameObject(tree, new Vector3(-121.20390892028809f, -77.51418113708496f, 5.485163927078247f)));
		objects.add(new GameObject(tree, new Vector3(-124.95246887207031f, -123.0549430847168f, 4.443018436431885f)));
		objects.add(new GameObject(tree, new Vector3(-137.69515991210938f, -54.49573993682861f, 5.674921870231628f)));
		objects.add(new GameObject(tree, new Vector3(-120.545654296875f, -72.4929428100586f, 5.154898166656494f)));
		objects.add(new GameObject(tree, new Vector3(-127.8366470336914f, -91.21761322021484f, 4.8049309849739075f)));
		objects.add(new GameObject(tree, new Vector3(-146.2720012664795f, -90.32527923583984f, 4.886061251163483f)));
		objects.add(new GameObject(tree, new Vector3(-140.67413330078125f, -22.279152870178223f, 4.930607080459595f)));
		objects.add(new GameObject(tree, new Vector3(-123.05888175964355f, -70.97721576690674f, 4.287601113319397f)));
		objects.add(new GameObject(tree, new Vector3(-122.22176551818848f, -32.79600143432617f, 5.7662248611450195f)));
		objects.add(new GameObject(tree, new Vector3(-141.74294471740723f, -51.13858222961426f, 5.471447706222534f)));
		objects.add(new GameObject(tree, new Vector3(-131.50818824768066f, -65.0919771194458f, 4.740660786628723f)));
		objects.add(new GameObject(tree, new Vector3(-143.86174201965332f, -90.50743103027344f, 4.365251660346985f)));
		objects.add(new GameObject(tree, new Vector3(-140.20280838012695f, -63.35449695587158f, 4.716293215751648f)));
		objects.add(new GameObject(tree, new Vector3(-133.45792770385742f, -96.20532989501953f, 4.4473278522491455f)));
		objects.add(new GameObject(tree, new Vector3(-127.81530380249023f, -94.5750904083252f, 4.462009966373444f)));
		objects.add(new GameObject(tree, new Vector3(-150.1045322418213f, -37.33898162841797f, 4.6363988518714905f)));
		objects.add(new GameObject(tree, new Vector3(-127.82527923583984f, -53.322505950927734f, 5.288458466529846f)));
		objects.add(new GameObject(tree, new Vector3(-119.27082061767578f, -139.83452796936035f, 4.9274808168411255f)));
		objects.add(new GameObject(tree, new Vector3(-117.73494720458984f, -148.9859104156494f, 4.768724143505096f)));
		objects.add(new GameObject(tree, new Vector3(-138.02433013916016f, -143.97032737731934f, 5.528389811515808f)));
		objects.add(new GameObject(tree, new Vector3(-147.03486442565918f, -135.1491641998291f, 5.425768494606018f)));
		objects.add(new GameObject(tree, new Vector3(-125.74813842773438f, -147.8829574584961f, 4.1420310735702515f)));
		objects.add(new GameObject(tree, new Vector3(-129.5670509338379f, -129.21442985534668f, 4.261589348316193f)));
		objects.add(new GameObject(tree, new Vector3(-122.73359298706055f, -128.19518089294434f, 5.703486204147339f)));
		objects.add(new GameObject(tree, new Vector3(-121.02559089660645f, -141.28068923950195f, 5.102224349975586f)));
		objects.add(new GameObject(tree, new Vector3(-146.59278869628906f, -148.94323348999023f, 4.459754824638367f)));
		objects.add(new GameObject(tree, new Vector3(-134.17738914489746f, -128.64482879638672f, 5.6715720891952515f)));
		objects.add(new GameObject(tree, new Vector3(-137.4141788482666f, -145.12395858764648f, 5.687230825424194f)));
		objects.add(new GameObject(tree, new Vector3(-121.37200355529785f, -139.7573184967041f, 5.147395133972168f)));
		objects.add(new GameObject(tree, new Vector3(-129.2775821685791f, -129.01483535766602f, 4.531567096710205f)));
		objects.add(new GameObject(tree, new Vector3(-127.49997138977051f, -143.3298110961914f, 5.637892484664917f)));
		objects.add(new GameObject(tree, new Vector3(-150.4587459564209f, -147.7657985687256f, 4.699137508869171f)));
		objects.add(new GameObject(tree, new Vector3(-148.82511138916016f, -147.3723030090332f, 4.188653230667114f)));
		objects.add(new GameObject(tree, new Vector3(-149.1049289703369f, -149.44299697875977f, 5.332436561584473f)));
		objects.add(new GameObject(tree, new Vector3(-121.96331024169922f, -150.79901695251465f, 4.757238924503326f)));
		objects.add(new GameObject(tree, new Vector3(-143.87084007263184f, -142.47342109680176f, 4.268094599246979f)));
		objects.add(new GameObject(tree, new Vector3(-147.03519821166992f, -143.83990287780762f, 4.147697389125824f)));
		objects.add(new GameObject(tree, new Vector3(-124.00065422058105f, -152.36599922180176f, 4.441259801387787f)));
		objects.add(new GameObject(tree, new Vector3(-122.70605087280273f, -150.5198097229004f, 4.131295680999756f)));
		objects.add(new GameObject(tree, new Vector3(-126.41000747680664f, -136.98920249938965f, 4.540724754333496f)));
		objects.add(new GameObject(tree, new Vector3(-152.40546226501465f, -150.64209938049316f, 4.787602722644806f)));
		objects.add(new GameObject(tree, new Vector3(-148.90704154968262f, -132.29986190795898f, 4.142056405544281f)));
		objects.add(new GameObject(tree, new Vector3(-149.89670753479004f, -140.79927444458008f, 4.889157712459564f)));
		objects.add(new GameObject(tree, new Vector3(-117.15367317199707f, -129.2659091949463f, 4.775042533874512f)));
		objects.add(new GameObject(tree, new Vector3(-143.2081699371338f, -129.45895195007324f, 5.23736298084259f)));
		objects.add(new GameObject(tree, new Vector3(-131.80237770080566f, -130.83683967590332f, 5.6292736530303955f)));
		objects.add(new GameObject(tree, new Vector3(-131.53396606445312f, -124.86752510070801f, 4.261118471622467f)));
		objects.add(new GameObject(tree, new Vector3(-148.7328815460205f, -147.10103034973145f, 4.863516688346863f)));
		objects.add(new GameObject(tree, new Vector3(-135.17373085021973f, -139.28861618041992f, 5.041701793670654f)));
		objects.add(new GameObject(tree, new Vector3(-117.73259162902832f, -125.36349296569824f, 4.74638044834137f)));
		objects.add(new GameObject(tree, new Vector3(-119.60911750793457f, -149.98629570007324f, 4.392763674259186f)));
		objects.add(new GameObject(tree, new Vector3(-140.85137367248535f, -146.48981094360352f, 4.922066926956177f)));
		objects.add(new GameObject(tree, new Vector3(-126.69293403625488f, -139.9455165863037f, 4.8449501395225525f)));
		objects.add(new GameObject(tree, new Vector3(-150.40410995483398f, -134.42126274108887f, 5.350350737571716f)));
		objects.add(new GameObject(tree, new Vector3(-142.67192840576172f, -151.6991138458252f, 5.2215176820755005f)));
		objects.add(new GameObject(tree, new Vector3(-116.89823150634766f, -148.80268096923828f, 4.430943131446838f)));
		objects.add(new GameObject(tree, new Vector3(-115.22796630859375f, -128.08417320251465f, 4.962687194347382f)));
		objects.add(new GameObject(tree, new Vector3(-131.26994132995605f, -139.14217948913574f, 4.3735072016716f)));
		objects.add(new GameObject(tree, new Vector3(-115.73373794555664f, -134.7188949584961f, 4.918338656425476f)));
		objects.add(new GameObject(tree, new Vector3(-118.89704704284668f, -134.79175567626953f, 4.6942585706710815f)));
		objects.add(new GameObject(tree, new Vector3(-133.1321620941162f, -151.7356777191162f, 5.316935181617737f)));
		objects.add(new GameObject(tree, new Vector3(-130.9819221496582f, -139.69170570373535f, 4.510225057601929f)));
		objects.add(new GameObject(tree, new Vector3(-150.43512344360352f, -139.5417022705078f, 4.543977677822113f)));
		objects.add(new GameObject(tree, new Vector3(-116.44413948059082f, -142.19656944274902f, 5.534844994544983f)));
		objects.add(new GameObject(tree, new Vector3(-136.10322952270508f, -140.3359889984131f, 4.1245004534721375f)));
		objects.add(new GameObject(tree, new Vector3(-136.9811248779297f, -152.73053169250488f, 4.353460967540741f)));
		objects.add(new GameObject(tree, new Vector3(-120.73368072509766f, -151.2345314025879f, 5.373068451881409f)));
		objects.add(new GameObject(tree, new Vector3(-116.3417911529541f, -151.39050483703613f, 5.334005951881409f)));
		objects.add(new GameObject(tree, new Vector3(-122.95759201049805f, -139.71529006958008f, 5.420898199081421f)));
		objects.add(new GameObject(tree, new Vector3(124.91421699523926f, -139.89585876464844f, 4.362685978412628f)));
		objects.add(new GameObject(tree, new Vector3(149.74843978881836f, -127.39415168762207f, 5.172985196113586f)));
		objects.add(new GameObject(tree, new Vector3(141.62799835205078f, -138.4939956665039f, 4.610351026058197f)));
		objects.add(new GameObject(tree, new Vector3(124.89745140075684f, -147.3795509338379f, 4.235402047634125f)));
		objects.add(new GameObject(tree, new Vector3(135.3199005126953f, -140.63613891601562f, 5.71044385433197f)));
		objects.add(new GameObject(tree, new Vector3(130.21291732788086f, -136.60024642944336f, 5.645906329154968f)));
		objects.add(new GameObject(tree, new Vector3(134.76497650146484f, -148.9535140991211f, 4.670788645744324f)));
		objects.add(new GameObject(tree, new Vector3(149.6047306060791f, -152.30511665344238f, 4.554077982902527f)));
		objects.add(new GameObject(tree, new Vector3(147.763090133667f, -134.21701431274414f, 5.326692461967468f)));
		objects.add(new GameObject(tree, new Vector3(151.92163467407227f, -140.39186477661133f, 4.187363088130951f)));
		objects.add(new GameObject(tree, new Vector3(148.4426498413086f, -142.2540283203125f, 4.541405737400055f)));
		objects.add(new GameObject(tree, new Vector3(137.34535217285156f, -131.78235054016113f, 4.797951579093933f)));
		objects.add(new GameObject(tree, new Vector3(124.70183372497559f, -141.25842094421387f, 4.859059154987335f)));
		objects.add(new GameObject(tree, new Vector3(147.25835800170898f, -132.22084045410156f, 4.156703948974609f)));
		objects.add(new GameObject(tree, new Vector3(141.02900505065918f, -138.55050086975098f, 5.563420057296753f)));
		objects.add(new GameObject(tree, new Vector3(134.55388069152832f, -134.40990447998047f, 5.462666153907776f)));
		objects.add(new GameObject(tree, new Vector3(132.5370216369629f, -136.2747573852539f, 5.360466241836548f)));
		objects.add(new GameObject(tree, new Vector3(133.59803199768066f, -147.62736320495605f, 4.421142041683197f)));
		objects.add(new GameObject(tree, new Vector3(131.32744789123535f, -146.48228645324707f, 4.981439709663391f)));
		objects.add(new GameObject(tree, new Vector3(130.77587127685547f, -135.7679557800293f, 5.581923127174377f)));
		objects.add(new GameObject(tree, new Vector3(148.91118049621582f, -126.4592456817627f, 4.948537349700928f)));
		objects.add(new GameObject(tree, new Vector3(130.73657989501953f, -147.568941116333f, 4.251137375831604f)));
		objects.add(new GameObject(tree, new Vector3(137.87400245666504f, -139.56037521362305f, 5.0154536962509155f)));
		objects.add(new GameObject(tree, new Vector3(124.13351058959961f, -134.35093879699707f, 5.705804228782654f)));
		objects.add(new GameObject(tree, new Vector3(125.02076148986816f, -144.91266250610352f, 4.764919579029083f)));
		objects.add(new GameObject(tree, new Vector3(131.38324737548828f, -145.27392387390137f, 5.6583380699157715f)));
		objects.add(new GameObject(tree, new Vector3(126.08230590820312f, -130.65637588500977f, 4.798037111759186f)));
		objects.add(new GameObject(tree, new Vector3(149.8649787902832f, -150.74241638183594f, 4.398590922355652f)));
		objects.add(new GameObject(tree, new Vector3(149.19633865356445f, -130.25498390197754f, 4.365563988685608f)));
		objects.add(new GameObject(tree, new Vector3(144.19852256774902f, -150.17351150512695f, 5.337724089622498f)));
		objects.add(new GameObject(tree, new Vector3(128.877534866333f, -144.67490196228027f, 5.060224533081055f)));
		objects.add(new GameObject(tree, new Vector3(127.27851867675781f, -149.58048820495605f, 5.583217740058899f)));
		objects.add(new GameObject(tree, new Vector3(134.7635555267334f, -140.7734489440918f, 4.329594671726227f)));
		objects.add(new GameObject(tree, new Vector3(146.08582496643066f, -135.77309608459473f, 4.733612239360809f)));
		objects.add(new GameObject(tree, new Vector3(134.67226028442383f, -131.04342460632324f, 4.515693187713623f)));
		objects.add(new GameObject(tree, new Vector3(139.21697616577148f, -142.5106430053711f, 5.529884099960327f)));
		objects.add(new GameObject(tree, new Vector3(126.60656929016113f, -139.7524356842041f, 5.554206967353821f)));
		objects.add(new GameObject(tree, new Vector3(-102.40249633789062f, -141.3045883178711f, 5.523078441619873f)));
		objects.add(new GameObject(tree, new Vector3(-83.53507995605469f, -130.27935028076172f, 4.206578433513641f)));
		objects.add(new GameObject(tree, new Vector3(-73.87762069702148f, -149.29323196411133f, 5.171549916267395f)));
		objects.add(new GameObject(tree, new Vector3(-92.7686882019043f, -151.52206420898438f, 5.178409218788147f)));
		objects.add(new GameObject(tree, new Vector3(-95.4042911529541f, -141.84791564941406f, 5.52772581577301f)));
		objects.add(new GameObject(tree, new Vector3(-85.17899513244629f, -143.07080268859863f, 4.273062646389008f)));
		objects.add(new GameObject(tree, new Vector3(-75.14220237731934f, -141.4101791381836f, 5.393139719963074f)));
		objects.add(new GameObject(tree, new Vector3(-77.4504280090332f, -143.30949783325195f, 4.441976249217987f)));
		objects.add(new GameObject(tree, new Vector3(-76.01625919342041f, -143.2161045074463f, 4.191192388534546f)));
		objects.add(new GameObject(tree, new Vector3(-98.04542541503906f, -126.4830207824707f, 4.467611312866211f)));
		objects.add(new GameObject(tree, new Vector3(-106.95611000061035f, -141.13214492797852f, 4.4176411628723145f)));
		objects.add(new GameObject(tree, new Vector3(-99.8525619506836f, -152.3123264312744f, 4.403690993785858f)));
		objects.add(new GameObject(tree, new Vector3(-103.80748748779297f, -129.72877502441406f, 4.333039224147797f)));
		objects.add(new GameObject(tree, new Vector3(-107.05178260803223f, -133.53468894958496f, 4.538994133472443f)));
		objects.add(new GameObject(tree, new Vector3(-94.90096092224121f, -147.9104709625244f, 4.78120744228363f)));
		objects.add(new GameObject(tree, new Vector3(-74.39821720123291f, -149.2610740661621f, 4.83317106962204f)));
		objects.add(new GameObject(tree, new Vector3(-109.00166511535645f, -136.45977020263672f, 5.266095995903015f)));
		objects.add(new GameObject(tree, new Vector3(-103.08708190917969f, -144.9954319000244f, 5.203351378440857f)));
		objects.add(new GameObject(tree, new Vector3(-76.28021717071533f, -131.5955638885498f, 4.197084903717041f)));
		objects.add(new GameObject(tree, new Vector3(-90.58069229125977f, -146.83263778686523f, 4.709052741527557f)));
		objects.add(new GameObject(tree, new Vector3(-108.97686958312988f, -146.00586891174316f, 4.933805465698242f)));
		objects.add(new GameObject(tree, new Vector3(-76.65040016174316f, -136.96386337280273f, 5.058664679527283f)));
		objects.add(new GameObject(tree, new Vector3(-88.04502487182617f, -142.17080116271973f, 5.647037625312805f)));
		objects.add(new GameObject(tree, new Vector3(-102.67361640930176f, -142.05429077148438f, 4.759143888950348f)));
		objects.add(new GameObject(tree, new Vector3(-109.19594764709473f, -138.95977020263672f, 5.0164371728897095f)));
		objects.add(new GameObject(tree, new Vector3(-98.6141300201416f, -150.0077724456787f, 4.997121095657349f)));
		objects.add(new GameObject(tree, new Vector3(-73.08526515960693f, -131.05448722839355f, 4.7494372725486755f)));
		objects.add(new GameObject(tree, new Vector3(-78.62144470214844f, -134.9732494354248f, 4.408935308456421f)));
		objects.add(new GameObject(tree, new Vector3(-104.9667739868164f, -146.54085159301758f, 4.587172865867615f)));
		objects.add(new GameObject(tree, new Vector3(-84.81460571289062f, -147.8266429901123f, 5.279952883720398f)));
		objects.add(new GameObject(tree, new Vector3(-101.56522750854492f, -143.16673278808594f, 4.685432016849518f)));
		objects.add(new GameObject(tree, new Vector3(-89.63830947875977f, -128.21232795715332f, 4.824805557727814f)));
		objects.add(new GameObject(tree, new Vector3(-85.16533851623535f, -133.25980186462402f, 5.65397322177887f)));
		objects.add(new GameObject(tree, new Vector3(-105.61491966247559f, -152.21463203430176f, 5.27435302734375f)));
		objects.add(new GameObject(tree, new Vector3(-77.22268581390381f, -124.4685173034668f, 5.058496594429016f)));
		objects.add(new GameObject(tree, new Vector3(-79.4664192199707f, -138.31293106079102f, 5.0808775424957275f)));
		objects.add(new GameObject(tree, new Vector3(-111.54603004455566f, -141.20100021362305f, 4.212061166763306f)));
		objects.add(new GameObject(tree, new Vector3(-72.72342205047607f, -143.18666458129883f, 4.87415611743927f)));
		objects.add(new GameObject(tree, new Vector3(-100.35348892211914f, -144.85107421875f, 4.238400757312775f)));
		objects.add(new GameObject(tree, new Vector3(-83.86757850646973f, -143.66633415222168f, 5.609904527664185f)));
		objects.add(new GameObject(tree, new Vector3(-97.33586311340332f, -148.1369972229004f, 4.794777035713196f)));
		objects.add(new GameObject(tree, new Vector3(-77.88409233093262f, -126.05218887329102f, 4.840741157531738f)));
		objects.add(new GameObject(tree, new Vector3(-112.7098274230957f, -130.91025352478027f, 4.537823796272278f)));
		objects.add(new GameObject(tree, new Vector3(-95.90599060058594f, -136.20844841003418f, 4.706638157367706f)));
		objects.add(new GameObject(tree, new Vector3(-99.46361541748047f, -148.853759765625f, 5.5980002880096436f)));
		objects.add(new GameObject(tree, new Vector3(-109.86159324645996f, -145.90593338012695f, 4.261187016963959f)));
		objects.add(new GameObject(tree, new Vector3(-100.73129653930664f, -148.9070987701416f, 4.727985262870789f)));
		objects.add(new GameObject(tree, new Vector3(-75.05730152130127f, -139.8641300201416f, 5.274678468704224f)));
		objects.add(new GameObject(tree, new Vector3(-91.01613998413086f, -146.56575202941895f, 5.44340968132019f)));
		objects.add(new GameObject(tree, new Vector3(-36.10135078430176f, -128.94757270812988f, 4.845851957798004f)));
		objects.add(new GameObject(tree, new Vector3(-26.276750564575195f, -143.80950927734375f, 5.261815786361694f)));
		objects.add(new GameObject(tree, new Vector3(0.7697753608226776f, -131.71775817871094f, 5.160264372825623f)));
		objects.add(new GameObject(tree, new Vector3(-22.51431941986084f, -131.854248046875f, 4.421812295913696f)));
		objects.add(new GameObject(tree, new Vector3(0.7114171981811523f, -150.00937461853027f, 5.392748713493347f)));
		objects.add(new GameObject(tree, new Vector3(-44.97709274291992f, -145.895357131958f, 4.625860154628754f)));
		objects.add(new GameObject(tree, new Vector3(-30.90057373046875f, -138.6496067047119f, 5.64416229724884f)));
		objects.add(new GameObject(tree, new Vector3(-42.969956398010254f, -126.6126823425293f, 5.67139744758606f)));
		objects.add(new GameObject(tree, new Vector3(-5.624202489852905f, -133.29904556274414f, 5.079678893089294f)));
		objects.add(new GameObject(tree, new Vector3(-49.77876663208008f, -147.04097747802734f, 4.651101231575012f)));
		objects.add(new GameObject(tree, new Vector3(-1.2252042442560196f, -133.0846881866455f, 5.040343999862671f)));
		objects.add(new GameObject(tree, new Vector3(-2.2887909412384033f, -132.34136581420898f, 5.198578834533691f)));
		objects.add(new GameObject(tree, new Vector3(-25.304641723632812f, -128.80227088928223f, 5.391218066215515f)));
		objects.add(new GameObject(tree, new Vector3(-2.384324073791504f, -144.44804191589355f, 5.595226287841797f)));
		objects.add(new GameObject(tree, new Vector3(-44.29112434387207f, -146.42821311950684f, 4.621199369430542f)));
		objects.add(new GameObject(tree, new Vector3(-51.35573387145996f, -146.35323524475098f, 5.511141419410706f)));
		objects.add(new GameObject(tree, new Vector3(-45.698485374450684f, -148.12036514282227f, 5.209177136421204f)));
		objects.add(new GameObject(tree, new Vector3(-47.594571113586426f, -145.374755859375f, 4.28383857011795f)));
		objects.add(new GameObject(tree, new Vector3(-10.040777921676636f, -146.84484481811523f, 5.6869882345199585f)));
		objects.add(new GameObject(tree, new Vector3(-36.06101989746094f, -138.4951400756836f, 5.67000150680542f)));
		objects.add(new GameObject(tree, new Vector3(-44.59770679473877f, -146.41990661621094f, 4.240961372852325f)));
		objects.add(new GameObject(tree, new Vector3(-45.647215843200684f, -138.63920211791992f, 4.151444137096405f)));
		objects.add(new GameObject(tree, new Vector3(-4.201804101467133f, -134.15754318237305f, 5.512611269950867f)));
		objects.add(new GameObject(tree, new Vector3(-10.767402648925781f, -150.377836227417f, 4.183657169342041f)));
		objects.add(new GameObject(tree, new Vector3(-23.602712154388428f, -132.79438972473145f, 5.572991371154785f)));
		objects.add(new GameObject(tree, new Vector3(-16.14283561706543f, -140.74461936950684f, 5.054372549057007f)));
		objects.add(new GameObject(tree, new Vector3(-48.079538345336914f, -125.75301170349121f, 5.225103497505188f)));
		objects.add(new GameObject(tree, new Vector3(-45.890841484069824f, -149.15118217468262f, 5.734506249427795f)));
		objects.add(new GameObject(tree, new Vector3(-52.16954231262207f, -139.65516090393066f, 4.70189094543457f)));
		objects.add(new GameObject(tree, new Vector3(-48.971824645996094f, -136.77964210510254f, 5.575827360153198f)));
		objects.add(new GameObject(tree, new Vector3(-53.80643367767334f, -140.2023983001709f, 5.379049181938171f)));
		objects.add(new GameObject(tree, new Vector3(-51.43218994140625f, -140.80228805541992f, 5.5994755029678345f)));
		objects.add(new GameObject(tree, new Vector3(-18.209532499313354f, -145.5691909790039f, 4.454968273639679f)));
		objects.add(new GameObject(tree, new Vector3(-23.478586673736572f, -144.9034595489502f, 4.303183555603027f)));
		objects.add(new GameObject(tree, new Vector3(-4.926232397556305f, -143.69385719299316f, 4.800048768520355f)));
		objects.add(new GameObject(tree, new Vector3(-32.159061431884766f, -126.19170188903809f, 5.704454779624939f)));
		objects.add(new GameObject(tree, new Vector3(-29.50132131576538f, -150.70728302001953f, 4.3701231479644775f)));
		objects.add(new GameObject(tree, new Vector3(-15.854588747024536f, -143.83466720581055f, 5.498422384262085f)));
		objects.add(new GameObject(tree, new Vector3(-2.3514504730701447f, -133.7854290008545f, 4.170047342777252f)));
		objects.add(new GameObject(tree, new Vector3(-51.58200740814209f, -125.40681838989258f, 4.308038055896759f)));
		objects.add(new GameObject(tree, new Vector3(-30.951833724975586f, -132.53474235534668f, 5.50909161567688f)));
		objects.add(new GameObject(tree, new Vector3(-8.55065107345581f, -134.39818382263184f, 4.8644861578941345f)));
		objects.add(new GameObject(tree, new Vector3(-4.096834659576416f, -130.38076400756836f, 4.214411079883575f)));
		objects.add(new GameObject(tree, new Vector3(-52.55423069000244f, -146.92859649658203f, 4.450684487819672f)));
		objects.add(new GameObject(tree, new Vector3(-23.473632335662842f, -125.47547340393066f, 5.597537159919739f)));
		objects.add(new GameObject(tree, new Vector3(-5.294346213340759f, -151.04225158691406f, 4.15097713470459f)));
		objects.add(new GameObject(tree, new Vector3(-11.28568172454834f, -138.5077953338623f, 5.666400790214539f)));
		objects.add(new GameObject(tree, new Vector3(-22.096590995788574f, -142.48196601867676f, 4.863863885402679f)));
		objects.add(new GameObject(tree, new Vector3(-17.150468826293945f, -147.57858276367188f, 5.340273976325989f)));
		objects.add(new GameObject(tree, new Vector3(-2.604725956916809f, -131.68006896972656f, 5.4948341846466064f)));
		objects.add(new GameObject(tree, new Vector3(-13.148524761199951f, -126.20344161987305f, 4.427923262119293f)));
		objects.add(new GameObject(tree, new Vector3(-5.81438422203064f, -136.43397331237793f, 5.129312872886658f)));
		objects.add(new GameObject(tree, new Vector3(-17.075072526931763f, -148.9050006866455f, 4.382392168045044f)));
		objects.add(new GameObject(tree, new Vector3(-10.152921676635742f, -130.50373077392578f, 4.368369877338409f)));
		objects.add(new GameObject(tree, new Vector3(-24.176466464996338f, -143.57589721679688f, 4.754877388477325f)));
		objects.add(new GameObject(tree, new Vector3(-19.438551664352417f, -136.60921096801758f, 5.330071449279785f)));
		objects.add(new GameObject(tree, new Vector3(-5.088366270065308f, -131.30375862121582f, 5.669564604759216f)));
		objects.add(new GameObject(tree, new Vector3(-10.31584620475769f, -146.5215301513672f, 5.190291404724121f)));
		objects.add(new GameObject(tree, new Vector3(-23.016605377197266f, -142.05963134765625f, 5.546305775642395f)));
		objects.add(new GameObject(tree, new Vector3(-51.715545654296875f, -135.68902015686035f, 5.584850311279297f)));
		objects.add(new GameObject(tree, new Vector3(-35.72242498397827f, -132.1479606628418f, 5.629347562789917f)));
		objects.add(new GameObject(tree, new Vector3(-30.166985988616943f, -139.45334434509277f, 5.586364269256592f)));
		objects.add(new GameObject(tree, new Vector3(-32.7469801902771f, -129.89012718200684f, 4.721105396747589f)));
		objects.add(new GameObject(tree, new Vector3(-6.258469820022583f, -142.2950267791748f, 5.653026700019836f)));
		objects.add(new GameObject(tree, new Vector3(-34.73104238510132f, -134.98848915100098f, 5.132821798324585f)));
		objects.add(new GameObject(tree, new Vector3(21.397228240966797f, -142.77103424072266f, 5.05713164806366f)));
		objects.add(new GameObject(tree, new Vector3(92.97587394714355f, -134.3979835510254f, 5.090256333351135f)));
		objects.add(new GameObject(tree, new Vector3(24.067842960357666f, -128.21589469909668f, 5.6602805852890015f)));
		objects.add(new GameObject(tree, new Vector3(49.71121788024902f, -150.9720230102539f, 5.3862786293029785f)));
		objects.add(new GameObject(tree, new Vector3(79.20185565948486f, -150.33273696899414f, 5.526190996170044f)));
		objects.add(new GameObject(tree, new Vector3(92.80863761901855f, -126.52021408081055f, 5.760940909385681f)));
		objects.add(new GameObject(tree, new Vector3(82.81797409057617f, -138.93529891967773f, 4.333829283714294f)));
		objects.add(new GameObject(tree, new Vector3(39.89800214767456f, -127.42563247680664f, 5.3738826513290405f)));
		objects.add(new GameObject(tree, new Vector3(85.13151168823242f, -148.768892288208f, 5.078510046005249f)));
		objects.add(new GameObject(tree, new Vector3(80.89729309082031f, -142.0738697052002f, 4.864482581615448f)));
		objects.add(new GameObject(tree, new Vector3(86.06773376464844f, -129.9171543121338f, 4.696408212184906f)));
		objects.add(new GameObject(tree, new Vector3(87.9856014251709f, -127.71697044372559f, 5.74199378490448f)));
		objects.add(new GameObject(tree, new Vector3(66.96325778961182f, -151.86445236206055f, 5.06148099899292f)));
		objects.add(new GameObject(tree, new Vector3(70.08476257324219f, -150.51223754882812f, 5.271902680397034f)));
		objects.add(new GameObject(tree, new Vector3(83.14145088195801f, -151.73316955566406f, 4.807220101356506f)));
		objects.add(new GameObject(tree, new Vector3(31.87941074371338f, -142.67940521240234f, 4.736873507499695f)));
		objects.add(new GameObject(tree, new Vector3(93.0044174194336f, -142.0456886291504f, 4.603763222694397f)));
		objects.add(new GameObject(tree, new Vector3(57.90062427520752f, -142.8666877746582f, 4.614427387714386f)));
		objects.add(new GameObject(tree, new Vector3(44.017629623413086f, -151.17327690124512f, 4.28254634141922f)));
		objects.add(new GameObject(tree, new Vector3(90.92339515686035f, -146.12276077270508f, 4.37437504529953f)));
		objects.add(new GameObject(tree, new Vector3(55.42956352233887f, -141.45262718200684f, 4.165092706680298f)));
		objects.add(new GameObject(tree, new Vector3(22.42825746536255f, -144.28093910217285f, 4.680175483226776f)));
		objects.add(new GameObject(tree, new Vector3(86.32864952087402f, -151.59682273864746f, 4.55440878868103f)));
		objects.add(new GameObject(tree, new Vector3(82.37068176269531f, -151.2252712249756f, 5.682511329650879f)));
		objects.add(new GameObject(tree, new Vector3(72.14529037475586f, -130.1165008544922f, 4.7629088163375854f)));
		objects.add(new GameObject(tree, new Vector3(69.01465892791748f, -135.54055213928223f, 5.121481418609619f)));
		objects.add(new GameObject(tree, new Vector3(55.36431312561035f, -142.3568058013916f, 4.951576292514801f)));
		objects.add(new GameObject(tree, new Vector3(90.1967716217041f, -131.8791103363037f, 4.759507477283478f)));
		objects.add(new GameObject(tree, new Vector3(67.70009994506836f, -131.47316932678223f, 5.436863899230957f)));
		objects.add(new GameObject(tree, new Vector3(47.65090465545654f, -147.17999458312988f, 5.139107704162598f)));
		objects.add(new GameObject(tree, new Vector3(60.74995040893555f, -136.3076877593994f, 4.121422469615936f)));
		objects.add(new GameObject(tree, new Vector3(47.39738464355469f, -142.04670906066895f, 5.013061165809631f)));
		objects.add(new GameObject(tree, new Vector3(46.33931636810303f, -128.17828178405762f, 5.593791604042053f)));
		objects.add(new GameObject(tree, new Vector3(64.68839168548584f, -127.8269100189209f, 5.2942997217178345f)));
		objects.add(new GameObject(tree, new Vector3(60.104408264160156f, -144.28401947021484f, 4.70982700586319f)));
		objects.add(new GameObject(tree, new Vector3(69.83703136444092f, -148.57629776000977f, 4.634371101856232f)));
		objects.add(new GameObject(tree, new Vector3(47.33883857727051f, -138.76093864440918f, 5.594061017036438f)));
		objects.add(new GameObject(tree, new Vector3(71.9212532043457f, -136.20599746704102f, 5.395155549049377f)));
		objects.add(new GameObject(tree, new Vector3(46.80981159210205f, -129.78174209594727f, 4.545466601848602f)));
		objects.add(new GameObject(tree, new Vector3(74.1590929031372f, -127.26630210876465f, 5.523005723953247f)));
		objects.add(new GameObject(tree, new Vector3(67.2722578048706f, -133.98319244384766f, 4.231519997119904f)));
		objects.add(new GameObject(tree, new Vector3(67.23533153533936f, -147.0259189605713f, 4.3403080105781555f)));
		objects.add(new GameObject(tree, new Vector3(61.901116371154785f, -145.17131805419922f, 4.401155412197113f)));
		objects.add(new GameObject(tree, new Vector3(86.69230461120605f, -136.25274658203125f, 5.289742946624756f)));
		objects.add(new GameObject(tree, new Vector3(88.19033622741699f, -147.18685150146484f, 5.4463130235672f)));
		objects.add(new GameObject(tree, new Vector3(29.105088710784912f, -149.06411170959473f, 4.382073283195496f)));
		objects.add(new GameObject(tree, new Vector3(95.47849655151367f, -134.43004608154297f, 4.789372086524963f)));
		objects.add(new GameObject(tree, new Vector3(26.707611083984375f, -147.41924285888672f, 4.395117163658142f)));
		objects.add(new GameObject(tree, new Vector3(42.59729862213135f, -150.40090560913086f, 4.883902370929718f)));
		objects.add(new GameObject(tree, new Vector3(64.1925048828125f, -150.1724910736084f, 4.781178534030914f)));
		objects.add(new GameObject(tree, new Vector3(41.40851020812988f, -146.93601608276367f, 5.504618287086487f)));
		objects.add(new GameObject(tree, new Vector3(85.8401870727539f, -132.5096893310547f, 5.568153262138367f)));
		objects.add(new GameObject(tree, new Vector3(62.953429222106934f, -130.16274452209473f, 4.361058175563812f)));
		objects.add(new GameObject(tree, new Vector3(70.02254009246826f, -144.2322826385498f, 4.6530649065971375f)));
		objects.add(new GameObject(tree, new Vector3(49.5849084854126f, -143.04773330688477f, 5.493362545967102f)));
		objects.add(new GameObject(tree, new Vector3(36.84915542602539f, -131.46893501281738f, 5.692495107650757f)));
		objects.add(new GameObject(tree, new Vector3(92.93887138366699f, -126.85847282409668f, 5.258178114891052f)));
		objects.add(new GameObject(tree, new Vector3(89.16089057922363f, -135.78208923339844f, 5.261041522026062f)));
		objects.add(new GameObject(tree, new Vector3(62.51795291900635f, -141.953763961792f, 5.2305638790130615f)));
		objects.add(new GameObject(tree, new Vector3(91.02777481079102f, -129.8279571533203f, 4.777256548404694f)));
		objects.add(new GameObject(tree, new Vector3(47.43171215057373f, -152.0528507232666f, 5.384608507156372f)));
		objects.add(new GameObject(tree, new Vector3(86.37619972229004f, -127.48618125915527f, 4.439322054386139f)));
		objects.add(new GameObject(tree, new Vector3(19.561444520950317f, -137.7388858795166f, 5.736868381500244f)));
		objects.add(new GameObject(tree, new Vector3(94.98093605041504f, -130.8275604248047f, 4.5155346393585205f)));
		objects.add(new GameObject(tree, new Vector3(40.14230728149414f, -133.6614990234375f, 4.91229772567749f)));
		objects.add(new GameObject(tree, new Vector3(52.26935863494873f, -140.24527549743652f, 5.657292604446411f)));
		objects.add(new GameObject(tree, new Vector3(89.61193084716797f, -146.78583145141602f, 5.543408989906311f)));
		objects.add(new GameObject(tree, new Vector3(70.45257568359375f, -145.99539756774902f, 4.858564734458923f)));
		objects.add(new GameObject(tree, new Vector3(54.50047969818115f, -128.54178428649902f, 4.895248711109161f)));
		objects.add(new GameObject(tree, new Vector3(70.01200199127197f, -133.84571075439453f, 4.877418279647827f)));
		objects.add(new GameObject(tree, new Vector3(80.13039588928223f, -140.6061553955078f, 5.1698243618011475f)));
		objects.add(new GameObject(tree, new Vector3(88.95956039428711f, -150.78296661376953f, 5.6200480461120605f)));
		objects.add(new GameObject(tree, new Vector3(64.86690521240234f, -128.88957977294922f, 4.441435635089874f)));
		objects.add(new GameObject(tree, new Vector3(22.357699871063232f, -135.90024948120117f, 5.747234225273132f)));
		objects.add(new GameObject(tree, new Vector3(78.00845146179199f, -135.99377632141113f, 4.370512664318085f)));
		objects.add(new GameObject(tree, new Vector3(85.54261207580566f, -144.77957725524902f, 5.401759743690491f)));
		objects.add(new GameObject(tree, new Vector3(62.2493839263916f, -143.3969497680664f, 4.4768401980400085f)));
		objects.add(new GameObject(tree, new Vector3(47.19983100891113f, -150.64244270324707f, 4.183420538902283f)));
		objects.add(new GameObject(tree, new Vector3(76.15251541137695f, -138.20782661437988f, 4.132022857666016f)));
		objects.add(new GameObject(tree, new Vector3(31.138429641723633f, -146.5049171447754f, 5.720040798187256f)));
		objects.add(new GameObject(tree, new Vector3(58.84848117828369f, -143.29163551330566f, 4.500797390937805f)));
		objects.add(new GameObject(tree, new Vector3(86.48147583007812f, -147.47597694396973f, 4.683605134487152f)));
		objects.add(new GameObject(tree, new Vector3(94.13122177124023f, -122.60279655456543f, 4.509821832180023f)));
		objects.add(new GameObject(tree, new Vector3(76.65274143218994f, -114.2903995513916f, 4.486810863018036f)));
		objects.add(new GameObject(tree, new Vector3(49.83806610107422f, -111.822509765625f, 4.954107105731964f)));
		objects.add(new GameObject(tree, new Vector3(45.72526454925537f, -111.63965225219727f, 4.585956931114197f)));
		objects.add(new GameObject(tree, new Vector3(17.601640224456787f, -124.13999557495117f, 4.621180295944214f)));
		objects.add(new GameObject(tree, new Vector3(25.678863525390625f, -108.28436851501465f, 5.463160872459412f)));
		objects.add(new GameObject(tree, new Vector3(71.11360549926758f, -111.35736465454102f, 5.146164894104004f)));
		objects.add(new GameObject(tree, new Vector3(26.604509353637695f, -110.11672973632812f, 4.5230138301849365f)));
		objects.add(new GameObject(tree, new Vector3(44.44493770599365f, -115.25792121887207f, 5.199659466743469f)));
		objects.add(new GameObject(tree, new Vector3(27.953104972839355f, -118.53361129760742f, 4.450706243515015f)));
		objects.add(new GameObject(tree, new Vector3(19.329848289489746f, -113.74747276306152f, 4.918748438358307f)));
		objects.add(new GameObject(tree, new Vector3(35.58256387710571f, -111.54851913452148f, 4.973664879798889f)));
		objects.add(new GameObject(tree, new Vector3(88.84695053100586f, -107.01886177062988f, 4.500868618488312f)));
		objects.add(new GameObject(tree, new Vector3(64.75533485412598f, -110.04840850830078f, 4.654635190963745f)));
		objects.add(new GameObject(tree, new Vector3(94.26355361938477f, -123.37791442871094f, 5.426279306411743f)));
		objects.add(new GameObject(tree, new Vector3(20.749633312225342f, -116.9786262512207f, 5.765271186828613f)));
		objects.add(new GameObject(tree, new Vector3(64.33647632598877f, -114.49793815612793f, 4.24727737903595f)));
		objects.add(new GameObject(tree, new Vector3(-7.637776732444763f, -116.6159725189209f, 5.136452317237854f)));
		objects.add(new GameObject(tree, new Vector3(-33.98714303970337f, -122.84239768981934f, 5.144577622413635f)));
		objects.add(new GameObject(tree, new Vector3(-52.879533767700195f, -113.10832977294922f, 4.596510827541351f)));
		objects.add(new GameObject(tree, new Vector3(0.8928817510604858f, -117.36452102661133f, 4.303320348262787f)));
		objects.add(new GameObject(tree, new Vector3(-39.5264196395874f, -123.10785293579102f, 5.643168091773987f)));
		objects.add(new GameObject(tree, new Vector3(-53.56823921203613f, -109.64287757873535f, 5.106459856033325f)));
		objects.add(new GameObject(tree, new Vector3(-55.44961929321289f, -106.69133186340332f, 4.277829229831696f)));
		objects.add(new GameObject(tree, new Vector3(-27.65068292617798f, -113.5898494720459f, 4.351053237915039f)));
		objects.add(new GameObject(tree, new Vector3(-33.85030508041382f, -111.03226661682129f, 4.535602629184723f)));
		objects.add(new GameObject(tree, new Vector3(-82.28029251098633f, -111.99345588684082f, 4.871078431606293f)));
		objects.add(new GameObject(tree, new Vector3(-96.63466453552246f, -121.42763137817383f, 5.203767418861389f)));
		objects.add(new GameObject(tree, new Vector3(-89.96703147888184f, -123.05654525756836f, 4.170824289321899f)));
		objects.add(new GameObject(tree, new Vector3(-101.78960800170898f, -121.88812255859375f, 4.909269213676453f)));
		objects.add(new GameObject(tree, new Vector3(-107.02985763549805f, -107.75200843811035f, 5.384899377822876f)));
		objects.add(new GameObject(tree, new Vector3(-84.41690444946289f, -121.5803337097168f, 4.113890826702118f)));
		objects.add(new GameObject(tree, new Vector3(-112.74778366088867f, -113.25994491577148f, 4.475294649600983f)));
		objects.add(new GameObject(tree, new Vector3(-107.03609466552734f, -124.21917915344238f, 5.437808632850647f)));
		objects.add(new GameObject(tree, new Vector3(-95.05297660827637f, -106.65446281433105f, 4.541029334068298f)));
		objects.add(new GameObject(tree, new Vector3(-81.42294883728027f, -111.98844909667969f, 5.227130055427551f)));
		objects.add(new GameObject(tree, new Vector3(-81.02385520935059f, -109.90913391113281f, 4.487192332744598f)));
		objects.add(new GameObject(tree, new Vector3(152.20627784729004f, -79.5038366317749f, 5.072444677352905f)));
		objects.add(new GameObject(tree, new Vector3(146.56193733215332f, -94.97973442077637f, 5.204535722732544f)));
		objects.add(new GameObject(tree, new Vector3(146.6413116455078f, -51.840243339538574f, 4.681068062782288f)));
		objects.add(new GameObject(tree, new Vector3(126.97240829467773f, -115.42290687561035f, 4.946845173835754f)));
		objects.add(new GameObject(tree, new Vector3(144.96947288513184f, -61.20365619659424f, 4.74520742893219f)));
		objects.add(new GameObject(tree, new Vector3(141.1658000946045f, -53.3723258972168f, 5.527779459953308f)));
		objects.add(new GameObject(tree, new Vector3(150.74438095092773f, -118.78400802612305f, 4.175015091896057f)));
		objects.add(new GameObject(tree, new Vector3(142.14080810546875f, -117.44115829467773f, 5.7297128438949585f)));
		objects.add(new GameObject(tree, new Vector3(138.79559516906738f, -52.617316246032715f, 4.255761504173279f)));
		objects.add(new GameObject(tree, new Vector3(141.5357780456543f, -65.92087268829346f, 4.354563653469086f)));
		objects.add(new GameObject(tree, new Vector3(138.16527366638184f, -32.470808029174805f, 4.242610931396484f)));
		objects.add(new GameObject(tree, new Vector3(138.42247009277344f, -66.3755989074707f, 5.655895471572876f)));
		objects.add(new GameObject(tree, new Vector3(139.11364555358887f, -44.985008239746094f, 4.918278753757477f)));
		objects.add(new GameObject(tree, new Vector3(133.82615089416504f, -70.65426349639893f, 4.312028586864471f)));
		objects.add(new GameObject(tree, new Vector3(150.61433792114258f, -53.489389419555664f, 5.600306391716003f)));
		objects.add(new GameObject(tree, new Vector3(148.49098205566406f, -90.61347007751465f, 5.147386193275452f)));
		objects.add(new GameObject(tree, new Vector3(128.18474769592285f, -77.53336906433105f, 5.065022706985474f)));
		objects.add(new GameObject(tree, new Vector3(127.32719421386719f, -75.15731811523438f, 5.393289923667908f)));
		objects.add(new GameObject(tree, new Vector3(135.3806495666504f, -76.36679649353027f, 5.492592453956604f)));
		objects.add(new GameObject(tree, new Vector3(140.39329528808594f, -37.75852918624878f, 4.488059878349304f)));
		objects.add(new GameObject(tree, new Vector3(128.8066577911377f, -76.2856912612915f, 5.415248870849609f)));
		objects.add(new GameObject(tree, new Vector3(140.16111373901367f, -120.17857551574707f, 5.421236753463745f)));
		objects.add(new GameObject(tree, new Vector3(148.6015510559082f, -98.01382064819336f, 5.758790969848633f)));
		objects.add(new GameObject(tree, new Vector3(124.31673049926758f, -120.94403266906738f, 4.276216626167297f)));
		objects.add(new GameObject(tree, new Vector3(133.03330421447754f, -94.02799606323242f, 5.679665803909302f)));
		objects.add(new GameObject(tree, new Vector3(126.1536693572998f, -45.08273124694824f, 5.026654601097107f)));
		objects.add(new GameObject(tree, new Vector3(129.16492462158203f, -91.18847846984863f, 5.610407590866089f)));
		objects.add(new GameObject(tree, new Vector3(131.77863121032715f, -81.16349220275879f, 4.714818298816681f)));
		objects.add(new GameObject(tree, new Vector3(127.8195571899414f, -74.60852146148682f, 4.180670082569122f)));
		objects.add(new GameObject(tree, new Vector3(149.88702774047852f, -105.42787551879883f, 4.780669808387756f)));
		objects.add(new GameObject(tree, new Vector3(147.5395107269287f, -50.5145788192749f, 4.52410489320755f)));
		objects.add(new GameObject(tree, new Vector3(149.54008102416992f, -61.19509696960449f, 5.477025508880615f)));
		objects.add(new GameObject(tree, new Vector3(126.9085693359375f, -39.27781343460083f, 4.595103561878204f)));
		objects.add(new GameObject(tree, new Vector3(145.88960647583008f, -86.48125648498535f, 4.998542964458466f)));
		objects.add(new GameObject(tree, new Vector3(152.40108489990234f, -96.01521492004395f, 4.348345994949341f)));
		objects.add(new GameObject(tree, new Vector3(139.04781341552734f, -81.39238357543945f, 4.20779287815094f)));
		objects.add(new GameObject(tree, new Vector3(132.01729774475098f, -22.734098434448242f, 5.611735582351685f)));
		objects.add(new GameObject(tree, new Vector3(124.98147010803223f, -75.89521408081055f, 4.241864085197449f)));
		objects.add(new GameObject(tree, new Vector3(140.4761505126953f, -46.000356674194336f, 4.554783403873444f)));
		objects.add(new GameObject(tree, new Vector3(150.74249267578125f, -76.62589073181152f, 4.498444199562073f)));
		objects.add(new GameObject(tree, new Vector3(140.39141654968262f, -111.61684989929199f, 4.148766100406647f)));
		objects.add(new GameObject(tree, new Vector3(143.67637634277344f, -28.828814029693604f, 4.171612560749054f)));
		objects.add(new GameObject(tree, new Vector3(134.3027114868164f, -74.50578689575195f, 4.693518877029419f)));
		objects.add(new GameObject(tree, new Vector3(131.89858436584473f, -83.73632431030273f, 5.4260969161987305f)));
		objects.add(new GameObject(tree, new Vector3(132.22850799560547f, -69.10793781280518f, 4.14562463760376f)));
		objects.add(new GameObject(tree, new Vector3(126.84370040893555f, -88.90409469604492f, 4.125147461891174f)));
		objects.add(new GameObject(tree, new Vector3(125.91840744018555f, -24.831418991088867f, 4.3287986516952515f)));
		objects.add(new GameObject(tree, new Vector3(149.20055389404297f, -107.73252487182617f, 5.765611529350281f)));
		objects.add(new GameObject(tree, new Vector3(131.57700538635254f, -98.8631534576416f, 5.308869481086731f)));
		objects.add(new GameObject(tree, new Vector3(133.97854804992676f, -68.58140468597412f, 4.5766982436180115f)));
		objects.add(new GameObject(tree, new Vector3(126.3161563873291f, -29.76141929626465f, 5.041012763977051f)));
		objects.add(new GameObject(tree, new Vector3(146.56217575073242f, -54.67945575714111f, 4.948101043701172f)));
		objects.add(new GameObject(tree, new Vector3(126.89367294311523f, -92.43642807006836f, 5.699595212936401f)));
		objects.add(new GameObject(tree, new Vector3(136.9040870666504f, -83.58582496643066f, 5.443467497825623f)));
		objects.add(new GameObject(tree, new Vector3(152.48249053955078f, -79.81200218200684f, 5.656746029853821f)));
		objects.add(new GameObject(tree, new Vector3(130.34964561462402f, -37.185728549957275f, 4.690488576889038f)));
		objects.add(new GameObject(tree, new Vector3(150.07229804992676f, -46.43245220184326f, 5.479656457901001f)));
		objects.add(new GameObject(tree, new Vector3(134.18606758117676f, -53.21267127990723f, 5.734871029853821f)));
		objects.add(new GameObject(tree, new Vector3(130.8332920074463f, -26.91173553466797f, 4.796628057956696f)));
		objects.add(new GameObject(tree, new Vector3(141.29255294799805f, -27.953801155090332f, 4.647285342216492f)));
		objects.add(new GameObject(tree, new Vector3(140.1715850830078f, -92.73048400878906f, 4.3887874484062195f)));
		objects.add(new GameObject(tree, new Vector3(132.94487953186035f, -21.39392375946045f, 5.602636337280273f)));
		objects.add(new GameObject(tree, new Vector3(127.76318550109863f, -122.59385108947754f, 4.815854132175446f)));
		objects.add(new GameObject(tree, new Vector3(151.05588912963867f, -113.62483024597168f, 4.965692162513733f)));
		objects.add(new GameObject(tree, new Vector3(141.61243438720703f, -108.52890968322754f, 4.441450238227844f)));
		objects.add(new GameObject(tree, new Vector3(152.52382278442383f, -78.3437728881836f, 4.9824342131614685f)));
		objects.add(new GameObject(tree, new Vector3(151.95877075195312f, -120.14606475830078f, 5.362109541893005f)));
		objects.add(new GameObject(tree, new Vector3(125.57180404663086f, -31.65421485900879f, 5.521383285522461f)));
		objects.add(new GameObject(tree, new Vector3(128.9145565032959f, -122.32989311218262f, 5.51746129989624f)));
		objects.add(new GameObject(tree, new Vector3(136.89257621765137f, -108.59122276306152f, 5.577021241188049f)));
		objects.add(new GameObject(tree, new Vector3(141.35363578796387f, -118.30849647521973f, 4.460265636444092f)));
		objects.add(new GameObject(tree, new Vector3(129.69670295715332f, -64.8575496673584f, 5.099886059761047f)));
		objects.add(new GameObject(tree, new Vector3(140.33774375915527f, -97.17724800109863f, 4.676614701747894f)));
		objects.add(new GameObject(tree, new Vector3(142.2590446472168f, -45.2857780456543f, 4.906229376792908f)));
		objects.add(new GameObject(tree, new Vector3(151.90022468566895f, -60.653676986694336f, 4.854984879493713f)));
		objects.add(new GameObject(tree, new Vector3(125.0093936920166f, -79.40525531768799f, 4.278103709220886f)));
		objects.add(new GameObject(tree, new Vector3(124.91582870483398f, -32.17166185379028f, 4.760201871395111f)));
		objects.add(new GameObject(tree, new Vector3(135.73996543884277f, -64.5503568649292f, 5.498692989349365f)));
		objects.add(new GameObject(tree, new Vector3(130.6358242034912f, -67.09671974182129f, 4.79893296957016f)));
		objects.add(new GameObject(tree, new Vector3(145.65095901489258f, -32.81689167022705f, 5.51912248134613f)));
		objects.add(new GameObject(tree, new Vector3(131.3529109954834f, -104.92966651916504f, 5.41296660900116f)));
		objects.add(new GameObject(tree, new Vector3(129.49554443359375f, -22.00354814529419f, 5.253927111625671f)));
		objects.add(new GameObject(tree, new Vector3(127.77990341186523f, -23.79636287689209f, 4.210037291049957f)));
		objects.add(new GameObject(tree, new Vector3(136.30457878112793f, -22.15076446533203f, 4.554315805435181f)));
		objects.add(new GameObject(tree, new Vector3(141.30188941955566f, -78.41312885284424f, 5.617703795433044f)));
		objects.add(new GameObject(tree, new Vector3(150.0644016265869f, -53.95864963531494f, 5.48004150390625f)));
		objects.add(new GameObject(tree, new Vector3(132.02792167663574f, -86.70075416564941f, 4.87118273973465f)));
		objects.add(new GameObject(tree, new Vector3(136.03466987609863f, -55.775108337402344f, 5.295865535736084f)));
		objects.add(new GameObject(tree, new Vector3(144.22744750976562f, -115.30827522277832f, 5.17369270324707f)));
		objects.add(new GameObject(tree, new Vector3(141.93138122558594f, -76.32808685302734f, 4.869992733001709f)));
		objects.add(new GameObject(tree, new Vector3(134.47734832763672f, -117.31282234191895f, 4.76051926612854f)));
		objects.add(new GameObject(tree, new Vector3(147.27434158325195f, -67.37060546875f, 5.47402024269104f)));
		objects.add(new GameObject(tree, new Vector3(144.5081329345703f, -46.916537284851074f, 4.335240423679352f)));
		objects.add(new GameObject(tree, new Vector3(138.42280387878418f, -81.61911010742188f, 5.3218865394592285f)));
		objects.add(new GameObject(tree, new Vector3(148.88927459716797f, -116.76501274108887f, 4.435449242591858f)));
		objects.add(new GameObject(tree, new Vector3(129.51083183288574f, -29.1841459274292f, 4.786081612110138f)));
		objects.add(new GameObject(tree, new Vector3(152.67306327819824f, -28.39249610900879f, 4.299136400222778f)));
		objects.add(new GameObject(tree, new Vector3(135.09201049804688f, -23.68011713027954f, 4.743218719959259f)));
		objects.add(new GameObject(tree, new Vector3(152.00295448303223f, -38.09904336929321f, 5.212852954864502f)));
		objects.add(new GameObject(tree, new Vector3(148.94139289855957f, -36.249403953552246f, 4.669337272644043f)));
		objects.add(new GameObject(tree, new Vector3(143.07186126708984f, -28.16899061203003f, 4.28059458732605f)));
		objects.add(new GameObject(tree, new Vector3(142.65460968017578f, -83.22060585021973f, 4.316178560256958f)));
		objects.add(new GameObject(tree, new Vector3(144.1036033630371f, 88.30967903137207f, 4.3705689907073975f)));
		objects.add(new GameObject(tree, new Vector3(145.9939670562744f, 58.926711082458496f, 5.4533785581588745f)));
		objects.add(new GameObject(tree, new Vector3(150.9010124206543f, 77.8162956237793f, 4.868833124637604f)));
		objects.add(new GameObject(tree, new Vector3(143.93004417419434f, 26.423256397247314f, 5.73805034160614f)));
		objects.add(new GameObject(tree, new Vector3(128.3847141265869f, 29.84180450439453f, 4.847650825977325f)));
		objects.add(new GameObject(tree, new Vector3(126.32138252258301f, 73.96443367004395f, 5.272255539894104f)));
		objects.add(new GameObject(tree, new Vector3(131.04110717773438f, 75.59944152832031f, 4.787388741970062f)));
		objects.add(new GameObject(tree, new Vector3(152.07234382629395f, 48.448476791381836f, 4.717271327972412f)));
		objects.add(new GameObject(tree, new Vector3(145.32599449157715f, 29.983346462249756f, 5.180574059486389f)));
		objects.add(new GameObject(tree, new Vector3(140.4105567932129f, 70.6974983215332f, 5.529295802116394f)));
		objects.add(new GameObject(tree, new Vector3(139.41396713256836f, 71.7112398147583f, 5.3477150201797485f)));
		objects.add(new GameObject(tree, new Vector3(129.10962104797363f, 46.8082857131958f, 4.356731474399567f)));
		objects.add(new GameObject(tree, new Vector3(136.81209564208984f, 3.8808226585388184f, 4.584779143333435f)));
		objects.add(new GameObject(tree, new Vector3(125.74419021606445f, 90.78361511230469f, 5.217100381851196f)));
		objects.add(new GameObject(tree, new Vector3(135.42909622192383f, 24.179553985595703f, 5.068874359130859f)));
		objects.add(new GameObject(tree, new Vector3(138.52078437805176f, 5.419286489486694f, 5.421307682991028f)));
		objects.add(new GameObject(tree, new Vector3(147.38073348999023f, 79.09764289855957f, 5.549068450927734f)));
		objects.add(new GameObject(tree, new Vector3(147.9599380493164f, 58.74977111816406f, 4.580124318599701f)));
		objects.add(new GameObject(tree, new Vector3(129.3325424194336f, 56.24087333679199f, 4.769991636276245f)));
		objects.add(new GameObject(tree, new Vector3(130.83674430847168f, 26.958003044128418f, 5.320888161659241f)));
		objects.add(new GameObject(tree, new Vector3(152.1115493774414f, 59.341421127319336f, 5.6651222705841064f)));
		objects.add(new GameObject(tree, new Vector3(136.0721206665039f, 57.85088062286377f, 4.2157164216041565f)));
		objects.add(new GameObject(tree, new Vector3(141.70636177062988f, 55.59866428375244f, 4.240741729736328f)));
		objects.add(new GameObject(tree, new Vector3(149.94214057922363f, 4.648534059524536f, 4.573303759098053f)));
		objects.add(new GameObject(tree, new Vector3(134.55218315124512f, 75.23215770721436f, 4.413851499557495f)));
		objects.add(new GameObject(tree, new Vector3(128.28696250915527f, 86.429443359375f, 5.478565096855164f)));
		objects.add(new GameObject(tree, new Vector3(129.1982364654541f, 83.51078987121582f, 5.66291868686676f)));
		objects.add(new GameObject(tree, new Vector3(127.33181953430176f, 80.27915000915527f, 4.689074754714966f)));
		objects.add(new GameObject(tree, new Vector3(140.0107192993164f, 1.935855895280838f, 4.240438342094421f)));
		objects.add(new GameObject(tree, new Vector3(140.9476661682129f, 65.217604637146f, 4.23829972743988f)));
		objects.add(new GameObject(tree, new Vector3(144.12261962890625f, 58.71218204498291f, 5.511561036109924f)));
		objects.add(new GameObject(tree, new Vector3(149.7472858428955f, 72.4052095413208f, 4.894754886627197f)));
		objects.add(new GameObject(tree, new Vector3(148.14513206481934f, 2.4889466166496277f, 5.349981188774109f)));
		objects.add(new GameObject(tree, new Vector3(150.55577278137207f, 20.902206897735596f, 5.68568229675293f)));
		objects.add(new GameObject(tree, new Vector3(145.78426361083984f, 34.670703411102295f, 5.5984944105148315f)));
		objects.add(new GameObject(tree, new Vector3(137.08735466003418f, 83.81420135498047f, 5.42868971824646f)));
		objects.add(new GameObject(tree, new Vector3(150.74590682983398f, 88.94160270690918f, 4.280261993408203f)));
		objects.add(new GameObject(tree, new Vector3(147.32389450073242f, 49.502787590026855f, 4.619455933570862f)));
		objects.add(new GameObject(tree, new Vector3(134.91477012634277f, 50.81778526306152f, 5.636888742446899f)));
		objects.add(new GameObject(tree, new Vector3(128.06612968444824f, 27.388522624969482f, 5.179510712623596f)));
		objects.add(new GameObject(tree, new Vector3(126.83757781982422f, 3.343830108642578f, 4.526109099388123f)));
		objects.add(new GameObject(tree, new Vector3(128.07306289672852f, 47.624874114990234f, 4.147614538669586f)));
		objects.add(new GameObject(tree, new Vector3(129.4446563720703f, 9.754654169082642f, 4.373071193695068f)));
		objects.add(new GameObject(tree, new Vector3(138.28243255615234f, 50.21728038787842f, 4.314239025115967f)));
		objects.add(new GameObject(tree, new Vector3(136.3654327392578f, 29.69974994659424f, 4.875344634056091f)));
		objects.add(new GameObject(tree, new Vector3(134.91620063781738f, 73.54364395141602f, 4.84967827796936f)));
		objects.add(new GameObject(tree, new Vector3(134.0904998779297f, 56.478238105773926f, 4.681700766086578f)));
		objects.add(new GameObject(tree, new Vector3(126.84689521789551f, 26.85023784637451f, 5.531232953071594f)));
		objects.add(new GameObject(tree, new Vector3(150.4525089263916f, 40.57786464691162f, 5.127183794975281f)));
		objects.add(new GameObject(tree, new Vector3(131.1380672454834f, 2.30418398976326f, 5.4911768436431885f)));
		objects.add(new GameObject(tree, new Vector3(125.20078659057617f, 90.0741958618164f, 5.1522427797317505f)));
		objects.add(new GameObject(tree, new Vector3(130.4062843322754f, 9.451441168785095f, 5.076303482055664f)));
		objects.add(new GameObject(tree, new Vector3(133.549165725708f, 36.19828462600708f, 5.564005374908447f)));
		objects.add(new GameObject(tree, new Vector3(144.30723190307617f, 40.70819854736328f, 5.728843808174133f)));
		objects.add(new GameObject(tree, new Vector3(146.51605606079102f, 57.69330978393555f, 5.199868083000183f)));
		objects.add(new GameObject(tree, new Vector3(143.6881923675537f, 60.13099193572998f, 4.36219334602356f)));
		objects.add(new GameObject(tree, new Vector3(142.09535598754883f, 74.05337810516357f, 4.7792136669158936f)));
		objects.add(new GameObject(tree, new Vector3(128.94156455993652f, 25.928103923797607f, 4.961094558238983f)));
		objects.add(new GameObject(tree, new Vector3(129.4879150390625f, 76.29667282104492f, 5.683045983314514f)));
		objects.add(new GameObject(tree, new Vector3(152.05018997192383f, 43.34653854370117f, 5.461238622665405f)));
		objects.add(new GameObject(tree, new Vector3(123.8450813293457f, 24.428822994232178f, 4.7662705183029175f)));
		objects.add(new GameObject(tree, new Vector3(124.18437004089355f, 73.47958564758301f, 4.261847734451294f)));
		objects.add(new GameObject(tree, new Vector3(147.0610809326172f, 9.975142478942871f, 5.157432556152344f)));
		objects.add(new GameObject(tree, new Vector3(140.84284782409668f, 52.26083278656006f, 5.407203435897827f)));
		objects.add(new GameObject(tree, new Vector3(125.4377269744873f, 79.1594123840332f, 5.704537630081177f)));
		objects.add(new GameObject(tree, new Vector3(134.59059715270996f, 78.40790271759033f, 4.163752198219299f)));
		objects.add(new GameObject(tree, new Vector3(143.90267372131348f, 61.977477073669434f, 4.489729106426239f)));
		objects.add(new GameObject(tree, new Vector3(146.78078651428223f, 80.83916664123535f, 5.236388444900513f)));
		objects.add(new GameObject(tree, new Vector3(145.02612113952637f, 88.93498420715332f, 4.282497763633728f)));
		objects.add(new GameObject(tree, new Vector3(124.81359481811523f, 73.34907531738281f, 4.724868834018707f)));
		objects.add(new GameObject(tree, new Vector3(150.33839225769043f, 12.97304630279541f, 5.637022256851196f)));
		objects.add(new GameObject(tree, new Vector3(150.58170318603516f, 80.1507568359375f, 4.6524447202682495f)));
		objects.add(new GameObject(tree, new Vector3(136.66216850280762f, 49.391255378723145f, 4.797143340110779f)));
		objects.add(new GameObject(tree, new Vector3(145.03365516662598f, 67.53089904785156f, 5.701447129249573f)));
		objects.add(new GameObject(tree, new Vector3(148.92315864562988f, 49.153075218200684f, 5.6681400537490845f)));
		objects.add(new GameObject(tree, new Vector3(130.68635940551758f, 27.143077850341797f, 5.056331157684326f)));
		objects.add(new GameObject(tree, new Vector3(141.95817947387695f, 20.706605911254883f, 5.043002367019653f)));
		objects.add(new GameObject(tree, new Vector3(133.8587474822998f, 32.633118629455566f, 5.673614144325256f)));
		objects.add(new GameObject(tree, new Vector3(142.46697425842285f, 4.8793816566467285f, 4.897754490375519f)));
		objects.add(new GameObject(tree, new Vector3(144.6061897277832f, 46.43430709838867f, 4.487941861152649f)));
		objects.add(new GameObject(tree, new Vector3(142.8028106689453f, 9.998211860656738f, 4.920791983604431f)));
		objects.add(new GameObject(tree, new Vector3(137.1915626525879f, 80.79636573791504f, 5.079798698425293f)));
		objects.add(new GameObject(tree, new Vector3(133.74573707580566f, 76.65852546691895f, 4.861203730106354f)));
		objects.add(new GameObject(tree, new Vector3(130.85925102233887f, 25.428056716918945f, 4.148050546646118f)));
		objects.add(new GameObject(tree, new Vector3(132.52878189086914f, 53.70310306549072f, 5.45318603515625f)));
		objects.add(new GameObject(tree, new Vector3(129.60920333862305f, 24.728200435638428f, 4.443018436431885f)));
		objects.add(new GameObject(tree, new Vector3(146.65996551513672f, 35.39933681488037f, 5.297820568084717f)));
		objects.add(new GameObject(tree, new Vector3(151.2747097015381f, 80.61367988586426f, 5.674921870231628f)));
		objects.add(new GameObject(tree, new Vector3(145.50073623657227f, 81.28705978393555f, 5.154898166656494f)));
		objects.add(new GameObject(tree, new Vector3(150.34221649169922f, 36.609413623809814f, 4.8049309849739075f)));
		objects.add(new GameObject(tree, new Vector3(151.1544895172119f, 45.71543216705322f, 4.886061251163483f)));
		objects.add(new GameObject(tree, new Vector3(128.62698554992676f, 58.24288368225098f, 5.7662248611450195f)));
		objects.add(new GameObject(tree, new Vector3(151.66330337524414f, 34.25355911254883f, 4.740660786628723f)));
		objects.add(new GameObject(tree, new Vector3(129.17085647583008f, 17.25888967514038f, 4.365251660346985f)));
		objects.add(new GameObject(tree, new Vector3(129.26361083984375f, -4.531728625297546f, 4.716293215751648f)));
		objects.add(new GameObject(tree, new Vector3(147.59796142578125f, 32.8205680847168f, 4.80409562587738f)));
		objects.add(new GameObject(tree, new Vector3(138.29862594604492f, -3.3090442419052124f, 4.462009966373444f)));
		objects.add(new GameObject(tree, new Vector3(141.10797882080078f, 39.933974742889404f, 4.6363988518714905f)));
		objects.add(new GameObject(tree, new Vector3(148.05890083312988f, 23.749370574951172f, 5.288458466529846f)));
		objects.add(new GameObject(tree, new Vector3(151.9587516784668f, 9.947459101676941f, 4.828932881355286f)));
		objects.add(new GameObject(tree, new Vector3(128.0863380432129f, 92.52437591552734f, 5.577032566070557f)));
		objects.add(new GameObject(tree, new Vector3(132.41748809814453f, 57.92142868041992f, 5.096517205238342f)));
		objects.add(new GameObject(tree, new Vector3(143.12597274780273f, 12.4456787109375f, 4.880868196487427f)));
		objects.add(new GameObject(tree, new Vector3(143.7356662750244f, 59.633803367614746f, 4.798988401889801f)));
		objects.add(new GameObject(tree, new Vector3(151.30552291870117f, 12.264528274536133f, 4.89918053150177f)));
		objects.add(new GameObject(tree, new Vector3(142.51105308532715f, 28.18286418914795f, 4.797235131263733f)));
		objects.add(new GameObject(tree, new Vector3(149.40731048583984f, 42.64385223388672f, 4.9274808168411255f)));
		objects.add(new GameObject(tree, new Vector3(125.46601295471191f, 78.2002305984497f, 5.553853511810303f)));
		objects.add(new GameObject(tree, new Vector3(141.9733715057373f, 15.676566362380981f, 4.768724143505096f)));
		objects.add(new GameObject(tree, new Vector3(136.60715103149414f, 32.98680305480957f, 5.425768494606018f)));
		objects.add(new GameObject(tree, new Vector3(152.21771240234375f, 32.849414348602295f, 4.1420310735702515f)));
		objects.add(new GameObject(tree, new Vector3(141.18364334106445f, -7.17610776424408f, 4.261589348316193f)));
		objects.add(new GameObject(tree, new Vector3(75.60839176177979f, 137.11395263671875f, 4.398590922355652f)));
		objects.add(new GameObject(tree, new Vector3(38.49285364151001f, 150.70425033569336f, 4.365563988685608f)));
		objects.add(new GameObject(tree, new Vector3(70.1237154006958f, 140.34791946411133f, 5.337724089622498f)));
		objects.add(new GameObject(tree, new Vector3(84.92717742919922f, 138.03141593933105f, 5.060224533081055f)));
		objects.add(new GameObject(tree, new Vector3(62.828264236450195f, 142.741117477417f, 5.583217740058899f)));
		objects.add(new GameObject(tree, new Vector3(89.5993423461914f, 139.00795936584473f, 4.329594671726227f)));
		objects.add(new GameObject(tree, new Vector3(92.40765571594238f, 140.3248691558838f, 4.515693187713623f)));
		objects.add(new GameObject(tree, new Vector3(66.75870895385742f, 147.69332885742188f, 5.523079037666321f)));
		objects.add(new GameObject(tree, new Vector3(28.324637413024902f, 140.70473670959473f, 4.206579625606537f)));
		objects.add(new GameObject(tree, new Vector3(69.92260932922363f, 151.83560371398926f, 5.1715511083602905f)));
		objects.add(new GameObject(tree, new Vector3(65.4867696762085f, 148.2698154449463f, 5.1784104108810425f)));
		objects.add(new GameObject(tree, new Vector3(87.64359474182129f, 148.81102561950684f, 5.527726411819458f)));
		objects.add(new GameObject(tree, new Vector3(34.390268325805664f, 143.17448616027832f, 4.273063242435455f)));
		objects.add(new GameObject(tree, new Vector3(16.328481435775757f, 137.60982513427734f, 4.20731782913208f)));
		objects.add(new GameObject(tree, new Vector3(78.45094680786133f, 146.89453125f, 4.441977143287659f)));
		objects.add(new GameObject(tree, new Vector3(95.84799766540527f, 146.20569229125977f, 4.191192984580994f)));
		objects.add(new GameObject(tree, new Vector3(28.318610191345215f, 140.55546760559082f, 4.417642056941986f)));
		objects.add(new GameObject(tree, new Vector3(46.79917335510254f, 152.34658241271973f, 4.40369188785553f)));
		objects.add(new GameObject(tree, new Vector3(82.0760440826416f, 148.63966941833496f, 4.592519104480743f)));
		objects.add(new GameObject(tree, new Vector3(18.89686346054077f, 135.1309585571289f, 4.333040416240692f)));
		objects.add(new GameObject(tree, new Vector3(25.70340871810913f, 147.4348545074463f, 4.781208634376526f)));
		objects.add(new GameObject(tree, new Vector3(19.071009159088135f, 142.9963493347168f, 4.833172261714935f)));
		objects.add(new GameObject(tree, new Vector3(86.7922306060791f, 132.35438346862793f, 5.203351974487305f)));
		objects.add(new GameObject(tree, new Vector3(57.516422271728516f, 140.72738647460938f, 4.197085797786713f)));
		objects.add(new GameObject(tree, new Vector3(91.8604564666748f, 146.54902458190918f, 5.554108023643494f)));
		objects.add(new GameObject(tree, new Vector3(93.2572078704834f, 136.98176383972168f, 4.336075186729431f)));
		objects.add(new GameObject(tree, new Vector3(24.104914665222168f, 136.57751083374023f, 5.058665871620178f)));
		objects.add(new GameObject(tree, new Vector3(80.84412574768066f, 143.50601196289062f, 5.647038221359253f)));
		objects.add(new GameObject(tree, new Vector3(79.39618110656738f, 135.78044891357422f, 4.759145081043243f)));
		objects.add(new GameObject(tree, new Vector3(31.515848636627197f, 146.0769557952881f, 5.016437768936157f)));
		objects.add(new GameObject(tree, new Vector3(83.08650016784668f, 136.1774730682373f, 4.997121691703796f)));
		objects.add(new GameObject(tree, new Vector3(81.41169548034668f, 142.44338035583496f, 5.4210251569747925f)));
		objects.add(new GameObject(tree, new Vector3(29.722113609313965f, 147.37464904785156f, 4.587173759937286f)));
		objects.add(new GameObject(tree, new Vector3(81.8162727355957f, 136.74501419067383f, 4.6854329109191895f)));
		objects.add(new GameObject(tree, new Vector3(62.72031307220459f, 143.82436752319336f, 4.824806451797485f)));
		objects.add(new GameObject(tree, new Vector3(53.35675239562988f, 146.6372299194336f, 5.137267708778381f)));
		objects.add(new GameObject(tree, new Vector3(83.14139366149902f, 138.64218711853027f, 5.274354815483093f)));
		objects.add(new GameObject(tree, new Vector3(39.30593252182007f, 147.18852043151855f, 5.058497786521912f)));
		objects.add(new GameObject(tree, new Vector3(31.06058120727539f, 147.3495388031006f, 4.354177713394165f)));
		objects.add(new GameObject(tree, new Vector3(66.60326480865479f, 141.69944763183594f, 5.080878734588623f)));
		objects.add(new GameObject(tree, new Vector3(17.980175018310547f, 136.63082122802734f, 5.5684202909469604f)));
		objects.add(new GameObject(tree, new Vector3(58.54802131652832f, 152.18988418579102f, 4.212062060832977f)));
		objects.add(new GameObject(tree, new Vector3(61.044020652770996f, 135.7585334777832f, 4.874157011508942f)));
		objects.add(new GameObject(tree, new Vector3(51.84685707092285f, 144.15918350219727f, 4.238401651382446f)));
		objects.add(new GameObject(tree, new Vector3(57.58221626281738f, 145.2208423614502f, 5.609905123710632f)));
		objects.add(new GameObject(tree, new Vector3(24.365122318267822f, 131.66202545166016f, 4.737813174724579f)));
		objects.add(new GameObject(tree, new Vector3(65.76051712036133f, 134.20387268066406f, 4.794777929782867f)));
		objects.add(new GameObject(tree, new Vector3(35.027923583984375f, 136.67885780334473f, 4.5140039920806885f)));
		objects.add(new GameObject(tree, new Vector3(28.852646350860596f, 138.68752479553223f, 4.840741753578186f)));
		objects.add(new GameObject(tree, new Vector3(85.6208324432373f, 143.03380966186523f, 4.5378246903419495f)));
		objects.add(new GameObject(tree, new Vector3(71.74094200134277f, 144.61750984191895f, 4.706639051437378f)));
		objects.add(new GameObject(tree, new Vector3(86.36983871459961f, 149.57202911376953f, 5.473199486732483f)));
		objects.add(new GameObject(tree, new Vector3(81.4560604095459f, 133.4004783630371f, 4.353564083576202f)));
		objects.add(new GameObject(tree, new Vector3(33.36506366729736f, 138.9467716217041f, 4.26118791103363f)));
		objects.add(new GameObject(tree, new Vector3(79.88462924957275f, 151.54465675354004f, 4.727985858917236f)));
		objects.add(new GameObject(tree, new Vector3(70.06662368774414f, 145.80548286437988f, 5.274679660797119f)));
		objects.add(new GameObject(tree, new Vector3(23.8191556930542f, 133.80610466003418f, 5.443410277366638f)));
		objects.add(new GameObject(tree, new Vector3(46.42642021179199f, 146.90373420715332f, 5.034856200218201f)));
		objects.add(new GameObject(tree, new Vector3(83.90563011169434f, 151.0075569152832f, 4.482224881649017f)));
		objects.add(new GameObject(tree, new Vector3(16.473604440689087f, 151.11366271972656f, 4.845851957798004f)));
		objects.add(new GameObject(tree, new Vector3(62.28927135467529f, 142.87776947021484f, 5.160264372825623f)));
		objects.add(new GameObject(tree, new Vector3(91.28908157348633f, 144.09537315368652f, 4.421812295913696f)));
		objects.add(new GameObject(tree, new Vector3(40.83010196685791f, 135.68865776062012f, 5.392748713493347f)));
		objects.add(new GameObject(tree, new Vector3(22.019367218017578f, 134.18869972229004f, 4.844422936439514f)));
		objects.add(new GameObject(tree, new Vector3(-24.624290466308594f, 141.5558624267578f, 5.554869771003723f)));
		objects.add(new GameObject(tree, new Vector3(-15.93074917793274f, 134.00185585021973f, 5.0714850425720215f)));
		objects.add(new GameObject(tree, new Vector3(-54.10787582397461f, 148.78884315490723f, 4.187439978122711f)));
		objects.add(new GameObject(tree, new Vector3(-32.1161675453186f, 145.58321952819824f, 4.651100337505341f)));
		objects.add(new GameObject(tree, new Vector3(-9.33514654636383f, 131.59727096557617f, 5.057575106620789f)));
		objects.add(new GameObject(tree, new Vector3(-41.5933895111084f, 147.76987075805664f, 5.198577046394348f)));
		objects.add(new GameObject(tree, new Vector3(-30.245347023010254f, 139.20294761657715f, 5.39121687412262f)));
		objects.add(new GameObject(tree, new Vector3(-0.7009901851415634f, 143.27860832214355f, 5.595225691795349f)));
		objects.add(new GameObject(tree, new Vector3(-25.064215660095215f, 135.3379726409912f, 4.621198773384094f)));
		objects.add(new GameObject(tree, new Vector3(-53.696465492248535f, 146.7326259613037f, 5.51114022731781f)));
		objects.add(new GameObject(tree, new Vector3(-15.72528600692749f, 143.77154350280762f, 5.209175944328308f)));
		objects.add(new GameObject(tree, new Vector3(-45.91010570526123f, 151.27080917358398f, 4.283837676048279f)));
		objects.add(new GameObject(tree, new Vector3(-20.10139226913452f, 138.3112621307373f, 5.686987638473511f)));
		objects.add(new GameObject(tree, new Vector3(-44.96750354766846f, 141.69584274291992f, 5.3082698583602905f)));
		objects.add(new GameObject(tree, new Vector3(-26.09637975692749f, 133.87993812561035f, 5.670000314712524f)));
		objects.add(new GameObject(tree, new Vector3(-11.336669921875f, 138.39463233947754f, 4.240960478782654f)));
		objects.add(new GameObject(tree, new Vector3(-28.885905742645264f, 149.0241241455078f, 4.151443541049957f)));
		objects.add(new GameObject(tree, new Vector3(-52.285523414611816f, 137.50713348388672f, 4.847488105297089f)));
		objects.add(new GameObject(tree, new Vector3(-28.21314573287964f, 144.85916137695312f, 5.512610673904419f)));
		objects.add(new GameObject(tree, new Vector3(-18.692739009857178f, 145.13232231140137f, 4.183656275272369f)));
		objects.add(new GameObject(tree, new Vector3(-24.20510768890381f, 148.11434745788574f, 5.572990775108337f)));
		objects.add(new GameObject(tree, new Vector3(-9.496623873710632f, 139.0918254852295f, 5.2251023054122925f)));
		objects.add(new GameObject(tree, new Vector3(-5.540416240692139f, 146.06125831604004f, 5.734505653381348f)));
		objects.add(new GameObject(tree, new Vector3(-30.933642387390137f, 140.6468677520752f, 4.701890051364899f)));
		objects.add(new GameObject(tree, new Vector3(-1.2646892666816711f, 135.7475471496582f, 5.5758267641067505f)));
		objects.add(new GameObject(tree, new Vector3(-45.07491588592529f, 144.53994750976562f, 4.242472946643829f)));
		objects.add(new GameObject(tree, new Vector3(-4.11532461643219f, 145.07587432861328f, 5.379047989845276f)));
		objects.add(new GameObject(tree, new Vector3(-33.41160535812378f, 150.592679977417f, 5.599474906921387f)));
		objects.add(new GameObject(tree, new Vector3(-27.081825733184814f, 152.1693992614746f, 4.454967379570007f)));
		objects.add(new GameObject(tree, new Vector3(-47.81831741333008f, 144.53649520874023f, 4.745457172393799f)));
		objects.add(new GameObject(tree, new Vector3(-46.41758441925049f, 132.20699310302734f, 5.061577558517456f)));
		objects.add(new GameObject(tree, new Vector3(-3.182886242866516f, 132.55775451660156f, 4.30318295955658f)));
		objects.add(new GameObject(tree, new Vector3(-3.9285925030708313f, 132.74616241455078f, 5.006127953529358f)));
		objects.add(new GameObject(tree, new Vector3(-15.445958375930786f, 143.44314575195312f, 5.7044535875320435f)));
		objects.add(new GameObject(tree, new Vector3(-19.20250654220581f, 146.0221004486084f, 4.37012255191803f)));
		objects.add(new GameObject(tree, new Vector3(-31.234028339385986f, 152.35403060913086f, 5.498421788215637f)));
		objects.add(new GameObject(tree, new Vector3(-28.916845321655273f, 133.16349983215332f, 5.071224570274353f)));
		objects.add(new GameObject(tree, new Vector3(-15.43520450592041f, 135.99186897277832f, 5.164119601249695f)));
		objects.add(new GameObject(tree, new Vector3(-26.123080253601074f, 144.52093124389648f, 4.170046746730804f)));
		objects.add(new GameObject(tree, new Vector3(-51.264448165893555f, 139.20236587524414f, 4.308037161827087f)));
		objects.add(new GameObject(tree, new Vector3(-21.168696880340576f, 142.7471923828125f, 4.864485561847687f)));
		objects.add(new GameObject(tree, new Vector3(-20.20669937133789f, 151.21421813964844f, 5.57840883731842f)));
		objects.add(new GameObject(tree, new Vector3(-0.6187092885375023f, 146.1286449432373f, 4.214410185813904f)));
		objects.add(new GameObject(tree, new Vector3(-12.972859144210815f, 147.92305946350098f, 4.450683891773224f)));
		objects.add(new GameObject(tree, new Vector3(-40.42954444885254f, 133.8309097290039f, 5.597536563873291f)));
		objects.add(new GameObject(tree, new Vector3(-45.300140380859375f, 152.4458122253418f, 4.150976240634918f)));
		objects.add(new GameObject(tree, new Vector3(-5.900600552558899f, 150.31538009643555f, 4.863862991333008f)));
		objects.add(new GameObject(tree, new Vector3(-34.53850507736206f, 141.8336296081543f, 5.340272784233093f)));
		objects.add(new GameObject(tree, new Vector3(-18.558064699172974f, 144.3809986114502f, 4.427922368049622f)));
		objects.add(new GameObject(tree, new Vector3(-30.700104236602783f, 148.12799453735352f, 4.368368983268738f)));
		objects.add(new GameObject(tree, new Vector3(-3.8156643509864807f, 143.16245079040527f, 4.986772835254669f)));
		objects.add(new GameObject(tree, new Vector3(-72.42699146270752f, 137.6708698272705f, 4.754876792430878f)));
		objects.add(new GameObject(tree, new Vector3(-82.47758865356445f, 142.68892288208008f, 5.6695640087127686f)));
		objects.add(new GameObject(tree, new Vector3(-83.95307540893555f, 138.5378074645996f, 5.100455284118652f)));
		objects.add(new GameObject(tree, new Vector3(-91.70854568481445f, 150.7088851928711f, 5.5463045835494995f)));
		objects.add(new GameObject(tree, new Vector3(-95.96705436706543f, 144.7104263305664f, 5.584849715232849f)));
		objects.add(new GameObject(tree, new Vector3(-106.99908256530762f, 139.71442222595215f, 4.131907522678375f)));
		objects.add(new GameObject(tree, new Vector3(-87.93718338012695f, 150.02686500549316f, 5.586363673210144f)));
		objects.add(new GameObject(tree, new Vector3(-78.197021484375f, 133.35698127746582f, 5.393926501274109f)));
		objects.add(new GameObject(tree, new Vector3(-110.36764144897461f, 147.90949821472168f, 5.653026103973389f)));
		objects.add(new GameObject(tree, new Vector3(-84.37722206115723f, 150.1581573486328f, 5.1328206062316895f)));
		objects.add(new GameObject(tree, new Vector3(-82.47026443481445f, 150.7070541381836f, 4.761768281459808f)));
		objects.add(new GameObject(tree, new Vector3(-112.3036003112793f, 137.77812004089355f, 5.057131052017212f)));
		objects.add(new GameObject(tree, new Vector3(-105.42936325073242f, 145.93578338623047f, 5.09025514125824f)));
		objects.add(new GameObject(tree, new Vector3(-90.55099487304688f, 145.12547492980957f, 4.130490720272064f)));
		objects.add(new GameObject(tree, new Vector3(-82.43901252746582f, 152.15136528015137f, 5.660279989242554f)));
		objects.add(new GameObject(tree, new Vector3(-100.29014587402344f, 149.3716049194336f, 5.386277437210083f)));
		objects.add(new GameObject(tree, new Vector3(-75.82763195037842f, 140.4062271118164f, 5.760939717292786f)));
		objects.add(new GameObject(tree, new Vector3(-86.76193237304688f, 152.28620529174805f, 5.33540666103363f)));
		objects.add(new GameObject(tree, new Vector3(-109.68659400939941f, 141.11592292785645f, 4.333828389644623f)));
		objects.add(new GameObject(tree, new Vector3(-80.02130508422852f, 144.996919631958f, 5.373880863189697f)));
		objects.add(new GameObject(tree, new Vector3(-79.91708278656006f, 142.16190338134766f, 4.336786568164825f)));
		objects.add(new GameObject(tree, new Vector3(-79.1050910949707f, 142.22965240478516f, 4.864481687545776f)));
		objects.add(new GameObject(tree, new Vector3(-86.88971519470215f, 143.32086563110352f, 4.300585091114044f)));
		objects.add(new GameObject(tree, new Vector3(-78.51028442382812f, 137.9150676727295f, 5.56853175163269f)));
		objects.add(new GameObject(tree, new Vector3(-104.32058334350586f, 146.8348503112793f, 5.7419925928115845f)));
		objects.add(new GameObject(tree, new Vector3(-88.97913932800293f, 139.60919380187988f, 5.361799001693726f)));
		objects.add(new GameObject(tree, new Vector3(-104.01182174682617f, 150.8204746246338f, 4.483196139335632f)));
		objects.add(new GameObject(tree, new Vector3(-114.08239364624023f, 134.57393646240234f, 5.271901488304138f)));
		objects.add(new GameObject(tree, new Vector3(-98.17205429077148f, 145.9145450592041f, 4.807219207286835f)));
		objects.add(new GameObject(tree, new Vector3(-73.49504947662354f, 138.8196086883545f, 4.736872613430023f)));
		objects.add(new GameObject(tree, new Vector3(-111.46756172180176f, 133.13007354736328f, 4.603762328624725f)));
		objects.add(new GameObject(tree, new Vector3(-87.19780921936035f, 140.43317794799805f, 5.34646213054657f)));
		objects.add(new GameObject(tree, new Vector3(-94.45052146911621f, 150.54235458374023f, 4.614426493644714f)));
		objects.add(new GameObject(tree, new Vector3(-95.81193923950195f, 142.80017852783203f, 4.58288699388504f)));
		objects.add(new GameObject(tree, new Vector3(-72.40116596221924f, 152.21341133117676f, 4.2825451493263245f)));
		objects.add(new GameObject(tree, new Vector3(-92.98135757446289f, 143.95416259765625f, 5.615091919898987f)));
		objects.add(new GameObject(tree, new Vector3(141.37414932250977f, 132.0700740814209f, 4.923124611377716f)));
		objects.add(new GameObject(tree, new Vector3(131.03633880615234f, 140.28181076049805f, 5.026957988739014f)));
		objects.add(new GameObject(tree, new Vector3(137.27056503295898f, 149.93610382080078f, 4.37437504529953f)));
		objects.add(new GameObject(tree, new Vector3(148.88492584228516f, 151.66864395141602f, 4.165092706680298f)));
		objects.add(new GameObject(tree, new Vector3(137.51070976257324f, 151.0604953765869f, 4.680175483226776f)));
		objects.add(new GameObject(tree, new Vector3(125.2231216430664f, 139.6794605255127f, 4.55440878868103f)));
		objects.add(new GameObject(tree, new Vector3(132.26508140563965f, 134.6430492401123f, 4.675477743148804f)));
		objects.add(new GameObject(tree, new Vector3(132.95716285705566f, 137.62184143066406f, 5.682511329650879f)));
		objects.add(new GameObject(tree, new Vector3(141.96972846984863f, 141.00576400756836f, 4.7629088163375854f)));
		objects.add(new GameObject(tree, new Vector3(151.3345718383789f, 149.7996425628662f, 5.121481418609619f)));
		objects.add(new GameObject(tree, new Vector3(144.35734748840332f, 138.487548828125f, 4.951576292514801f)));
		objects.add(new GameObject(tree, new Vector3(141.32226943969727f, 145.7190704345703f, 4.759507477283478f)));
		objects.add(new GameObject(tree, new Vector3(136.79637908935547f, 145.0901699066162f, 4.444526135921478f)));
		objects.add(new GameObject(tree, new Vector3(133.31865310668945f, 143.6806297302246f, 5.139107704162598f)));
		objects.add(new GameObject(tree, new Vector3(143.73680114746094f, 140.02795219421387f, 4.121422469615936f)));
		objects.add(new GameObject(tree, new Vector3(138.96846771240234f, 142.65588760375977f, 5.236191749572754f)));
		objects.add(new GameObject(tree, new Vector3(130.19984245300293f, 147.7223300933838f, 5.181964635848999f)));
		objects.add(new GameObject(tree, new Vector3(138.2581901550293f, 143.2137107849121f, 5.013061165809631f)));
		objects.add(new GameObject(tree, new Vector3(147.72159576416016f, 132.18589782714844f, 5.593791604042053f)));
		objects.add(new GameObject(tree, new Vector3(137.03519821166992f, 142.05738067626953f, 5.2942997217178345f)));
		objects.add(new GameObject(tree, new Vector3(144.70182418823242f, 140.91140747070312f, 4.70982700586319f)));
		objects.add(new GameObject(tree, new Vector3(148.85339736938477f, 133.03449630737305f, 4.6685948967933655f)));
		objects.add(new GameObject(tree, new Vector3(125.31644821166992f, 139.51878547668457f, 4.634371101856232f)));
		objects.add(new GameObject(tree, new Vector3(129.40799713134766f, 145.34479141235352f, 5.594061017036438f)));
		objects.add(new GameObject(tree, new Vector3(136.73169136047363f, 147.77481079101562f, 5.395155549049377f)));
		objects.add(new GameObject(tree, new Vector3(146.04026794433594f, 137.2014617919922f, 4.545466601848602f)));
		objects.add(new GameObject(tree, new Vector3(140.14798164367676f, 144.23874855041504f, 5.523005723953247f)));
		objects.add(new GameObject(tree, new Vector3(130.31620025634766f, 152.0967197418213f, 4.231519997119904f)));
		objects.add(new GameObject(tree, new Vector3(125.83954811096191f, 145.08295059204102f, 5.173973441123962f)));
	}
}
