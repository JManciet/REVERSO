import dao.ConnectionDao;

public class Main {
    public static void main(String[] args) {

        ConnectionDao.getConnection();
        ConnectionDao.getConnection();
        System.out.println("Hello world!");
    }
}