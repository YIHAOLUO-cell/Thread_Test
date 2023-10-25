import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Booking类 and BookingResult是我们模拟出租车调度系统的关键部分
 * Booking类代表一个乘客的旅行过程，乘客上车 - 乘客到达目的地
 */

public class Booking {
    private static AtomicInteger nextId = new AtomicInteger();
    private int id;
    private NuberDispatch dispatch;
    private Passenger passenger;
    private Driver driver;
    private Date startTime;
    /**
     * Creates a new booking for a given Nuber dispatch and passenger, noting that no
     * driver is provided as it will depend on whether one is available when the region
     * can begin processing this booking.
     *
     * @param dispatch
     * @param passenger
     */
    public Booking(NuberDispatch dispatch, Passenger passenger)
    {
        this.id = nextId.getAndIncrement();
        this.dispatch = dispatch;
        this.passenger = passenger;
        this.startTime = new Date();
    }

    /**
     * At some point, the Nuber Region responsible for the booking can start it (has free spot),
     * and calls the Booking.call() function, which:
     * 1.	Asks Dispatch for an available driver
     * 2.	If no driver is currently available, the booking must wait until one is available.
     * 3.	Once it has a driver, it must call the Driver.pickUpPassenger() function, with the
     * 			thread pausing whilst as function is called.
     * 4.	It must then call the Driver.driveToDestination() function, with the thread pausing
     * 			whilst as function is called.
     * 5.	Once at the destination, the time is recorded, so we know the total trip duration.
     * 6.	The driver, now free, is added back into Dispatch�s list of available drivers.
     * 7.	The call() function the returns a BookingResult object, passing in the appropriate
     * 			information required in the BookingResult constructor.
     *
     * @return A BookingResult containing the final information about the booking
     */

    /**
     * 描述了一个乘客的整个旅行过程。在这个方法中，
     * 我们需要从NuberDispatch获取一个可用的司机，然后让司机接客和送客。
     * 我们需要记录旅行的开始时间和结束时间，以便计算旅行的总时间。
     * 在旅行结束后，我们需要将司机添加回NuberDispatch的可用司机列表。
     * @return
     */
    public BookingResult call() throws InterruptedException {
        driver = dispatch.getDriver();
        while(driver == null) {
            Thread.sleep(1000);
            driver = dispatch.getDriver();
        }
        driver.pickUpPassenger(passenger);
        driver.driveToDestination();
        Date endTime = new Date();
        long tripDuration = endTime.getTime() - startTime.getTime();
        dispatch.addDriver(driver);
        return new BookingResult(id, passenger, driver, tripDuration);
    }

    /***
     * Should return the:
     * - booking ID,
     * - followed by a colon,
     * - followed by the driver's name (if the driver is null, it should show the word "null")
     * - followed by a colon,
     * - followed by the passenger's name (if the passenger is null, it should show the word "null")
     *
     * @return The compiled string
     */

    /**
     * 实现toString方法，这个方法需要返回一个包含了Booking的ID、司机的名字和乘客的名字的字符串。
     * @return
     */
    @Override
    public String toString()
    {
        String driverName = driver == null ? "null" : driver.name;
        String passengerName = passenger == null ? "null" : passenger.name;
        return id + ":" + driverName + ":" + passengerName;
    }

}
