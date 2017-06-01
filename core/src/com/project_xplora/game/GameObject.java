package com.project_xplora.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable {
	public final btCollisionObject body;
	
	public GameObject(Model model, btCollisionShape shape, Vector3 loc) {
		super(model);
		this.transform.translate(loc);
		body = new btCollisionObject();
		body.setCollisionShape(shape);
		body.setWorldTransform(this.transform);
		this.transform.rotate(1, 0, 0, 90);
	}
	
	public GameObject(Model model, btCollisionShape shape) {
		this(model, shape, new Vector3(0, 0, 0));
	}
	public GameObject(Model model, Vector3 loc) {
		super(model);
		this.transform.translate(loc);
		this.transform.rotate(1, 0, 0, 90);
		body = null;
	}

	public GameObject(Model model) {
		super(model);
		this.transform.rotate(1, 0, 0, 90);
		body = null;
	}

	@Override
	public void dispose() {
		body.dispose();
	}
}
