package com.guxuede.gm.gdx.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.guxuede.gm.gdx.Mappers;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class ActorComponent extends Actor implements Component {

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Entity entity = (Entity) this.getUserObject();
        TextureComponent textureComponent = Mappers.textureCM.get(entity);
        TextureRegion textureRegion = textureComponent.region;
        if (textureRegion != null) {
            batch.draw(textureRegion, getX(), getY());
        }
    }
}
