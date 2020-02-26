package com.avisfy;

import java.util.Scanner;
import com.avisfy.DataCalc;

public class Main {

    public static void main(String[] args) {
	System.out.println("Set initial date");
	System.out.print("> ");
	Scanner in = new Scanner(System.in);
	String inpInit  = in.nextLine();
	DataCalc calc = new DataCalc(inpInit);
	if (calc.errorOccurred) {
	    System.out.println("Error, end");
	    return;
    }
	System.out.println("Set date to calculate");
	System.out.print("> ");
	String inpCalc = in.nextLine();
	String res = calc.typeOfDay(inpCalc);
	System.out.println(res);
    }
}
