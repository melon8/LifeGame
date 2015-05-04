package com.lifegame2;

import java.util.concurrent.ExecutorService;

/**
 * ������Ϸ���������ϸ�����ˣ���Χϸ����3������ô��ϸ����������Χϸ��<2����>3��ô��ϸ����ȥ�������Χϸ��=2ά��ԭ״̬
 * 
 * @author Song ���ö��߳�ʵ�ֵ�������Ϸ
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

	// ������һ��,�����������һ��
	public void next() {

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int cellsAroundAlive = this.checkNeighbor(i, j);
				// ���������ϸ�����ˣ���Χϸ����3������ô��ϸ����������Χϸ��<2����>3��ô��ϸ����ȥ�������Χϸ��=2��ά��ԭ״̬
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

	// ���أ�x,y��ϸ����Χ�˸�ϸ��(i,j)�л��ŵ�ϸ������
	public int checkNeighbor(int a, int b) {
		int count = 0;
		for (int i = a - 1; i <= a + 1; i++) {
			for (int j = b - 1; j <= b + 1; j++) {
				// �ų�����0��rows��0��cols������
				if (i >= 0 && i < rows && j >= 0 && j < cols) {
					// �ų�ϸ���Լ�
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
		// ��һ���� �ڼ���
		String map = "��" + generation + "��" + "\n";
		// �ڶ����� ������
		for (int k = 0; k < cols; k++) {
			if (k < 10)
				map += " " + k + " ";
			else
				map += k + " ";
		}
		map += "\n";
		// ���ϸ���� 0 ������ϸ���� _

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				if (currentCells[i][j] == true) {
					map += " 0 ";
				} else {
					map += " _ ";
				}
			}
			// ÿ�н����Ǻ�����
			map += " " + i + "\n";
		}

		return map;
	}

}
