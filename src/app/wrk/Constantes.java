/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk;

import java.awt.Color;

/**
 *
 * @author dingl01
 */
public interface Constantes {

    // Wrk
    public final int ENTITY_UPDATE_SCREEN_OFFSET = 4 * 32;

    // Sound
    public final String SOUND_MAIN_THEME = "main_theme";
    public final String SOUND_UNDERGROUND_THEME = "underground_theme";
    public final String SOUND_BUMP = "bump";
    public final String SOUND_POWERUP_APPEARS = "powerup_appears";
    public final String SOUND_BREAK_BRICK = "break_brick";
    public final String SOUND_COIN = "coin";
    public final String SOUND_POWERUP_CONSUMED = "powerup_consumed";
    public final String SOUND_1UP = "1_up";
    public final String SOUND_DEATH = "death";
    public final String SOUND_FIREBALL = "fireball";
    public final String SOUND_GAME_OVER = "game_over";
    public final String SOUND_JUMP = "jump";
    public final String SOUND_SHELL_BOUNCE = "shell_bounce";
    public final String SOUND_SHELL_THROWN = "shell_thrown";
    public final String SOUND_PIPE = "pipe";
    public final String SOUND_FLAG = "flag";
    public final String SOUND_STAGE_CLEAR = "stage_clear";

    // Map
    public final char LETTER_BLOCK_VOID = '.';
    public final char LETTER_BLOCK_FLOOR = 'f';
    public final char LETTER_BLOCK_BRICK = 'b';
    public final char LETTER_BLOCK_MISTERY_FULL_MUSCHROOM = 'm';
    public final char LETTER_BLOCK_MISTERY_EMPTY = 'M';
    public final char LETTER_BLOCK_MISTERY_FULL_COIN = 'c';
    public final char LETTER_BLOCK_MISTERY_FULL_FLOWER = 'F';
    public final char LETTER_BLOCK_PIPE = 'p';
    public final char LETTER_BLOCK_PIPE_TOP = 'P';
    public final char LETTER_BLOCK_CONCRETE = 'C';
    public final char LETTER_ENTITY_COIN = '°';
    public final char NUMBER_ENTITY_GOOMBA = '1';
    public final char NUMBER_ENTITY_KOOPA = '2';
    public final char OTHER_START_POINT = '$';
    public final char OTHER_END_FLAG_TOP = '+';
    public final char OTHER_END_FLAG_BODY = '£';
    public final char OTHER_MOVING_PLATFORM = '_';

    // Map types
    public final int MAP_TYPE_NORMAL = 0;
    public final int MAP_TYPE_UNDERGROUND = 1;
    public final int MAP_TYPE_SKY = 2;

    // Physics
    public final float GRAVITY = 0.43f; //0.43
    public final float PARTICLE_GRAVITY = 0.4f;
    public final float AIR_SLOW_EFFECT = 0.2f;
    public final float GROUND_SLOW_EFFECT = 0.5f; 
    public final float PARTICLE_AIR_SLOW_EFFECT = 0.4f;

    // Blocks
    public final float BLOCK_BUMP_UP_VEL = 2.5f;

    // Entity
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final float POWERUP_GROWING_TIME = 100f;

    // Mario
    public final int MARIO_STATE_SMALL = 0;
    public final int MARIO_STATE_NORMAL = 1;
    public final int MARIO_STATE_FIRE = 2;
    public final float MARIO_HIT_INVINCIBLE_DURATION = 5f;

    // Koopa
    public final float SHELLMODE_VELX = 4f;

    // Fireball
    public final float FIREBALL_BOUNCE_VELOCITY = 7;
    public final int FIREBALL_BOUNCES_AMOUNT = 3;
    public final float FIREBALL_COOLDOWN_TIME = 0.2f;
    public final float FIREBALL_MAX_QTT_AMOUNT = 2;
    public final int FIREBALL_ID = 30;

    // Animation
    public final String ANIMATION_FILE_EXT = ".anm";

    // Still / Jump
    public final int ANIMATION_MARIO_LEFT = 0;
    public final int ANIMATION_MARIO_RIGHT = 1;

    // End Flag
    public final float END_FLAG_FALLING_SPEED = 3f;
    public final int END_FLAG_ID = 10;

    // Debug
    public final int DEBUG_SHOW_HITBOX = 114; //F3
    public final Color DEBUG_HITBOX_COLOR = Color.green;
    public final int DEBUG_NOCLIP = 112; //F1
    public final float NO_CLIP_SPEED = 7;

    // Muschroom
    public final int POWERUP_ID = 20;

    // Link
    public final int LINK_WIDTH = 16;
    public final int LINK_HEIGHT = 64;

    // Decoration 
    public final String DECORATION_TYPE_GROUND = "ground";
    public final String DECORATION_TYPE_SKY = "sky";
    public final String DECORATION_TYPE_CASTLE = "castle";

    // Castles
    public final String CASTLE_TYPE_END = "castle_end.png";
    public final String CASTLE_TYPE_START = "castle_start.png";

    // Moving Platform 
    public final float MOVING_PLATFORM_SPEED = 0.5f;
    
}
