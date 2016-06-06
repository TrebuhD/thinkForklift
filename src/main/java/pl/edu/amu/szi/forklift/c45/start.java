package pl.edu.amu.szi.forklift.c45;

public class start {
	public static void generate(String[] testingInput) {


//		Tree tree = new Tree();
//		tree.generateBestTree(dataFile, 1000);
//		tree.generateClassification(testFile);

		Tree tree = new Tree();


		String dataFile = "F:\\Github\\...\\C\\trainProdSelection.arff";
		//String testFile = "F:\\Github\\...\\C\\testProdSelection.arff"; // only for testing!
		tree.generateBestTree(dataFile, 1000);
		//tree.generateClassification(testFile);
		tree.generateFunClassification(testingInput);

	}
}
