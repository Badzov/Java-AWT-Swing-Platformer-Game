package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import utils.LoadSave;
import static utils.Constants.Enviroment.*;


public class Playing extends State implements Statemethods{
	
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	
	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	
	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	public Playing(Game game) {
		super(game);
		initClasses();
		
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for(int i=0; i<smallCloudsPos.length; i++) {
			smallCloudsPos[i] = (int) (90 * Game.SCALE) +rnd.nextInt((int) (100 * game.SCALE));
			}
	}

	
	
private void initClasses() {
		
		enemyManager = new EnemyManager(this);
		levelManager = new LevelManager(game);
		player = new Player(200, 200, (int)(64*Game.SCALE), (int)(40*Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		}


public void update() {

	levelManager.update();
	player.update();
	enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
	checkCloseToBorder();
	
}



private void checkCloseToBorder() {
	
	int playerX = (int) (player.getHitbox().x);
	int diff = playerX - xLvlOffset;
	
	if (diff > rightBorder) 
		xLvlOffset += diff - rightBorder;
	else if (diff < leftBorder) 
		xLvlOffset += diff - leftBorder;
	
	if(xLvlOffset > maxLvlOffsetX) {
		xLvlOffset = maxLvlOffsetX;
	}
	else if(xLvlOffset < 0) {
		xLvlOffset = 0;
	}
	
	
}



public void draw(Graphics g) {
	
	g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	
	drawClouds(g);
	
	levelManager.draw(g, xLvlOffset);
	player.render(g, xLvlOffset);
	enemyManager.draw(g, xLvlOffset);
	
}



private void drawClouds(Graphics g) {
	
	for (int i = 0; i<3; i++)
		g.drawImage(bigCloud, i*BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int)(204*Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
	
	for (int i=0; i < smallCloudsPos.length; i++) 
		g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
}



public void mouseClicked(MouseEvent e) {

	if (e.getButton() == MouseEvent.BUTTON1) {
		player.setAttacking(true);
	}
	
}



public void mousePressed(MouseEvent e) {

	
}



public void mouseReleased(MouseEvent e) {

	
}



public void mouseMoved(MouseEvent e) {

	
}




public void keyPressed(KeyEvent e) {

	switch(e.getKeyCode()) {
	

	case KeyEvent.VK_A:
		player.setLeft(true);
		break;
	case KeyEvent.VK_D:
		player.setRight(true);
		break;
	case KeyEvent.VK_SPACE:
		player.setJump(true);
		break;
	case KeyEvent.VK_ESCAPE:
		Gamestate.state = Gamestate.MENU; 
	}
	
	
}




public void keyReleased(KeyEvent e) {

	switch(e.getKeyCode()) {
	

	case KeyEvent.VK_A:
		player.setLeft(false);
		break;
	case KeyEvent.VK_D:
		player.setRight(false);
		break;
	case KeyEvent.VK_SPACE:
		player.setJump(false);
		break;
	
	}
	
}

public Player getPlayer() {
	return player;
}

public void windowFocusLost() {
	player.resetDirBooleans();
}


	
}
