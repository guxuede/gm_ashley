package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.component.ActorStateComponent;
import com.guxuede.gm.gdx.component.CameraComponent;

public class CameraSystem extends IteratingSystem {

    private SpriteBatch spriteBatch;

	public CameraSystem(SpriteBatch spriteBatch) {
		super(Family.all(CameraComponent.class).get());
        this.spriteBatch = spriteBatch;
	}

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
	public void processEntity(Entity entity, float deltaTime) {
		CameraComponent cam = Mappers.cameraCM.get(entity);
		
		if (cam.target == null) {
			return;
		}

        ActorStateComponent target = Mappers.actorStateCM.get(cam.target);

		if (target == null) {
			return;
		}
		
		cam.camera.position.x = target.position.x;
        cam.camera.position.y = target.position.y;
        cam.camera.update();
        spriteBatch.setProjectionMatrix(cam.camera.combined);
	}
}
