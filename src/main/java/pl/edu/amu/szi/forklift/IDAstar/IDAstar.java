package pl.edu.amu.szi.forklift.IDAstar;

import pl.edu.amu.szi.forklift.utils.Distance;
import pl.edu.amu.szi.forklift.utils.Node;
import pl.edu.amu.szi.forklift.Map;
import pl.edu.amu.szi.forklift.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khartv on 23.05.2016.
 */
public class IDAstar
{
	int sizeX = Constants.MAP_SIZE_X;
	int sizeY = Constants.MAP_SIZE_Y;

	private Node[][] passablePath;

	public List<Node> findPath(int startX, int startY, int destinationX, int destinationY)
	{
		System.out.print("X ");
		for (int j = 0; j < sizeY; j++) {
			System.out.print(j + " ");
		}
		System.out.println();
		for (int i = 0; i < sizeX; i++)
		{
			System.out.print(i + " ");
			for (int j = 0; j < sizeY; j++)
			{
				passablePath[j][i].setH(Distance.distance(passablePath[j][i], passablePath[destinationX][destinationY]));
				System.out.print(passablePath[j][i].getH() + " ");
			}
			System.out.println();
		}


		List<Node> out = new ArrayList<Node>();
		//if(true)
		//return out;

		int bound = calculateBound(out);

		int t = 0;
		while(true)
		{
			t = search(passablePath[startX][startY], passablePath[destinationX][destinationY], 0, bound, out);

			if(t == 999999)
			{
				return null;
			}

			if(t == -1)
			{
				return out;
			}

			bound = t;

		}
	}


	private int search(Node current, Node end, int g, int bound, List<Node> out)
	{
		int temp;

		int f = g + current.getH();
		if(f > bound)
		{
			//out = new ArrayList<Node>();
			return f;
		}
		if(current.equals(end))
		{
			out.add(current);
			return -1;
		}
		int min = 999999;

		for(Node n : findSuccessors(current))
		{
			temp = search(n, end, g + n.getCost(), bound, out);
			if(temp == -1)
			{
				out.add(n);
				return -1;
			}
			if(temp < min)
			{
				min = temp;
			}
		}
		return min;
	}

	private List<Node> findSuccessors(Node current)
	{
		List<Node> adjecencies = new ArrayList<>();
		Node tempNode;
		if (current.getPosX() - 1 >= 0) {
			tempNode = passablePath[current.getPosX() - 1][current.getPosY()];
			if (tempNode.isPassable()) {
				adjecencies.add(tempNode);
			}
		}
		if (current.getPosX() + 1 <= Constants.MAP_SIZE_X - 1) {
			tempNode = passablePath[current.getPosX() + 1][current.getPosY()];
			if (tempNode.isPassable()) {
				adjecencies.add(tempNode);
			}
		}
		if (current.getPosY() - 1 >= 0) {
			tempNode = passablePath[current.getPosX()][current.getPosY() - 1];
			if (tempNode.isPassable()) {
				adjecencies.add(tempNode);
			}
		}
		if (current.getPosY() + 1 <= Constants.MAP_SIZE_Y + 1) {
			tempNode = passablePath[current.getPosX()][current.getPosY() + 1];
			if (tempNode.isPassable()) {
				adjecencies.add(tempNode);
			}
		}

		return adjecencies;
	}
	private int calculateBound(List<Node> list)
	{
		int sum = 0;
		for(Node n : list)
		{
			sum += n.getH();
		}
		return sum;
	}

	public IDAstar() {
		this.passablePath = preparePassablePath();
	}

	private Node[][] preparePassablePath() {
		Map map = Map.getInstance();

		Node[][] path = new Node[sizeX][sizeY];

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {

				path[i][j] = new Node(i, j, map.isPassable(i, j));
			}
		}

		return path;
	}
}
