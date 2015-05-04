package com.lifegame2;

import java.util.concurrent.ExecutorService;

/**
 * 生命游戏规则：如果本细胞死了，周围细胞都3个，那么死细胞复活；如果周围细胞<2或者>3那么活细胞死去；如果周围细胞=2维持原状态
 * 
 * @author Song 不用多线程实现的生命游戏
 *
 */
public class Game2 {
	boolean[][] currentCells;
	boolean[][] nextCells;
	boolean[][] tempCells;
	int rows, cols;
	int generation;
	ExecutorService threadPool;

	public static void main(String[] args) {
		Game2 g = new Game2(20, 20);
		System.out.println(g);
		while (true) {
			g.next();
			System.out.println(g);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// for(int i=0;i<20;i++){
		// g.next();
		// System.out.println(g);
		// }

	}

	public Game2(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		generation = 1;
		currentCells = new boolean[rows][cols];
		nextCells = new boolean[rows][cols];
		tempCells = new boolean[rows][cols];
		init();
	}

	public void init() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				nextCells[i][j] = false;
				tempCells[i][j] = false;
				if (i >= 10 && i <= 10 && j >= 3 && j <= 7)
					currentCells[i][j] = true;
				else
					currentCells[i][j] = false;
			}
		}
	}

	// 产生下一代,并且算出是哪一代
	public void next() {

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int cellsAroundAlive = this.checkNeighbor(i, j);
				// 规则：如果本细胞死了，周围细胞都3个，那么死细胞复活；如果周围细胞<2或者>3那么活细胞死去；如果周围细胞=2，维持原状态
				if (cellsAroundAlive == 3) {
					nextCells[i][j] = true;
				} else if (cellsAroundAlive == 2) {
					nextCells[i][j] = currentCells[i][j];
				} else
					nextCells[i][j] = false;
			}
		}
		tempCells = currentCells;
		currentCells = nextCells;
		nextCells = tempCells;
		generation++;
	}

	// 返回（x,y）细胞周围八个细胞(i,j)中活着的细胞数量
	public int checkNeighbor(int a, int b) {
		int count = 0;
		for (int i = a - 1; i <= a + 1; i++) {
			for (int j = b - 1; j <= b + 1; j++) {
				// 排除超过0到rows和0到cols的坐标
				if (i >= 0 && i < rows && j >= 0 && j < cols) {
					// 排除细胞自己
					if (!(i == a && j == b)) {
						if (currentCells[i][j] == true)
							count++;
					}
				}
			}
		}
		return count;
	}

	@Override
	public String toString() {
		// 第一行是 第几代
		String map = "第" + generation + "代" + "\n";
		// 第二行是 纵坐标
		for (int k = 0; k < cols; k++) {
			if (k < 10)
				map += " " + k + " ";
			else
				map += k + " ";
		}
		map += "\n";
		// 活的细胞是 0 ，死的细胞是 _

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (currentCells[i][j] == true) {
					map += " 0 ";
				} else {
					map += " _ ";
				}
			}
			// 每行结束是横坐标
			map += " " + i + "\n";
		}

		return map;
	}

}
