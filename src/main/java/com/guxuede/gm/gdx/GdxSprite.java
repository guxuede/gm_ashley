package com.guxuede.gm.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by guxuede on 2016/6/1 .
 */
public class GdxSprite extends Sprite {

    public float offSetX, offSetY;

    public GdxSprite() {
    }

    public GdxSprite(Texture texture) {
        super(texture);
    }

    public GdxSprite(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
    }

    public GdxSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

    public GdxSprite(TextureRegion region) {
        super(region);
    }

    public GdxSprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
    }

    public GdxSprite(Sprite sprite) {
        super(sprite);
    }

    public void setOffSet(float offSetX, float offSetY) {
        this.offSetX = offSetX;
        this.offSetY = offSetY;
    }

    @Override
    public void setX(float x) {
        super.setX(x  - this.getRegionWidth() / 2 + offSetX);
    }

    @Override
    public void setY(float y) {
        super.setY(y  - this.getRegionHeight() / 2 + offSetY);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x - this.getRegionWidth() / 2 + offSetX, y - this.getRegionHeight()/2 + offSetY);
    }


    public void draw(Batch batch, float alphaModulation, float rotation, float scaleX, float scaleY, Color color) {
        float oldRotation = getRotation();
        float oldScaleX = getScaleX();
        float oldScaleY = getScaleY();

        setColor(color);
        setRotation(rotation+oldRotation);
        setScale(oldScaleX * scaleX, oldScaleY * scaleY);

        draw(batch,alphaModulation);

        setRotation(oldRotation);
        setScale(oldScaleX, oldScaleY);
    }
}
