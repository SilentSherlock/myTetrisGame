package tetrisGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import java.io.*;

import tetrisGame.Tetris.Tetrises;

//��Ϸ����ʱ�Ŀ����߼�����Ʋ���
public class GameControl implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//��Ϸ�����ڻ����������������������13�������������24��
	private static final int boardWidth = 13;
	private static final int boardHeight = 24;
	
	//��Ϸ״̬����
	private Timer timer;
	private boolean isStart = false;
	private boolean isPause = false;
	private boolean isFallingDone = false;//�жϷ��������Ƿ����
	private int gameScore = 0;//��������÷�
	private int diffDegree = 400;//ˢ��Ƶ�ʣ������Ѷ�
	
	private int currentX = 0;
	private int currentY = 0;
	
	//��һ�����飬����λ��
	private Tetris nextTetris;
	private int nextX = 0;
	private int nextY = 0;
	//��ǰ���飬����λ��
	private Tetris currentTetris;
	//��Ϸ���棬����ˢ�½���
	private GamePanel gamePanel;
	
	//��һά�������ʽ���߼��ϱ�ʾ��ά����Ϸ����
	private Tetrises[] gameBoard;
	
	//���캯��
	public GameControl(GamePanel tmpgamePanel, int tmpDiffDegree){
		gameBoard = new Tetrises[boardHeight * boardWidth];
		nextTetris = new Tetris();
		currentTetris = new Tetris();
		gameScore = 0;
		if(tmpDiffDegree != 0){
			diffDegree = tmpDiffDegree;
		}
		timer = new Timer(diffDegree, this);
		gamePanel = tmpgamePanel;
		createNextTetris();
		initialBoard();
	}
	
	//��ʼ����Ϸ����
	public void initialBoard() {
		for(int i = 0;i < boardWidth*boardHeight;i++)
			gameBoard[i] = Tetrises.Block_None;
	}
	
	//������Ҫ�ֶ������Ѷȣ����ô˺���
	public void changeDiffDegree(int tmp){
		diffDegree = tmp;
		timer.setDelay(diffDegree);
	}
	
	//ͨ������ˢ��Ƶ���������Ѷ�
		private void setDiffDegree(){
			
			int tmpDiffDegree = 0;
			switch(getGameScore()/18){
			case 0:
				tmpDiffDegree = 400;
				break;
			case 1:
				tmpDiffDegree = 370;
				break;
			case 2:
				tmpDiffDegree = 340;
				break;
			case 3:
				tmpDiffDegree = 300;
				break;
			case 4:
				tmpDiffDegree = 250;
				break;
			case 5:
				tmpDiffDegree = 200;
				break;
			case 6:
				tmpDiffDegree = 100;
				break;
			default:
				tmpDiffDegree = 120;
				break;
			}
			if(tmpDiffDegree < diffDegree)
				diffDegree = tmpDiffDegree;
			timer.setDelay(diffDegree);
		}
	//��Ϸ��ʼ
	public void start(){
		if(isPause)
			return;
		
		isStart = true;
		isFallingDone = false;
		gameScore = 0;
		timer.start();
		initialBoard();
		
		getNewTetris();
		createNextTetris();
	}
	
	//��Ϸ��ͣ
	public void pause(){
		if(!isStart)
			return;
		
		isPause = !isPause;
		if(isPause){
			timer.stop();
		}else{
			timer.start();
		}		
		gamePanel.repaint();//������
	}
	
	//�����Ϸ״̬����
	public boolean getIsStart(){
		return isStart;
	}
	public boolean getIsPause(){
		return isPause;
	}
	public Tetris getCurrentTetris(){
		return currentTetris;
	}
	public int getCurrentX(){
		return currentX;
	}
	public int getCurrentY(){
		return currentY;
	}
	public boolean getIsFallingDone(){
		return isFallingDone;
	}
	public void setIsFallingDone(boolean flag){
		isFallingDone = flag;
	}
	public int getGameScore(){
		return gameScore;
	}
	public Tetris getNextTetris() {
		return nextTetris;
	}
	public int getNextX() {
		return nextX;
	}
	public int getNextY() {
		return nextY;
	}
	//��ö�Ӧλ�õķ�������
	public Tetrises curTetrisType(int x, int y) {
		return gameBoard[y*boardWidth+x];
	}
	
	//�����ƶ�����㷨,�жϲ�������һ��λ���Ƿ�ɴ�
	public boolean isBlockMovable(Tetris tmp, int tmpX, int tmpY) {
		for(int i = 0;i < 4;i++) {
			int x = tmpX + tmp.getBlockX(i);
			int y = tmpY - tmp.getBlockY(i);
			if(x < 0 || x >= boardWidth || y < 0 || y >= boardHeight)
				return false;
			if(curTetrisType(x, y) != Tetrises.Block_None)
				return false;
		}
		//����ɴ�,Ҫ���»�ͼ���˲���ͼ���Է���
		currentTetris = tmp;
		currentX = tmpX;
		currentY = tmpY;
		gamePanel.repaint();
		return true;
	}
	
	//��ͼ�����·���
	public void getNewTetris(){
		currentTetris.setBlockType(nextTetris.getBlockType());
		currentX = boardWidth/2;
		currentY = boardHeight - 1 + currentTetris.minY();
		if(!isBlockMovable(currentTetris, currentX, currentY)){
			currentTetris.setBlockType(Tetrises.Block_None);
			timer.stop();
			isStart = false;
			gameOver();
		}
	}
	
	//������һ������
	public void createNextTetris() {
		nextTetris.getRandomBlock();
		nextX = boardWidth/2 +boardWidth - 1;
		nextY = boardHeight - 6 + nextTetris.minY();
	}
	//��Ϸ������������
	public void gameOver(){
		int maxRecord = readGameScore();
		if(maxRecord < gameScore) {
			saveGameScore();
		}
		String tmp = new String("Game over\n Your Game Score is ") + String.valueOf(gameScore);
		UIManager.put("OptionPane.background", Color.BLACK);
		UIManager.put("OptionPane.font", new FontUIResource(new Font("Matrix", Font.PLAIN, 26)));
		UIManager.put("OptionPane.titleText", "YOU ARE FAILED");
		//IManager.put("OptionPane.border", )
		JOptionPane.showMessageDialog(null, tmp, "I'm ending",JOptionPane.WARNING_MESSAGE, new ImageIcon("./iconImages/badGuy.png"));
	}
	
	//��������㷨
	public void blockFix(){
		for(int i = 0;i < 4;i++){
			int x = currentX + currentTetris.getBlockX(i);
			int y = currentY - currentTetris.getBlockY(i);
			gameBoard[y * boardWidth + x] = currentTetris.getBlockType();
		}
		
		//�������
		int fullLines = 0;
		for(int i = boardHeight -1;i >= 0;i--){
			boolean isFull = true;
			for(int j = 0;j < boardWidth;j++){
				if(curTetrisType(j, i) == Tetrises.Block_None){
					isFull = false;
					break;
				}
			}
			
			//ͨ����һ�еķ��鸲�����ı�����ִ���������еĲ���
			if(isFull){
				fullLines++;
				for(int m = i;m < boardHeight-1;m++)
					for(int n = 0;n < boardWidth;n++){
						gameBoard[m * boardWidth + n] = curTetrisType(n, m + 1);
					}
			}
		}
		if(fullLines > 0){
			gameScore+=fullLines*fullLines;//�÷�Ϊ�������е�ƽ��
			isFallingDone = true;
			currentTetris.setBlockType(Tetrises.Block_None);
			setDiffDegree();
			gamePanel.repaint();//���»�ͼ
		}
		
		//������ɣ������·���
		if(!isFallingDone){
			getNewTetris();
			createNextTetris();
		}	
	}
	//�����ƶ��㷨
	public void moveOneLine() {
		if(!isBlockMovable(currentTetris, currentX, currentY-1))
			blockFix();
	}
	public void moveToBottom(){
		int tmp = currentY;
		while(tmp > 0){
			if(!isBlockMovable(currentTetris, currentX, tmp-1))
				break;
			tmp--;
		}
		blockFix();
	}
	//���ݶ�ʱ�����ظ����û�ͼ
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(isFallingDone){
				isFallingDone = false;
				//createNextTetris();
				getNewTetris();
			}else{
				moveOneLine();
			}
		}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!isStart || currentTetris.getBlockType() == Tetrises.Block_None)
			return;
		
		//��ͣ���ٴΰ�p���ɼ���
		if(e.getKeyCode() == 'p' || e.getKeyCode() == 'P'){
			pause();
			return;
		}
		if(isPause)
			return;
		
		switch(e.getKeyCode()){
		case 'a':
		case 'A':
		case KeyEvent.VK_LEFT:
			isBlockMovable(currentTetris, currentX - 1, currentY);
			break;
		case 'd':
		case 'D':
		case KeyEvent.VK_RIGHT:
			isBlockMovable(currentTetris, currentX + 1, currentY);
			break;
		case 's':
		case 'S':
		case KeyEvent.VK_DOWN:
			moveOneLine();
			break;
		case 'w':
		case 'W':
		case KeyEvent.VK_UP:
			isBlockMovable(currentTetris.rotateBlock(), currentX, currentY);
			break;
		case KeyEvent.VK_SPACE:
			moveToBottom();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	//��ȡ��Ϸ����
	public int readGameScore() {
		try {
			BufferedReader tmpBR = new BufferedReader(new FileReader("gameData.txt"));
			int maxScore = Integer.parseInt(tmpBR.readLine());
			tmpBR.close();
			return maxScore;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}	
	//������Ϸ����,���÷ִ�����߷ֲŽ���д
	public void saveGameScore() {
		try {
			File gameData = new File("gameData.txt");
			if(!gameData.exists()) {
				gameData.createNewFile();
			}
			FileWriter tmpFW = new FileWriter(gameData);
			BufferedWriter tmpBW = new BufferedWriter(tmpFW);
			tmpBW.write(String.valueOf(gameScore));
			tmpBW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
