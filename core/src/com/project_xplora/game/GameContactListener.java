package com.project_xplora.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;

public class GameContactListener extends ContactListener {
	public Array<btCollisionObject> objects;
	public btCollisionObject player;
	public GameContactListener(Array<btCollisionObject> objects, btCollisionObject player) {
		this.objects = objects;
		this.player = player;
	}

	public GameContactListener(long cPtr, boolean cMemoryOwn) {
		super(cPtr, cMemoryOwn);
		// TODO Auto-generated constructor stub
	}
	
	public GameContactListener(String className, long cPtr, boolean cMemoryOwn) {
		super(className, cPtr, cMemoryOwn);
		// TODO Auto-generated constructor stub
	}

}
