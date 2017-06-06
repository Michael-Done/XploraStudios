/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

/**
 * This is a helper class to detect collisions between the player and the
 * ground. It used a collision world and ray casting in order to detect where
 * the user's z value should be in the world.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * private static final Vector3 <b> rayFrom </b> - the point at which the ray
 * starts
 * <p>
 * private static final Vector3 <b> rayTo </b> - the point at which the ray ends
 * <p>
 * private static final {@link ClosestRayResultCallback} <b> callBack </b> - the
 * callback class used to detect the ray collision
 * <p>
 * Time taken to complete: 30 mins
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 *
 */

public class GroundCollisionDetector {
	/** the point at which the ray starts */
	private static final Vector3 rayFrom = new Vector3();
	/** the point at which the ray ends */
	private static final Vector3 rayTo = new Vector3();
	/** the callback class used to detect the ray collision */
	private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

	/**
	 * Default constructor. This class is never instantiated.
	 */
	public GroundCollisionDetector() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * The rayTest method. detects the location of the nearest collision with
	 * any collision object in the world. Used an if statement to determine if
	 * the ray hit an object
	 * 
	 * @param collisionWorld
	 * @param start
	 * @param end
	 * @return the collision location
	 */
	public static Vector3 rayTest(btCollisionWorld collisionWorld, Vector3 start, Vector3 end) {
		rayFrom.set(start);
		rayTo.set(end);

		// we reuse the ClosestRayResultCallback, thus we need to reset its
		// values
		callback.setCollisionObject(null);
		callback.setClosestHitFraction(0.1f);
		callback.setRayFromWorld(rayFrom);
		callback.setRayToWorld(rayTo);

		collisionWorld.rayTest(rayFrom, rayTo, callback);

		if (callback.hasHit()) {
			Vector3 out = new Vector3();
			callback.getHitPointWorld(out);
			return out;
		}

		return null;
	}
}
