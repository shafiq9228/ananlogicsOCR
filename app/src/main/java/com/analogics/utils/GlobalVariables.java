package com.analogics.utils;

public class GlobalVariables {

	public static byte[] portData;
	
	public static int BAUD_RATE = 9600; // BaudRate. Change this value if you need
	public static String PORTIRDA = "/dev/ttySC0"; //irda
	public static String PORTIR = "/dev/ttySC1";   //ir


	public static int getBAUD_RATE() {
		return BAUD_RATE;
	}

	public static void setBAUD_RATE(int bAUD_RATE) {
		BAUD_RATE = bAUD_RATE;
	}

	public static String getPORTIRDA() {
		return PORTIRDA;
	}

	public static void setPORTIRDA(String pORTIRDA) {
		PORTIRDA = pORTIRDA;
	}

	public static String getPORTIR() {
		return PORTIR;
	}

	public static void setPORTIR(String pORTIR) {
		PORTIR = pORTIR;
	}

	public static byte[] getPortData() {
		return portData;
	}

	public static void setPortData(byte[] portData) {
		GlobalVariables.portData = portData;
	}



}
