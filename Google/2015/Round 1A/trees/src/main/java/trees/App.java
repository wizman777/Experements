package trees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";
	
	public static class Tree {
		int x;
		int y;
		boolean cut = false;
		Integer need = null;
		
		List<Triangle> boundaries = new ArrayList<Triangle>();
	//	List<Triangle> triangles = new ArrayList<Triangle>();
		
		public Tree(String[] data) {
			x = Integer.parseInt(data[0]);
			y = Integer.parseInt(data[1]);
		}
		
		public boolean equals(Tree t) {
			return x == t.x && y == t.y;
		}
		
		public boolean isBoundary() {
			for (Triangle triangle : boundaries) 
				if (!triangle.isBroken())
					return false;
			
			return true;
		}
		
		public void calcNumber() {
			Set<Tree> trees = new HashSet<Tree>();
			for (Triangle triangle : boundaries) 
				if (triangle.isBroken()) {
					if (triangle.t1.cut) 
						trees.add(triangle.t1);
					if (triangle.t2.cut) 
						trees.add(triangle.t2);
					if (triangle.t3.cut) 
						trees.add(triangle.t3);
				} else 
					return;
			
			need = trees.size();			
		}

		@Override
		public String toString() {
			return "Tree [x=" + x + ", y=" + y + ", cut=" + cut + ", need="
					+ need + "]";
		}

		
	}
	
	public static class Triangle {
		Tree t1;
		Tree t2;
		Tree t3;
		
		//List<Tree> inside = new ArrayList<Tree>();
		
		public Triangle(Tree t1, Tree t2, Tree t3) {
			this.t1 = t1;
			this.t2 = t2;
			this.t3 = t3;
			
	//		this.t1.triangles.add(this);
	//		this.t2.triangles.add(this);
	//		this.t3.triangles.add(this);
		}
		
		public boolean inInside(Tree t) {
			if (t.equals(t1) || t.equals(t2) || t.equals(t3))
				return false;
			
			float alpha = (float) ((t2.y - t3.y)*(t.x - t3.x) + (t3.x - t2.x)*(t.y - t3.y)) /
					(float) ((t2.y - t3.y)*(t1.x - t3.x) + (t3.x - t2.x)*(t1.y - t3.y));
			if (alpha < 0)
				return false;
			
			float beta = (float) ((t3.y - t1.y)*(t.x - t3.x) + (t1.x - t3.x)*(t.y - t3.y)) /
					(float) ((t2.y - t3.y)*(t1.x - t3.x) + (t3.x - t2.x)*(t1.y - t3.y));
			if (beta < 0)
				return false;
			
			float gamma = 1.0f - alpha - beta;
			
			return gamma > 0;
		}
		
		public boolean isBroken() {
			return t1.cut || t2.cut || t3.cut;
		}

		@Override
		public String toString() {
			return "Triangle [t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + "]";
		}
	}
	
	public static void main(String[] args) {
		long beginTime = System.currentTimeMillis();
		
		try {
			String inputName = INPUT_FILE_NAME;
			String outputName = OUTPUT_FILE_NAME;
		
			if (args.length > 0)
				inputName = args[0];
		
			if (args.length > 1)
				outputName = args[1];
		
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputName)))) {
				try (PrintWriter writer = new PrintWriter(outputName)) {
					int nCases = Integer.parseInt(reader.readLine());
				
					for (int nCase = 0; nCase < nCases; ++nCase) {
						int nTrees = Integer.parseInt(reader.readLine());
						Tree[] trees = new Tree[nTrees];
						
						for (int nTree = 0; nTree < nTrees; ++nTree) 
							trees[nTree] = new Tree(reader.readLine().split(" "));
						
					//	List<Triangle> triangles = new ArrayList<Triangle>();
						
						for (int nTree1 = 0; nTree1 < nTrees-2; ++nTree1) 
							for (int nTree2 = nTree1+1; nTree2 < nTrees-1; ++nTree2) 
								for (int nTree3 = nTree2+1; nTree3 < nTrees; ++nTree3) {
									Triangle tr = new Triangle(trees[nTree1], trees[nTree2], trees[nTree3]);
									
									for (int nTree = 0; nTree < nTrees; ++nTree)
										if (tr.inInside(trees[nTree])) {
										//	tr.inside.add(trees[nTree]);
											trees[nTree].boundaries.add(tr);
										}
									
							//		triangles.add(tr);
								}
						
						boolean uncutTree = true;
						while (uncutTree) {
							for (int nTree = 0; nTree < nTrees; ++nTree) {
								if (!trees[nTree].cut) 
									trees[nTree].calcNumber();								
							}

							uncutTree = false;
							for (int nTree = 0; nTree < nTrees; ++nTree) {
								if (!trees[nTree].cut) {
									if (trees[nTree].need != null)
										trees[nTree].cut = true;
									else
										uncutTree = true;
								}
							}
						}
								
						
						
						System.out.println("Case #" + (nCase + 1) + ":");
						writer.println("Case #" + (nCase + 1) + ":");

						for (Tree tree : trees)
						{
							System.out.println(tree.need);
							writer.println(tree.need);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(String.format("Done. Spend %d ms", endTime - beginTime));
	}
}
