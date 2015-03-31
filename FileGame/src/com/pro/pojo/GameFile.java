package com.pro.pojo;

import java.io.File;

public class GameFile {

	private Simulator simulator;
	public Simulator getSimulator() {
		return simulator;
	}
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	private File file;
}
