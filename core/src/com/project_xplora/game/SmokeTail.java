/**
 * 
 */
package com.project_xplora.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

/**
 * @author Michael
 *
 */
public class SmokeTail {
	Model ball;
	Array<ModelInstance> smokeBalls;
	/**
	 * 
	 */
	public SmokeTail() {
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ball";
		mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLACK)))
				.sphere(1, 1, 1, 5, 5);
		ball = mb.end();
	}
	public void update(){
		
	}
}
