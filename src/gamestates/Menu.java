package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import ui.MenuButton;
import utils.LoadSave;
import main.Game;

public class Menu extends State implements Statemethods {
	
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;
	
	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
	}


	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth()*Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);
	}


	private void loadButtons() {
		
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150*Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220*Game.SCALE), 1, Gamestate.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290*Game.SCALE), 2, Gamestate.QUIT);
		
	}


	public void update() {
		
		for(MenuButton mb : buttons) 
			mb.update();
		
	}


	public void draw(Graphics g) {
		
		g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, game.GAME_HEIGHT, null);
	
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		
		for(MenuButton mb : buttons) 
			mb.draw(g);
		
	}


	public void mouseClicked(MouseEvent e) {

		
	}

	
	public void mousePressed(MouseEvent e) {
	
		for(MenuButton mb : buttons) {
			if(isIn(e,mb)) {
				mb.setMousePressed(true);
				break;
			}
		}
		
	}

	
	public void mouseReleased(MouseEvent e) {
	
		for(MenuButton mb : buttons) {
			if(isIn(e,mb)) {
				
				if(mb.isMousePressed())
					mb.applyGameState();
				break;
			}
		}
		
		resetButtons();
		
	}

	
	private void resetButtons() {
		
		for(MenuButton mb : buttons) {
			mb.resetBools();
		}
		
	}


	public void mouseMoved(MouseEvent e) {
		
		for(MenuButton mb : buttons)
			mb.setMouseOver(false);
		for(MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
		
	}

	
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Gamestate.state = Gamestate.PLAYING;
		}
		
	}

	
	public void keyReleased(KeyEvent e) {
		
		
	}

}
