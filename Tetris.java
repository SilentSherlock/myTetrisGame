package tetrisGame;

import java.util.Random;

//方块类，总共有七种方块类型，分别是Z型，S型，I型，T型，O型，L型，J型.用二维数组表示，二维数组中的数据代
//表点的坐标,另外用Block_none代表空白
public class Tetris {
	enum Tetrises{
		Block_None, Block_Z, Block_S, Block_I, Block_T, Block_O, BLock_L, BLock_J;
	}
	private Tetrises currentType;//保存当前方块的类型
	private int tetrisTypes[][][];//二维数组的数组表示八种方块的坐标
	private int currentTetris[][];;//表示当前的方块
	
	//设置当前方块的类型
	public void setBlockType(Tetrises type){
		for(int i = 0;i < currentTetris.length;i++){
			for(int j = 0;j < currentTetris[i].length;j++){
				currentTetris[i][j] = tetrisTypes[type.ordinal()][i][j];
			}
		}
		currentType = type;
	}
	//获得当前方块的类型
	public Tetrises getBlockType(){
		return currentType;
	}
	//构造函数
	public Tetris(){
		currentTetris = new int[4][2];
		tetrisTypes = new int[][][]{
				{{0, 0}, {0, 0}, {0, 0},{0, 0}},//Block_None
				{{-1, 0}, {0, 0}, {0, -1}, {1, -1}},//Block_Z
				{{-1, -1}, {0, -1}, {0, 0}, {1, 0}},//Block_S
				{{-1, 0}, {0, 0}, {1, 0}, {2, 0}},//Block_I
				{{-1, 0}, {0, 0}, {1, 0}, {0, 1}},//Block_T
				{{0, 0}, {1, 0}, {0, 1}, {1, 1}},//Block_O
				{{-1, 0}, {0, 0}, {1, 0}, {1, 1}},//Block_L
				{{-1, 1}, {-1, 0}, {0, 0}, {1, 0}}//Block_J
		};
		setBlockType(Tetrises.Block_None);
	}
	//产生随机方块
	public void getRandomBlock(){
		Random tmp = new Random();
		int index = tmp.nextInt(19981128)%7 + 1;//加1避免随机到Blcok_None
		setBlockType(Tetrises.values()[index]);
	}
	//在方块生成算法中用来确定currentY的位置
	public int minY(){
		int minY = 0;
		for(int i = 0;i < 4;i++)
			minY = Math.min(minY, getBlockY(i));
		return minY;
	}
	//从旋转算法中拆出的函数,简化算法
	public int getBlockX(int indexX){
		return currentTetris[indexX][0];
	}
	public int getBlockY(int indexY){
		return currentTetris[indexY][1];
	}
	public void setBlockX(int indexX, int x){
		currentTetris[indexX][0] = x;
	}
	public void setBlockY(int indexY, int y){
		currentTetris[indexY][1] = y;
	}
	//方块旋转算法,算法是通过先调换当前方块的X，Y坐标，并将Y坐标的相反数赋给X，达到旋转的目的
	public Tetris rotateBlock(){
		if(currentType == Tetrises.Block_O){
			return this;
		}
		Tetris tmp = new Tetris();
		tmp.currentType = currentType;//保存方块的类型
		for(int i = 0; i < currentTetris.length;i++){
			tmp.setBlockX(i, -getBlockY(i));
			tmp.setBlockY(i, getBlockX(i));
		}
		return tmp;
	}
}