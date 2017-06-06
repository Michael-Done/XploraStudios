/**
 * 
 */
package com.project_xplora.collision_util;

import com.badlogic.gdx.math.Vector2;

/**
 * This class is the abstract class used for checking collisions between the
 * player and vertical objects.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * protected float <b> margin </b> - the distance away from the object the
 * player will stop
 * <p>
 * Time taken to complete: 15 mins
 * 
 * @author Michael Done, Cyrus Gandevia
 * @version 5.0
 *
 */
public abstract class CollisionShape {
	/** the distance away from the object the player will stop */
	protected float margin;

	/**
	 * The isInside method. An abstract method that checks if a given point is
	 * inside of the object. used for collision detection
	 * 
	 * @param point
	 */
	public abstract boolean isInside(Vector2 point);

	/**
	 * Overload of the isInside method. takes 2 floats instead of a
	 * vector @param x, y
	 */
	public boolean isInside(float x, float y) {
		return isInside(new Vector2(x, y));
	}

	/**
	 * The abstract method newPointCalculation. This method determines the
	 * location of the new point after a collision has occurred given the
	 * current point and the last point
	 * 
	 * @param currentPoint,
	 *            lastPoint
	 */
	public abstract Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint);
}
