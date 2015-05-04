package com.lifegame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 生命游戏规则：如果本细胞死了，周围细胞都3个，那么死细胞复活；如果周围细胞<2或者>3那么活细胞死去；如果周围细胞=2，维持原状态
 * 运用线程池，每一代创建一个线程池，将每一个细胞任务（继承runnable接口）加入到线程池中，执行完全部任务后线程池shutdown
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
//				//随机产生活着的细胞
//				 double random = Math.random();
//				 if (random < 0.2) {
//				 cells[i][j] = true;
//				 } else {
//				 cells[i][j] = false;
//				 }
				
//				//一条直线
//				if (i >= 10 && i <= 10 && j >= 7 && j <= 11)
//					currentCells[i][j] = true;
//				else
//					currentCells[i][j] = false;
				
				//一个方块
				if (i >= 7 && i <= 11 && j >= 7 && j <= 11)
					currentCells[i][j] = true;
				else
					currentCells[i][j] = false;
			}
		}
	}
	
	// 产生下一代,并且算出是哪一代，每一代里新建一个线程池
	public void next(){
		// 创建一个可根据需要创建新线程的线程池，将细胞二维矩阵中每一个细胞任务线程加入线程池。
		threadPool = Executors.newFixedThreadPool(3);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				threadPool.execute(new CellTask(currentCells, i, j));
			}
		}
		
     //如果任务没有执行完毕，不再接收新任务，如果里面有任务，就执行完，直到所有任务执行完毕。
       while(!threadPool.isTerminated()) {  	   
    	   threadPool.shutdown();  
	    }  
       
       
		//另一种关闭线程池的方法
       
//		// 启动一次顺序关闭，执行以前提交的任务
//		threadPool.shutdown();
//		// 请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。
//		try {
//			threadPool.awaitTermination(10, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		   
       //全部执行完并且关闭后将改变状态的nextCells再重新赋给currentCells以此完成循环
	   	tempCells = currentCells;
		currentCells = nextCells;
		nextCells = tempCells;
		
		generation++;
		
	}
	
	// 重写toString方法，将二维数组以成字符串形式输出
	@Override
	public String toString() {
		// 第几代
		String map = "第" + generation + "代" + "\n";
		// 纵坐标
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
			// 横坐标
			map += " " + i + "\n";
		}
		return map;
	}

	
	
			
	/**
	 * @author Song 内部类细胞线程任务类，checkNeighbor方法，算出周围细胞活着的细胞个数，返回细胞个数；run方法中判断下一代细胞生死情况，赋给nextCells[][]
	 */
	class CellTask implements Runnable {
		private boolean[][] currentCells;
		private int i, j;
		
		public CellTask(boolean[][] currentCells, int i, int j) {
			this.currentCells = currentCells;
			this.i = i;
			this.j = j;
		}
		
		// 返回（x,y）细胞周围八个细胞(i,j)中活着的细胞数量
		public int checkNeighbor(int a, int b) {
			int count = 0;
			for (int i = a - 1; i <= a + 1; i++) {
				for (int j = b - 1; j <= b + 1; j++) {
					// 排除超过0到rows和0到cols的坐标
					if (i >= 0 && i < rows && j >= 0 && j < cols) {
						//排除细胞自己
						if (!(i == a && j == b) ){
							if( currentCells[i][j] == true)
								count++;
						}					
					}
				}
			}
			return count;
		}
				
		
		//判断下一代细胞生死情况，赋给nextCells[][]
		@Override
		public void run() {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int cellsAroundAlive = checkNeighbor(i, j);
					// 规则：如果本细胞死了，周围细胞都3个，那么死细胞复活；如果周围细胞<2或者>3那么活细胞死去；如果周围细胞=2，维持原状态
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
