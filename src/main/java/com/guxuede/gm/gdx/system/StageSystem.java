package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.guxuede.gm.gdx.component.ActorComponent;

import static com.guxuede.gm.gdx.Mappers.actorCM;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class StageSystem extends IteratingSystem {

    private static final Family family = Family.all(ActorComponent.class).get();

    private Stage stage;

    public StageSystem() {
        super(family);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(family, 0, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                ActorComponent actorComponent = actorCM.get(entity);
                actorComponent.setUserObject(entity);
                stage.addActor(actorComponent);
            }
            @Override
            public void entityRemoved(Entity entity) {
                actorCM.get(entity).remove();
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

}
