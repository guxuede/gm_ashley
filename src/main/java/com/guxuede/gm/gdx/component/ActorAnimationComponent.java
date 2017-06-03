
package com.guxuede.gm.gdx.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.guxuede.gm.gdx.AnimationHolder;

public class ActorAnimationComponent implements Component {
    public static final int STOP=0, DOWN=1,LEFT=2,RIGHT=3,UP=4;

	public AnimationHolder animationHolder;
    public int direction;
    public boolean isMoving;
    public final Vector2 animationPosition = new Vector2();
}
