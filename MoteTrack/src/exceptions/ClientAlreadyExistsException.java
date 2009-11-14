package exceptions;

import network.ServerReader;

/**
 *
 * @author Scheinecker Thomas
 */
public class ClientAlreadyExistsException extends Exception{

    private ServerReader client;

    public ClientAlreadyExistsException(ServerReader client) {
        super();
        this.client = client;
    }

    public ServerReader getClient() {
        return client;
    }

}
