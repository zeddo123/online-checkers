package src.server;

import src.shared.ClientIterface;

public class Pair {
    public User user1;
    public User user2;

    public Pair(){}
    public Pair(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }

    public Pair contains(ClientIterface client) {
        if(user1.equals(client))
            return this;
        if(user2.equals(client))
            return new Pair(user2, user1);
        return null;
    }
}
