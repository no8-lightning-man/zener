package com.n8lm.zener.netdemo;

import com.n8lm.zener.netdemo.messages.AddCharacterMessage;
import com.n8lm.zener.netdemo.messages.LoginMessage;
import com.n8lm.zener.netdemo.messages.UpdateCharacterMessage;
import com.n8lm.zener.network.NetworkConfiguration;
import com.n8lm.zener.netdemo.messages.MoveMessage;

/**
 * Created on 2014/11/2.
 *
 * @author Alchemist
 */
public class MyNetworkConfiguration extends NetworkConfiguration {
    @Override
    protected void addMessageTypes() {
        addMessage(LoginMessage.class);
        addMessage(MoveMessage.class);
        addMessage(AddCharacterMessage.class);
        addMessage(UpdateCharacterMessage.class);
    }

    @Override
    public int getServerTCPPort() {
        return 55554;
    }

    @Override
    public int getServerUDPPort() {
        return 55555;
    }

}
