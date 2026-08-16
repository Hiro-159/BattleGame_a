import java.util.Random;

public class Enemy extends Unit {
	////コンストラクタ////
	private int Ex;
	private int Ey;
	private boolean isAlive;
	//int hp
	//int ATK
	//int maxHp
	private int mode;
	
	public Enemy(int Ex, int Ey, int EnemyHp, int EnemyATK, int mode) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(EnemyHp,EnemyATK);
		this.Ex = Ex;
		this.Ey = Ey;
		isAlive = true;
		this.mode = mode;
	}
	
	public void move(int moveMode) {
		if (moveMode == 1) {
			Ex -= 1;
		} else if (moveMode == 2) {
			Ey -= 1;
		} else if (moveMode == 3) {
			Ex += 1;
		} else if (moveMode == 4) {
			Ey += 1;
		} else {
			System.out.println("err:Enemy_move");
		}
	}
	//moveMode: up=1,left=2,down=3,right=4
	
	@Override
	public void damage(int n) {
		super.damage(n);
		if (getHP() == 0) {
			isAlive = false;
		}
	}
	
	public void deth() {
		Ex = -1;
		Ey = -1;
		isAlive = false;
	}
	
	public int getEx() {
		return Ex;
	}
	public int getEy() {
		return Ey;
	}
	
	public int getMode() {
		return mode;
	}
	
	public boolean IsAlive() {
		return isAlive;
	}
	
	public static void showData(Enemy[] enemyData) {
		System.out.println("-----------敵の情報------------");
		System.out.println("        | x, y, e,HP,ATK,mode ");
		for (int i = 0; i < enemyData.length; i++) {
			System.out.printf("敵No.%2d |%2d,%2d,",i,enemyData[i].getEx(),enemyData[i].getEy());
			if (enemyData[i].isAlive) {
				System.out.print("○,");
			} else {
				System.out.print("×,");
			}
			System.out.printf("%2d,%3d",enemyData[i].getHP(),enemyData[i].getATK());
			if(enemyData[i].mode == 0) {
				System.out.print(",待機");
			} else if (enemyData[i].mode == 1) {
				System.out.print(",移動");
			} else if (enemyData[i].mode == 2) {
				System.out.print(",追跡");
			} else {
				System.out.printf("%2d",enemyData[i].mode);
			}
			System.out.println();
		}
	}
	
	public static boolean checkAllEnemy (Enemy[] enemyData) {
		boolean existEnemy = false;
		for (int i = 0; i < enemyData.length; i++) {
			if (enemyData[i].isAlive) {
				existEnemy = true;
				break;
			}
		}
		return existEnemy;
	}
	
	//仮
	public static Enemy[] makeEnemyData(int[][] gameMap, int Ec, int seed) {
		Random rand = new Random(seed);
		Enemy[] EnemyData = new Enemy[Ec];
		int D = 0;
		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j <gameMap[i].length; j++) {
				if (gameMap[i][j] == 3) {
					EnemyData[D] = new Enemy(i, j, 80, 10, rand.nextInt(2)+1);
					D++;
				}
			}
		}
		
		return EnemyData;
	}
	
	static int getEnemyNumber (Enemy[] enemyData ,int Ex ,int Ey) {
		for (int i = 0; i < enemyData.length; i++) {
			if (enemyData[i].Ex == Ex && enemyData[i].Ey == Ey) {
				return i;
			}
		}
		System.out.println("err:enemyNumber");
		return -1;
	}
	
}










