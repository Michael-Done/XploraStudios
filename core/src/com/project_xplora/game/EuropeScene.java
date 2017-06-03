/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * @author Michael
 *
 */
public class EuropeScene extends GameObjectController {
	Array<Vector3> grassLocations = new Array<Vector3>();
	Array<Vector3> activeGrass = new Array<Vector3>();
	Array<Vector3> sleepingGrass = new Array<Vector3>();

	/**
	 * @param settings
	 */
	public EuropeScene(Settings settings) {
		super(settings);
		initalize();
		// TODO Auto-generated constructor stub
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
		assets.finishLoading();
	}

	@Override
	public void update() {
		super.update();
	}

	private void initalizeGrassLocations() {
		for (int i = 0; i < 100000; i++) {
			grassLocations.add(new Vector3((float) ((Math.random() * 305f) + 152.5f),
					(float) ((Math.random() * 305f) + 152.5f), 0f));
		}
		for (Vector3 i : grassLocations) {
			if (i.dst(new Vector3(0, 0, 0)) < 50) {
				activeGrass.add(i);
			} else {
				sleepingGrass.add(i);
			}
		}
	}

	@Override
	public void loadModelInstances() {
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
	}
}
