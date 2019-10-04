package tetrisGame;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//程序运行时出现的游戏窗口
public class TheGameWindow extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar toolBar; 
	private final JMenu[] menus = {new JMenu("游戏"), new JMenu("难度"), new JMenu("选项"), new JMenu("暂停(p)")};
	private final JMenuItem [][] menuItems = {
			{new JMenuItem("新游戏"), new JMenuItem("重新开始"), new JMenuItem("退出")},
			{new JMenuItem("简单"), new JMenuItem("一般"), new JMenuItem("困难"), new JMenuItem("地狱")},
			{new JMenuItem("帮助"), new JMenuItem("关于")}
	};
	
	private GamePanel gamePanel;
	//构造函数\
	
	public TheGameWindow(){
		
		setTitle("Totally Accurate Tetris Simulater");
		setSize(600, 600);//游戏窗口大小
		setResizable(false);//大小不可调整
		setMenu(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		gamePanel = new GamePanel(this, 400);//可以自由设定难度，初始值为400
		add(gamePanel);
		
		
		setVisible(true);
	}
	public void setMenu(TheGameWindow gameWindow){
		toolBar = new JMenuBar();
		
		for(int i = 0;i < menus.length - 1;i++){
			for(int j = 0;j < menuItems[i].length;j++){
				menus[i].add(menuItems[i][j]);
				menuItems[i][j].addActionListener(gameWindow);
			}
			toolBar.add(menus[i]);
		}
		toolBar.add(menus[3]);
		setJMenuBar(toolBar);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if(command.equals("简单") || command.equals("一般") || command.equals("困难") || command.equals("地狱")){
			switch (command) {
			case "简单":
				gamePanel.changeDiffDegree(400);
				break;
			case "一般":
				gamePanel.changeDiffDegree(370);
				break;
			case "困难":
				gamePanel.changeDiffDegree(200);
				break;
			case "地狱":
				gamePanel.changeDiffDegree(120);
				break;
			default:
				break;
			}
		}
		switch(command){
		case "新游戏":
		case "重新开始":
			gamePanel.start();
			break;
		case "退出":
			System.exit(0);
			break;
		}
	}
}
