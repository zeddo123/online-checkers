package src.shared;

import src.server.GameMetaData;

public interface ServerInterface {
    public boolean registerPlayer(ClientIterface client, GameMetaData metaData);
}
