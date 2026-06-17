import java.util.Random;
import java.util.Scanner;

public class Main {
	//1番下にゲームの操作説明やソースコードの説明
		static int[][] makeGameMap(int x,int y,int n) {
			//マップを生成する処理//
			Random rand = new Random();
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
			//敵の生成する処理//
			/*
			for (int i = 0; i < 1; ) {
				int Ex = rand.nextInt(gameMap.length);
				int Ey = rand.nextInt(gameMap[Ex].length);
				if (gameMap[Ex][Ey] == 0) {
					gameMap[Ex][Ey] = 3;
					i++;
				} 
			}
			*/
			return gameMap;
		}
		
		static void showMap(int[][] map ,int[][] enemyData) {
			//マップの表示をする処理//
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j] == 0) {
						System.out.print("-");
					} else if (map[i][j] == 1) {
						System.out.print("@");
					} else if (map[i][j] == 2) {
						System.out.print("#");
					} else if (map[i][j] == 3) {
						if (enemyData[getEnemyNumber(enemyData, i, j)][5] == 1) {
							System.out.print("*");
						} else if (enemyData[getEnemyNumber(enemyData, i, j)][5] == 2) {
							System.out.print("+");
						}
					} else if (map[i][j] == 4) {
						System.out.print("$");
					} else {
						System.out.println("?");	//マップの要素が不明な場合
					}
				}
				System.out.println();
			}
		}
		
		static void movePoint(int[][] gameMap,String key,int[] playerData ,int[][] enemyData ,Scanner stdIn) {
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
			boolean battleWin = false;
			//プレイヤーを動かす処理//
			if (key.equals("w")) {
				if (Px-1 >= 0 && gameMap[Px-1][Py] == 3) {
					//System.out.println("接敵!");
					battleWin = battleMode(gameMap, playerData, enemyData, Px-1, Py, battleWin, stdIn);
					if (battleWin) {
						//System.out.println("勝利!");
						gameMap[Px][Py] = 0;
						gameMap[Px-1][Py] = 1;
						playerData[0] = Px-1;
					}
				}
				else if (Px-1 >= 0 && gameMap[Px-1][Py] == 0) {
					gameMap[Px][Py] = 0;
					gameMap[Px-1][Py] = 1;
					playerData[0] = Px-1;
				} else if (Px-1 >= 0 && gameMap[Px-1][Py] == 4) {
					healPlayer(playerData, 15);
					gameMap[Px][Py] = 0;
					gameMap[Px-1][Py] = 1;
					playerData[0] = Px-1;
				}
			} else if (key.equals("s")) {
				if (Px+1 < gameMap.length && gameMap[Px+1][Py] == 3) {
					//System.out.println("接敵!");
					battleWin = battleMode(gameMap, playerData, enemyData,Px+1,Py ,battleWin, stdIn);
					if (battleWin) {
						//System.out.println("勝利!");
						gameMap[Px][Py] = 0;
						gameMap[Px+1][Py] = 1;
						playerData[0] = Px+1;
					}
				}
				else if (Px+1 < gameMap.length && gameMap[Px+1][Py] == 0) {
					gameMap[Px][Py] = 0;
					gameMap[Px+1][Py] = 1;
					playerData[0] = Px+1;
				} 
				else if (Px+1 < gameMap.length && gameMap[Px+1][Py] == 4) {
					healPlayer(playerData, 15);
					gameMap[Px][Py] = 0;
					gameMap[Px+1][Py] = 1;
					playerData[0] = Px+1;
				}
			} else if (key.equals("d")) {
				if (Py+1 < gameMap[Px].length && gameMap[Px][Py+1] == 3) {
					//System.out.println("接敵!");
					battleWin = battleMode(gameMap, playerData, enemyData, Px, Py+1, battleWin, stdIn);
					if (battleWin) {
						//System.out.println("勝利!");
						gameMap[Px][Py] = 0;
						gameMap[Px][Py+1] = 1;
						playerData[1] = Py+1;
					}
				}
				else if (Py+1 < gameMap[Px].length && gameMap[Px][Py+1] == 0) {
					gameMap[Px][Py] = 0;
					gameMap[Px][Py+1] = 1;
					playerData[1] = Py+1;
				}
				else if (Py+1 < gameMap[Px].length && gameMap[Px][Py+1] == 4) {
					healPlayer(playerData, 15);
					gameMap[Px][Py] = 0;
					gameMap[Px][Py+1] = 1;
					playerData[1] = Py+1;
				}
			} else if (key.equals("a")) {
				if (Py-1 >= 0 && gameMap[Px][Py-1] == 3) {
					//System.out.println("接敵!");
					battleWin = battleMode(gameMap, playerData, enemyData,Px,Py-1,battleWin, stdIn);
					if (battleWin) {
						//System.out.println("勝利!");
						gameMap[Px][Py] = 0;
						gameMap[Px][Py-1] = 1;
						playerData[1] = Py-1;
					}
				}
				else if (Py-1 >= 0 && gameMap[Px][Py-1] == 0) {
					gameMap[Px][Py] = 0;
					gameMap[Px][Py-1] = 1;
					playerData[1] = Py-1;
				}
				else if (Py-1 >= 0 && gameMap[Px][Py-1] == 4) {
					healPlayer(playerData, 15);
					gameMap[Px][Py] = 0;
					gameMap[Px][Py-1] = 1;
					playerData[1] = Py-1;
				}
			}
			
			return;
		}

		static int[][] RandomSetEnemy (int[][] gameMap,int Ec) {
			Random rand = new Random();
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
			return gameMap;
		}
		
		static int[][] makeEnemyData (int[][] gameMap,int Ec) {
			Random rand = new Random();
			int[][] enemyData = new int[Ec][6];		//{Ex,Ey,E,HP,ATK,M} * 敵の数
			int D = 0;
			for (int i = 0; i < gameMap.length; i++) {
				for (int j = 0; j < gameMap[i].length; j++) {
					if (gameMap[i][j] == 3) {
						enemyData[D][0] = i;
						enemyData[D][1] = j;
						enemyData[D][2] = 1;
						enemyData[D][3] = 80;
						enemyData[D][4] = 10;
						int modeRoll = rand.nextInt(2) + 1;
						enemyData[D][5] = modeRoll;
						D++;
					}
				}
			}
			return enemyData;
		}
		
		static void moveEnemyMulti (int[][] gameMap, int[] playerData, int[][] enemyData, int Ec, Scanner stdIn) {
			Random rand = new Random();
			for (int i = 0; i < Ec; i++) {
				int Ex = enemyData[i][0];
				int Ey = enemyData[i][1];
				if (enemyData[i][2] == 1 && enemyData[i][5] == 2) {
					enemyChasePlayerMuiti(gameMap, playerData, enemyData, Ex, Ey, i, stdIn);
				}
				else if (enemyData[i][2] == 1 && gameMap[Ex][Ey] == 3) {
					boolean moveing = true;		//移動したかどうか
					boolean battleWin = false;	//プレイヤーが戦闘に勝利したかどうか
					////敵をランダムに動かす処理////
					while (moveing) {
						int moveKey = rand.nextInt(5);
						if (moveKey == 0) {
							moveing = false;
						}
						//敵が上に動く場合//
						if (moveKey == 1) {
							if (Ex-1 >= 0 && gameMap[Ex-1][Ey] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex-1][Ey] = 3;
									enemyData[i][0] = Ex-1;
									moveing = false;
									break;
								}
							}
							else if (Ex-1 >= 0 && gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex-1][Ey] = 3;
								enemyData[i][0] = Ex-1;
								moveing = false;
								break;
							}
						} 
						//敵が下に動く場合//
						else if (moveKey == 2) {
							if (Ex+1 < gameMap.length && gameMap[Ex+1][Ey] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex+1][Ey] = 3;
									enemyData[i][0] = Ex+1;
									moveing = false;
									break;
								}
							}
							else if (Ex+1 < gameMap.length && gameMap[Ex+1][Ey] != 2 && gameMap[Ex+1][Ey] != 3) {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex+1][Ey] = 3;
								enemyData[i][0] = Ex+1;
								moveing = false;
								break;
							}
						}
						//敵を右に動かす場合//
						else if (moveKey == 3) {
							if (Ey+1 < gameMap[Ex].length && gameMap[Ex][Ey+1] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex][Ey+1] = 3;
									enemyData[i][1] = Ey+1;
									moveing = false;
									break;
								}
							}
							else if (Ey+1 < gameMap[Ex].length && gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey+1] = 3;
								enemyData[i][1] = Ey+1;
								moveing = false;
								break;
								
							}
						}
						//敵を左に動かす場合//
						else if (moveKey == 4) {
							if (Ey-1 >= 0 && gameMap[Ex][Ey-1] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing =false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex][Ey-1] = 3;
									enemyData[i][1] = Ey-1;
									moveing = false;
									break;
								}
							}
							else if (Ey-1 >= 0 && gameMap[Ex][Ey-1]!= 2 && gameMap[Ex][Ey-1]!= 3) {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey-1] = 3;
								enemyData[i][1] = Ey-1;
								moveing = false;
								break;
							}
						}
					}
				}
			}
		}
		
		static boolean enemyScanPlayerMuiti (int[][] gameMap, int[][] enemyData, int Ec) {
			boolean hit = false;		//敵の周囲1マスにプレイヤーがいるかどうか
			
			enemyScanPlayer:for (int i = 0; i < Ec; i++) {
				if (enemyData[i][2] == 1) {
					int Ex = enemyData[i][0];
					int Ey = enemyData[i][1];
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
					} else {
						enemyData[i][2] = 0;
					}
				}
			}
			return hit;
		}
		
		static void showEnemyData (int[][] enemyData) {
			//enemyDataを表示する処理//
			System.out.println("-----------敵の情報------------");
			System.out.println("        | x, y, e,HP,ATK,mode ");
			for (int i = 0; i < enemyData.length; i++) {
				for (int j = 0; j < enemyData[i].length; j++) {
					if (j == 0) {
						System.out.printf("敵No.%2d |%2d",i,enemyData[i][j]);
					}
					else if (j == 4) {
						System.out.printf(",%3d",enemyData[i][j]);
					} else if (j == 5){
						if(enemyData[i][j] == 0) {
							System.out.print(",待機");
						} else if (enemyData[i][j] == 1) {
							System.out.print(",移動");
						} else if (enemyData[i][j] == 2) {
							System.out.print(",追跡");
						} else {
							System.out.printf("%2d",enemyData[i][j]);
						}
					}
					else {
						System.out.printf(",%2d",enemyData[i][j]);
					}
				}
				System.out.println();
			}
			System.out.println("-------------------------------");
		}
		
		static int[] makePlayerData (int HP ,int ATK) {
			int[] playerData = new int[5];
			playerData[0] = 0;
			playerData[1] = 0;
			playerData[2] = HP;		//実際のHP
			playerData[3] = ATK;
			playerData[4] = HP;		//maxHP
			return playerData;
		}
		
		static void showPlayerData (int[] playerData) {
			System.out.printf("HP:%d\n",playerData[2]);
			System.out.printf("ATK:%d\n",playerData[3]);
		}
		
		static boolean battleMode (int[][] gameMap, int[] playerData ,int[][] enemyData ,int Ex ,int Ey ,boolean battleWin ,Scanner stdIn) {
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
				System.out.printf("プレイヤーのHP,ATK:%d,%d\n",playerData[2],playerData[3]);
				System.out.printf("敵のHP,ATK:%d,%d\n",enemyData[En][3],enemyData[En][4]);
				System.out.println("攻撃:A,撤退:Q");
				String keyIn = stdIn.nextLine();
				
				if (keyIn.equals("A") || keyIn.equals("a")) {
					enemyData[En][3] -= playerData[3];
					playerData[2] -= enemyData[En][4];
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
				else if (playerData[2] <= 0) {
					playerData[2] = 1;
					System.out.println("敗北");
					battleLoop = false;
				}
			}
			
			return battleWin;
		}
		
		static void healPlayer (int[] playerData, int healHP) {
			if (playerData[2] + healHP >= playerData[4]) {
				playerData[2] = playerData[4];
			} else {
				playerData[2] += healHP;
			}
		}
		
		static boolean checkAllEnemy (int[][] enemyData) {
			boolean existEnemy = false;
			for (int i = 0; i < enemyData.length; i++) {
				if (enemyData[i][2] == 1) {
					existEnemy = true;
				}
			}
			return existEnemy;
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
		
		static void enemyChasePlayerMuiti (int[][] gameMap, int[] playerData, int[][] enemyData, int Ex, int Ey, int i, Scanner stdIn) {
			Random rand = new Random();
			int Px = playerData[0];
			int Py = playerData[1];
			
			////敵がプレイヤーを追跡する処理////
			int count = 0;
			boolean moveing = true;
			boolean battleWin = false;
			while (moveing) {
				if (count > 10) {
					onlyMoveOneEnemyRandom(gameMap, playerData, enemyData, Ex, Ey, i);
					moveing = false;
					break;
				}
				//プレイヤーが敵の斜めにいる場合//
				if(Px-Ex != 0 && Py-Ey != 0) {
					int R = rand.nextInt(2);
					if (R == 0) {
						if (Px-Ex > 0 && gameMap[Ex+1][Ey] != 2 && gameMap[Ex+1][Ey] != 3) {
							if (gameMap[Ex+1][Ey] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex+1][Ey] = 3;
									enemyData[i][0] = Ex+1;
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex+1][Ey] = 3;
								enemyData[i][0] = Ex+1;
								moveing = false;
								break;
							}
						} else if (Px-Ex < 0 && gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
							if (gameMap[Ex-1][Ey] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex-1][Ey] = 3;
									enemyData[i][0] = Ex-1;
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex-1][Ey] = 3;
								enemyData[i][0] = Ex-1;
								moveing = false;
								break;
							}
						}
					} 
					else if (R == 1) {
						if (Py-Ey > 0 && gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
							if (gameMap[Ex][Ey+1] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex][Ey+1] = 3;
									enemyData[i][1] = Ey+1;
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey+1] = 3;
								enemyData[i][1] = Ey+1;
								moveing = false;
								break;
							}
						} else if (Py-Ey < 0 && gameMap[Ex][Ey-1] != 2 && gameMap[Ex][Ey-1] != 3) {
							if (gameMap[Ex][Ey-1] == 1) {
								battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
								if (battleWin) {
									gameMap[Ex][Ey] = 0;
									moveing = false;
									break;
								} else {
									gameMap[Ex][Ey] = 0;
									gameMap[Ex][Ey-1] = 3;
									enemyData[i][1] = Ey-1;
									moveing = false;
									break;
								}
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey-1] = 3;
								enemyData[i][1] = Ey-1;
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
							battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex+1][Ey] = 3;
								enemyData[i][0] = Ex+1;
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex+1][Ey] = 3;
							enemyData[i][0] = Ex+1;
							moveing = false;
							break;
						}
					} else if (Px-Ex < 0 && gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
						if (gameMap[Ex-1][Ey] == 1) {
							battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex-1][Ey] = 3;
								enemyData[i][0] = Ex-1;
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex-1][Ey] = 3;
							enemyData[i][0] = Ex-1;
							moveing = false;
							break;
						}
					}
				} 
				//プレイヤーが敵と同じ行にいる場合//
				else if (Px-Ex == 0 && Py-Ey != 0) {
					if (Py-Ey > 0 && gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
						if (gameMap[Ex][Ey+1] == 1) {
							battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey+1] = 3;
								enemyData[i][1] = Ey+1;
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex][Ey+1] = 3;
							enemyData[i][1] = Ey+1;
							moveing = false;
							break;
						}
					} else if (Py-Ey < 0 && gameMap[Ex][Ey-1] != 2 && gameMap[Ex][Ey-1] != 3) {
						if (gameMap[Ex][Ey-1] == 1) {
							battleWin = battleMode(gameMap, playerData, enemyData, Ex, Ey, battleWin, stdIn);
							if (battleWin) {
								gameMap[Ex][Ey] = 0;
								moveing = false;
								break;
							} else {
								gameMap[Ex][Ey] = 0;
								gameMap[Ex][Ey-1] = 3;
								enemyData[i][1] = Ey-1;
								moveing = false;
								break;
							}
						} else {
							gameMap[Ex][Ey] = 0;
							gameMap[Ex][Ey-1] = 3;
							enemyData[i][1] = Ey-1;
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
		
		static void onlyMoveOneEnemyRandom (int[][] gameMap, int[] playerData, int[][] enemyData, int Ex, int Ey, int i) {
			Random rand = new Random();
			boolean moveing = true;
			
			while (moveing) {
				int moveKey = rand.nextInt(5);
				if (moveKey == 0) {
					moveing = false;
					//System.out.println("a");  //テスト用
					break;
				}
				//敵が上に動く場合//
				if (moveKey == 1) {
					if (Ex-1 >= 0 && gameMap[Ex-1][Ey] != 2 && gameMap[Ex-1][Ey] != 3) {
						gameMap[Ex][Ey] = 0;
						gameMap[Ex-1][Ey] = 3;
						enemyData[i][0] = Ex-1;
						moveing = false;
						break;
					}
				} 
				//敵が下に動く場合//
				else if (moveKey == 2) {
					if (Ex+1 < gameMap.length && gameMap[Ex+1][Ey] != 2 && gameMap[Ex+1][Ey] != 3) {
						gameMap[Ex][Ey] = 0;
						gameMap[Ex+1][Ey] = 3;
						enemyData[i][0] = Ex+1;
						moveing = false;
						break;
					}
				}
				//敵を右に動かす場合//
				else if (moveKey == 3) {
					if (Ey+1 < gameMap[Ex].length && gameMap[Ex][Ey+1] != 2 && gameMap[Ex][Ey+1] != 3) {
						gameMap[Ex][Ey] = 0;
						gameMap[Ex][Ey+1] = 3;
						enemyData[i][1] = Ey+1;
						moveing = false;
						break;
						
					}
				}
				//敵を左に動かす場合//
				else if (moveKey == 4) {
					if (Ey-1 >= 0 && gameMap[Ex][Ey-1]!= 2 && gameMap[Ex][Ey-1]!= 3) {
						gameMap[Ex][Ey] = 0;
						gameMap[Ex][Ey-1] = 3;
						enemyData[i][1] = Ey-1;
						moveing = false;
						break;
					}
				}
			}
		}
		
		static int getEnemyNumber (int[][] enemyData ,int Ex ,int Ey) {
			for (int i = 0; i < enemyData.length; i++) {
				if (enemyData[i][0] == Ex && enemyData[i][1] == Ey) {
					return i;
				}
			}
			return -1;
		}
		
		static int[][] randomSetItem (int[][] gameMap, int ItemCount) {
			int[][] ItemData = new int[ItemCount][3];	//{x,y,mode}
			for (int i = 0; i < ItemCount;) {
				Random rand = new Random();
				int Ix = rand.nextInt(gameMap.length);
				int Iy = rand.nextInt(gameMap[Ix].length);
				if (gameMap[Ix][Iy] == 0) {
					gameMap[Ix][Iy] = 4;
					ItemData[i][0] = Ix;
					ItemData[i][1] = Iy;
					ItemData[i][2] = 0;	//仮のモード
					i++;
				} else {
					continue;
				}
			}
			
			
			return ItemData;
		}
		
		static void showItemData(int[][] itemData) {
			System.out.println("-----------------");
			System.out.println("x :y :M");
			for (int i = 0; i < itemData.length; i++) {
				System.out.printf("%2d,%2d,%2d\n",itemData[i][0],itemData[i][1],itemData[i][2]);
			}
			System.out.println("-----------------");
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
			System.out.println("アイテム($)の数:");
			int itemCount = stdIn.nextInt();
			
			if (x * y < n+Ec+itemCount) {
				System.out.println("error");
				return;
			}
			
			int playerHP = 100;				//プレイヤーのHP
			int playerATK = 30;				//プレイヤーの攻撃力
			int[] playerData = makePlayerData(playerHP,playerATK);
			
			int[][] gameMap = makeGameMap(x, y, n);
			gameMap = RandomSetEnemy(gameMap, Ec);
			
			int[][] enemyData = makeEnemyData(gameMap, Ec);
			showEnemyData(enemyData);
			
			int[][] ItemData = randomSetItem(gameMap, itemCount);
			
			System.out.printf("HP:%d\n",playerData[2]);
			showMap(gameMap,enemyData);
			boolean gameLoop = true;
			String space = stdIn.nextLine();
			System.out.print(">");
			String keyIn = stdIn.nextLine();
			
			////ゲームのメイン処理////
			boolean existEnemy;		//敵が存在しているかどうか
			boolean existPleyer;	//プレイヤーが存在しているかどうか
			int turn = 1;			//ターン数
			while (gameLoop) {
				
				if (keyIn.equals("q")) {
					gameLoop = false;
					break;
				}
				if (keyIn.equals("m")) {
					showEnemyData(enemyData);
					keyIn = stdIn.nextLine();
				}
				if (keyIn.equals("e")) {
					showPlayerData(playerData);
					keyIn = stdIn.nextLine();
				}
				if (keyIn.equals("i")) {
					showItemData(ItemData);
					keyIn = stdIn.nextLine();
				}
				else {
					movePoint(gameMap, keyIn, playerData, enemyData, stdIn);
					moveEnemyMulti(gameMap,playerData ,enemyData, Ec, stdIn);
					boolean scan = enemyScanPlayerMuiti(gameMap, enemyData, Ec);
					healPlayer(playerData, 5);
					if (scan) {
						System.out.printf("<T:%d> [!] HP:%d\n",turn,playerData[2]);
					} else {
						System.out.printf("<T:%d> HP:%d\n",turn,playerData[2]);
					}
					showMap(gameMap,enemyData);
					existEnemy = checkAllEnemy(enemyData);
					if (!existEnemy && Ec > 0) {
						gameLoop = false;
						System.out.println("全ての敵を撃破!");
						break;
					}
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
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * --------操作説明--------
 * --初期設定--
 * ・x,yでマップの広さを指定
 * ・nで障害物の個数を指定
 * ・敵の数を指定
 * 
 * --マップでの操作--
 * ・「 wasd 」で動かす
 * ・「 q 」を入力すると終了
 * ・「 m 」を入力すると全ての敵の状態を表示
 * ・敵が撃破された場合、敵の座標(x,y)は(-1,-1)に設定される。
 * ・「 e 」を入力するとプレイヤーの状態を表示
 * ・敵の周囲1マスにプレイヤーが入ると、マップ上部に [!] が表示される
 * ・全ての敵を倒すと終了
 * ・マップ上部の「 <T:n> 」の n は現在のターン数
 * 
 * --戦闘中での操作・説明--
 * ・[ A ]で攻撃
 * ・[ Q ]で撤退
 * ・プレイヤーは攻撃での戦闘に負けた場合、HPが1になる(死なない)
 * 	 防御での戦闘で負けたor撤退した場合、ゲームオーバー
 * 
 * 
 * --マップ上の番号の意味と表示--
 * 何もなし:0 , 「 - 」
 * プレイヤー:1 , 「 @ 」
 * 壁(障害物):2, 「 # 」
 * 敵:3 , 「 * 」
 * 
 * --ソースコードについて--
 * ・xが行、yが列に関する値。Eが敵、Pがプレイヤーに関する値		例)int Ex -> 敵のx座標の値
 * ・Don't二次配布、改造・参考にするのはOK
 * ・プレイヤーが復活する等のバグがある
 * ・ソースコードの「 //～～する処理//　」などは、これ以下のコードの処理の概要を説明している。(多少読みやすくなるはず)
 * ・enemyDataの形式：{
 * 						{ Ex(敵のx座標)、Ey(敵のy座標)、E(敵が存在するかどうか)、敵のHP、敵のATK 、m(敵の動作モード)}, 	//敵0
 * 						{"},												　					//敵1
 * 						...,
 * 						{"}													  					//敵n
 * 					  }
 * ・敵が撃破された場合(E=1のとき)、敵の座標(x,y)は(-1,-1)に設定される。
 * ・敵の動作モード「 M 」：0=動作しない、1=ランダム移動、2=プレイヤーを追いかける動作
 * ・playerDataの形式：{x座標、y座標、HP、ATK, maxHP}
 * 
 * 
 * --------メソットの概要・説明(雑)--------
 * ・makeGameMap			:2次元配列gameMapの作成する
 * ・showMap				:コンソールにマップを表示する
 * ・movePoint				:プレイヤー(ポイント)を動かす処理、敵を踏むと「接敵!」と表示する処理、戦闘後の移動処理
 *
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
 * ・onlyMoveOneEnemyRandom	;enemyChasePlayerMuitiの内部メソッド。戦闘を発生させずに、敵をランダムに動かす処理。
 * ・getEnemyNumber			;[内部処理] 入力された敵の座標から，敵の番号を返すメソッド．
 * ・randomSetItem			;アイテムをマップ上にランダムにセットするメソッド．int[][]アイテムデータを返す．
 * ・showItemData			;アイテムデータを表示するメソッド．
 */
