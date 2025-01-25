package com.siondream.superjumper;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

//
public class DesktopLauncher1 {
	public static void main (String[] arg) {
		MainMenuScreen.playerId = 1;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Super Jumper(playerId:" + MainMenuScreen.playerId+")";
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new SuperJumper(), config);
	}
}
