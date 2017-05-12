package com.project_xplora.game;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public class CollisionHandler extends ContactListener{
	btCollisionShape groundShape;
    btCollisionShape ballShape;
    
    btCollisionObject groundObject;
    btCollisionObject ballObject;
    
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    
    public CollisionHandler(){
    	collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
    }
    
    @Override
    public boolean onContactAdded (btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
        btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
        //instances.get(colObj0Wrap.getCollisionObject().getUserValue()).moving = false;
        //instances.get(colObj1Wrap.getCollisionObject().getUserValue()).moving = false;
        return true;
    }
}
