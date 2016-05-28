package pl.edu.amu.szi.forklift.utils;

/**
 * Created by khartv on 23.05.2016.
 */
public class Distance {
	private static String metric = "";

	public static int distance(Node begin, Node end)
	{
		int result = 0;
		switch(getMetric())
		{
			case "EUKLIDES":
				result = euklidesDistance(begin, end);
				break;
			case "JEŻ":
				result = jezDistance(begin, end);
				break;
			case "RZEKA":
				result = rzekaDistance(begin, end);
				break;
			case "CZEBYSZEW":
				result = czebyszewDistance(begin, end);
				break;
			default:
				result = manhattanDistance(begin, end);
				break;

		}
		return result;
	}

	private static int jezDistance(Node begin, Node end)
	{
		if((begin.getPosX() == end.getPosX()) || (begin.getPosY() == end.getPosY()))
		{
			return euklidesDistance(begin, end);
		}
		else
		{
			return euklidesDistance(begin, new Node(0, 0, true)) + euklidesDistance(new Node(0, 0, true), end);
		}
	}

	private static int rzekaDistance(Node begin, Node end)
	{
		if(begin.getPosY() == end.getPosY())
		{
			return euklidesDistance(begin, end);
		}
		else
		{
			return euklidesDistance(begin, new Node(begin.getPosY(), 0, true)) + euklidesDistance(new Node(begin.getPosY(), 0, true), new Node(end.getPosY(), 0, true)) + euklidesDistance(new Node(end.getPosY(), 0, true), end);
		}
	}

	private static int czebyszewDistance(Node begin, Node end)
	{
		int distX = Math.abs(end.getPosX() - begin.getPosX());
		int distY = Math.abs(end.getPosY() - begin.getPosY());
		return Math.max(distX, distY);
	}

	private static int euklidesDistance(Node begin, Node end)
	{
		int distX = Math.abs(end.getPosX() - begin.getPosX());
		int distY = Math.abs(end.getPosY() - begin.getPosY());
		Double sqrt =Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
		return sqrt.intValue();
	}

	private static int manhattanDistance(Node begin, Node end) {
		int distX = Math.abs(end.getPosX() - begin.getPosX());
		int distY = Math.abs(end.getPosY() - begin.getPosY());
		return distX + distY;
	}

	public static String getMetric() {
		return metric;
	}

	public static void setMetric(String metric) {
		Distance.metric = metric;
	}
}
