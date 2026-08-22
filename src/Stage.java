
public class Stage {
	//フィールド//
	private Player player;
	private int[][] gameMap;
	private Enemy[] enemyData;
	private int[][] itemData;
	static String[] stageName = {"t"}; //あえてnotPrivate
	
	public Stage(String stageKey) {
		// TODO 自動生成されたコンストラクター・スタブ
		if (stageKey.equals("t")) {
			player = new Player(100, 30);
			enemyData = new Enemy[4];
			enemyData[0] = new Enemy(2, 2, 100, 10, 1);
			enemyData[1] = new Enemy(4, 4, 200, 20, 1);
			enemyData[2] = new Enemy(5, 2, 100, 10, 2);
			enemyData[3] = new Enemy(2, 5, 100, 10, 2);
			int[][]gameMap = {
					{1,0,0,0,0,0},
					{0,0,2,0,0,0},
					{0,2,3,0,0,3},
					{0,0,0,0,2,0},
					{0,0,0,2,3,0},
					{0,0,3,0,0,0}
			};
			this.gameMap = gameMap;
			this.itemData = Main.randomSetItem(gameMap, 5, 112);
		} 
		/*
		else if (false) {
			
			//ここに新しい固定ステージ
			
		}
		*/
		else {
			System.out.println("err:stageSelect");
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Enemy[] getEnemyData() {
		return enemyData;
	}
	
	public int[][] getGameMap() {
		return gameMap;
	}
	
	public int[][] getItemData() {
		return itemData;
	}
	
	public static boolean checkStageKey(String stageKey) {
		for (int i = 0; i < stageName.length; i++) {
			if (stageName[i].equals(stageKey)) {
				return true;
			}
		}
		return false;
	}
}
