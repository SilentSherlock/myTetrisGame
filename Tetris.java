package tetrisGame;

import java.util.Random;

//�����࣬�ܹ������ַ������ͣ��ֱ���Z�ͣ�S�ͣ�I�ͣ�T�ͣ�O�ͣ�L�ͣ�J��.�ö�ά�����ʾ����ά�����е����ݴ�
//��������,������Block_none����հ�
public class Tetris {
	enum Tetrises{
		Block_None, Block_Z, Block_S, Block_I, Block_T, Block_O, BLock_L, BLock_J;
	}
	private Tetrises currentType;//���浱ǰ���������
	private int tetrisTypes[][][];//��ά����������ʾ���ַ��������
	private int currentTetris[][];;//��ʾ��ǰ�ķ���
	
	//���õ�ǰ���������
	public void setBlockType(Tetrises type){
		for(int i = 0;i < currentTetris.length;i++){
			for(int j = 0;j < currentTetris[i].length;j++){
				currentTetris[i][j] = tetrisTypes[type.ordinal()][i][j];
			}
		}
		currentType = type;
	}
	//��õ�ǰ���������
	public Tetrises getBlockType(){
		return currentType;
	}
	//���캯��
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
	//�����������
	public void getRandomBlock(){
		Random tmp = new Random();
		int index = tmp.nextInt(19981128)%7 + 1;//��1���������Blcok_None
		setBlockType(Tetrises.values()[index]);
	}
	//�ڷ��������㷨������ȷ��currentY��λ��
	public int minY(){
		int minY = 0;
		for(int i = 0;i < 4;i++)
			minY = Math.min(minY, getBlockY(i));
		return minY;
	}
	//����ת�㷨�в���ĺ���,���㷨
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
	//������ת�㷨,�㷨��ͨ���ȵ�����ǰ�����X��Y���꣬����Y������෴������X���ﵽ��ת��Ŀ��
	public Tetris rotateBlock(){
		if(currentType == Tetrises.Block_O){
			return this;
		}
		Tetris tmp = new Tetris();
		tmp.currentType = currentType;//���淽�������
		for(int i = 0; i < currentTetris.length;i++){
			tmp.setBlockX(i, -getBlockY(i));
			tmp.setBlockY(i, getBlockX(i));
		}
		return tmp;
	}
}