//import java.util.Scanner;

public class Player extends Unit {
	////コンストラクタ////
	private int Px;			//[0]
	private int Py;
	//private int hp;
	//private int ATK;
	//private int maxHp;	//[4]
	
	public Player(int PlayerHp, int PlayerATK) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(PlayerHp, PlayerATK);
		Px = 0;	//[0]
		Py = 0;	//[1]
	}
	
	public void showPlayerData() {
		System.out.printf("HP:%d\n",getHP());
		System.out.printf("ATK:%d\n",getATK());
	}
	
	public void move(int moveMode) {
		if (moveMode == 1) {
			Px -= 1;
		} else if (moveMode == 2) {
			Py -= 1;
		} else if (moveMode == 3) {
			Px += 1;
		} else if (moveMode == 4) {
			Py += 1;
		} else {
			System.out.println("err:Player_move");
		}
	}
	
	void useItem(int[][] itemData, int Ix, int Iy) {
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
				heal(10);
			}
			//
			//ここに他のアイテムの効果を追加
			//
			itemData[itemNumber][3] = 0;
		} else {
			return;
		}
	}
	
	public int getPx() {
		return Px;
	}
	
	public int getPy() {
		return Py;
	}
}


//-----memo------
/*
 * ・move処理をMainに移行
 * ・
 * ・
 * 
 */
