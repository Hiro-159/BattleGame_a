import java.util.Scanner;

public class Player {
	////コンストラクタ////
	private int Px;			//[0]
	private int Py;
	private int hp;
	private int ATK;
	private int maxHp;		//[4]
	
	public Player(int PlayerHp, int PlayerATK) {
		// TODO 自動生成されたコンストラクター・スタブ
		Px = 0;				//[0]
		Py = 0;				//[1]
		maxHp = PlayerHp;	//[4]
		hp = maxHp;			//[2]
		ATK = PlayerATK;	//[3]
	}
	
	public void showPlayerData() {
		System.out.printf("HP:%d\n",hp);
		System.out.printf("ATK:%d\n",ATK);
	}
	
	public void movePoint(int[][] gameMap, String key, int[][] enemyData, Scanner stdIn, int[][] itemData) {
		//int[] playerData;//仮のデータ
		boolean battleWin = false;
		//プレイヤーを動かす処理//
		int dx = -1; //縦方向に動く差分
		int dy = -1; //横方向に動く差分
		if (key.equals("w") || key.equals("s")) {
			if (key.equals("w")) {
				dx = Px-1;
			} else if (key.equals("s")) {
				dx = Px+1;
			}
			if (dx >= 0 && gameMap[dx][Py] == 3) {
				//System.out.println("接敵!");
				battleWin = battleMode(gameMap, enemyData, dx, Py, battleWin, stdIn);
				if (battleWin) {
					//System.out.println("勝利!");
					gameMap[Px][Py] = 0;
					gameMap[dx][Py] = 1;
					Px = dx;
				}
			}
			else if (dx >= 0 && gameMap[dx][Py] == 0) {
				gameMap[Px][Py] = 0;
				gameMap[dx][Py] = 1;
				Px = dx;
			} else if (dx >= 0 && gameMap[dx][Py] == 4) {
				useItem(itemData, dx,Py);
				gameMap[Px][Py] = 0;
				gameMap[dx][Py] = 1;
				Px = dx;
			}
		}
		if (key.equals("d") || key.equals("a")) {
			if (key.equals("d")) {
				dy = Py+1; //右に移動
			} else if (key.equals("a")) {
				dy = Py-1; //左に移動
			}
			if (dy < gameMap[Px].length && gameMap[Px][dy] == 3) {
				//System.out.println("接敵!");
				battleWin = battleMode(gameMap, enemyData, Px, dy, battleWin, stdIn);
				if (battleWin) {
					//System.out.println("勝利!");
					gameMap[Px][Py] = 0;
					gameMap[Px][dy] = 1;
					Py = dy;
				}
			}
			else if (dy < gameMap[Px].length && gameMap[Px][dy] == 0) {
				gameMap[Px][Py] = 0;
				gameMap[Px][dy] = 1;
				Py = dy;
			}
			else if (dy < gameMap[Px].length && gameMap[Px][dy] == 4) {
				useItem(itemData, Px,dy);
				gameMap[Px][Py] = 0;
				gameMap[Px][dy] = 1;
				Py = dy;
			}
		}
		/*
		if (key.equals("w")) {
			if (Px-1 >= 0 && gameMap[Px-1][Py] == 3) {
				//System.out.println("接敵!");
				battleWin = battleMode(gameMap, enemyData, Px-1, Py, battleWin, stdIn);
				if (battleWin) {
					//System.out.println("勝利!");
					gameMap[Px][Py] = 0;
					gameMap[Px-1][Py] = 1;
					Px = Px-1;
				}
			}
			else if (Px-1 >= 0 && gameMap[Px-1][Py] == 0) {
				gameMap[Px][Py] = 0;
				gameMap[Px-1][Py] = 1;
				Px = Px-1;
			} else if (Px-1 >= 0 && gameMap[Px-1][Py] == 4) {
				useItem(itemData, Px-1,Py);
				gameMap[Px][Py] = 0;
				gameMap[Px-1][Py] = 1;
				Px = Px-1;
			}
		} else if (key.equals("s")) {
			if (Px+1 < gameMap.length && gameMap[Px+1][Py] == 3) {
				//System.out.println("接敵!");
				battleWin = battleMode(gameMap, enemyData,Px+1,Py ,battleWin, stdIn);
				if (battleWin) {
					//System.out.println("勝利!");
					gameMap[Px][Py] = 0;
					gameMap[Px+1][Py] = 1;
					Px = Px+1;
				}
			}
			else if (Px+1 < gameMap.length && gameMap[Px+1][Py] == 0) {
				gameMap[Px][Py] = 0;
				gameMap[Px+1][Py] = 1;
				Px = Px+1;
			} 
			else if (Px+1 < gameMap.length && gameMap[Px+1][Py] == 4) {
				useItem(itemData, Px+1,Py);
				gameMap[Px][Py] = 0;
				gameMap[Px+1][Py] = 1;
				Px = Px+1;
			}
		} else if (key.equals("d")) {
			if (Py+1 < gameMap[Px].length && gameMap[Px][Py+1] == 3) {
				//System.out.println("接敵!");
				battleWin = battleMode(gameMap, enemyData, Px, Py+1, battleWin, stdIn);
				if (battleWin) {
					//System.out.println("勝利!");
					gameMap[Px][Py] = 0;
					gameMap[Px][Py+1] = 1;
					Py = Py+1;
				}
			}
			else if (Py+1 < gameMap[Px].length && gameMap[Px][Py+1] == 0) {
				gameMap[Px][Py] = 0;
				gameMap[Px][Py+1] = 1;
				Py = Py+1;
			}
			else if (Py+1 < gameMap[Px].length && gameMap[Px][Py+1] == 4) {
				useItem(itemData, Px,Py+1);
				gameMap[Px][Py] = 0;
				gameMap[Px][Py+1] = 1;
				Py = Py+1;
			}
		} else if (key.equals("a")) {
			if (Py-1 >= 0 && gameMap[Px][Py-1] == 3) {
				//System.out.println("接敵!");
				battleWin = battleMode(gameMap, enemyData,Px,Py-1,battleWin, stdIn);
				if (battleWin) {
					//System.out.println("勝利!");
					gameMap[Px][Py] = 0;
					gameMap[Px][Py-1] = 1;
					Py = Py-1;
				}
			}
			else if (Py-1 >= 0 && gameMap[Px][Py-1] == 0) {
				gameMap[Px][Py] = 0;
				gameMap[Px][Py-1] = 1;
				Py = Py-1;
			}
			else if (Py-1 >= 0 && gameMap[Px][Py-1] == 4) {
				useItem(itemData, Px,Py-1);
				gameMap[Px][Py] = 0;
				gameMap[Px][Py-1] = 1;
				Py = Py-1;
			}
		}
		*/
		return;
	}
	
	public void healPlayer(int healHP) {
		if (hp + healHP >= maxHp) {
			hp = maxHp;
		} else {
			hp += healHP;
		}
	}
	
	public void damage(int n) {
		hp -= n;
	}
	
	private void useItem(int[][] itemData, int Ix, int Iy) {
		//アイテムの番号を求める//
		int itemNumber = -1;
		for (int i = 0; i < itemData.length; i++) {
			if (itemData[i][0] == Ix && itemData[i][1] == Iy) {
				itemNumber = i;
				break;
			}
		}
		//アイテムを使用する//
		if (itemData[itemNumber][3] == 1) {
			if (itemData[itemNumber][2] == 0) {
				//healPlayer(playerData, 10);
				healPlayer(10);
			}
			//
			//ここに他のアイテムの効果を追加
			//
			itemData[itemNumber][3] = 0;
		} else {
			return;
		}
	}
	
	private boolean battleMode (int[][] gameMap, int[][] enemyData ,int Ex ,int Ey ,boolean battleWin ,Scanner stdIn) {
		int En = -1;		//敵の番号
		//敵の番号を求める処理//
		for (int i = 0; i < enemyData.length; i++) {
			if (Ex == enemyData[i][0] && Ey == enemyData[i][1]) {
				En = i;
				break;
			}
		}
		//戦闘処理//
		System.out.println("接敵!");
		boolean battleLoop = true;
		battle: while (battleLoop) {
			System.out.printf("プレイヤーのHP,ATK:%d,%d\n",hp,ATK);
			System.out.printf("敵のHP,ATK:%d,%d\n",enemyData[En][3],enemyData[En][4]);
			System.out.println("攻撃:A,撤退:Q");
			String keyIn = stdIn.nextLine();
			
			if (keyIn.equals("A") || keyIn.equals("a")) {
				enemyData[En][3] -= ATK;
				hp -= enemyData[En][4];
			} 
			else if (keyIn.equals("Q") || keyIn.equals("q")) {
				battleLoop = false;
				System.out.println("撤退");
			}
			
			if (enemyData[En][3] <= 0) {
				battleLoop = false;
				enemyData[En][0] = -1;
				enemyData[En][1] = -1;
				enemyData[En][2] = 0;
				gameMap[Ex][Ey] = 0;
				battleWin = true;
				System.out.println("勝利!");
				break battle;
			}
			else if (hp <= 0) {
				hp = 1;//要改善?
				System.out.println("敗北");
				battleLoop = false;
			}
		}
		
		return battleWin;
	}
	
	public int getHP() {
		return hp;
	}
	
	public void setHP(int n) {
		hp = n;
	}
	
	public int getPx() {
		return Px;
	}
	
	public int getPy() {
		return Py;
	}
	
	public int getATK() {
		return ATK;
	}
	
	
}
