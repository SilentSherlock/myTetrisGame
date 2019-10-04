package tetrisGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import tetrisGame.Tetris.Tetrises;


public class GamePanel extends JPanel{
	
	//��Ϸ�����ڻ����������������������13�������������24��
	private static final int boardWidth = 13;
	private static final int boardHeight = 24;
	//��Ϸ������
	private GameControl gameControl;
	
	//���ַ�����ɫ
	private Color[] blockColor;
	
	//���캯��
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
	//�ֶ������Ѷ�
	public void changeDiffDegree(int tmp) {
		gameControl.changeDiffDegree(tmp);
	}
	//�õ�һ����������Ļ�ϵĿ�Ⱥ͸߶�
	private int blockWidth() {
		return 320/boardWidth;
		//return (int)getSize().getWidth()/boardWidth;
	}
	private int blockHeight() {
		return 540/boardHeight;
		//return (int)getSize().getHeight()/boardHeight;
	}
	//��һ������
	private void drawOneBlock(Graphics g, Tetrises curBlock, int x, int y) {
		g.setColor(blockColor[curBlock.ordinal()]);
		g.fillRect(x + 1, y + 1, blockWidth() - 2, blockHeight() - 2);//���޸�
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
		//��Ϸ����,�Ե�����������ͼ�������߼���ʾ��ʽ�ǵ�һ ���ޣ�������Ҫ���ж�Ӧ��ת��
		for(int i = 0;i < boardHeight;i++) {
			for(int j = 0; j < boardWidth;j++) {
				Tetrises type = gameControl.curTetrisType(j, boardHeight - i - 1);
				if(type != Tetrises.Block_None)
					drawOneBlock(g, type, j * blockWidth(), realBoardHeight + i * blockHeight());
			}
		}
		//����ǰ�ķ���
		if(currentBlock.getBlockType() != Tetrises.Block_None){
			for(int i = 0;i < 4;i++){
				int x = currentX + currentBlock.getBlockX(i);
				int y = currentY - currentBlock.getBlockY(i);
				drawOneBlock(g, currentBlock.getBlockType(), x * blockWidth(), 
						realBoardHeight + (boardHeight - y -1) * blockHeight());
			}
		}
		//����һ������
		if(nextBlock.getBlockType() != Tetrises.Block_None) {
			for(int i = 0;i < 4;i++) {
				int x = nextX + nextBlock.getBlockX(i);
				int y = nextY - nextBlock.getBlockY(i);
				drawOneBlock(g, nextBlock.getBlockType(), x * blockWidth(), 
						realBoardHeight + (boardHeight - y -1) * blockHeight());
			}
		}
		//����ͣ״̬
		if(gameControl.getIsPause()){
			g.setColor(new Color(238,233,233));
			g.setFont(new Font("Matrix", Font.PLAIN, 18));
			String str = "Paused now.Press P to return";
			g.drawString(str, infoPanelPole/8, 18);
		}
		//���÷�
		String score = new String("YOURSCORE:") + String.valueOf(gameControl.getGameScore());
		String maxScore = new String("MAXSCORE:") + String.valueOf(gameControl.readGameScore());
		
		g.setColor(new Color(238,233,233));
		g.setFont(new Font("����", Font.PLAIN, 17));
		g.drawString(score, infoPanelPole+73, panelHeight-150);
		g.drawString(maxScore, infoPanelPole+73, panelHeight-120);
		//���߿�
		g.setColor(new Color(255, 250, 205));
		//g.fillRect(0, 0, boardWidth*blockWidth(), 4);
		g.fillRect(boardWidth*blockWidth(), 0, 1, panelHeight);
		g.fillRect(boardWidth*blockWidth() + 3, 0, 1, panelHeight);
		g.drawArc(boardWidth*blockWidth() + 4, panelHeight/11, 80, 120, 90, 99);
		g.drawArc(boardWidth*blockWidth() + 4, panelHeight/11+ 3, 80, 120, 90, 99);
		g.fillRect(boardWidth*blockWidth()+44, panelHeight/11, 180, 3);//���յ㴦
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
