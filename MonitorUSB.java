package backend;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorUSB {

	public static void main(String[] args) {
		AtomicInteger i = new AtomicInteger(0);
		ExecutorService workerPool = Executors.newFixedThreadPool(1);
		workerPool.execute(new Runnable() {

			@Override
			public void run() {
				while (true) {
					System.out.println("checking for usb");
					if (isUSBPresent()) {
						System.out.println("Triggering alert");
						CustomUIManager.alert();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						logoutNow();
						break;
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i.incrementAndGet();
				}
			}

			private boolean isUSBPresent() {
				//

				String[] letters = new String[] { "E", "F", "G", "H", "I", "J", "K", "L", "M", "N" };
				File[] Externaldrives = new File[letters.length];
				boolean[] FoundDrive = new boolean[letters.length];

				// init the file objects and the initial drive state

				for (int i = 0; i < letters.length; ++i) {
					Externaldrives[i] = new File(letters[i] + ":/");

					FoundDrive[i] = Externaldrives[i].canRead();
				}

				System.out.println("Waiting for device, Please Wait");

				// Search each drive
				while (true) {
					for (int i = 0; i < letters.length; ++i) {
						boolean pluggedIn = Externaldrives[i].canRead();

						// if the state has changed output a message
						if (pluggedIn != FoundDrive[i]) {
							if (pluggedIn) {
								System.out.println("Drive " + letters[i] + " has been plugged in");

								return true;
							}
							FoundDrive[i] = pluggedIn;

						}

					}

				}

			}
		});

		workerPool.shutdown();
	}

	protected static void logoutNow() {
	
		 try { 
			 Runtime.getRuntime().
			 exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation"); 
		 }
		 catch (IOException e) { e.printStackTrace(); }
	
	}

}
