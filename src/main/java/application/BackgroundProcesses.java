package application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javafx.animation.AnimationTimer;

public class BackgroundProcesses extends Thread {	
	private static BackgroundProcesses backgroundProcesses;
	private static HashMap<String, HashSet<Runnable>> actionMap;
	
	public BackgroundProcesses() {
		actionMap = new HashMap<>();
	}
	
	public static void addAction(Runnable action) {
		SceneManager sceneManager = SceneManager.get();
		String key = sceneManager.getCurrentSceneEntry().getKey();
		
		if(actionMap.get(key) == null) {
			actionMap.put(key, new HashSet<>());
		}
		actionMap.get(key).add(action);
	}
	
	public void run() {
		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				runProcesses();
			}
		};
		animator.start();
	}
	
	private void runProcesses() {
		SceneManager sceneManager = SceneManager.get();
		String key = sceneManager.getCurrentSceneEntry().getKey();
		
		if(actionMap.get(key) != null) {
			Iterator<Runnable> iter = actionMap.get(key).iterator();
			
			while(iter.hasNext()) {
				Runnable action = iter.next();
				action.run();
			}
		}
	}
	
	public static BackgroundProcesses get() {
		if(backgroundProcesses == null) {
			backgroundProcesses = new BackgroundProcesses();
			backgroundProcesses.start();
		}
		return backgroundProcesses;
	}
}
