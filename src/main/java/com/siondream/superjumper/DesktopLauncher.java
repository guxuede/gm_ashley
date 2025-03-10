package com.siondream.superjumper;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

//游玩方式, 先启动JumperGameServer,再启动DesktopLauncher 玩家1
//DesktopLauncher1 玩家2
public class DesktopLauncher {
	public static void main (String[] arg) {
		MainMenuScreen.playerId = 0;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Super Jumper(playerId:" + MainMenuScreen.playerId+")";
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new SuperJumper(), config);
	}
}
