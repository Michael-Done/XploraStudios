/**
 * 
 */
package com.project_xplora.collision_util;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Michael
 *
 */
public class CollisionCircle extends CollisionShape {
	Vector2 center;
	float radius;

	/**
	 * 
	 */
	public CollisionCircle(Vector2 center, float radius) {
		this.center = center;
		this.radius = radius;
		margin = 0.15f;
	}

	/**
	 * 
	 * @see com.project_xplora.collision_util.CollisionShape#isInside(com.badlogic.
	 * gdx.math.Vector2)
	 */
	@Override
	public boolean isInside(Vector2 point) {
		return (point.dst(center) < radius + margin);
	}

	@Override
	public Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint) {
		Vector2 tmp = new Vector2(currentPoint.x - center.x, currentPoint.y - center.y);
		tmp.nor().scl(radius + margin);
		tmp.add(center);
		return tmp;
	}

}
