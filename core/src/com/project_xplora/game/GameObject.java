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
	public final btRigidBody body;
	public GameObject.MotionState motionState;

	public GameObject(Model model, btCollisionShape shape, float mass, Vector3 loc) {
		super(model);
		this.transform.translate(loc);
		this.transform.rotate(1, 0, 0, 90);

		Vector3 localInertia = new Vector3();

		if (mass > 0f)
			shape.calculateLocalInertia(mass, localInertia);
		else
			localInertia.set(0, 0, 0);
		body = new btRigidBody(new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia));
	}

	public GameObject(Model model, btCollisionShape shape, float mass) {
		this(model, shape, mass, new Vector3(0, 0, 0));
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

	static class MotionState extends btMotionState {
		private final Matrix4 transform;

		public MotionState(final Matrix4 transform) {
			this.transform = transform;
		}

		/**
		 * For dynamic and static bodies this method is called by bullet once to
		 * get the initial state of the body. For kinematic bodies this method
		 * is called on every update, unless the body is deactivated.
		 */
		@Override
		public void getWorldTransform(final Matrix4 worldTrans) {
			worldTrans.set(transform);
		}

		/**
		 * For dynamic bodies this method is called by bullet every update to
		 * inform about the new position and rotation.
		 */
		@Override
		public void setWorldTransform(final Matrix4 worldTrans) {
			transform.set(worldTrans);
		}
	}
}
