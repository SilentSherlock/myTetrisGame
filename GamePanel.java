package tetrisGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import tetrisGame.Tetris.Tetrises;


public class GamePanel extends JPanel{
	
	//游戏界面内基本方块数量，横向最多有13个，纵向最多有24个
	private static final int boardWidth = 13;
	private static final int boardHeight = 24;
	//游戏控制器
	private GameControl gameControl;
	
	//八种方块颜色
	private Color[] blockColor;
	
	//构造函数
	public GamePanel(TheGameWindow gameWindow, int tmpDiffDegree){
		
		setFocusable(true);
		setBackground(new Color(2, 2, 2));
		gameControl = new GameControl(this, tmpDiffDegree);
		
		blockColor = new Color[]{
				new Color(0, 0, 0), new Color(187, 255, 255), new Color(67, 205, 128),
				new Color(255, 246, 143), new Color(139, 117, 0), new Color(255, 106, 106),
				new Color(255, 48, 48), new Color(154, 50, 205)
		};
		addKeyListener(gameControl);
	}
	//手动调整难度
	public void changeDiffDegree(int tmp) {
		gameControl.changeDiffDegree(tmp);
	}
	//得到一个方块在屏幕上的宽度和高度
	private int blockWidth() {
		return 320/boardWidth;
		//return (int)getSize().getWidth()/boardWidth;
	}
	private int blockHeight() {
		return 540/boardHeight;
		//return (int)getSize().getHeight()/boardHeight;
	}
	//画一个方块
	private void drawOneBlock(Graphics g, Tetrises curBlock, int x, int y) {
		g.setColor(blockColor[curBlock.ordinal()]);
		g.fillRect(x + 1, y + 1, blockWidth() - 2, blockHeight() - 2);//待修改
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//int panelWidth = (int)getSize().getWidth();
		int panelHeight = (int)getSize().getHeight();
		int infoPanelPole = boardWidth*blockWidth();
		
		Tetris currentBlock = gameControl.getCurrentTetris();
		int currentX = gameControl.getCurrentX();
		int currentY = gameControl.getCurrentY();
		
		Tetris nextBlock = gameControl.getNextTetris();
		int nextX = gameControl.getNextX();
		int nextY = gameControl.getNextY();
		
		int realBoardHeight = (int)getSize().getHeight() - boardHeight * blockHeight();
		//游戏界面,以第四象限来画图，面板的逻辑表示形式是第一 象限，纵坐标要进行对应的转换
		for(int i = 0;i < boardHeight;i++) {
			for(int j = 0; j < boardWidth;j++) {
				Tetrises type = gameControl.curTetrisType(j, boardHeight - i - 1);
				if(type != Tetrises.Block_None)
					drawOneBlock(g, type, j * blockWidth(), realBoardHeight + i * blockHeight());
			}
		}
		//画当前的方块
		if(currentBlock.getBlockType() != Tetrises.Block_None){
			for(int i = 0;i < 4;i++){
				int x = currentX + currentBlock.getBlockX(i);
				int y = currentY - currentBlock.getBlockY(i);
				drawOneBlock(g, currentBlock.getBlockType(), x * blockWidth(), 
						realBoardHeight + (boardHeight - y -1) * blockHeight());
			}
		}
		//画下一个方块
		if(nextBlock.getBlockType() != Tetrises.Block_None) {
			for(int i = 0;i < 4;i++) {
				int x = nextX + nextBlock.getBlockX(i);
				int y = nextY - nextBlock.getBlockY(i);
				drawOneBlock(g, nextBlock.getBlockType(), x * blockWidth(), 
						realBoardHeight + (boardHeight - y -1) * blockHeight());
			}
		}
		//画暂停状态
		if(gameControl.getIsPause()){
			g.setColor(new Color(238,233,233));
			g.setFont(new Font("Matrix", Font.PLAIN, 18));
			String str = "Paused now.Press P to return";
			g.drawString(str, infoPanelPole/8, 18);
		}
		//画得分
		String score = new String("YOURSCORE:") + String.valueOf(gameControl.getGameScore());
		String maxScore = new String("MAXSCORE:") + String.valueOf(gameControl.readGameScore());
		
		g.setColor(new Color(238,233,233));
		g.setFont(new Font("宋体", Font.PLAIN, 17));
		g.drawString(score, infoPanelPole+73, panelHeight-150);
		g.drawString(maxScore, infoPanelPole+73, panelHeight-120);
		//画边框，
		g.setColor(new Color(255, 250, 205));
		//g.fillRect(0, 0, boardWidth*blockWidth(), 4);
		g.fillRect(boardWidth*blockWidth(), 0, 1, panelHeight);
		g.fillRect(boardWidth*blockWidth() + 3, 0, 1, panelHeight);
		g.drawArc(boardWidth*blockWidth() + 4, panelHeight/11, 80, 120, 90, 99);
		g.drawArc(boardWidth*blockWidth() + 4, panelHeight/11+ 3, 80, 120, 90, 99);
		g.fillRect(boardWidth*blockWidth()+44, panelHeight/11, 180, 3);//弧拐点处
		g.fillRect(boardWidth*blockWidth()+102, panelHeight/11+ 3, 1, 40);
		g.fillRect(boardWidth*blockWidth()+105, panelHeight/11+ 3, 1, 40);
		g.fillRect(boardWidth*blockWidth()+163, panelHeight/11+ 3, 1, 40);
		g.fillRect(boardWidth*blockWidth()+166, panelHeight/11+ 3, 1, 40);
		g.drawRect(boardWidth*blockWidth()+70, panelHeight/11+ 43, 130, 110);
		g.drawRect(boardWidth*blockWidth()+72, panelHeight/11+ 45, 126, 106);
		
		g.fillRect(infoPanelPole+102, panelHeight-100, 1, 100);
		g.fillRect(infoPanelPole+105, panelHeight-100, 1, 100);
		g.fillRect(infoPanelPole+163, panelHeight-100, 1, 100);
		g.fillRect(infoPanelPole+166, panelHeight-100, 1, 100);
		
		g.drawRect(infoPanelPole+60, panelHeight-180, 150, 80);
		g.drawRect(infoPanelPole+63, panelHeight-177, 144, 74);
	}
	public void start() {
		gameControl.start();
	}
}
