import java.util.Random;

public class GameMap {
	
	////コンストラクタ////
	private int[][] gameMap;
	
	//makeGameMap
	public GameMap(int x, int y, int n, int seed) {
		// TODO 自動生成されたコンストラクター・スタブ
		Random rand = new Random(seed);
		int[][] gameMap = new int[x][y];
		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[i].length; j++) {
				gameMap[i][j] = 0;
			}
		}
		gameMap[0][0] = 1;		//ポイント(プレイヤー)の位置
		//障害物のランダム生成する処理//
		for (int i = 0; i < n; ) {
			int Nx = rand.nextInt(gameMap.length);
			int Ny = rand.nextInt(gameMap[Nx].length);
			if (gameMap[Nx][Ny] == 0) {
				gameMap[Nx][Ny] = 2;
				i++;
			} 
		}
	}
	
	void show(int[][] enemyData, int[][] itemData) {
		//マップの表示をする処理//
		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[i].length; j++) {
				if (gameMap[i][j] == 0) {
					boolean isItem = false;
					for (int k = 0; k < itemData.length; k++) {
						if (itemData[k][0] == i && itemData[k][1] == j && itemData[k][3] == 1) {
							isItem = true;
							break;
						}
					}
					if (isItem) {
						System.out.print("$");
						gameMap[i][j] = 4;
					} else {
						System.out.print("-");
					}
				} else if (gameMap[i][j] == 1) {
					System.out.print("@");
				} else if (gameMap[i][j] == 2) {
					System.out.print("#");
				} else if (gameMap[i][j] == 3) {
					if (enemyData[Main.getEnemyNumber(enemyData, i, j)][5] == 1) {
						System.out.print("*");
					} else if (enemyData[Main.getEnemyNumber(enemyData, i, j)][5] == 2) {
						System.out.print("+");
					}
				} else if (gameMap[i][j] == 4) {
					System.out.print("$");
				} else {
					System.out.println("?");	//マップの要素が不明な場合
				}
			}
			System.out.println();
		}
	}
	
	void randomSetEnemy(int Ec, int seed) {
		Random rand = new Random(seed);
		//プレイヤーの位置を求める処理//
		int Px = 0;
		int Py = 0;
		searchPoint:for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[i].length; j++) {
				if (gameMap[i][j] == 1) {
					Px = i;
					Py = j;
					break searchPoint;
				}
			}
		}
		//敵をプレイヤーの周囲1マス以外に配置する処理//
		for (int i = 0; i < Ec; ) {
			int Ex = rand.nextInt(gameMap.length);
			int Ey = rand.nextInt(gameMap[Ex].length);
			if ( (Px-1<=Ex && Ex<=Px+1) && (Py-1<=Ey && Ey<=Py+1) ) {
				continue;
			} else {
				if (gameMap[Ex][Ey] == 0)  {
				gameMap[Ex][Ey] = 3;
				i++;
				}
			} 
		}
	}
	
	
}
