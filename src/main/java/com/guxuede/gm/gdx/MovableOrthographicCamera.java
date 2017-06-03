package com.guxuede.gm.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class MovableOrthographicCamera extends OrthographicCamera {

	private float speed = 5.0f;
	
	public MovableOrthographicCamera() {
        this(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}
	
	public MovableOrthographicCamera(float width, float height) {
		super(width, height);
	}

	@Override
	public void update() {
		if(Gdx.input.isKeyPressed(Keys.PAGE_UP)){
			this.translate(0, speed);
		}else if(Gdx.input.isKeyPressed(Keys.PAGE_DOWN)){
			this.translate(0, -speed);
		}else if(Gdx.input.isKeyPressed(Keys.HOME)){
			this.translate(speed, 0);
		}else if(Gdx.input.isKeyPressed(Keys.END)){
			this.translate(-speed, 0);
		} 
		super.update();
	}

	
}
