package pl.edu.amu.szi.forklift.c45;

public class start {
	public static void generate(String testingInput) {


//		Tree tree = new Tree();
//		tree.generateBestTree(dataFile, 1000);
//		tree.generateClassification(testFile);

		Tree tree = new Tree();

		System.out.println("Preparing tree");

		String dataFile = "/home/dere/git/thinkForklift/src/main/java/pl/edu/amu/szi/forklift/c45/data/C/trainProdSelection.arff";
		//String testFile = "~/git/thinkForklift/src/main/java/pl/edu/amu/szi/forklift/c45/data/C/testProdSelection.arff"; // only for testing!
		tree.generateBestTree(dataFile, 1000);
		//tree.generateClassification(testFile);

		System.out.println("Preparing classification");
		tree.generateFunClassification(testingInput);
	}
}
