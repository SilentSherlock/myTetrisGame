package tetrisGame;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//��������ʱ���ֵ���Ϸ����
public class TheGameWindow extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar toolBar; 
	private final JMenu[] menus = {new JMenu("��Ϸ"), new JMenu("�Ѷ�"), new JMenu("ѡ��"), new JMenu("��ͣ(p)")};
	private final JMenuItem [][] menuItems = {
			{new JMenuItem("����Ϸ"), new JMenuItem("���¿�ʼ"), new JMenuItem("�˳�")},
			{new JMenuItem("��"), new JMenuItem("һ��"), new JMenuItem("����"), new JMenuItem("����")},
			{new JMenuItem("����"), new JMenuItem("����")}
	};
	
	private GamePanel gamePanel;
	//���캯��\
	
	public TheGameWindow(){
		
		setTitle("Totally Accurate Tetris Simulater");
		setSize(600, 600);//��Ϸ���ڴ�С
		setResizable(false);//��С���ɵ���
		setMenu(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		gamePanel = new GamePanel(this, 400);//���������趨�Ѷȣ���ʼֵΪ400
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
		if(command.equals("��") || command.equals("һ��") || command.equals("����") || command.equals("����")){
			switch (command) {
			case "��":
				gamePanel.changeDiffDegree(400);
				break;
			case "һ��":
				gamePanel.changeDiffDegree(370);
				break;
			case "����":
				gamePanel.changeDiffDegree(200);
				break;
			case "����":
				gamePanel.changeDiffDegree(120);
				break;
			default:
				break;
			}
		}
		switch(command){
		case "����Ϸ":
		case "���¿�ʼ":
			gamePanel.start();
			break;
		case "�˳�":
			System.exit(0);
			break;
		}
	}
}
