import java.util.Random;
import java.util.Scanner;

public class Main {
	//1番下にゲームの操作説明やソースコードの説明
		static void moveEnemyMulti (int[][] gameMap, Player player, Enemy[] enemyData, int Ec, Scanner stdIn) {
			for (int i = 0; i < Ec; i++) {
				if (enemyData[i].IsAlive()) {
					int Ex = enemyData[i].getEx();
					int Ey = enemyData[i].getEy();
					int mode = enemyData[i].getMode();
					if (mode == 2) {
						enemyChasePlayerMuiti(gameMap, player, enemyData, Ex, Ey, i, stdIn);
					}
					else if (gameMap[Ex][Ey] == 3 && mode == 1) {//敵をランダムに動かす処理
						moveEnemyRandom(gameMap, player, enemyData, Ex, Ey, i, stdIn);
					}
				}
			}
		}
		
		static boolean enemyScanPlayerMuiti (int[][] gameMap, Enemy[] enemyData, int Ec) {
			boolean hit = false;		//敵の周囲1マスにプレイヤーがいるかどうか
			
			enemyScanPlayer:for (int i = 0; i < Ec; i++) {
				if (enemyData[i].IsAlive()) {
					int Ex = enemyData[i].getEx();
					int Ey = enemyData[i].getEy();
					if (gameMap[Ex][Ey] == 3) {
						//敵の上の行を調べる処理//
						int xs = Ex-1;
						if (xs>=0) {
							for (int j = Ey-1; j <= Ey+1; j++) {
								if (j < 0 || j >= gameMap[xs].length) {
									continue;
								}
								if (gameMap[xs][j] == 1) {
									hit = true;
									break enemyScanPlayer;
								}
							}
						}
						//敵と同じ行を調べる処理//
						if (!(hit)) {
							xs = Ex; 
							if (Ey-1 >= 0) {
								if (gameMap[xs][Ey-1] == 1) {
									hit = true;
									break enemyScanPlayer;
								}
							}
							if (Ey+1 < gameMap[xs].length) {
								if (gameMap[xs][Ey+1] == 1) {
									hit = true;
									break enemyScanPlayer;
								}
							}
						}
						//敵の下の行を調べる処理//
						if (!(hit)) {
							xs = Ex+1;
							if (xs < gameMap.length) {
								for (int j = Ey-1; j <= Ey+1; j++) {
									if (j < 0 || j >= gameMap[xs].length) {
										continue;
									}
									if (gameMap[xs][j] == 1) {
										hit = true;
										break enemyScanPlayer;
									}
								}
							}
						}
					}
				}
			}
			return hit;
		}
		
		static boolean battleMode (int[][] gameMap, Player player,Enemy[] enemyData ,int Ex ,int Ey ,Scanner stdIn, boolean isAttack) {
			//Random rand = new Random();
			boolean battleWin = false;
			int En = -1;	//敵の番号
			//敵の番号を求める処理//
			En = Enemy.getEnemyNumber(enemyData, Ex, Ey);
			
			//戦闘処理//
			System.out.print("接敵! ");
			if (isAttack) {
				System.out.println("(攻撃側)");
			} else {
				System.out.println("(防衛側)");
			}
			System.out.println("-----------戦闘開始-----------");
			boolean battleLoop = true;
			battle: while (battleLoop) {
				int enemyATK = enemyData[En].getATK();
				int enemyHP = enemyData[En].getHP();
				System.out.printf("プレイヤーのHP,ATK:%d,%d\n",player.getHP(),player.getATK());
				System.out.printf("敵のHP,ATK:%d,%d\n",enemyHP,enemyATK);
				System.out.println("攻撃:A,撤退:Q");
				System.out.print(">>");
				String keyIn = stdIn.nextLine();
				
				if (keyIn.equals("A") || keyIn.equals("a")) { //攻撃時の処理
					int playerATK_R = calculateDamage(player.getATK(), 0.2);
					System.out.printf("プレイヤー ->> 敵 : %d ダメージ\n",playerATK_R);
					enemyData[En].damage(playerATK_R);
					if (enemyData[En].IsAlive()) {
						int enemyATK_R = calculateDamage(enemyATK, 0.2);
						System.out.printf("敵 ->> プレイヤー : %d ダメージ\n",enemyATK_R);
						player.damage(enemyATK_R);
					}
					
				} 
				else if (keyIn.equals("Q") || keyIn.equals("q")) {
					if (!(isAttack)) {
						System.out.println("撤退すると負けるよ？");
						System.out.println("OK: \"Y\" , continue: \"N\"");
						while (true) {
							System.out.print(">>");
							keyIn = stdIn.nextLine();
							if (keyIn.equals("y") || keyIn.equals("Y")) {
								break;
							} else if (keyIn.equals("n") || keyIn.equals("N")) {
								continue battle;
							} else {
								System.out.println("無効な入力");
								
							}
						}
						
					}
					battleLoop = false;
					System.out.println("撤退");
				}
				
				if (!(enemyData[En].IsAlive())) {
					battleLoop = false;
					enemyData[En].deth();
					gameMap[Ex][Ey] = 0;
					battleWin = true;
					System.out.println("勝利!");
					break battle;
				}
				else if (player.getHP() <= 0) {
					player.setHP(1);//要改善?
					System.out.println("敗北");
					battleLoop = false;
				}
			}
			System.out.println("-----------戦闘終了-----------");
			return battleWin;
		}		
		
		static boolean checkplayer (int[][] gameMap) {
			boolean existPleyer = false;;
			for (int i = 0; i < gameMap.length; i++) {
				for (int j = 0; j < gameMap.length; j++) {
					if (gameMap[i][j] == 1) {
						existPleyer = true;
						break;
					}
				}
			}
			return existPleyer;
		}
		
		static void enemyChasePlayerMuiti (int[][] gameMap, Player player, Enemy[] enemyData, int Ex, int Ey, int i, Scanner stdIn) {
			Random rand = new Random();
			int Px = player.getPx();
			int Py = player.getPy();
			
			////敵がプレイヤーを追跡する処理////
			int count = 0;
			boolean moveing = true;
			boolean battleWin = false;
			while (moveing) {
				if (count > 10) {
					moveEnemyRandom(gameMap, player, enemyData, Ex, Ey, i, stdIn);
					moveing = false;
					break;
				}
				//プレイヤーが敵の斜めにいる場合//
				if(Px-Ex != 0 && Py-Ey != 0) {
					int R = rand.nextInt(2);
					if (R == 0) {
						if (Px-Ex > 0 && gameMap[Ex+1][Ey] != 2 && gameMap[Ex+1][Ey] != 3) {
							if (gameMap[Ex+1][Ey] == 1) {
								battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex+1][Ey] = 3;
									enemyData[i].move(3);
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex+1][Ey] = 3;
								enemyData[i].move(3);
								moveing = false;
								break;
							}
						} else if (Px-Ex < 0 && gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
							if (gameMap[Ex-1][Ey] == 1) {
								battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex-1][Ey] = 3;
									enemyData[i].move(1);
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex-1][Ey] = 3;
								enemyData[i].move(1);
								moveing = false;
								break;
							}
						}
					} 
					else if (R == 1) {
						if (Py-Ey > 0 && gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
							if (gameMap[Ex][Ey+1] == 1) {
								battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex][Ey+1] = 3;
									enemyData[i].move(4);
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey+1] = 3;
								enemyData[i].move(4);
								moveing = false;
								break;
							}
						} else if (Py-Ey < 0 && gameMap[Ex][Ey-1] != 2 && gameMap[Ex][Ey-1] != 3) {
							if (gameMap[Ex][Ey-1] == 1) {
								battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex][Ey-1] = 3;
									enemyData[i].move(2);
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey-1] = 3;
								enemyData[i].move(2);
								moveing = false;
								break;
							}
						}
					}
				} 
				
				//プレイヤーが敵と同じ列にいる場合//
				else if (Px-Ex != 0 && Py-Ey == 0) {
					if (Px-Ex > 0 && gameMap[Ex+1][Ey] != 2 && gameMap[Ex+1][Ey] != 3) {
						if (gameMap[Ex+1][Ey] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey,  stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex+1][Ey] = 3;
								enemyData[i].move(3);
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex+1][Ey] = 3;
							enemyData[i].move(3);
							moveing = false;
							break;
						}
					} else if (Px-Ex < 0 && gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
						if (gameMap[Ex-1][Ey] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex-1][Ey] = 3;
								enemyData[i].move(1);
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex-1][Ey] = 3;
							enemyData[i].move(1);
							moveing = false;
							break;
						}
					}
				} 
				//プレイヤーが敵と同じ行にいる場合//
				else if (Px-Ex == 0 && Py-Ey != 0) {
					if (Py-Ey > 0 && gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
						if (gameMap[Ex][Ey+1] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey+1] = 3;
								enemyData[i].move(4);
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex][Ey+1] = 3;
							enemyData[i].move(4);
							moveing = false;
							break;
						}
					} else if (Py-Ey < 0 && gameMap[Ex][Ey-1] != 2 && gameMap[Ex][Ey-1] != 3) {
						if (gameMap[Ex][Ey-1] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey-1] = 3;
								enemyData[i].move(2);
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex][Ey-1] = 3;
							enemyData[i].move(2);
							moveing = false;
							break;
						}
					}
				}
			
				if (Px-Ex == 0 && Py-Ey == 0) {
					System.out.println("error");
					break;
				}
				count++;
			}
		}
		
		static void moveEnemyRandom (int[][] gameMap, Player player, Enemy[] enemyData, int Ex, int Ey, int i, Scanner stdIn) {
			Random rand = new Random();
			boolean moveing = true;
			boolean battleWin;
			
			while (moveing) {
				int moveKey = rand.nextInt(5);
				if (moveKey == 0) {
					moveing = false;
					//System.out.println("a");  //テスト用
					break;
				}
				//敵が上に動く場合//
				if (moveKey == 1) {
					if (Ex-1 >= 0) {
						if (gameMap[Ex-1][Ey] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
								moveing = false;
								break;
							}
						}
						else if (gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
							moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
							moveing = false;
							break;
						}
						
						
					}
				} 
				//敵が下に動く場合//
				else if (moveKey == 3) {
					if (Ex+1 < gameMap.length) {
						if (gameMap[Ex+1][Ey] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
								moveing = false;
								break;
							}
						}
						else if (gameMap[Ex+1][Ey] != 2 && gameMap[Ex+1][Ey] != 3) {
							moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
							moveing = false;
							break;
						}
						
					}
				}
				//敵を右に動かす場合//
				else if (moveKey == 4) {
					if (Ey+1 < gameMap[Ex].length) {
						if (gameMap[Ex][Ey+1] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
								moveing = false;
								break;
							}
						}
						else if (gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
							moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
							moveing = false;
							break;
						}
					}
				}
				//敵を左に動かす場合//
				else if (moveKey == 2) {
					if (Ey-1 >= 0) {
						if (gameMap[Ex][Ey-1] == 1) {
							battleWin = battleMode(gameMap, player, enemyData, Ex, Ey, stdIn, false);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing =false;
								break;
							} else {
								moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
								moveing = false;
								break;
							}
						}
						else if (gameMap[Ex][Ey-1]!= 2 && gameMap[Ex][Ey-1]!= 3) {
							moveEnemy(moveKey, gameMap, Ex, Ey, enemyData, i);
							moveing = false;
							break;
						}
						
					}
				}
			}
		}
		
		static int[][] randomSetItem (int[][] gameMap, int ItemCount, int seed) {
			int[][] ItemData = new int[ItemCount][4];	//{x,y,mode,A}
			Random rand = new Random(seed);
			for (int i = 0; i < ItemCount;) {
				int Ix = rand.nextInt(gameMap.length);
				int Iy = rand.nextInt(gameMap[Ix].length);
				if (gameMap[Ix][Iy] == 0) {
					gameMap[Ix][Iy] = 4;
					//
					ItemData[i][0] = Ix;
					ItemData[i][1] = Iy;
					ItemData[i][2] = 0;	//仮のモード
					ItemData[i][3] = 1; //1;使用可, 0;使用済
					//
					
					i++;
				} else {
					//System.out.println(Ix +","+Iy); //確認用
					continue;
				}
			}
			
			return ItemData;
		}
		
		static void showItemData(int[][] itemData) {
			System.out.println("-----------------");
			System.out.println("x :y :type");
			for (int i = 0; i < itemData.length; i++) {
				System.out.printf("%2d,%2d,%4d, ",itemData[i][0],itemData[i][1],itemData[i][2]);
				if (itemData[i][3] == 1) {
					System.out.println("使用可");
				} else {
					System.out.println("使用済");
				}
			}
			System.out.println("-----------------");
		}
		
		static int makeSeed(int inSeed) {
			if (inSeed == 0) {
				Random rand = new Random();
				return rand.nextInt(100001);
			}
			else {
				return inSeed;
			}
			
		}
		
		static void movePoint(int[][] gameMap,String key,Player player,Enemy[] enemyData ,Scanner stdIn, int[][] itemData) {
			//プレイヤーの位置を求める処理//
			int Px = player.getPx();
			int Py = player.getPy();
			boolean battleWin = false;
			//プレイヤーを動かす処理//
			int dx = -1; //縦方向に動く差分
			int dy = -1; //横方向に動く差分
			int moveMode = -1;
			
			if (key.equals("w") || key.equals("s")) {
				if (key.equals("w")) {
					dx = Px-1;
					moveMode = 1;
				} else if (key.equals("s")) {
					dx = Px+1;
					moveMode = 3;
				}
				if (0 <= dx && dx < gameMap.length)  {
					if (gameMap[dx][Py] == 0) {
						gameMap[Px][Py] = 0;
						gameMap[dx][Py] = 1;
						player.move(moveMode);
					} else if (gameMap[dx][Py] == 3) {
						battleWin = battleMode(gameMap, player, enemyData, dx, Py, stdIn, true);
						if (battleWin) {
							gameMap[Px][Py] = 0;
							gameMap[dx][Py] = 1;
							player.move(moveMode);
						}
					} else if (gameMap[dx][Py] == 4) {
						player.useItem(itemData, dx,Py);
						gameMap[Px][Py] = 0;
						gameMap[dx][Py] = 1;
						player.move(moveMode);
					}
				}
			}
			if (key.equals("d") || key.equals("a")) {
				if (key.equals("d")) {
					dy = Py+1; //右に移動
					moveMode = 4;
				} else if (key.equals("a")) {
					dy = Py-1; //左に移動
					moveMode = 2;
				}
				if (-1 < dy && dy < gameMap[Px].length) {
					if (gameMap[Px][dy] == 0) {
						gameMap[Px][Py] = 0;
						gameMap[Px][dy] = 1;
						player.move(moveMode);
					}else if (gameMap[Px][dy] == 3) {
						battleWin = battleMode(gameMap, player, enemyData, Px, dy, stdIn, true);
						if (battleWin) {
							gameMap[Px][Py] = 0;
							gameMap[Px][dy] = 1;
							player.move(moveMode);
						}
					} else if (gameMap[Px][dy] == 4) {
						player.useItem(itemData, Px,dy);
						gameMap[Px][Py] = 0;
						gameMap[Px][dy] = 1;
						player.move(moveMode);
					}
				}
			}
			return;
		}
		
		static void moveEnemy(int moveMode, int[][] gameMap, int Ex, int Ey, Enemy[] enemyData, int En) {
			if (moveMode == 1) {
				gameMap[Ex][Ey] = 0;
				gameMap[Ex-1][Ey] = 3;
				enemyData[En].move(moveMode);
			} else if (moveMode == 2) {
				gameMap[Ex][Ey] = 0;
				gameMap[Ex][Ey-1] = 3;
				enemyData[En].move(moveMode);
			} else if (moveMode == 3) {
				gameMap[Ex][Ey] = 0;
				gameMap[Ex+1][Ey] = 3;
				enemyData[En].move(moveMode);
			} else if (moveMode == 4) {
				gameMap[Ex][Ey] = 0;
				gameMap[Ex][Ey+1] = 3;
				enemyData[En].move(moveMode);
			} else {
				System.out.println("err:main_moveEnemy");
			}
		}
		
		static int calculateDamage (int ATK, double per) {
			Random rand = new Random();
			int F = 0;
			if (rand.nextBoolean()) {
				F = 1;
			} else {
				F = -1;
			}
			return ATK + rand.nextInt((int)(ATK*per))*F;
		}
		
		public static void main(String[] args) {
			// TODO 自動生成されたメソッド・スタブ
			Scanner stdIn = new Scanner(System.in);
			System.out.print("マップの広さ(x):");
			int x = stdIn.nextInt();
			//int x = 5;
			//System.out.println("x=5");
			System.out.print("マップの広さ(y):");
			int y = stdIn.nextInt();
			//int y = 5;
			//System.out.println("y=5");
			System.out.print("障害物(#)の数:");
			int n = stdIn.nextInt();
			//int n = 0;
			//System.out.println("n=0");
			System.out.print("敵の数:");
			int Ec = stdIn.nextInt();
			//int Ec = 2;
			//System.out.println("Ec=2");
			System.out.print("アイテム($)の数:");
			int itemCount = stdIn.nextInt();
			
			System.out.print("シード値:");
			int inSeed = stdIn.nextInt();
			
			if (x * y < n+Ec+itemCount) {
				System.out.println("error");
				return;
			}
			
			int playerHP = 100;	//プレイヤーのHP
			int playerATK = 30;	//プレイヤーの攻撃力
			Player player = new Player(playerHP, playerATK);
			
			int seed = makeSeed(inSeed);
			System.out.println("シード値: " + seed);
			
			int[][] gameMap = GameMap.make(x, y, n, seed);
			gameMap = GameMap.RandomSetEnemy(gameMap, Ec, seed);
			//GameMap GameMap = new GameMap(x, y, n, seed);	//作成途中
			
			//敵データの生成//
			Enemy[] EnemyData = Enemy.makeEnemyData(gameMap, Ec, seed);
			Enemy.showData(EnemyData);
			
			int[][] ItemData = randomSetItem(gameMap, itemCount, seed);
			
			System.out.printf("HP:%d\n",player.getHP());
			GameMap.show(gameMap,EnemyData,ItemData);
			boolean gameLoop = true;
			String space = stdIn.nextLine();
			System.out.print(">");
			String keyIn = stdIn.nextLine();
			
			////ゲームのメイン処理////
			boolean existEnemy;		//敵が存在しているかどうか
			boolean existPleyer;	//プレイヤーが存在しているかどうか
			int turn = 1;			//ターン数
			//メインループ//
			while (gameLoop) {
				
				if (keyIn.equals("q")) {
					gameLoop = false;
					break;
				}
				else if (keyIn.equals("m")) {
					Enemy.showData(EnemyData);
					keyIn = stdIn.nextLine();
				}
				else if (keyIn.equals("e")) {
					player.showPlayerData();
					keyIn = stdIn.nextLine();
				}
				else if (keyIn.equals("i")) {
					showItemData(ItemData);
					keyIn = stdIn.nextLine();
				}
				else {
					//回復＆敵・味方の移動//
					player.heal(5);
					movePoint(gameMap, keyIn, player, EnemyData, stdIn, ItemData);
					moveEnemyMulti(gameMap,player ,EnemyData, Ec, stdIn);
					
					boolean scan = enemyScanPlayerMuiti(gameMap, EnemyData, Ec);
					if (scan) {
						System.out.printf("<Turn:%d> [!] HP:%d\n",turn,player.getHP());
					} else {
						System.out.printf("<Turn:%d> HP:%d\n",turn,player.getHP());
					}
					GameMap.show(gameMap,EnemyData,ItemData);
					
					//敵の存在判定//
					existEnemy = Enemy.checkAllEnemy(EnemyData);
					if (!existEnemy && Ec > 0) {
						gameLoop = false;
						System.out.println("全ての敵を撃破!");
						break;
					}
					//プレイヤーの存在判定//
					existPleyer = checkplayer(gameMap);
					if (!existPleyer) {
						System.out.println("プレイヤーが撃破された");
						gameLoop = false;
						break;
					}
					System.out.print(">");
					keyIn = stdIn.nextLine();
					turn += 1;
					System.out.println();
				}
			}
			System.out.println("終了!");
		}
	}

/* --個人的メモ--
 * ・敵の情報の管理方法が悪い
 * 　→gameMapとenemyDataでの2重の座標管理
 *   →enemyData,playerDataが正とする
 * ・作成したメソッドは出来る限り改変しない方針、カプセル化する
 *   →普通に難しい(改変しないことが)
 * ・コードが雑
 * 
 * 
 * 
 * 
 * 
 * 
 * --------ゲーム説明--------
 *
 * --初期設定--
 * ・x,yでマップの広さを指定
 * ・障害物(#)の個数を指定
 * ・敵(*,+)の数を指定
 * ・アイテム($)の個数を指定
 * 
 * 
 * --マップでの操作・説明--
 * ・「 wasd 」でプレイヤーを動かす
 * ・「 q 」：ゲームの終了
 * ・「 m 」：全ての敵の状態を表示
 * ・「 e 」：プレイヤーの状態を表示
 * ・「 i 」：アイテムの状態を表示
 * ・敵の周囲1マスにプレイヤーが入ると、マップ上部に [!] が表示される
 * ・全ての敵を倒すと終了
 * ・マップ上部の「 <Turn:n> 」の n は現在のターン数
 * 
 * 
 * --戦闘中での操作・説明--
 * ・[ A ]で攻撃
 * ・[ Q ]で撤退
 * ・プレイヤーは攻撃側での戦闘に負けた場合、HPが1になる(死なない)
 * ・防御側での戦闘で負けたor撤退した場合、ゲームオーバー
 * 
 * 
 * --マップ上の意味と表示--
 * ・何もなし			:0 「 - 」
 * ・プレイヤー			:1 「 @ 」
 * ・壁(障害物)			:2 「 # 」
 * ・敵(ランダム移動)	:3 「 * 」
 * ・敵(追跡移動)		:3 「 + 」
 * ・アイテム			:4 「 $ 」
 * 
 * 
 * --ソースコードについて--
 * ・xが行、yが列に関する値。Eが敵、Pがプレイヤーに関する値		例)int Ex -> 敵のx座標の値
 * ・Don't二次配布、改造・参考にするのはOK
 * ・プレイヤーが復活する等のバグがある
 * ・ソースコードの「 //～～する処理//　」などは、これ以下のコードの処理の概要を説明している。(多少読みやすくなるはず)
 * 
 * ・ItemDataの形式： {
 * 						{ x(x座標), y(y座標), mode(アイテムの種類), A(使用済みかどうか) }
 * 
 * 					  }
 * 
 * ・敵が撃破された場合(E=1のとき)、敵の座標(x,y)は(-1,-1)に設定される。
 * ・敵の動作モード：0=動作しない、1=ランダム移動、2=プレイヤーを追いかける動作
 * ・敵が撃破された場合、敵の座標(x,y)は(-1,-1)に設定される。
 * ・シード値について：
 * 		マップ上の配置がシード値によって決められる．
 * 		シード値は敵の動作には影響しない．
 * 
 * 
 * --------メソットの概要・説明(雑)--------
 * ・makeGameMap			:2次元配列gameMapの作成する
 * ・showMap				:コンソールにマップを表示する，アイテムの位置の更新
 * ・movePoint				:プレイヤー(ポイント)を動かす処理、戦闘後の移動処理
 * ・RandomSetEnemy			:プレイヤーの周囲1マス以外の場所にランダムに敵を配置する処理
 * ・makeEnemyData			;敵の情報(enemyData)を作成する
 * ・moveEnemyMulti			;複数(単数)の敵を動かす処理、敵の存在の可否を確認する処理
 * ・enemyScanPlayerMuiti	;全ての存在する敵において、周囲１マスにプレイヤーがいるかBoolean型で返す
 * ・showEnemyData			:その時点でのenemyDataを表示する
 * ・makePlayerData			;playerDataを作成する処理
 * ・showPlayerData			;その時点のplayerDataを表示する処理
 * ・battleMode				;主な戦闘処理を行う、戦闘結果をBoolean型で返す、メソッドmovePointとの関連が強い
 * ・healPlayer				;プレイヤーのHPを設定した値分回復させる、上限は最初のHPの値
 * ・checkAllEnemy			;enemyDataから敵がマップに残っているかどうか調べる処理、Boolean型で返す
 * ・checkPlayer			;gameMapからプレイヤーが存在するかどうか調べる処理、Boolean型で返す
 * ・enemyChasePlayerMuiti	;moveEnemyMultiの内部メソッド。enemyChasePlayerを複数の敵に対応できるように改良した処理。
 * ・moveEnemyRandom		;単体の敵をランダムに動かす処理。
 * ・getEnemyNumber			;[内部処理] 入力された敵の座標から，敵の番号を返すメソッド．
 * ・randomSetItem			;アイテムをマップ上にランダムにセットするメソッド．int[][]アイテムデータを返す．
 * ・showItemData			;アイテムデータを表示するメソッド．
 * ・useItem				;プレイヤーがアイテムを使う処理．アイテム使用時の効果を設定できる
 * ・makeSeed				;入力値が0の場合，シード値(0～100000)を作るメソット．入力値がそれ以外は入力値を返す．
 * ・calculateDamage		:入力されたダメージを"per"分ランダムに増減して返す処理．
 */
