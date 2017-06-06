package com.project_xplora.collision_util;

import com.badlogic.gdx.math.Vector2;

/**
 * This class is the circle implementation of the {@link CollisionShape} class.
 * It detects collisions with a circular shape.
 * <p>
 * Time Taken to Complete Class: 30 minutes
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * private {@link Vector2} <b> center </b> - The CollisionCircle object's center
 * point.
 * <p>
 * private float <b> radius </b> - The CollisionCircle object's radius.
 * <p>
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 *
 */
public class CollisionCircle extends CollisionShape {
	private Vector2 center;
	private float radius;

	/**
	 * The CollisionCircle Class constructor. Constructs a new CollisionCircle
	 * based on the center and radius provided through the parameters.
	 * 
	 * @param center
	 *            - Location of the vector's center.
	 * @param radius
	 *            - Length of the radius.
	 */
	public CollisionCircle(Vector2 center, float radius) {
		this.center = center;
		this.radius = radius;
		margin = 0.15f;
	}

	/**
	 * The circle implementation of the isInside ( ) method.
	 * 
	 * @param point
	 *            - Point location of the vector.
	 * @return Returns true if the point is inside the object.
	 */
	@Override
	public boolean isInside(Vector2 point) {
		return (point.dst(center) < radius + margin);
	}

	/**
	 * The circle implementation of the newPointCacluation ( ) method.
	 * <p>
	 * <b> Local Variables: </b>
	 * <p>
	 * Vector2 <b> tmp </b> - Temporary vector used for the collision
	 * calculations.
	 * 
	 * @param currentPoint
	 *            - Current point location of the vector.
	 * @param lastPoint
	 *            - Previous point location of the vector.
	 * @return Returns the point location of the vector after the collision
	 *         occurs.
	 */
	@Override
	public Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint) {
		Vector2 tmp = new Vector2(currentPoint.x - center.x, currentPoint.y - center.y);
		tmp.nor().scl(radius + margin);
		tmp.add(center);
		return tmp;
	}

}
