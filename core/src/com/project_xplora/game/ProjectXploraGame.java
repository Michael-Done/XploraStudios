/**
 *  XploraStudios
 *  Ms. Krasteva
 *  ProjectXploraGame.java
 *  @author Michael Done, CyrusGandevia
 *  @Version 1.0 | 12/05/2017
 */
package com.project_xplora.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;

public class ProjectXploraGame implements ApplicationListener {
	ModelBatch modelBatch;
	GameObjectController menu;
	
	

	public int screenWidth;
	public int screenHeight;

	Array<ModelInstance> instances = new Array<ModelInstance>();
    
	@Override
	public void create() {
		Bullet.init();
		menu = new GameObjectController();
        
		// Get screen dimensions
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		System.out.println("(create - width = " + screenWidth + " height = " + screenHeight);
		// Create ModelBatch that will render all models using a camera
		modelBatch = new ModelBatch();
		
		menu.initalize();
	}

	@Override
	public void dispose() {
//		groundObject.dispose();
//        groundShape.dispose();
//
//        ballObject.dispose();
//        ballShape.dispose();
//
//        dispatcher.dispose();
//        collisionConfig.dispose();

		// Release all resources
		modelBatch.dispose();
		instances.clear();
		menu.disposeAll();
	}
	boolean collision;


//    boolean checkCollision() {
//    	CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
//        CollisionObjectWrapper co1 = new CollisionObjectWrapper(groundObject);
//
//        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
//        ci.setDispatcher1(dispatcher);
//        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false); 
//
//        btDispatcherInfo info = new btDispatcherInfo();
//        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);
//
//        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);
//
//        boolean r = result.getPersistentManifold().getNumContacts() > 0;
//
//        result.dispose();
//        info.dispose();
//        algorithm.dispose();
//        ci.dispose();
//        co1.dispose();
//        co0.dispose();
//
//        return r;
//    }
	@Override
	public void render() {
//		final float delta = 0.1f;
//
//        if (!collision) {
//            instances.get(1).transform.translate(0f, 0f, -delta);
//            ballObject.setWorldTransform(instances.get(1).transform);
//            collision = checkCollision();
//        }
//        
		// Respond to user events and update the camera
		//cameraController.update();
		//instances.get(3).transform.translate(0f, 0.1f, 0f);
		
//		instances.get(3).transform.rotate(0, 1f, 0f, -1);
		// Clear the viewport
		Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		menu.update();
		// Draw all model instances using the camera
		modelBatch.begin(menu.getCamera());
		modelBatch.render(menu.objects, menu.getEnvironment());
		modelBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// Update screen dimensions
		screenWidth = width;
		screenHeight = height;

		// Update viewport size and refresh camera matrices
		menu.cameraResize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
