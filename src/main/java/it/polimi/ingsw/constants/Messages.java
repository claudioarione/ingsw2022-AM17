package it.polimi.ingsw.constants;

public class Messages {
    public static final String ASK_USERNAME = "Insert username: ";
    public static final String GRACEFUL_TERM = "Application will now close...";

    // Possible status in messages
    public static final String STATUS_LOGIN = "LOGIN";
    public static final String STATUS_ACTION = "ACTION";
    public static final String STATUS_UPDATE = "UPDATE";
    public static final String STATUS_END = "END";

    // Errors while joining game
    public static final String INVALID_USERNAME = "Invalid username field";
    public static final String USERNAME_TOO_LONG = "Username is too long (max 32 characters)";
    public static final String USERNAME_ALREADY_TAKEN = "Username is too long (max 32 characters)";
    public static final String LOBBY_FULL = "The lobby is full";
    public static final String INVALID_NUM_PLAYERS = "Num players must be between 2 and 4";
    public static final String INVALID_PLAYER_CREATING_GAME = "You can't set this game's parameters";
    public static final String INVALID_FORMAT_NUM_PLAYER = "Num players must be a number";

    // Messages in game creation
    public static final String ADDED_PLAYER = "Added player ";
    public static final String SET_GAME_PARAMETERS = "Added player ";
    public static final String NEW_PLAYER_JOINED = "A new player has joined";
    public static final String GAME_STARTING = "A new game is starting";
    public static final String GAME_CREATED = "A new game was created!";
}
