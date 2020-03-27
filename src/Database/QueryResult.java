package Database;

public class QueryResult {
    public Boolean IsSuccessful;
    public String Message;
    public boolean isSpecificException;

    public QueryResult(Boolean isSuccessful, String message) {
        this.IsSuccessful = isSuccessful;
        this.Message = message;
    }

    public QueryResult(Boolean isSuccessful, String message, boolean isSpecificException) {
        this.IsSuccessful = isSuccessful;
        this.Message = message;
        this.isSpecificException = isSpecificException;
    }
}