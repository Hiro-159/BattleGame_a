
//------メモ------//
/*・各種getter．setter
 *・damageメソッド
 *・healメソッド
 * 
 */


public class Unit {
	////コンストラクタ////
	private int Ux;			//[0]
	private int Uy;
	private int hp;
	private int ATK;
	private int maxHp;		//[4]
	
	public Unit(int hp, int ATK) {
		// TODO 自動生成されたコンストラクター・スタブ
		maxHp = hp;
		this.hp = maxHp;
		this.ATK = ATK;
	}
	
	void showData() {
		
	}
	
	public void move() {
		
	}
	
	public void heal (int healHP) {
		if (hp + healHP >= maxHp) {
			hp = maxHp;
		} else {
			hp += healHP;
		}
	}
	
	public void damage(int n) {
		hp -= n;
		if (hp < 0) {
			hp = 0;
		}
	}
	
	public int getHP() {
		return hp;
	}
	
	public void setHP(int n) {
		hp = n;
	}
	
	public int getUx() {
		return Ux;
	}
	
	public int getUy() {
		return Uy;
	}
	
	public int getATK() {
		return ATK;
	}
}
