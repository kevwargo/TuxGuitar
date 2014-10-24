package org.herac.tuxguitar.gui;

public class TGMain {
	
	public static void main(String[] args){
        System.out.println("Zaraz novy debugmessage");
		TuxGuitar.instance().displayGUI(args);
		System.exit(0);
	}
	
}
