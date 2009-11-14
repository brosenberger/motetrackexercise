package exceptions;

/**
 *
 * @author Scheinecker Thomas
 */
public class IllegalTagIdFormatException extends Exception{

    private String illegalId;

    public IllegalTagIdFormatException() {
        super();
    }

    public IllegalTagIdFormatException(String id) {
        this();
        illegalId = id;
    }

    public String getIllegalTagId() {
        return illegalId;
    }


}
