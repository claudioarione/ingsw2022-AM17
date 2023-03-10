package it.polimi.ingsw.client.observers.game_actions.play_assistant;

import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.messages.action.Action;
import it.polimi.ingsw.messages.action.ActionArgs;
import it.polimi.ingsw.messages.action.ClientActionMessage;
import it.polimi.ingsw.utils.constants.Constants;

public class SendPlayAssistantObserver implements PlayAssistantObserver {

    private final MessageHandler mh;

    public SendPlayAssistantObserver(MessageHandler mh) {
        this.mh = mh;
        mh.attachPlayAssistantObserver(this);
    }


    @Override
    public void onAssistantPlayed(int value) {
        ActionArgs args = new ActionArgs();
        args.setValue(value);

        Action action = new Action(Constants.ACTION_PLAY_ASSISTANT, args);

        ClientActionMessage toSend = new ClientActionMessage();
        toSend.setAction(action);
        toSend.setPlayer(mh.getNetworkClient().getClient().getUsername());

        mh.getNetworkClient().sendMessageToServer(toSend.toJson());
    }
}
