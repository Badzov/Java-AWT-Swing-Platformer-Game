package entities;

import static utils.Constants.Directions.*;
import static utils.HelpMethods.*;
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;
import utils.LoadSave;

public class Player extends Entity {

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 25;
	private int playerAction = IDLE;
	private boolean up, down, left, right, jump;
	private boolean moving = false, attacking = false;
	private float playerSpeed = 1.0f * Game.SCALE;
	private int[][] lvlData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4* Game.SCALE;
	
	// jumping and gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	
	public Player(float x, float y, int width, int height) {
		
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, (int) (20*Game.SCALE), (int) (27*Game.SCALE));
		
	}

	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffset - lvlOffset), (int)(hitbox.y - yDrawOffset), width, height, null);
		drawHitbox(g, lvlOffset);
	}
	
	

	private void updatePos() {
		
		moving = false;
		
		if (jump) {
			jump();
		}
		
//		if (!left && !right && !inAir) {
//			return;
//		}
		
		if(!inAir) {
			if((!left && !right) || (left && right)) {
				return;
			}
		}
		
		float xSpeed = 0;
		
		if (left) 
			xSpeed -= playerSpeed;
		if(right) 
			xSpeed += playerSpeed;
		
		if (!inAir) 
			if (!IsEntityOnFloor(hitbox, lvlData)) 
				inAir = true;
			
		
		if (inAir) {
			
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			}
			else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0) {
					resetInAir();
				}
				else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
			
		}
		else {
			updateXPos(xSpeed);
		}
		
		moving = true;
		
		
		
	
		
	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		}
		else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
		
		
	}

	private void setAnimation() {
		
		int startAni = playerAction;
		
		if(moving) {
			playerAction = RUNNING;
		}
		else {
			playerAction = IDLE;
		}
		
		if(inAir) {
			if (airSpeed < 0) 
				playerAction = JUMPING;
			else 
				playerAction = FALLING;
		}
		
		if(attacking) {
			playerAction = ATTACK1;
		}
		
		if (startAni != playerAction) {
			resetAniTick();
			
		}
		
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updateAnimationTick() {
		
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
			}
		}
	}
	
	private void loadAnimations() {
		
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			
			animations = new BufferedImage[9][6];
			for (int j = 0; j<animations.length; j++)
			for (int i = 0; i<animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i*64, j*40, 64, 40);
			}
	}
	
	public void loadLevelData(int[][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
}
