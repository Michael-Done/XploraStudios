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
 * The BritishColumbiaScene class sets up Project Xplorer's third level.
 * <p>
 * Time taken to complete: 8 hours.
 * <p>
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class BritishColombiaScene extends GameObjectController {
	//Fields
	Array<Vector3> treeLocations;
	Model treeHighPoly;
	Model treeLowPoly;
	private Array<GroundObjectData> groundObjDataList;
	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btCollisionWorld collisionWorld;
	ModelInstance collisionLocation;
	public boolean isQuiz = false;
	int artifactsUnlocked = 0;
	public boolean moveToNext = false;
	public Stage exitStage;
	private Label exitLabel;
	private Skin exitSkin;
	public Stage hud;
	private Label time;
	private Label artifacts;
	private Array<TreasureChest> chests;

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
	public BritishColombiaScene(Settings settings) {
		super(settings);
		groundObjDataList = new Array<GroundObjectData>();
		treeLocations = new Array<Vector3>();
		chests = new Array<TreasureChest>();
		initalizeGroundObjectData();
		initalizeTreeLocations();
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
		assets.load("Mountains3.g3db", Model.class);
		assets.load("Boardwalk.g3db", Model.class);
		assets.load("BoardwalkSupports.g3db", Model.class);
		assets.load("SkyDome.g3db", Model.class);
		assets.load("Fog.g3db", Model.class);
		assets.load("TreeHighPoly.g3db", Model.class);
		assets.load("RomanStatue.g3db", Model.class);
		assets.load("Water.g3db", Model.class);
		assets.load("Tower.g3db", Model.class);
		assets.load("House.g3db", Model.class);
		assets.load("Bucket.g3db", Model.class);
		assets.load("IceSculpture.g3db", Model.class);
		assets.load("CanadaMap.g3db", Model.class);
		assets.load("MountainsModel.g3db", Model.class);
		assets.load("Redwood.g3db", Model.class);

		assets.load("BucketDescription.g3db", Model.class);
		assets.load("IcesculptureDescription.g3db", Model.class);
		assets.load("MapDescription.g3db", Model.class);
		assets.load("MountainsDescription.g3db", Model.class);
		assets.load("RedwoodDescription.g3db", Model.class);
		assets.finishLoading();
	}

	/** Initializes the CollisionWorld Configurations. */
	private void initalizeCollisionWorld() {
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

		for (GroundObjectData i : groundObjDataList) {
			collisionWorld.addCollisionObject(i.construct());
		}
	}

	@Override
	public void loadModelInstances() {
		chests.add(new TreasureChest(117.074f, -106.2915f, 12.365f, 180f, assets.get("Bucket.g3db", Model.class),
				assets.get("BucketDescription.g3db", Model.class)));
		chests.add(new TreasureChest(5.7923f, 73.2825f, 56.4242f, 90f, assets.get("IceSculpture.g3db", Model.class),
				assets.get("IcesculptureDescription.g3db", Model.class)));
		chests.add(new TreasureChest(133.3986f, 108.9361f, 57.9891f, 180f, assets.get("Redwood.g3db", Model.class),
				assets.get("RedwoodDescription.g3db", Model.class)));
		chests.add(new TreasureChest(-145.2268f, 110.3029f, 80.5643f, 0f,
				assets.get("MountainsModel.g3db", Model.class), assets.get("MountainsDescription.g3db", Model.class)));
		chests.add(new TreasureChest(-126.8985f, -135.4488f, 15.2694f, 180f, assets.get("CanadaMap.g3db", Model.class),
				assets.get("MapDescription.g3db", Model.class)));
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

		treeHighPoly = assets.get("TreeHighPoly.g3db", Model.class);

		Model mountain = assets.get("Mountains3.g3db", Model.class);
		ModelInstance mountain_inst = new GameObject(mountain);
		objects.add(mountain_inst);

		Model boardwalk = assets.get("Boardwalk.g3db", Model.class);
		ModelInstance boardwalk_inst = new GameObject(boardwalk);
		objects.add(boardwalk_inst);

		Model boardwalkSupports = assets.get("BoardwalkSupports.g3db", Model.class);
		ModelInstance boardwalkSupports_inst = new GameObject(boardwalkSupports);
		objects.add(boardwalkSupports_inst);

		Model sky = assets.get("SkyDome.g3db", Model.class);
		ModelInstance sky_inst = new GameObject(sky);
		sky_inst.transform.scale(50, 50, 50);
		objects.add(sky_inst);

		Model water = assets.get("Water.g3db", Model.class);
		ModelInstance water_inst = new GameObject(water);
		objects.add(water_inst);

		Model tower1 = assets.get("Tower.g3db", Model.class);
		ModelInstance tower1_inst = new GameObject(tower1);
		tower1_inst.transform.translate(6.2923f, 55.9242f, -73.2825f).rotate(0, 1, 0, 95.795f);
		objects.add(tower1_inst);

		Model tower2 = assets.get("Tower.g3db", Model.class);
		ModelInstance tower2_inst = new GameObject(tower2);
		tower2_inst.transform.translate(133.3986f, 57.3891f, -109.3361f).rotate(0, 1, 0, 180f);
		objects.add(tower2_inst);

		Model house = assets.get("House.g3db", Model.class);
		ModelInstance house_inst = new GameObject(house, new Vector3(-136.7401f, 110.3048f, 82.4468f));
		objects.add(house_inst);

		for (Vector3 i : treeLocations) {
			objects.add(new GameObject(treeHighPoly, new Vector3(i.x, i.y, (float) (i.z + Math.random() * 0.5f))));
		}
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ball";
		mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
		Model a = mb.end();
		collisionLocation = new ModelInstance(a);
	}

	@Override
	public void camSetup() {
		// Create a camera and point it to our model
		Gdx.input.setCursorCatched(true);
		float playerHeight = 1f;
		ProjectXploraGame.camera.position.set(149f, -50f, playerHeight + 12.36f);
		ProjectXploraGame.camera.lookAt(149f, -49f, playerHeight + 12.36f);
		ProjectXploraGame.camera.near = 0.1f;
		ProjectXploraGame.camera.far = 1000f;
		ProjectXploraGame.camera.update();
		cameraController = new PlayerCameraController(ProjectXploraGame.camera, settings);
		initalizeCollisionShapes();
		// cameraController.unlockPosition();
		Gdx.input.setInputProcessor(cameraController);
		cameraResize(screenWidth, screenHeight);
	}

	/** Initializes all collision shapes. */
	private void initalizeCollisionShapes() {
		cameraController.addCollision(new CollisionRect(new Vector2(-137.98783361911774f, 114.8446736484766f),
				new Vector2(-134.45952355861664f, 116.84577129781246f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-135.9876924753189f, 101.64572574198246f),
				new Vector2(-132.4593824148178f, 103.64682339131832f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-141.31417334079742f, 103.08030545711517f),
				new Vector2(-131.13909661769867f, 115.60232698917389f)));
		cameraController.addCollision(new CollisionRect(new Vector2(0.04222393035888672f, -5.5783772468566895f),
				new Vector2(12.45469331741333f, 67.93929815292358f)));
		cameraController.addCollision(new CollisionRect(new Vector2(154.4150948524475f, -160.55537700653076f),
				new Vector2(185.5892539024353f, 2.8989267349243164f)));
		cameraController.addCollision(new CollisionRect(new Vector2(89.31862592697144f, -182.9452645778656f),
				new Vector2(158.02403688430786f, -157.5610601902008f)));
		cameraController.addCollision(new CollisionRect(new Vector2(70.56520104408264f, -159.88481521606445f),
				new Vector2(91.2093198299408f, 1.9711112976074219f)));
		cameraController.addCollision(new CollisionRect(new Vector2(107.50589370727539f, 1.5654397010803223f),
				new Vector2(156.98833465576172f, 75.0831151008606f)));
		cameraController.addCollision(new CollisionRect(new Vector2(103.9190137386322f, 119.16249990463257f),
				new Vector2(135.08880257606506f, 124.02336359024048f)));
		cameraController.addCollision(new CollisionRect(new Vector2(127.35429540276527f, 114.81724545359612f),
				new Vector2(132.042867988348f, 115.444805175066f)));
		cameraController.addCollision(new CollisionRect(new Vector2(134.78045418858528f, 114.77835416793823f),
				new Vector2(139.59044501185417f, 119.63921785354614f)));
		cameraController.addCollision(new CollisionRect(new Vector2(138.837880641222f, 103.47116708755493f),
				new Vector2(143.6478714644909f, 115.37275075912476f)));
		cameraController.addCollision(new CollisionRect(new Vector2(127.37725496292114f, 103.22064377367496f),
				new Vector2(139.55471754074097f, 103.87862227857113f)));
		cameraController.addCollision(new CollisionRect(new Vector2(107.74365663528442f, 103.14402520656586f),
				new Vector2(128.022563457489f, 115.44482290744781f)));
		cameraController.addCollision(new CollisionRect(new Vector2(64.75916624069214f, 74.66835379600525f),
				new Vector2(108.7635350227356f, 105.33715844154358f)));
		cameraController.addCollision(new CollisionRect(new Vector2(11.759467124938965f, 1.5654373168945312f),
				new Vector2(104.14308071136475f, 71.92264080047607f)));
		cameraController.addCollision(new CollisionRect(new Vector2(11.759469509124756f, 74.62890058755875f),
				new Vector2(61.50050401687622f, 79.25555795431137f)));
		cameraController.addCollision(new CollisionRect(new Vector2(0.36410093307495117f, 78.76856327056885f),
				new Vector2(61.50048494338989f, 105.25779247283936f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-163.5484367609024f, -46.687615513801575f),
				new Vector2(-151.8335098028183f, -27.750069499015808f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-168.4709495306015f, -159.4420337677002f),
				new Vector2(-163.42213213443756f, -27.5991153717041f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-165.60160160064697f, -163.82354527711868f),
				new Vector2(-114.36257839202881f, -158.77472132444382f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-114.87668454647064f, -159.4420337677002f),
				new Vector2(-109.8278671503067f, -27.5991153717041f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-169.5159101486206f, -27.920445203781128f),
				new Vector2(-118.41286182403564f, 8.081303834915161f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-114.87671375274658f, -27.940891981124878f),
				new Vector2(-88.04564952850342f, 4.267534017562866f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-88.75124841928482f, 3.590989112854004f),
				new Vector2(-83.72342199087143f, 50.79911708831787f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-84.60538864135742f, 50.056984424591064f),
				new Vector2(0.8111953735351562f, 105.25779485702515f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-55.09624123573303f, 137.16913133859634f),
				new Vector2(-33.53801131248474f, 143.292256295681f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-34.08017635345459f, 108.78418803215027f),
				new Vector2(104.19849872589111f, 143.76811146736145f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-111.6797423362732f, 108.78418803215027f),
				new Vector2(-54.74622964859009f, 143.76811146736145f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-124.96147453784943f, 94.39419150352478f),
				new Vector2(-111.33960425853729f, 131.2423288822174f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-150.5501902103424f, 120.69405496120453f),
				new Vector2(-122.96797156333923f, 135.29666006565094f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-177.77828812599182f, 94.39419150352478f),
				new Vector2(-150.19606947898865f, 131.2423288822174f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-169.76149201393127f, 57.82462954521179f),
				new Vector2(-142.1792733669281f, 94.67276692390442f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-158.3502173423767f, 7.694408893585205f),
				new Vector2(-92.0298981666565f, 58.07585954666138f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-107.76008784770966f, 57.62707471847534f),
				new Vector2(-92.02989399433136f, 105.26759386062622f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-138.92181038856506f, 61.505534648895264f),
				new Vector2(-111.33959174156189f, 94.67278718948364f)));
		cameraController.addCollision(new CollisionRect(new Vector2(-89.0821472927928f, 54.18705224990845f),
				new Vector2(-88.14071495085955f, 108.70761632919312f)));
		for (Vector3 i : treeLocations) {
			cameraController.addCollision(new CollisionCircle(new Vector2(i.x, i.y), 1.5f));
		}
	}

	@Override
	/** updates all aspects of the scene */
	public void update() {
		super.update();
		// System.out.println(1 / Gdx.graphics.getDeltaTime() + " FPS");
		for (TreasureChest t : chests) {
			t.update(ProjectXploraGame.camera.position);
			isQuiz |= t.isQuiz();
			if (!t.isQuiz() && t.unlocked() && !t.added) {
				objects.add(t.artifact);
				objects.add(t.description);
				artifactsUnlocked++;
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
		time.setText((ProjectXploraGame.timer.player.getBCTime()) + " Seconds");
		artifacts.setText(artifactsUnlocked + "/5");
	}

	/** Resets the isQuiz variable. */
	public void resetIsQuiz() {
		isQuiz = false;
	}

	@Override
	public void updateCamera() {
		super.updateCamera();
		Vector3 intersectLocation = GroundCollisionDetector.rayTest(collisionWorld, cameraController.getRayFrom(),
				cameraController.getRayTo());
		if (intersectLocation != null) {
			cameraController.setZ(intersectLocation.z + 1);
			collisionLocation.transform.setTranslation(intersectLocation);
		}

	}

	/** Initializes placements of all ground objects. */
	private void initalizeGroundObjectData() {
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-119.27326202392578f, 22.249927520751953f, 20.606400966644287f), new Vector3(0, 0, 0), 0f,
				new Vector3(108.82850646972656f, 66.30624771118164f, 5.000000596046448f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-125.45814514160156f, -96.67216300964355f, 12.769299745559692f), new Vector3(0, 0, 0), 0f,
				new Vector3(108.82850646972656f, 156.4835262298584f, 5.000000596046448f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(118.28474998474121f, -75.0916862487793f, 9.8649001121521f), new Vector3(0, 0, 0), 0f,
				new Vector3(108.82850646972656f, 204.33692932128906f, 5.000000596046448f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-90.57474136352539f, 79.83270645141602f, 44.14041519165039f), new Vector3(1, 0, 0),
				26.445655750463672f, new Vector3(4.000000059604645f, 56.22746729981598f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-140.55505752563477f, 78.13718795776367f, 72.86617279052734f), new Vector3(1, 0, 0),
				25.527814497793166f, new Vector3(4.000000059604645f, 35.948121602959354f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-116.60064697265625f, -13.32534909248352f, 20.30721426010132f), new Vector3(1, 0, 0),
				18.980708272603348f, new Vector3(4.000000059604645f, 36.75827297590803f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-90.57474136352539f, 29.35767412185669f, 28.952512741088867f), new Vector3(1, 0, 0),
				7.137170707423063f, new Vector3(4.000000059604645f, 42.93909892336672f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(105.8681583404541f, 36.117708683013916f, 16.579017639160156f), new Vector3(1, 0, 0),
				6.9858768271571865f, new Vector3(4.000000059604645f, 70.3142388155523f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(84.45240020751953f, 73.00661087036133f, 28.736438751220703f), new Vector3(0, 1, 0),
				22.09372480510053f, new Vector3(41.90890525120255f, 3.985562324523926f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(36.762564182281494f, 73.00661087036133f, 46.49491310119629f), new Vector3(0, 1, 0),
				21.902713776137414f, new Vector3(52.955436799257264f, 3.985562324523926f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(13.760716915130615f, 106.98049545288086f, 34.08492565155029f), new Vector3(0, 1, 0),
				-3.0480533342780034f, new Vector3(95.271519398826f, 4.0283966064453125f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-57.31091499328613f, 106.98049545288086f, 30.765011310577393f), new Vector3(0, 1, 0),
				-19.843569599489747f, new Vector3(4.636622116629835f, 4.0283966064453125f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-63.283348083496094f, 106.98049545288086f, 29.22511339187622f), new Vector3(0, 1, 0),
				-11.231261544292732f, new Vector3(7.731594489151341f, 4.028406143188477f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-70.40708541870117f, 106.98050498962402f, 28.52463722229004f), new Vector3(0, 1, 0),
				0.902080125631737f, new Vector3(6.664796971752482f, 4.0283966064453125f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-76.5721321105957f, 106.98050498962402f, 29.122724533081055f), new Vector3(0, 1, 0),
				10.90123123324936f, new Vector3(5.770233422924673f, 4.0283966064453125f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-81.90244674682617f, 106.98050498962402f, 30.644173622131348f), new Vector3(0, 1, 0),
				21.3434551739629f, new Vector3(5.362309048456149f, 4.0283966064453125f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(-125.06488800048828f, 59.91782188415527f, 60.89055061340332f), new Vector3(0, 1, 0),
				17.408345103720652f, new Vector3(28.27542251062869f, 4.000000953674316f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(84.48987007141113f, 106.98049545288086f, 43.619747161865234f), new Vector3(0, 1, 0),
				-19.795466219023897f, new Vector3(41.34994777020991f, 4.028406143188477f, 0.1f)));
		groundObjDataList.add(new GroundObjectData(
				new Vector3(119.92803573608398f, 117.2661018371582f, 54.23365592956543f), new Vector3(0, 1, 0),
				-16.67353071023783f, new Vector3(25.178297772374822f, 3.9999961853027344f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(63.182854652404785f, 89.98284339904785f, 36.61788463592529f),
						new Vector3(0, 0, 0), 0f, new Vector3(3.707546293735504f, 29.966905117034912f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(105.8681583404541f, 73.00661087036133f, 20.854992866516113f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 3.985562324523926f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(63.182854652404785f, 106.98049545288086f, 36.61788463592529f),
						new Vector3(0, 0, 0), 0f, new Vector3(3.707546293735504f, 4.0283966064453125f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(63.182854652404785f, 73.00661087036133f, 36.61788463592529f),
						new Vector3(0, 0, 0), 0f, new Vector3(3.707546293735504f, 3.985562324523926f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-44.46896553039551f, 106.98049545288086f, 31.551969051361084f),
						new Vector3(0, 0, 0), 0f, new Vector3(21.322617530822754f, 4.0283966064453125f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-44.46896553039551f, 123.89877319335938f, 31.551969051361084f),
						new Vector3(0, 0, 0), 0f, new Vector3(21.322617530822754f, 29.80815887451172f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-86.48723602294922f, 106.98050498962402f, 31.620001792907715f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.17501837015152f, 4.0283966064453125f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-86.48723602294922f, 52.660865783691406f, 31.620001792907715f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.17501837015152f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-86.48723602294922f, 79.81358528137207f, 31.620001792907715f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.17501837015152f, 50.30543327331543f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-90.57474136352539f, 52.660865783691406f, 31.620001792907715f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-109.57473754882812f, 106.99687957763672f, 56.66083335876465f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 3.984689712524414f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-100.0747299194336f, 106.99687957763672f, 56.66083335876465f),
						new Vector3(0, 0, 0), 0f, new Vector3(15.0f, 3.984689712524414f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-90.57474136352539f, 106.99687957763672f, 56.66083335876465f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 3.984689712524414f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-109.57473754882812f, 59.91782188415527f, 56.66083335876465f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-140.55505752563477f, 59.91782188415527f, 65.12026786804199f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(105.90564727783203f, 112.13040351867676f, 50.621604919433594f),
						new Vector3(0, 0, 0), 0f, new Vector3(3.925078809261322f, 6.271400451660156f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(105.90564727783203f, 106.98050498962402f, 50.621604919433594f),
						new Vector3(0, 0, 0), 0f, new Vector3(3.925078809261322f, 4.0283966064453125f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(105.90564727783203f, 117.2661018371582f, 50.621604919433594f),
						new Vector3(0, 0, 0), 0f, new Vector3(3.925078809261322f, 3.9999961853027344f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(133.39404106140137f, 117.2661018371582f, 57.845706939697266f),
						new Vector3(0, 0, 0), 0f, new Vector3(2.812337875366211f, 3.9999961853027344f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-90.57474136352539f, 6.054480075836182f, 26.28502368927002f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-116.60064697265625f, 6.054480075836182f, 26.28502368927002f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-103.58768463134766f, 6.054480075836182f, 26.28502368927002f),
						new Vector3(0, 0, 0), 0f, new Vector3(22.025909423828125f, 4.000000953674316f, 0.1f)));
		groundObjDataList
				.add(new GroundObjectData(new Vector3(-109.57473754882812f, 83.46117973327637f, 56.66083335876465f),
						new Vector3(0, 0, 0), 0f, new Vector3(4.000000059604645f, 43.08670997619629f, 0.1f)));
	}

	private void initalizeTreeLocations() {
		treeLocations.add(new Vector3(-158.50319862365723f, -151.54881477355957f, 15.269314050674438f));
		treeLocations.add(new Vector3(-165.73089599609375f, -143.62805366516113f, 15.269314050674438f));
		treeLocations.add(new Vector3(-159.09725189208984f, -86.40056610107422f, 15.269314050674438f));
		treeLocations.add(new Vector3(-138.3052635192871f, -79.7669267654419f, 15.269314050674438f));
		treeLocations.add(new Vector3(150.2395248413086f, -111.81793212890625f, 12.36488938331604f));
		treeLocations.add(new Vector3(141.42767906188965f, -67.2636604309082f, 12.36488938331604f));
		treeLocations.add(new Vector3(133.5069179534912f, -50.53105354309082f, 12.36488938331604f));
		treeLocations.add(new Vector3(145.5871295928955f, -144.92180824279785f, 12.364890575408936f));
		treeLocations.add(new Vector3(88.4749984741211f, -147.49464988708496f, 12.364890575408936f));
		treeLocations.add(new Vector3(-141.94236755371094f, -148.9964771270752f, 15.269314050674438f));
		treeLocations.add(new Vector3(142.24928855895996f, -135.47138214111328f, 12.364890575408936f));
		treeLocations.add(new Vector3(114.4644546508789f, -147.5438404083252f, 12.36488938331604f));
		treeLocations.add(new Vector3(119.06254768371582f, -135.68538665771484f, 12.36488938331604f));
		treeLocations.add(new Vector3(113.50227355957031f, -137.14598655700684f, 12.364890575408936f));
		treeLocations.add(new Vector3(139.79729652404785f, -146.72471046447754f, 12.364890575408936f));
		treeLocations.add(new Vector3(107.81875610351562f, -137.60717391967773f, 12.364890575408936f));
		treeLocations.add(new Vector3(130.91235160827637f, -139.19156074523926f, 12.364890575408936f));
		treeLocations.add(new Vector3(122.98033714294434f, -139.60615158081055f, 12.364890575408936f));
		treeLocations.add(new Vector3(86.08614921569824f, -147.93262481689453f, 11.946511268615723f));
		treeLocations.add(new Vector3(102.65656471252441f, -135.22509574890137f, 12.364890575408936f));
		treeLocations.add(new Vector3(144.24339294433594f, -126.1142349243164f, 12.364890575408936f));
		treeLocations.add(new Vector3(128.3563232421875f, -132.2858428955078f, 12.36488938331604f));
		treeLocations.add(new Vector3(130.48633575439453f, -128.7427806854248f, 12.364890575408936f));
		treeLocations.add(new Vector3(135.39827346801758f, -131.4972972869873f, 12.364890575408936f));
		treeLocations.add(new Vector3(130.8336067199707f, -133.29605102539062f, 12.364890575408936f));
		treeLocations.add(new Vector3(130.62002182006836f, -130.4086685180664f, 12.364890575408936f));
		treeLocations.add(new Vector3(103.94106864929199f, -122.47291564941406f, 12.364890575408936f));
		treeLocations.add(new Vector3(96.13655090332031f, -114.35384750366211f, 12.36488938331604f));
		treeLocations.add(new Vector3(115.5103874206543f, -128.7502384185791f, 12.364890575408936f));
		treeLocations.add(new Vector3(141.0978889465332f, -121.72557830810547f, 12.364890575408936f));
		treeLocations.add(new Vector3(105.74997901916504f, -125.36343574523926f, 12.36488938331604f));
		treeLocations.add(new Vector3(99.71714973449707f, -119.09364700317383f, 12.364890575408936f));
		treeLocations.add(new Vector3(115.05821228027344f, -120.34967422485352f, 12.364891767501831f));
		treeLocations.add(new Vector3(146.94965362548828f, -121.86354637145996f, 12.364890575408936f));
		treeLocations.add(new Vector3(92.694091796875f, -117.9261302947998f, 12.36488938331604f));
		treeLocations.add(new Vector3(124.26630973815918f, -117.00509071350098f, 12.364890575408936f));
		treeLocations.add(new Vector3(107.00078964233398f, -123.69661331176758f, 12.364890575408936f));
		treeLocations.add(new Vector3(138.87052536010742f, -124.16584968566895f, 12.36488938331604f));
		treeLocations.add(new Vector3(135.1867389678955f, -119.15351867675781f, 12.364890575408936f));
		treeLocations.add(new Vector3(117.4130630493164f, -120.18328666687012f, 12.364890575408936f));
		treeLocations.add(new Vector3(97.98175811767578f, -116.90601348876953f, 12.364890575408936f));
		treeLocations.add(new Vector3(122.19758033752441f, -126.986083984375f, 12.36488938331604f));
		treeLocations.add(new Vector3(128.58546257019043f, -119.94649887084961f, 12.364890575408936f));
		treeLocations.add(new Vector3(110.3580093383789f, -118.18639755249023f, 12.364890575408936f));
		treeLocations.add(new Vector3(97.1798038482666f, -134.76238250732422f, 12.364890575408936f));
		treeLocations.add(new Vector3(97.75161743164062f, -129.13206100463867f, 12.364890575408936f));
		treeLocations.add(new Vector3(118.56460571289062f, -118.51803779602051f, 12.364890575408936f));
		treeLocations.add(new Vector3(-142.920503616333f, -144.11684036254883f, 15.269314050674438f));
		treeLocations.add(new Vector3(-132.49506950378418f, -141.03596687316895f, 15.269314050674438f));
		treeLocations.add(new Vector3(-143.688325881958f, -130.91090202331543f, 15.269317626953125f));
		treeLocations.add(new Vector3(-139.38770294189453f, -132.2121810913086f, 15.269314050674438f));
		treeLocations.add(new Vector3(-150.03987312316895f, -128.75282287597656f, 15.269315242767334f));
		treeLocations.add(new Vector3(-148.03926467895508f, -136.6294765472412f, 15.269315242767334f));
		treeLocations.add(new Vector3(-147.94818878173828f, -132.1472454071045f, 15.269314050674438f));
		treeLocations.add(new Vector3(-140.47865867614746f, -139.22443389892578f, 15.269312858581543f));
		treeLocations.add(new Vector3(-136.8915843963623f, -135.19479751586914f, 15.269314050674438f));
		treeLocations.add(new Vector3(-148.02172660827637f, -138.99808883666992f, 15.269315242767334f));
		treeLocations.add(new Vector3(-140.19662857055664f, -126.60216331481934f, 15.269312858581543f));
		treeLocations.add(new Vector3(119.02250289916992f, -112.87452697753906f, 12.364890575408936f));
		treeLocations.add(new Vector3(128.13323974609375f, -115.5669116973877f, 12.364890575408936f));
		treeLocations.add(new Vector3(133.04848670959473f, -115.75680732727051f, 12.364891767501831f));
		treeLocations.add(new Vector3(115.99228858947754f, -113.76855850219727f, 12.364890575408936f));
		treeLocations.add(new Vector3(142.6975154876709f, -117.0775318145752f, 12.364890575408936f));
		treeLocations.add(new Vector3(-152.38917350769043f, -120.3391170501709f, 15.269314050674438f));
		treeLocations.add(new Vector3(-149.46735382080078f, -118.53311538696289f, 15.269314050674438f));
		treeLocations.add(new Vector3(-129.9743366241455f, -126.15096092224121f, 15.269312858581543f));
		treeLocations.add(new Vector3(125.17780303955078f, -109.31576728820801f, 12.364890575408936f));
		treeLocations.add(new Vector3(146.02746963500977f, -106.49838447570801f, 12.364890575408936f));
		treeLocations.add(new Vector3(135.56256294250488f, -109.98958587646484f, 12.36488938331604f));
		treeLocations.add(new Vector3(111.27376556396484f, -110.63602447509766f, 12.364890575408936f));
		treeLocations.add(new Vector3(126.64352416992188f, -109.64360237121582f, 12.364890575408936f));
		treeLocations.add(new Vector3(125.99443435668945f, -110.13581275939941f, 12.36488938331604f));
		treeLocations.add(new Vector3(132.21588134765625f, -108.90539169311523f, 12.364891767501831f));
		treeLocations.add(new Vector3(138.70726585388184f, -110.20713806152344f, 12.364890575408936f));
		treeLocations.add(new Vector3(-149.70279693603516f, -109.91562843322754f, 15.269314050674438f));
		treeLocations.add(new Vector3(-148.816556930542f, -82.46055603027344f, 15.269314050674438f));
		treeLocations.add(new Vector3(-142.86721229553223f, -84.79740142822266f, 15.269314050674438f));
		treeLocations.add(new Vector3(-125.5921459197998f, -85.74221611022949f, 15.269314050674438f));
		treeLocations.add(new Vector3(-134.42082405090332f, -87.66724586486816f, 15.269315242767334f));
		treeLocations.add(new Vector3(-129.31052207946777f, -118.78561973571777f, 15.269314050674438f));
		treeLocations.add(new Vector3(-137.1497631072998f, -114.44524765014648f, 15.269314050674438f));
		treeLocations.add(new Vector3(-152.25201606750488f, -113.56853485107422f, 15.269314050674438f));
		treeLocations.add(new Vector3(95.17031669616699f, -105.33605575561523f, 12.364890575408936f));
		treeLocations.add(new Vector3(142.6917552947998f, -101.95472717285156f, 12.364891767501831f));
		treeLocations.add(new Vector3(144.54298973083496f, -102.51104354858398f, 12.364891767501831f));
		treeLocations.add(new Vector3(121.6058349609375f, -102.9200553894043f, 12.364890575408936f));
		treeLocations.add(new Vector3(139.48741912841797f, -104.17901992797852f, 12.364890575408936f));
		treeLocations.add(new Vector3(135.19094467163086f, -104.94502067565918f, 12.364890575408936f));
		treeLocations.add(new Vector3(136.1197566986084f, -104.8441219329834f, 12.364890575408936f));
		treeLocations.add(new Vector3(88.34546089172363f, -112.23093032836914f, 12.364890575408936f));
		treeLocations.add(new Vector3(125.19159317016602f, -105.56037902832031f, 12.364891767501831f));
		treeLocations.add(new Vector3(108.74292373657227f, -102.79966354370117f, 12.364890575408936f));
		treeLocations.add(new Vector3(90.68779945373535f, -100.7880687713623f, 12.36488938331604f));
		treeLocations.add(new Vector3(-146.35708808898926f, -64.32161331176758f, 15.269314050674438f));
		treeLocations.add(new Vector3(-146.16990089416504f, -70.57252407073975f, 15.269314050674438f));
		treeLocations.add(new Vector3(-136.15216255187988f, -69.2650842666626f, 15.269314050674438f));
		treeLocations.add(new Vector3(-119.42855834960938f, -68.73715400695801f, 15.269314050674438f));
		treeLocations.add(new Vector3(-150.56280136108398f, -63.45273971557617f, 15.269314050674438f));
		treeLocations.add(new Vector3(-149.62912559509277f, -108.8448715209961f, 15.269314050674438f));
		treeLocations.add(new Vector3(-144.2513084411621f, -105.5061149597168f, 15.269315242767334f));
		treeLocations.add(new Vector3(-151.8602180480957f, -108.21627616882324f, 15.269315242767334f));
		treeLocations.add(new Vector3(-148.9691925048828f, -104.80639457702637f, 15.269314050674438f));
		treeLocations.add(new Vector3(-147.47568130493164f, -105.28475761413574f, 15.269314050674438f));
		treeLocations.add(new Vector3(-147.57991790771484f, -107.87280082702637f, 15.269312858581543f));
		treeLocations.add(new Vector3(-140.4860782623291f, -107.96018600463867f, 15.269314050674438f));
		treeLocations.add(new Vector3(-148.0709171295166f, -103.37887763977051f, 15.269312858581543f));
		treeLocations.add(new Vector3(-150.16486167907715f, -106.17425918579102f, 15.269314050674438f));
		treeLocations.add(new Vector3(-141.7060947418213f, -90.64519882202148f, 15.269314050674438f));
		treeLocations.add(new Vector3(-141.653470993042f, -91.00893974304199f, 15.269314050674438f));
		treeLocations.add(new Vector3(-151.87786102294922f, -91.87647819519043f, 15.269314050674438f));
		treeLocations.add(new Vector3(-133.73127937316895f, -101.01983070373535f, 15.269314050674438f));
		treeLocations.add(new Vector3(-126.1290454864502f, -80.82322120666504f, 15.269314050674438f));
		treeLocations.add(new Vector3(-145.81825256347656f, -76.37746334075928f, 15.269312858581543f));
		treeLocations.add(new Vector3(-145.8181858062744f, -78.54209423065186f, 15.269314050674438f));
		treeLocations.add(new Vector3(-131.37459754943848f, -79.27187919616699f, 15.269314050674438f));
		treeLocations.add(new Vector3(-131.31766319274902f, -81.8547534942627f, 15.269314050674438f));
		treeLocations.add(new Vector3(-126.61540031433105f, -84.66328620910645f, 15.269314050674438f));
		treeLocations.add(new Vector3(138.0320167541504f, -96.22251510620117f, 12.36488938331604f));
		treeLocations.add(new Vector3(124.17886734008789f, -99.10127639770508f, 12.364890575408936f));
		treeLocations.add(new Vector3(131.56837463378906f, -97.88652420043945f, 12.364890575408936f));
		treeLocations.add(new Vector3(141.28260612487793f, -98.29883575439453f, 12.364890575408936f));
		treeLocations.add(new Vector3(145.14801025390625f, -101.84680938720703f, 12.364891767501831f));
		treeLocations.add(new Vector3(117.90975570678711f, -93.85878562927246f, 12.36488938331604f));
		treeLocations.add(new Vector3(104.87870216369629f, -94.44453239440918f, 12.36488938331604f));
		treeLocations.add(new Vector3(125.58659553527832f, -92.08752632141113f, 12.364890575408936f));
		treeLocations.add(new Vector3(138.11747550964355f, -99.06020164489746f, 12.364891767501831f));
		treeLocations.add(new Vector3(106.50315284729004f, -91.6885757446289f, 12.364891767501831f));
		treeLocations.add(new Vector3(96.69923782348633f, -95.00689506530762f, 12.364891767501831f));
		treeLocations.add(new Vector3(126.18989944458008f, -97.45429992675781f, 12.364891767501831f));
		treeLocations.add(new Vector3(114.01998519897461f, -99.56332206726074f, 12.364890575408936f));
		treeLocations.add(new Vector3(124.63214874267578f, -96.86012268066406f, 12.36488938331604f));
		treeLocations.add(new Vector3(137.43045806884766f, -99.0138053894043f, 12.36488938331604f));
		treeLocations.add(new Vector3(90.52024841308594f, -83.81041526794434f, 12.364890575408936f));
		treeLocations.add(new Vector3(109.56058502197266f, -96.08036041259766f, 12.36488938331604f));
		treeLocations.add(new Vector3(136.77071571350098f, -95.22685050964355f, 12.364890575408936f));
		treeLocations.add(new Vector3(146.09975814819336f, -98.23297500610352f, 12.364890575408936f));
		treeLocations.add(new Vector3(-123.39751243591309f, -65.75361251831055f, 15.269314050674438f));
		treeLocations.add(new Vector3(-133.24009895324707f, -60.59443950653076f, 15.269314050674438f));
		treeLocations.add(new Vector3(-134.04728889465332f, -60.01676082611084f, 15.269314050674438f));
		treeLocations.add(new Vector3(-142.82958984375f, -58.48065376281738f, 15.269314050674438f));
		treeLocations.add(new Vector3(-143.42631340026855f, -56.27441883087158f, 15.269314050674438f));
		treeLocations.add(new Vector3(-151.94446563720703f, -54.53253746032715f, 15.269314050674438f));
		treeLocations.add(new Vector3(-152.75395393371582f, -98.93258094787598f, 15.269315242767334f));
		treeLocations.add(new Vector3(-139.39631462097168f, -96.69795989990234f, 15.269314050674438f));
		treeLocations.add(new Vector3(-128.53631019592285f, -93.43279838562012f, 15.269314050674438f));
		treeLocations.add(new Vector3(-147.93692588806152f, -97.51070976257324f, 15.269314050674438f));
		treeLocations.add(new Vector3(-124.7105884552002f, -99.18484687805176f, 15.269314050674438f));
		treeLocations.add(new Vector3(-129.2029571533203f, -93.51243019104004f, 15.269315242767334f));
		treeLocations.add(new Vector3(112.26758003234863f, -84.01412963867188f, 12.364890575408936f));
		treeLocations.add(new Vector3(116.93081855773926f, -83.26508522033691f, 12.364890575408936f));
		treeLocations.add(new Vector3(112.80990600585938f, -83.85305404663086f, 12.364890575408936f));
		treeLocations.add(new Vector3(137.76301383972168f, -88.09494018554688f, 12.364890575408936f));
		treeLocations.add(new Vector3(118.85407447814941f, -88.61668586730957f, 12.364891767501831f));
		treeLocations.add(new Vector3(137.7672290802002f, -92.53837585449219f, 12.364890575408936f));
		treeLocations.add(new Vector3(122.5442886352539f, -88.14542770385742f, 12.364891767501831f));
		treeLocations.add(new Vector3(117.26794242858887f, -86.81239128112793f, 12.364891767501831f));
		treeLocations.add(new Vector3(-123.71157646179199f, -56.180787086486816f, 15.269314050674438f));
		treeLocations.add(new Vector3(-120.13213157653809f, -63.96146774291992f, 15.269314050674438f));
		treeLocations.add(new Vector3(-138.23737144470215f, -54.19344902038574f, 15.269314050674438f));
		treeLocations.add(new Vector3(-125.27213096618652f, -57.156033515930176f, 15.269312858581543f));
		treeLocations.add(new Vector3(-125.85548400878906f, -60.37924766540527f, 15.269314050674438f));
		treeLocations.add(new Vector3(-150.2933406829834f, -48.940744400024414f, 15.269314050674438f));
		treeLocations.add(new Vector3(-122.73643493652344f, -62.36389636993408f, 15.269315242767334f));
		treeLocations.add(new Vector3(-118.37862968444824f, -60.89121341705322f, 15.269314050674438f));
		treeLocations.add(new Vector3(-142.42216110229492f, -39.78662967681885f, 15.269315242767334f));
		treeLocations.add(new Vector3(-138.28601837158203f, -41.150360107421875f, 15.269314050674438f));
		treeLocations.add(new Vector3(-143.707275390625f, -35.96719026565552f, 15.269314050674438f));
		treeLocations.add(new Vector3(-148.82193565368652f, -40.61873912811279f, 15.269314050674438f));
		treeLocations.add(new Vector3(-148.46031188964844f, -39.11728858947754f, 15.269314050674438f));
		treeLocations.add(new Vector3(-133.92343521118164f, -44.78395938873291f, 15.269314050674438f));
		treeLocations.add(new Vector3(-122.32138633728027f, -47.71872043609619f, 15.269312858581543f));
		treeLocations.add(new Vector3(-151.08065605163574f, -42.18707084655762f, 15.269314050674438f));
		treeLocations.add(new Vector3(118.60427856445312f, -61.79140567779541f, 12.364890575408936f));
		treeLocations.add(new Vector3(105.8160400390625f, -60.869855880737305f, 12.364891767501831f));
		treeLocations.add(new Vector3(131.84650421142578f, -59.52667713165283f, 12.364890575408936f));
		treeLocations.add(new Vector3(120.85424423217773f, -62.91649341583252f, 12.364890575408936f));
		treeLocations.add(new Vector3(122.78071403503418f, -63.020453453063965f, 12.364890575408936f));
		treeLocations.add(new Vector3(131.4510726928711f, -58.06787967681885f, 12.364890575408936f));
		treeLocations.add(new Vector3(130.32386779785156f, -59.411959648132324f, 12.36488938331604f));
		treeLocations.add(new Vector3(132.08990097045898f, -57.32361316680908f, 12.364891767501831f));
		treeLocations.add(new Vector3(144.5210838317871f, -57.450385093688965f, 12.364890575408936f));
		treeLocations.add(new Vector3(116.87276840209961f, -57.57016658782959f, 12.36488938331604f));
		treeLocations.add(new Vector3(146.83616638183594f, -52.08679676055908f, 12.36488938331604f));
		treeLocations.add(new Vector3(-151.93695068359375f, 0.34910690039396286f, 23.106441497802734f));
		treeLocations.add(new Vector3(133.37937355041504f, -28.477702140808105f, 12.36488938331604f));
		treeLocations.add(new Vector3(145.91873168945312f, -35.339882373809814f, 12.364890575408936f));
		treeLocations.add(new Vector3(136.08689308166504f, -27.875688076019287f, 12.364891767501831f));
		treeLocations.add(new Vector3(134.4599723815918f, -26.574153900146484f, 12.36488938331604f));
		treeLocations.add(new Vector3(118.81650924682617f, -28.453073501586914f, 12.364891767501831f));
		treeLocations.add(new Vector3(146.1337375640869f, -25.064735412597656f, 12.36488938331604f));
		treeLocations.add(new Vector3(101.75256729125977f, -24.256114959716797f, 12.364890575408936f));
		treeLocations.add(new Vector3(147.8887939453125f, -24.97459650039673f, 12.364890575408936f));
		treeLocations.add(new Vector3(138.72410774230957f, -28.43642234802246f, 12.364890575408936f));
		treeLocations.add(new Vector3(132.85597801208496f, -28.465421199798584f, 12.364890575408936f));
		treeLocations.add(new Vector3(116.65817260742188f, -24.993245601654053f, 12.364890575408936f));
		treeLocations.add(new Vector3(146.3629150390625f, -29.717161655426025f, 12.364890575408936f));
		treeLocations.add(new Vector3(112.07414627075195f, -28.41923952102661f, 12.364890575408936f));
		treeLocations.add(new Vector3(143.65829467773438f, -34.27158832550049f, 12.364890575408936f));
		treeLocations.add(new Vector3(142.10972785949707f, -12.845017910003662f, 12.364891767501831f));
		treeLocations.add(new Vector3(138.88401985168457f, -15.602178573608398f, 12.364890575408936f));
		treeLocations.add(new Vector3(114.69551086425781f, -9.297512173652649f, 12.364890575408936f));
		treeLocations.add(new Vector3(142.98352241516113f, -13.113514184951782f, 12.364890575408936f));
		treeLocations.add(new Vector3(134.917631149292f, -11.154813766479492f, 12.36488938331604f));
		treeLocations.add(new Vector3(139.05961990356445f, -12.185187339782715f, 12.364890575408936f));
		treeLocations.add(new Vector3(115.89099884033203f, -13.558151721954346f, 12.364890575408936f));
		treeLocations.add(new Vector3(132.28489875793457f, -36.40339136123657f, 12.364890575408936f));
		treeLocations.add(new Vector3(100.14083862304688f, -54.3360710144043f, 12.364890575408936f));
		treeLocations.add(new Vector3(144.45428848266602f, -44.281086921691895f, 12.364890575408936f));
		treeLocations.add(new Vector3(91.45220756530762f, -45.00899314880371f, 12.364890575408936f));
		treeLocations.add(new Vector3(113.86784553527832f, -32.22830295562744f, 12.364890575408936f));
		treeLocations.add(new Vector3(101.4238452911377f, -49.41788196563721f, 12.364890575408936f));
		treeLocations.add(new Vector3(112.9537582397461f, -55.486578941345215f, 12.364890575408936f));
		treeLocations.add(new Vector3(144.25894737243652f, -43.06354999542236f, 12.36488938331604f));
		treeLocations.add(new Vector3(135.08411407470703f, -39.42936897277832f, 12.364891767501831f));
		treeLocations.add(new Vector3(100.87896347045898f, -47.90915489196777f, 12.364890575408936f));
		treeLocations.add(new Vector3(101.98726654052734f, -28.294734954833984f, 12.364890575408936f));
		treeLocations.add(new Vector3(125.89509010314941f, -33.868677616119385f, 12.364890575408936f));
		treeLocations.add(new Vector3(88.55133056640625f, -56.61054611206055f, 12.364890575408936f));
		treeLocations.add(new Vector3(146.1251926422119f, -49.37843322753906f, 12.364890575408936f));
		treeLocations.add(new Vector3(116.63471221923828f, -53.225646018981934f, 12.364891767501831f));
		treeLocations.add(new Vector3(142.64659881591797f, -40.42515277862549f, 12.364890575408936f));
		treeLocations.add(new Vector3(144.849853515625f, -46.09389305114746f, 12.364891767501831f));
		treeLocations.add(new Vector3(120.91614723205566f, -47.23036766052246f, 12.364891767501831f));
		treeLocations.add(new Vector3(126.61140441894531f, -45.04972457885742f, 12.364891767501831f));
		treeLocations.add(new Vector3(98.86474609375f, -34.446372985839844f, 12.364890575408936f));
		treeLocations.add(new Vector3(114.32769775390625f, -34.70085620880127f, 12.364890575408936f));
		treeLocations.add(new Vector3(94.92254257202148f, -45.80625534057617f, 12.364890575408936f));
		treeLocations.add(new Vector3(90.61230659484863f, -42.95848846435547f, 12.36488938331604f));
		treeLocations.add(new Vector3(143.90521049499512f, -50.09115695953369f, 12.364890575408936f));
		treeLocations.add(new Vector3(92.02239036560059f, -44.08012866973877f, 12.364890575408936f));
		treeLocations.add(new Vector3(-146.5030574798584f, 4.565021395683289f, 23.106443881988525f));
		treeLocations.add(new Vector3(146.5491771697998f, -3.361833095550537f, 12.36488938331604f));
		treeLocations.add(new Vector3(124.94298934936523f, -5.930938720703125f, 12.364890575408936f));
		treeLocations.add(new Vector3(119.70162391662598f, -8.024652600288391f, 12.364891767501831f));
		treeLocations.add(new Vector3(123.20496559143066f, -5.982003211975098f, 12.364891767501831f));
		treeLocations.add(new Vector3(120.34013748168945f, -3.231983780860901f, 12.364890575408936f));
		treeLocations.add(new Vector3(130.53645133972168f, -4.503418803215027f, 12.36488938331604f));
		treeLocations.add(new Vector3(145.90283393859863f, -1.7716234922409058f, 12.364890575408936f));
		treeLocations.add(new Vector3(141.79821014404297f, 1.1726778000593185f, 12.364890575408936f));
		treeLocations.add(new Vector3(140.33093452453613f, 0.38209471851587296f, 12.364890575408936f));
		treeLocations.add(new Vector3(133.6135482788086f, 1.213054284453392f, 12.364890575408936f));
		treeLocations.add(new Vector3(143.54270935058594f, -1.6178034245967865f, 12.364890575408936f));
		treeLocations.add(new Vector3(143.93604278564453f, -1.9812145829200745f, 12.364890575408936f));
		treeLocations.add(new Vector3(129.76881980895996f, -0.5505623668432236f, 12.364890575408936f));
		treeLocations.add(new Vector3(118.95008087158203f, -1.3253748416900635f, 12.36488938331604f));
		treeLocations.add(new Vector3(146.43372535705566f, 0.08962307125329971f, 12.364890575408936f));
		treeLocations.add(new Vector3(112.20061302185059f, -6.503767371177673f, 12.364890575408936f));
		treeLocations.add(new Vector3(147.2852325439453f, 54.72397327423096f, 17.914462089538574f));
		treeLocations.add(new Vector3(144.67172622680664f, 148.12128067016602f, 44.55612659454346f));
		treeLocations.add(new Vector3(-122.1619701385498f, -156.06261253356934f, 15.269315242767334f));
		treeLocations.add(new Vector3(-118.67358207702637f, -151.0790252685547f, 15.269314050674438f));
		treeLocations.add(new Vector3(-130.88812828063965f, -157.45574951171875f, 15.269315242767334f));
		treeLocations.add(new Vector3(-126.55075073242188f, -153.24715614318848f, 15.269314050674438f));
		treeLocations.add(new Vector3(-128.4510326385498f, -159.2247772216797f, 15.269314050674438f));
		treeLocations.add(new Vector3(-137.87240982055664f, -150.2419948577881f, 15.16014575958252f));
		treeLocations.add(new Vector3(-133.7899112701416f, -158.0590534210205f, 14.233558177947998f));
		treeLocations.add(new Vector3(-149.09847259521484f, -156.45203590393066f, 15.269314050674438f));
		treeLocations.add(new Vector3(-155.1323413848877f, -159.21287536621094f, 15.269314050674438f));
		treeLocations.add(new Vector3(-141.06907844543457f, -151.46540641784668f, 15.269314050674438f));
		treeLocations.add(new Vector3(-144.47223663330078f, -151.67880058288574f, 15.269314050674438f));
		treeLocations.add(new Vector3(-143.0075454711914f, -150.44912338256836f, 15.269314050674438f));
		treeLocations.add(new Vector3(-154.55355644226074f, -140.50494194030762f, 15.269315242767334f));
		treeLocations.add(new Vector3(-158.16777229309082f, -144.40190315246582f, 15.269314050674438f));
		treeLocations.add(new Vector3(-158.11838150024414f, -135.01864433288574f, 15.269314050674438f));
		treeLocations.add(new Vector3(-166.04137420654297f, -132.93670654296875f, 15.269314050674438f));
		treeLocations.add(new Vector3(-165.49158096313477f, -127.45829582214355f, 15.269314050674438f));
		treeLocations.add(new Vector3(-153.03839683532715f, -123.94185066223145f, 15.269314050674438f));
		treeLocations.add(new Vector3(-161.9552230834961f, -121.89990997314453f, 15.269314050674438f));
		treeLocations.add(new Vector3(-160.46794891357422f, -113.02751541137695f, 15.269314050674438f));
		treeLocations.add(new Vector3(-155.97097396850586f, -117.69329071044922f, 15.269314050674438f));
		treeLocations.add(new Vector3(-155.53186416625977f, -118.99392127990723f, 15.269314050674438f));
		treeLocations.add(new Vector3(-166.88030242919922f, -105.2740478515625f, 15.269314050674438f));
		treeLocations.add(new Vector3(-157.80302047729492f, -106.38866424560547f, 15.269314050674438f));
		treeLocations.add(new Vector3(-152.88515090942383f, -102.89220809936523f, 15.269314050674438f));
		treeLocations.add(new Vector3(-165.48728942871094f, -100.1955509185791f, 15.269312858581543f));
		treeLocations.add(new Vector3(-157.31974601745605f, -100.54388999938965f, 15.269314050674438f));
		treeLocations.add(new Vector3(-165.00751495361328f, -94.75356101989746f, 15.269314050674438f));
		treeLocations.add(new Vector3(-154.518404006958f, -92.84790992736816f, 15.269314050674438f));
		treeLocations.add(new Vector3(-154.23847198486328f, -72.7625036239624f, 15.269314050674438f));
		treeLocations.add(new Vector3(-154.48335647583008f, -71.95297718048096f, 15.269314050674438f));
		treeLocations.add(new Vector3(-156.72508239746094f, -63.150320053100586f, 15.269314050674438f));
		treeLocations.add(new Vector3(-161.51458740234375f, -61.726884841918945f, 15.269314050674438f));
		treeLocations.add(new Vector3(-154.68716621398926f, -57.33073711395264f, 15.269314050674438f));
		treeLocations.add(new Vector3(-153.83322715759277f, -5.794500112533569f, 22.86252498626709f));
		treeLocations.add(new Vector3(154.84521865844727f, -133.219575881958f, 12.364890575408936f));
		treeLocations.add(new Vector3(-154.17716026306152f, 46.36087417602539f, 53.542985916137695f));
		treeLocations.add(new Vector3(156.9452953338623f, -98.82434844970703f, 12.364890575408936f));
		treeLocations.add(new Vector3(160.1111602783203f, -83.23736190795898f, 12.364890575408936f));
		treeLocations.add(new Vector3(152.12928771972656f, -79.53119277954102f, 12.364891767501831f));
		treeLocations.add(new Vector3(155.62010765075684f, -69.27454471588135f, 12.364891767501831f));
		treeLocations.add(new Vector3(149.72478866577148f, -56.43575668334961f, 12.364891767501831f));
		treeLocations.add(new Vector3(151.5114402770996f, -51.75048351287842f, 12.364890575408936f));
		treeLocations.add(new Vector3(153.9475917816162f, -37.54741191864014f, 12.364890575408936f));
		treeLocations.add(new Vector3(157.28264808654785f, -29.48070526123047f, 12.364890575408936f));
		treeLocations.add(new Vector3(158.56756210327148f, -43.31575870513916f, 12.364890575408936f));
		treeLocations.add(new Vector3(151.31131172180176f, -29.656758308410645f, 12.36488938331604f));
		treeLocations.add(new Vector3(154.15401458740234f, -41.88368320465088f, 12.364890575408936f));
		treeLocations.add(new Vector3(151.73352241516113f, -13.426629304885864f, 12.364891767501831f));
		treeLocations.add(new Vector3(156.41891479492188f, -18.795613050460815f, 12.364890575408936f));
		treeLocations.add(new Vector3(154.8130702972412f, -12.811837196350098f, 12.36488938331604f));
		treeLocations.add(new Vector3(150.3049087524414f, -9.095057249069214f, 12.364891767501831f));
		treeLocations.add(new Vector3(151.5407371520996f, -20.103635787963867f, 12.364890575408936f));
		treeLocations.add(new Vector3(-83.47923278808594f, 156.14572525024414f, 30.820624828338623f));
		treeLocations.add(new Vector3(73.28799247741699f, 152.8041172027588f, 69.32066440582275f));
		treeLocations.add(new Vector3(11.300828456878662f, 153.98927688598633f, 47.635135650634766f));
		treeLocations.add(new Vector3(-150.8051300048828f, -143.80435943603516f, 15.269314050674438f));
		treeLocations.add(new Vector3(92.28320121765137f, -132.03837394714355f, 12.364890575408936f));
		treeLocations.add(new Vector3(-128.30190658569336f, -144.23284530639648f, 15.269314050674438f));
		treeLocations.add(new Vector3(-134.1902732849121f, -147.52037048339844f, 15.269314050674438f));
		treeLocations.add(new Vector3(-128.21853637695312f, -144.95654106140137f, 15.269314050674438f));
		treeLocations.add(new Vector3(-132.51035690307617f, -145.1785373687744f, 15.269314050674438f));
		treeLocations.add(new Vector3(83.09908866882324f, -117.17915534973145f, 10.519790649414062f));
		treeLocations.add(new Vector3(-142.3976707458496f, -71.76862716674805f, 15.269314050674438f));
		treeLocations.add(new Vector3(-140.80894470214844f, -72.06766605377197f, 15.269315242767334f));
		treeLocations.add(new Vector3(-137.8901767730713f, -72.73729801177979f, 15.269312858581543f));
		treeLocations.add(new Vector3(-151.79734230041504f, -72.96017646789551f, 15.269314050674438f));
		treeLocations.add(new Vector3(-146.58536911010742f, -73.59737396240234f, 15.269314050674438f));
		treeLocations.add(new Vector3(-117.3405933380127f, -68.96307468414307f, 15.269315242767334f));
		treeLocations.add(new Vector3(-129.41914558410645f, -70.86593627929688f, 15.269314050674438f));
		treeLocations.add(new Vector3(84.3189811706543f, -103.6772346496582f, 11.667704582214355f));
		treeLocations.add(new Vector3(144.7761058807373f, -76.23971939086914f, 12.364890575408936f));
		treeLocations.add(new Vector3(139.06976699829102f, -82.28790283203125f, 12.364890575408936f));
		treeLocations.add(new Vector3(144.0704345703125f, -86.17180824279785f, 12.364890575408936f));
		treeLocations.add(new Vector3(115.85299491882324f, -76.32879257202148f, 12.364890575408936f));
		treeLocations.add(new Vector3(109.41825866699219f, -77.9404067993164f, 12.36488938331604f));
		treeLocations.add(new Vector3(144.9137783050537f, -83.76136779785156f, 12.364890575408936f));
		treeLocations.add(new Vector3(139.9425506591797f, -79.28540706634521f, 12.364891767501831f));
		treeLocations.add(new Vector3(136.2852668762207f, -75.80146789550781f, 12.364890575408936f));
		treeLocations.add(new Vector3(147.15394020080566f, -76.89769744873047f, 12.364890575408936f));
		treeLocations.add(new Vector3(147.170991897583f, -79.24134731292725f, 12.364890575408936f));
		treeLocations.add(new Vector3(131.2366485595703f, -77.68069744110107f, 12.364890575408936f));
		treeLocations.add(new Vector3(109.09996032714844f, -78.09111595153809f, 12.364890575408936f));
		treeLocations.add(new Vector3(97.0174789428711f, -80.21583557128906f, 12.364890575408936f));
		treeLocations.add(new Vector3(126.78191184997559f, -79.72407817840576f, 12.36488938331604f));
		treeLocations.add(new Vector3(133.7049388885498f, -70.33295154571533f, 12.36488938331604f));
		treeLocations.add(new Vector3(88.88700485229492f, -76.59874439239502f, 12.364890575408936f));
		treeLocations.add(new Vector3(103.30923080444336f, -66.31584167480469f, 12.364890575408936f));
		treeLocations.add(new Vector3(108.17340850830078f, -65.95009326934814f, 12.364890575408936f));
		treeLocations.add(new Vector3(124.46407318115234f, -74.1917896270752f, 12.364890575408936f));
		treeLocations.add(new Vector3(116.8963623046875f, -73.06303024291992f, 12.364890575408936f));
		treeLocations.add(new Vector3(113.1766414642334f, -75.56164264678955f, 12.364891767501831f));
		treeLocations.add(new Vector3(123.83548736572266f, -69.67836380004883f, 12.364890575408936f));
		treeLocations.add(new Vector3(106.95080757141113f, -64.90642070770264f, 12.36488938331604f));
		treeLocations.add(new Vector3(-138.14048767089844f, -29.950172901153564f, 15.269314050674438f));
		treeLocations.add(new Vector3(-128.8567066192627f, -33.38571310043335f, 15.269314050674438f));
		treeLocations.add(new Vector3(-131.08369827270508f, -38.426268100738525f, 15.269314050674438f));
		treeLocations.add(new Vector3(-133.89739990234375f, -34.57363843917847f, 15.269314050674438f));
		treeLocations.add(new Vector3(-136.5403175354004f, -32.97889709472656f, 15.269314050674438f));
		treeLocations.add(new Vector3(-133.34240913391113f, -34.09671068191528f, 15.269314050674438f));
		treeLocations.add(new Vector3(-122.2885799407959f, -39.11813020706177f, 15.269314050674438f));
		treeLocations.add(new Vector3(-140.6241798400879f, -35.77083349227905f, 15.269314050674438f));
		treeLocations.add(new Vector3(-129.83588218688965f, -43.17809581756592f, 15.269314050674438f));
		treeLocations.add(new Vector3(85.04284858703613f, -71.83285236358643f, 10.689828395843506f));
		treeLocations.add(new Vector3(84.15301322937012f, -70.71652889251709f, 10.042479038238525f));
		treeLocations.add(new Vector3(-114.06822204589844f, -40.495591163635254f, 15.269314050674438f));
		treeLocations.add(new Vector3(-116.0498046875f, -36.0805606842041f, 15.269314050674438f));
		treeLocations.add(new Vector3(-115.59347152709961f, -35.46238899230957f, 15.269314050674438f));
		treeLocations.add(new Vector3(-122.04715728759766f, -33.93861770629883f, 15.269314050674438f));
		treeLocations.add(new Vector3(-119.31131362915039f, -35.58767795562744f, 15.269314050674438f));
		treeLocations.add(new Vector3(-143.88689041137695f, -21.38817310333252f, 16.556302309036255f));
		treeLocations.add(new Vector3(-127.27066040039062f, -20.436718463897705f, 15.269312858581543f));
		treeLocations.add(new Vector3(82.77222633361816f, -49.6420955657959f, 9.470612406730652f));
		treeLocations.add(new Vector3(96.93744659423828f, -19.54360604286194f, 12.36488938331604f));
		treeLocations.add(new Vector3(128.1160545349121f, -21.27852201461792f, 12.364890575408936f));
		treeLocations.add(new Vector3(129.70779418945312f, -21.83940887451172f, 12.36488938331604f));
		treeLocations.add(new Vector3(135.39512634277344f, -23.79856824874878f, 12.364890575408936f));
		treeLocations.add(new Vector3(114.98306274414062f, -22.69136667251587f, 12.364890575408936f));
		treeLocations.add(new Vector3(118.70306968688965f, -17.38584041595459f, 12.364890575408936f));
		treeLocations.add(new Vector3(130.32066345214844f, -23.895134925842285f, 12.364890575408936f));
		treeLocations.add(new Vector3(140.53434371948242f, -22.93321132659912f, 12.364891767501831f));
		treeLocations.add(new Vector3(141.93950653076172f, -20.169973373413086f, 12.364890575408936f));
		treeLocations.add(new Vector3(85.95758438110352f, -46.73859119415283f, 12.364890575408936f));
		treeLocations.add(new Vector3(87.00549125671387f, -43.359551429748535f, 12.36488938331604f));
		treeLocations.add(new Vector3(86.54338836669922f, -59.109296798706055f, 12.364890575408936f));
		treeLocations.add(new Vector3(86.57634735107422f, -55.72526931762695f, 12.364890575408936f));
		treeLocations.add(new Vector3(86.17803573608398f, -28.21272611618042f, 12.364890575408936f));
		treeLocations.add(new Vector3(-142.53029823303223f, 16.697871685028076f, 23.106441497802734f));
		treeLocations.add(new Vector3(-142.93705940246582f, 16.310672760009766f, 23.106441497802734f));
		treeLocations.add(new Vector3(-141.98139190673828f, 3.545246124267578f, 23.106443881988525f));
		treeLocations.add(new Vector3(-142.22997665405273f, 1.4041107892990112f, 23.106443881988525f));
		treeLocations.add(new Vector3(-141.72005653381348f, 9.414280652999878f, 23.106443881988525f));
		treeLocations.add(new Vector3(-125.13480186462402f, -7.886595726013184f, 22.43447780609131f));
		treeLocations.add(new Vector3(-123.63039970397949f, -3.0561381578445435f, 22.92696714401245f));
		treeLocations.add(new Vector3(-137.71602630615234f, 10.05914568901062f, 23.106443881988525f));
		treeLocations.add(new Vector3(124.26395416259766f, 5.598562955856323f, 12.364890575408936f));
		treeLocations.add(new Vector3(115.6777286529541f, 1.7924021184444427f, 12.364890575408936f));
		treeLocations.add(new Vector3(125.02364158630371f, 9.005324840545654f, 12.364891767501831f));
		treeLocations.add(new Vector3(119.86289978027344f, 11.883223056793213f, 12.364890575408936f));
		treeLocations.add(new Vector3(96.2909984588623f, -10.537364482879639f, 12.194617986679077f));
		treeLocations.add(new Vector3(111.28866195678711f, 4.527069628238678f, 12.36488938331604f));
		treeLocations.add(new Vector3(116.019287109375f, 9.960428476333618f, 12.364890575408936f));
		treeLocations.add(new Vector3(111.43582344055176f, 6.598893404006958f, 12.364891767501831f));
		treeLocations.add(new Vector3(-136.43580436706543f, 13.80968689918518f, 23.106441497802734f));
		treeLocations.add(new Vector3(-125.37580490112305f, 2.5779378414154053f, 23.106443881988525f));
		treeLocations.add(new Vector3(-130.22302627563477f, 8.6556875705719f, 23.106441497802734f));
		treeLocations.add(new Vector3(-124.49470520019531f, 5.495100021362305f, 23.106443881988525f));
		treeLocations.add(new Vector3(115.6839370727539f, 12.669569253921509f, 12.364890575408936f));
		treeLocations.add(new Vector3(-109.92116928100586f, -1.6923516988754272f, 20.705084800720215f));
		treeLocations.add(new Vector3(-106.54976844787598f, 13.361507654190063f, 23.106441497802734f));
		treeLocations.add(new Vector3(-125.64295768737793f, 7.688775062561035f, 23.106443881988525f));
		treeLocations.add(new Vector3(-122.39365577697754f, 7.760891318321228f, 23.106446266174316f));
		treeLocations.add(new Vector3(-112.19704627990723f, 1.168757677078247f, 22.788336277008057f));
		treeLocations.add(new Vector3(-125.3188705444336f, 29.602198600769043f, 23.106441497802734f));
		treeLocations.add(new Vector3(-123.42514991760254f, 20.318758487701416f, 23.106441497802734f));
		treeLocations.add(new Vector3(-118.86388778686523f, 18.426204919815063f, 23.106443881988525f));
		treeLocations.add(new Vector3(-116.83226585388184f, 15.598435401916504f, 23.106441497802734f));
		treeLocations.add(new Vector3(-120.96467971801758f, 10.529276132583618f, 23.106441497802734f));
		treeLocations.add(new Vector3(-98.51947784423828f, 13.396809101104736f, 22.77071237564087f));
		treeLocations.add(new Vector3(-98.46853256225586f, 17.31127142906189f, 23.106441497802734f));
		treeLocations.add(new Vector3(8.375862836837769f, 31.375598907470703f, 3.296351730823517f));
		treeLocations.add(new Vector3(-111.13079071044922f, 43.273258209228516f, 25.093955993652344f));
		treeLocations.add(new Vector3(-106.15228652954102f, 19.831275939941406f, 23.106441497802734f));
		treeLocations.add(new Vector3(-104.48686599731445f, 28.858299255371094f, 23.106441497802734f));
		treeLocations.add(new Vector3(-102.87917137145996f, 18.400803804397583f, 23.106441497802734f));
		treeLocations.add(new Vector3(-102.88020133972168f, 22.758779525756836f, 23.106443881988525f));
		treeLocations.add(new Vector3(4.207274317741394f, 30.4978346824646f, 2.8112995624542236f));
		treeLocations.add(new Vector3(-107.23714828491211f, 43.96312713623047f, 23.106443881988525f));
		treeLocations.add(new Vector3(-107.23898887634277f, 42.89004325866699f, 23.106443881988525f));
		treeLocations.add(new Vector3(-105.85049629211426f, 49.57308769226074f, 25.827503204345703f));
		treeLocations.add(new Vector3(-90.69664001464844f, 42.91548252105713f, 23.106441497802734f));
		treeLocations.add(new Vector3(-15.661619901657104f, 30.36738395690918f, 4.119799435138702f));
		treeLocations.add(new Vector3(23.305726051330566f, 35.22562265396118f, 5.921305418014526f));
		treeLocations.add(new Vector3(-85.3123950958252f, 48.56708526611328f, 23.106443881988525f));
		treeLocations.add(new Vector3(35.1860237121582f, 38.76924991607666f, 8.047856092453003f));
		treeLocations.add(new Vector3(-24.058854579925537f, 32.20564842224121f, 7.16178297996521f));
		treeLocations.add(new Vector3(39.68476057052612f, 39.81720447540283f, 8.233169913291931f));
		treeLocations.add(new Vector3(38.22965383529663f, 48.164000511169434f, 14.130102396011353f));
		treeLocations.add(new Vector3(116.50712013244629f, 62.26615905761719f, 15.877193212509155f));
		treeLocations.add(new Vector3(-83.30860137939453f, 78.62037181854248f, 25.440008640289307f));
		treeLocations.add(new Vector3(-36.88759803771973f, 39.81821537017822f, 16.777008771896362f));
		treeLocations.add(new Vector3(127.7415943145752f, 59.020395278930664f, 18.444770574569702f));
		treeLocations.add(new Vector3(30.97609519958496f, 49.776763916015625f, 17.66106128692627f));
		treeLocations.add(new Vector3(28.820583820343018f, 51.65712833404541f, 20.418243408203125f));
		treeLocations.add(new Vector3(-92.80128479003906f, 112.36440658569336f, 34.733595848083496f));
		treeLocations.add(new Vector3(-92.06514358520508f, 115.82348823547363f, 35.36302328109741f));
		treeLocations.add(new Vector3(-110.69141387939453f, 122.53046989440918f, 56.13844394683838f));
		treeLocations.add(new Vector3(137.49494552612305f, 70.14692306518555f, 29.36373233795166f));
		treeLocations.add(new Vector3(44.84792709350586f, 52.1592378616333f, 13.505778312683105f));
		treeLocations.add(new Vector3(29.7670316696167f, 55.171170234680176f, 22.50187873840332f));
		treeLocations.add(new Vector3(69.92814540863037f, 70.34535884857178f, 9.503799676895142f));
		treeLocations.add(new Vector3(78.40913772583008f, 70.45094966888428f, 10.50624132156372f));
		treeLocations.add(new Vector3(100.61760902404785f, 79.77739334106445f, 18.85865092277527f));
		treeLocations.add(new Vector3(120.2835750579834f, 80.66481590270996f, 31.586625576019287f));
		treeLocations.add(new Vector3(33.385045528411865f, 62.81205654144287f, 23.872339725494385f));
		treeLocations.add(new Vector3(71.22486114501953f, 78.50456237792969f, 14.69279170036316f));
		treeLocations.add(new Vector3(93.5478401184082f, 84.40260887145996f, 19.21115279197693f));
		treeLocations.add(new Vector3(51.895413398742676f, 69.20557498931885f, 12.127304077148438f));
		treeLocations.add(new Vector3(79.28991794586182f, 88.85946273803711f, 20.187172889709473f));
		treeLocations.add(new Vector3(-47.583656311035156f, 63.164873123168945f, 22.807252407073975f));
		treeLocations.add(new Vector3(-41.45383358001709f, 62.53499507904053f, 25.15523910522461f));
		treeLocations.add(new Vector3(50.04824161529541f, 86.80651664733887f, 12.87721872329712f));
		treeLocations.add(new Vector3(-49.16046619415283f, 77.67490863800049f, 21.141035556793213f));
		treeLocations.add(new Vector3(-58.296241760253906f, 128.9216136932373f, 26.109132766723633f));
		treeLocations.add(new Vector3(48.99434566497803f, 115.60525894165039f, 40.66720485687256f));
		treeLocations.add(new Vector3(-37.447266578674316f, 152.57439613342285f, 32.953550815582275f));
		treeLocations.add(new Vector3(34.60968494415283f, 114.75642204284668f, 37.561912536621094f));
		treeLocations.add(new Vector3(32.54527807235718f, 119.47021484375f, 39.94044303894043f));
		treeLocations.add(new Vector3(30.374319553375244f, 117.35430717468262f, 38.1156063079834f));
		treeLocations.add(new Vector3(49.513163566589355f, 126.30297660827637f, 50.9796667098999f));
		treeLocations.add(new Vector3(26.070022583007812f, 114.9504280090332f, 32.04582929611206f));
		treeLocations.add(new Vector3(-29.83649253845215f, 123.06724548339844f, 29.179599285125732f));
		treeLocations.add(new Vector3(22.886710166931152f, 116.43583297729492f, 31.114697456359863f));
		treeLocations.add(new Vector3(-21.767702102661133f, 145.8497428894043f, 30.648691654205322f));
		treeLocations.add(new Vector3(2.0396944880485535f, 135.9665584564209f, 36.01658582687378f));
		treeLocations.add(new Vector3(10.4471755027771f, 132.5027084350586f, 36.855628490448f));
		treeLocations.add(new Vector3(10.815199613571167f, 130.14324188232422f, 35.28654098510742f));
		treeLocations.add(new Vector3(-6.486768126487732f, 140.1264190673828f, 35.39564609527588f));
		treeLocations.add(new Vector3(-128.1565761566162f, -161.56431198120117f, 14.98856782913208f));
		treeLocations.add(new Vector3(-143.93482208251953f, -164.6356964111328f, 15.181998014450073f));
		treeLocations.add(new Vector3(-162.6944923400879f, -158.82625579833984f, 15.269314050674438f));
		treeLocations.add(new Vector3(-154.56007957458496f, -160.68504333496094f, 15.269314050674438f));
		treeLocations.add(new Vector3(-170.24038314819336f, -155.66736221313477f, 15.269314050674438f));
		treeLocations.add(new Vector3(-155.91819763183594f, -78.2902479171753f, 15.269314050674438f));
		treeLocations.add(new Vector3(-155.16754150390625f, -79.27059173583984f, 15.269314050674438f));
		treeLocations.add(new Vector3(-162.53570556640625f, -79.89235877990723f, 15.269312858581543f));
		treeLocations.add(new Vector3(-164.4399642944336f, -60.20786762237549f, 17.01645851135254f));
		treeLocations.add(new Vector3(-156.88848495483398f, -44.216299057006836f, 15.39843201637268f));
		treeLocations.add(new Vector3(-159.52698707580566f, -10.73992133140564f, 22.101035118103027f));
		treeLocations.add(new Vector3(140.67350387573242f, -163.93104553222656f, 21.752538681030273f));
		treeLocations.add(new Vector3(-188.40978622436523f, 50.9596061706543f, 70.46897888183594f));
		treeLocations.add(new Vector3(154.29783821105957f, -124.99094009399414f, 12.364890575408936f));
		treeLocations.add(new Vector3(155.71124076843262f, -124.64539527893066f, 12.364890575408936f));
		treeLocations.add(new Vector3(155.00850677490234f, -123.6178970336914f, 12.364890575408936f));
		treeLocations.add(new Vector3(151.59947395324707f, -99.99905586242676f, 12.364890575408936f));
		treeLocations.add(new Vector3(152.3346710205078f, -45.85203170776367f, 12.364890575408936f));
		treeLocations.add(new Vector3(159.01814460754395f, -44.98199462890625f, 12.364890575408936f));
		treeLocations.add(new Vector3(161.4310646057129f, 46.64797782897949f, 9.653470516204834f));
		treeLocations.add(new Vector3(162.90386199951172f, 130.04632949829102f, 35.086984634399414f));
	}

}
