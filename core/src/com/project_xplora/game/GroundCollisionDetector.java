/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

/**
 * @author Michael
 *
 */
public class GroundCollisionDetector {
	private static final Vector3 rayFrom = new Vector3();
	private static final Vector3 rayTo = new Vector3();
	private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

	/**
	 * 
	 */
	public GroundCollisionDetector() {
		// TODO Auto-generated constructor stub
	}
	public static Vector3 rayTest(btCollisionWorld collisionWorld, Vector3 start, Vector3 end) {
	    rayFrom.set(start);
	    // 50 meters max from the origin
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
