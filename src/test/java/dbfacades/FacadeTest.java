package dbfacades;

import dbfacades.DemoFacade;
import entity.Car;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * UNIT TEST example that mocks away the database with an in-memory database See
 * Persistence unit in persistence.xml in the test packages
 *
 * Use this in your own project by: - Delete everything inside the setUp method,
 * but first, observe how test data is created - Delete the single test method,
 * and replace with your own tests
 *
 */
public class FacadeTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu-test", null);
//EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu", null);

    DemoFacade facade = new DemoFacade(emf);

    /**
     * Setup test data in the database to a known state BEFORE Each test
     */
    @Before
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete all, since some future test cases might add/change data
            em.createQuery("delete from Car").executeUpdate();
            //Add our test data
            Car e1 = new Car("Volve");
            Car e2 = new Car("WW");
            em.persist(e1);
            em.persist(e2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    

    @Test
    public void testGetCarByMake() {
        Car car = facade.getCarsByMake("WW");
        Assert.assertEquals("WW", car.getMake());
    }
    
    @Test
    public void testGetCarByID() {
        List<Car> cars = facade.getAllCars();
        int ID = cars.get(0).getId();
        String make = cars.get(0).getMake();
        Car car = facade.getCarByID(ID);
        Assert.assertEquals(make, car.getMake());
    }
    
    @Test
    public void testDeleteCarByID(){
        List<Car> cars = facade.getAllCars();
        facade.deleteCarByID(cars.get(0).getId());
        long count = facade.countCars();
        Assert.assertEquals(1, count);
    }

    @Test
    public void testGetAllCars() {
        List<Car> allCars = facade.getAllCars();
        Assert.assertEquals(2, allCars.size());
        Assert.assertEquals("Volve", allCars.get(0).getMake());
    }

    // Test the single method in the Facade
    @Test
    public void countEntities() {
        EntityManager em = emf.createEntityManager();
        try {
            long count = facade.countCars();
            Assert.assertEquals(2, count);
        } finally {
            em.close();
        }
    }

}
