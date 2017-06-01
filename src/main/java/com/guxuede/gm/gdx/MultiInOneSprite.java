package com.guxuede.gm.gdx;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by guxuede on 2016/5/31 .
 */
public class MultiInOneSprite extends GdxSprite {

    GdxSprite[] sprites;

    public MultiInOneSprite(GdxSprite[] sprites){
        this.sprites = sprites;
        this.set(sprites[0]);//no use ，just void NPE,
    }


    public void draw(Batch batch){
        for(GdxSprite sprite : sprites){
            sprite.setPosition(this.getX(), this.getY());
            sprite.draw(batch,1,getRotation(),getScaleX(),getScaleY(),getColor());
        }
    }

    /**
     * 这个是包含多个sprites的，他们本身会偏移，画它时，不要再自身偏移
     */
    @Override
    public void setX(float x) {
        translateX(x - this.getX());
    }

    @Override
    public void setY(float y) {
        translateY(y - this.getY());
    }

    @Override
    public void setPosition(float x, float y) {
        translate(x - this.getX(), y - this.getY());
    }
}
