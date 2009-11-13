package exceptions;

/**
 *
 * @author Scheinecker Thomas
 */
public class IllegalTagIdFormatException extends Exception{

    private String illegalId;

    public IllegalTagIdFormatException() {}

    public IllegalTagIdFormatException(String id) {
        illegalId = id;
    }

    public String getIllegalTagId() {
        return illegalId;
    }


}
