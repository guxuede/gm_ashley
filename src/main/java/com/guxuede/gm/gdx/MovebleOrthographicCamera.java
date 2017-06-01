package com.guxuede.gm.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class MovebleOrthographicCamera extends OrthographicCamera {

	private float speed = 5.0f;
	
	public MovebleOrthographicCamera() {
	}
	
	public MovebleOrthographicCamera(float width, float height) {
		super(width, height);
	}

	@Override
	public void update() {
		if(Gdx.input.isKeyPressed(Keys.UP)){
			this.translate(0, speed);
		}else if(Gdx.input.isKeyPressed(Keys.DOWN)){
			this.translate(0, -speed);
		}else if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			this.translate(speed, 0);
		}else if(Gdx.input.isKeyPressed(Keys.LEFT)){
			this.translate(-speed, 0);
		} 
		super.update();
	}

	
}
