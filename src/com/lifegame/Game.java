package com.lifegame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * ������Ϸ���������ϸ�����ˣ���Χϸ����3������ô��ϸ����������Χϸ��<2����>3��ô��ϸ����ȥ�������Χϸ��=2��ά��ԭ״̬
 * �����̳߳أ�ÿһ������һ���̳߳أ���ÿһ��ϸ�����񣨼̳�runnable�ӿڣ����뵽�̳߳��У�ִ����ȫ��������̳߳�shutdown
 * @author Song
 *
 */
public class Game {
	private  boolean[][] currentCells;
	private boolean[][] nextCells;
	private boolean[][] tempCells;
	private int rows, cols;
	private int generation;
	private ExecutorService threadPool;
	
	public static void main(String[] args) {
		Game g = new Game(20, 20);
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
	}

	public Game(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		generation = 1;
		currentCells = new boolean[rows][cols];
		nextCells = new boolean[rows][cols];
		tempCells= new boolean[rows][cols];
		init();
					
	}

	public void init() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
//				//����������ŵ�ϸ��
//				 double random = Math.random();
//				 if (random < 0.2) {
//				 cells[i][j] = true;
//				 } else {
//				 cells[i][j] = false;
//				 }
				
//				//һ��ֱ��
//				if (i >= 10 && i <= 10 && j >= 7 && j <= 11)
//					currentCells[i][j] = true;
//				else
//					currentCells[i][j] = false;
				
				//һ������
				if (i >= 7 && i <= 11 && j >= 7 && j <= 11)
					currentCells[i][j] = true;
				else
					currentCells[i][j] = false;
			}
		}
	}
	
	// ������һ��,�����������һ����ÿһ�����½�һ���̳߳�
	public void next(){
		// ����һ���ɸ�����Ҫ�������̵߳��̳߳أ���ϸ����ά������ÿһ��ϸ�������̼߳����̳߳ء�
		threadPool = Executors.newFixedThreadPool(3);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				threadPool.execute(new CellTask(currentCells, i, j));
			}
		}
		
     //�������û��ִ����ϣ����ٽ���������������������񣬾�ִ���ֱ꣬����������ִ����ϡ�
       while(!threadPool.isTerminated()) {  	   
    	   threadPool.shutdown();  
	    }  
       
       
		//��һ�ֹر��̳߳صķ���
       
//		// ����һ��˳��رգ�ִ����ǰ�ύ������
//		threadPool.shutdown();
//		// ����رա�������ʱ���ߵ�ǰ�߳��жϣ�������һ�����ȷ���֮�󣬶�������������ֱ�������������ִ�С�
//		try {
//			threadPool.awaitTermination(10, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		   
       //ȫ��ִ���겢�ҹرպ󽫸ı�״̬��nextCells�����¸���currentCells�Դ����ѭ��
	   	tempCells = currentCells;
		currentCells = nextCells;
		nextCells = tempCells;
		
		generation++;
		
	}
	
	// ��дtoString����������ά�����Գ��ַ�����ʽ���
	@Override
	public String toString() {
		// �ڼ���
		String map = "��" + generation + "��" + "\n";
		// ������
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
			// ������
			map += " " + i + "\n";
		}
		return map;
	}

	
	
			
	/**
	 * @author Song �ڲ���ϸ���߳������࣬checkNeighbor�����������Χϸ�����ŵ�ϸ������������ϸ��������run�������ж���һ��ϸ���������������nextCells[][]
	 */
	class CellTask implements Runnable {
		private boolean[][] currentCells;
		private int i, j;
		
		public CellTask(boolean[][] currentCells, int i, int j) {
			this.currentCells = currentCells;
			this.i = i;
			this.j = j;
		}
		
		// ���أ�x,y��ϸ����Χ�˸�ϸ��(i,j)�л��ŵ�ϸ������
		public int checkNeighbor(int a, int b) {
			int count = 0;
			for (int i = a - 1; i <= a + 1; i++) {
				for (int j = b - 1; j <= b + 1; j++) {
					// �ų�����0��rows��0��cols������
					if (i >= 0 && i < rows && j >= 0 && j < cols) {
						//�ų�ϸ���Լ�
						if (!(i == a && j == b) ){
							if( currentCells[i][j] == true)
								count++;
						}					
					}
				}
			}
			return count;
		}
				
		
		//�ж���һ��ϸ���������������nextCells[][]
		@Override
		public void run() {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int cellsAroundAlive = checkNeighbor(i, j);
					// ���������ϸ�����ˣ���Χϸ����3������ô��ϸ����������Χϸ��<2����>3��ô��ϸ����ȥ�������Χϸ��=2��ά��ԭ״̬
					 if (cellsAroundAlive == 3 ) {
						 nextCells[i][j] = true;
					 } 
					 else if (cellsAroundAlive == 2  ) {
						 nextCells[i][j] = currentCells[i][j];
					 }
					 else
						 nextCells[i][j] =false; 
				}
			}
		
		}
	}

}
