package DataBase;

public enum DatabaseCredentialFirebase
{
    SEVICECONNECTION("JSON INFO"),
    URLDATABASE("https://<PROJECTID>.firebaseio.com/");

    private final String VALUE;

    private DatabaseCredentialFirebase(String VALUE) {
        this.VALUE = VALUE;
    }

    public String getVALUE() {
        return VALUE;
    }
}
