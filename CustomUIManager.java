package backend;

import application.Main;

public class CustomUIManager {

	public static void alert() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Main().runner();
			}
		}).start();
	}

}
