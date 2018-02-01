package satd_detector.core.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import satd_detector.core.utils.SATDDetector;

public class Test {
	private static String prompt = ">";

	public static void main(String[] args) {
		test(args);
	}

	public static void test(String[] args) {
		Options opts = createOptions();
		CommandLineParser parser = new DefaultParser();
		 HelpFormatter formatter = new HelpFormatter();
		CommandLine cl = null;
		String modelDir = null;
		try {
			cl = parser.parse(opts, args);
			modelDir = cl.getOptionValue("model_dir");
		} catch (ParseException e) {
			// e.printStackTrace();
			formatter.printHelp("test", opts);
			return;
		}

		if (cl.hasOption("h")) {
			formatter.printHelp("test", opts);
			return;
		}
		modelDir = cl.getOptionValue("model_dir");
		if (modelDir != null) {
			File modelFile = new File(modelDir);
			if (!modelFile.isDirectory()) {
				formatter.printHelp("test", opts);
			}
		}

		SATDDetector detector = new SATDDetector(modelDir);
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		while (true) {
			try {
				System.out.print(prompt);
				String comment = br.readLine();
				if (comment.equals("/exit")) {
					System.out.println("bye!");
					break;
				}
				if (detector.isSATD(comment))
					System.out.println("SATD");
				else
					System.out.println("Not SATD");
				// System.out.println(comment);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private static Options createOptions() {
		Options opts = new Options();
		opts.addOption("h", false, "Show help message");
		opts.addOption("model_dir", true,
				"Dir which stores all the models. Using build-in models if not specified.");
		return opts;
	}
}
