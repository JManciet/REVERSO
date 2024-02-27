import dao.ConnectionDao;
import exceptions.DaoException;

public class Main {
    public static void main(String[] args) {

        try {
            ConnectionDao.getConnection();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Hello world!");
    }
}