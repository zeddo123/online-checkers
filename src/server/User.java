package src.server;

import src.shared.ClientIterface;

public class User {
    public ClientIterface client;
    public GameMetaData metaData;

    public User(ClientIterface client, GameMetaData metaData) {
        this.client = client; this.metaData = metaData;
    }

    public boolean equals(ClientIterface client) {
        return this.client.equals(client);
    }

}
