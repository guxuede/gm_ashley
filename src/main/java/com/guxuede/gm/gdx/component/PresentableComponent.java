package com.guxuede.gm.gdx.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PresentableComponent implements Component {
	public TextureRegion region = null;
    public final Vector2 renderPosition = new Vector2();
    public float zIndex; //值越大画的越靠前(越高，越在其他基色之上，不易遮挡)
}
