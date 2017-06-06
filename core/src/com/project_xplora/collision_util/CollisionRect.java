/**
 * 
 */
package com.project_xplora.collision_util;

import com.badlogic.gdx.math.Vector2;

/*
 * Diagram:
 * |---------ne
 * |          |
 * |          |
 * |          |
 * sw---------|
 */
/**
 * This class is the rectangle implementation of the {@link CollisionShape}
 * class. It detects collisions with a rectangle shape.
 * <p>
 * <b> Class Fields: </b>
 * <p>
 * private {@link Vector2} <b> ne </b> - the north east corner
 * <p>
 * private {@link Vector2} <b> sw </b> - the south west corner
 * <p>
 * Time taken to complete: 30 mins
 * 
 * @version 5.0 | 06.06.2017
 * @author <b> XploraStudios </b> - [Cyrus Gandevia and Michael Done].
 *
 */
public class CollisionRect extends CollisionShape {
	/** the north east corner */
	private Vector2 ne;
	/** the south west corner */
	private Vector2 sw;

	/**
	 * Class constructor. Constructs a new CollisionRect given two points
	 * 
	 * @param sw,
	 *            ne
	 */
	public CollisionRect(Vector2 sw, Vector2 ne) { // Liam was here
		this.ne = ne;
		this.sw = sw;
		margin = 0.15f;
	}

	/**
	 * The rectangle implementation of the isInside method.
	 * 
	 * @param point
	 * @return true if the point is inside the object
	 */
	@Override
	public boolean isInside(Vector2 point) {
		return (point.x < ne.x + margin && point.x > sw.x + margin && point.y < ne.y + margin
				&& point.y > sw.y + margin);

	}

	/**
	 * The rectangle implementation of the newPointCacluation method.
	 * 
	 * @param currentPoint,
	 *            lastPoint
	 * @return the point after the collision occurs
	 */
	@Override
	public Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint) {
		if (lastPoint.x <= sw.x + margin) {
			return new Vector2(sw.x + margin, currentPoint.y);
		} else if (lastPoint.x >= ne.x + margin) {
			return new Vector2(ne.x + margin, currentPoint.y);
		} else if (lastPoint.y <= sw.y + margin) {
			return new Vector2(currentPoint.x, sw.y + margin);
		} else if (lastPoint.y >= ne.y + margin) {
			return new Vector2(currentPoint.x, ne.y + margin);
		}
		return lastPoint;
	}

}
