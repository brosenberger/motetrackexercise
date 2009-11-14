package exceptions;

import network.ServerDataReader;

/**
 *
 * @author Scheinecker Thomas
 */
public class ClientAlreadyExistsException extends Exception{

    private ServerDataReader client;

    public ClientAlreadyExistsException(ServerDataReader client) {
        super();
        this.client = client;
    }

    public ServerDataReader getClient() {
        return client;
    }

}
