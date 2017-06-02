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
 * @author Michael
 *
 */
public class CollisionRect extends CollisionShape {
	Vector2 ne;
	Vector2 sw;

	/**
	 * 
	 */
	public CollisionRect(Vector2 sw, Vector2 ne) { // Liam was here
		this.ne = ne;
		this.sw = sw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.project_xplora.collision_util.CollisionShape#isInside(com.badlogic.
	 * gdx.math.Vector2)
	 */
	@Override
	public boolean isInside(Vector2 point) {
		return (point.x < ne.x && point.x > sw.x && point.y < ne.y && point.y > sw.y);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.project_xplora.collision_util.CollisionShape#newPointCaclulation(com.
	 * badlogic.gdx.math.Vector2, com.badlogic.gdx.math.Vector2)
	 */
	@Override
	public Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint) {
		if (lastPoint.x <= sw.x) {
			return new Vector2(sw.x, currentPoint.y);
		} else if (lastPoint.x >= ne.x) {
			return new Vector2(ne.x, currentPoint.y);
		} else if (lastPoint.y <= sw.y) {
			return new Vector2(currentPoint.x, sw.y);
		} else if (lastPoint.y >= ne.y) {
			return new Vector2(currentPoint.x, ne.y);
		}
		return null;
	}

}
