package DataBase;

public interface DatabaseConnectionAdapter<I,C>
{
    public String createConnection(C c);
    public I getConnection();
}
