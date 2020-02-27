package com.avisfy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import com.avisfy.DataCalc;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String inpStr;
		String operation;
		DataCalc calc =  null;
		boolean isExit = true;
		//String inps = in.nextLine();
		//LocalDate test;
		//DateTimeFormatter inf = DateTimeFormatter.ofPattern("dd MMMM yy", new Locale("ru","RU"));
		//DateTimeFormatter outf = DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru","RU"));
		//test = LocalDate.parse(inps, inf);
		//System.out.println(test.format(outf));

		System.out.println("Set initial date");

		do{
			System.out.print("> ");
			inpStr = in.nextLine();
			int i;
			if ((i =  inpStr.indexOf(" ")) >= 0) {
				operation = inpStr.substring(0, i);
				inpStr = inpStr.substring(i + 1);
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