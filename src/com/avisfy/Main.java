package com.avisfy;

import java.util.Scanner;
import com.avisfy.DataCalc;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String inpStr;
		String operation;
		DataCalc calc =  null;
		boolean isExit = true;
		System.out.println("Set initial date");

		do{
			System.out.print("> ");
			inpStr = in.nextLine();
			int i;
			if ((i =  inpStr.indexOf(" ")) >= 0) {
				operation = inpStr.substring(0, i);
				inpStr = inpStr.substring(i + 1);
				System.out.println(operation + inpStr);
			} else {
				operation = inpStr;
			}
			switch(operation) {
				case "/init":
					calc = new DataCalc(inpStr);
					if (calc.errorOccurred) {
						System.out.println("Error, incorrect date");
						calc = null;
					}
					break;
				case "/in_day":
					if (calc !=  null) {
						String res = calc.typeOfDay(inpStr);
						System.out.println(res);
					} else {
						System.out.println("Error, /init first");
					}
					break;
				case "/in_week":
					System.out.println("in_week");
					break;
				case "exit":
					isExit = false;
				default:
					System.out.println("Error, unknown operator");
			}
		} while (isExit);

	}
}