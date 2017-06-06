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

/**
 * The game Object class. this class is used as a class for all the 3-d objects
 * in the game. It provides useful tools for the other classes.
 * <p>
 * Time taken to complete: 1 hours
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 */
public class GameObject extends ModelInstance implements Disposable {
	/**
	 * class constructor.
	 * 
	 * @param model
	 * @param loc
	 */
	public GameObject(Model model, Vector3 loc) {
		super(model);
		this.transform.translate(loc);
		this.transform.rotate(1, 0, 0, 90);
	}

	/**
	 * class constructor.
	 * 
	 * @param model
	 * @param loc
	 * @param rot
	 */
	public GameObject(Model model, Vector3 loc, Vector3 rot) {
		super(model);
		this.transform.translate(loc);
		this.transform.rotate(1, 0, 0, rot.x);
		this.transform.rotate(0, 1, 0, rot.y);
		this.transform.rotate(0, 0, 1, rot.z);
		this.transform.rotate(1, 0, 0, 90);
	}

	/**
	 * class constructor
	 * 
	 * @param model
	 * @param loc
	 * @param axis
	 * @param angle
	 */
	public GameObject(Model model, Vector3 loc, Vector3 axis, float angle) {
		super(model);
		this.transform.translate(loc);
		this.transform.rotate(axis, angle);
	}

	/**
	 * class constructor
	 * 
	 * @param model
	 */
	public GameObject(Model model) {
		super(model);
		this.transform.rotate(1, 0, 0, 90);
	}

	/**
	 * rotates the model around the given axis
	 * 
	 * @param axis
	 * @param rot
	 */
	public void rotate(Vector3 axis, float rot) {
		this.transform.rotate(1, 0, 0, -90);
		this.transform.rotate(axis, rot);
		this.transform.rotate(1, 0, 0, 90);
	}

	@Override
	public void dispose() {

	}
}
