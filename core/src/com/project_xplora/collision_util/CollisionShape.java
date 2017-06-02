/**
 * 
 */
package com.project_xplora.collision_util;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Michael
 *
 */
public abstract class CollisionShape {
	
	public abstract boolean isInside(Vector2 point);
	
	public boolean isInside(float x, float y){
		return isInside(new Vector2(x, y));
	}
	public abstract Vector2 newPointCaclulation(Vector2 currentPoint, Vector2 lastPoint);
}
