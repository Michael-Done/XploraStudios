/**
 * 
 */
package com.project_xplora.collision_util;

import com.badlogic.gdx.math.Vector2;

/**
 * This class is the circle implementation of the {@link CollisionShape} class.
 * It detects collisions with a circular shape.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * private {@link Vector2} <b> center </b> - the center
 * <p>
 * private float <b> radius </b> - the radius
 * <p>
 * Time taken to complete: 30 mins
 * 
 * @author Michael Done, Cyrus Gandevia
 * @version 5.0
 *
 */
public class CollisionCircle extends CollisionShape {
	/** the center */
	private Vector2 center;
	/** the radius */
	private float radius;

	/**
	 * Class constructor. Constructs a new CollisionCircle given a center and a
	 * radius
	 * 
	 * @param center,
	 *            radius
	 */
	public CollisionCircle(Vector2 center, float radius) {
		this.center = center;
		this.radius = radius;
		margin = 0.15f;
	}

	/**
	 * The circle implementation of the isInside method.
	 * 
	 * @param point
	 * @return true if the point is inside the object
	 */
	@Override
	public boolean isInside(Vector2 point) {
		return (point.dst(center) < radius + margin);
	}

	/**
	 * The circle implementation of the newPointCacluation method.
	 * 
	 * @param currentPoint,
	 *            lastPoint
	 * @return the point after the collision occurs
	 */
	@Override
	public Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint) {
		Vector2 tmp = new Vector2(currentPoint.x - center.x, currentPoint.y - center.y);
		tmp.nor().scl(radius + margin);
		tmp.add(center);
		return tmp;
	}

}
