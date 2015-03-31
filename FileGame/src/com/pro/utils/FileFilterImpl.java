package com.pro.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import com.pro.game.GameApp;
import com.pro.pojo.GameFile;
import com.pro.pojo.Simulator;

public class FileFilterImpl implements FileFilter {

	private Simulator slr;
	private List<GameFile> files;

	public FileFilterImpl(Simulator slr, List<GameFile> files) {
		this.slr = slr;
		this.files = files;
	}

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub

		if (GameApp.dir.equals(slr.getName())) {
			if (pathname.isDirectory()) {
				GameFile gf = new GameFile();
				gf.setFile(pathname);
				files.add(gf);
				return true;
			}
			for (int i = 0; i < GameApp.Simulatorlist.size(); i++) {
				Simulator simulator = GameApp.Simulatorlist.get(i);
				if (simulator.isGameFile(pathname)) {
					GameFile gf = new GameFile();
					gf.setFile(pathname);
					gf.setSimulator(simulator);
					files.add(gf);
					return true;
				}
			}
			return false;
		} else {
			if (pathname.isDirectory()) {
				return true;
			}
			if (slr.getName().equals(GameApp.allgamename)) {
				for (int i = 0; i < GameApp.Simulatorlist.size(); i++) {
					Simulator simulator = GameApp.Simulatorlist.get(i);
					if (simulator.isGameFile(pathname)) {
						GameFile gf = new GameFile();
						gf.setFile(pathname);
						gf.setSimulator(simulator);
						files.add(gf);
						return true;
					}
				}
			}
			boolean flag = slr.isGameFile(pathname);
			if (flag) {
				GameFile gf = new GameFile();
				gf.setFile(pathname);
				gf.setSimulator(slr);
				files.add(gf);
			}
			return flag;

		}
	}

}
