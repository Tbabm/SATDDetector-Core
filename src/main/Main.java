package main;

import java.util.Arrays;

import satd_detector.core.test.Test;
import satd_detector.core.train.Train;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			printHelp();
			return;
		}

		String subprocess = args[0].toLowerCase();
		if (!subprocess.equals("test") && !subprocess.equals("train")) {
			printHelp();
			return;
		}
		
		String[] subargs = Arrays.copyOfRange(args, 1, args.length);
		if (subprocess.equals("test")) {
			Test.test(subargs);
		}
		if (subprocess.equals("train")) {
			Train.train(subargs);
		}
	}

	private static void printHelp() {
		System.out.println("Please specify the subprocess: either test or train.");
		System.out.println("For example:\n$ java -jar satd_detector.jar test");
	}
}
