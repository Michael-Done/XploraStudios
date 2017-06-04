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
		margin = 0.15f;
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
		return (point.x < ne.x + margin && point.x > sw.x + margin && point.y < ne.y + margin
				&& point.y > sw.y + margin);

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
