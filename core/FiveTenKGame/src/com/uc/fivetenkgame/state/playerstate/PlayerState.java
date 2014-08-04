package com.uc.fivetenkgame.state.playerstate;

import com.uc.fivetenkgame.player.PlayerContext;
import com.uc.fivetenkgame.state.State;

/**
 * player状态的父类抽象类，引入PlayerContext作为状态与player交互的接口
 * 
 * @author lm
 * 
 */
public abstract class PlayerState implements State {

    protected PlayerContext mPlayerContext;

    public PlayerState(PlayerContext context) {
        this.mPlayerContext = context;
    }

    @Override
    public String toString() {
        String name = this.getClass().getName();
        return "当前状态：" + name.substring(name.lastIndexOf("."));
    }
}
