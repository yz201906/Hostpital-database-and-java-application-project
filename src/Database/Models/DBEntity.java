package Database.Models;

public interface DBEntity {
    public void add ();

    public void update ( Object o );

    public void delete ( int id );

    public static Object getById(int id)
    {
    	
    	return new Object();
    }
}
