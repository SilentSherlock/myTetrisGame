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

//游戏运行时的控制逻辑与控制操作
public class GameControl implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//游戏界面内基本方块数量，横向最多有13个，纵向最多有24个
	private static final int boardWidth = 13;
	private static final int boardHeight = 24;
	
	//游戏状态数据
	private Timer timer;
	private boolean isStart = false;
	private boolean isPause = false;
	private boolean isFallingDone = false;//判断方块下落是否结束
	private int gameScore = 0;//消除方块得分
	private int diffDegree = 400;//刷新频率，代表难度
	
	private int currentX = 0;
	private int currentY = 0;
	
	//下一个方块，及其位置
	private Tetris nextTetris;
	private int nextX = 0;
	private int nextY = 0;
	//当前方块，及其位置
	private Tetris currentTetris;
	//游戏界面，用来刷新界面
	private GamePanel gamePanel;
	
	//以一维数组的形式在逻辑上表示二维的游戏界面
	private Tetrises[] gameBoard;
	
	//构造函数
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
	
	//初始化游戏界面
	public void initialBoard() {
		for(int i = 0;i < boardWidth*boardHeight;i++)
			gameBoard[i] = Tetrises.Block_None;
	}
	
	//如果玩家要手动调整难度，调用此函数
	public void changeDiffDegree(int tmp){
		diffDegree = tmp;
		timer.setDelay(diffDegree);
	}
	
	//通过调整刷新频率来更改难度
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
	//游戏开始
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
	
	//游戏暂停
	public void pause(){
		if(!isStart)
			return;
		
		isPause = !isPause;
		if(isPause){
			timer.stop();
		}else{
			timer.start();
		}		
		gamePanel.repaint();//待更改
	}
	
	//获得游戏状态数据
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
	//获得对应位置的方块类型
	public Tetrises curTetrisType(int x, int y) {
		return gameBoard[y*boardWidth+x];
	}
	
	//方块移动检测算法,判断操作的下一个位置是否可达
	public boolean isBlockMovable(Tetris tmp, int tmpX, int tmpY) {
		for(int i = 0;i < 4;i++) {
			int x = tmpX + tmp.getBlockX(i);
			int y = tmpY - tmp.getBlockY(i);
			if(x < 0 || x >= boardWidth || y < 0 || y >= boardHeight)
				return false;
			if(curTetrisType(x, y) != Tetrises.Block_None)
				return false;
		}
		//如果可达,要重新绘图，此步绘图可以分离
		currentTetris = tmp;
		currentX = tmpX;
		currentY = tmpY;
		gamePanel.repaint();
		return true;
	}
	
	//画图产生新方块
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
	
	//产生下一个方块
	public void createNextTetris() {
		nextTetris.getRandomBlock();
		nextX = boardWidth/2 +boardWidth - 1;
		nextY = boardHeight - 6 + nextTetris.minY();
	}
	//游戏结束，待补充
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
	
	//方块清除算法
	public void blockFix(){
		for(int i = 0;i < 4;i++){
			int x = currentX + currentTetris.getBlockX(i);
			int y = currentY - currentTetris.getBlockY(i);
			gameBoard[y * boardWidth + x] = currentTetris.getBlockType();
		}
		
		//清除整行
		int fullLines = 0;
		for(int i = boardHeight -1;i >= 0;i--){
			boolean isFull = true;
			for(int j = 0;j < boardWidth;j++){
				if(curTetrisType(j, i) == Tetrises.Block_None){
					isFull = false;
					break;
				}
			}
			
			//通过上一行的方块覆盖满的本行来执行消除整行的操作
			if(isFull){
				fullLines++;
				for(int m = i;m < boardHeight-1;m++)
					for(int n = 0;n < boardWidth;n++){
						gameBoard[m * boardWidth + n] = curTetrisType(n, m + 1);
					}
			}
		}
		if(fullLines > 0){
			gameScore+=fullLines*fullLines;//得分为消除的行的平方
			isFallingDone = true;
			currentTetris.setBlockType(Tetrises.Block_None);
			setDiffDegree();
			gamePanel.repaint();//重新绘图
		}
		
		//消除完成，产生新方块
		if(!isFallingDone){
			getNewTetris();
			createNextTetris();
		}	
	}
	//方块移动算法
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
	//根据定时器，重复调用画图
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
		
		//暂停，再次按p即可继续
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
	//读取游戏数据
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
	//保存游戏数据,当得分大于最高分才进行写
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
