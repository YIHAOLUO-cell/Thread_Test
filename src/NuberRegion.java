import java.util.concurrent.Future;
import java.util.concurrent.*;

/**
 * NuberRegion 类表示一个独立的区域，每个区域都有一个最大同时工作数的设置
 * 这个设置定义了在任何时候可以有多少个预定可以同时有司机在执行
 */
public class NuberRegion {

    private volatile boolean isShuttingDown = false;
    private NuberDispatch dispatch;
    private String regionName;
    private int maxSimultaneousJobs;
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Booking> pendingBookings;


    public NuberRegion(NuberDispatch dispatch, String regionName, int maxSimultaneousJobs)
    {
        this.dispatch = dispatch;
        this.regionName = regionName;
        this.maxSimultaneousJobs = maxSimultaneousJobs;
        this.executor = Executors.newFixedThreadPool(maxSimultaneousJobs);
        this.pendingBookings = new ConcurrentLinkedQueue<>(); // 初始化 pendingBookings
    }

    /**
     * Creates a booking for given passenger, and adds the booking to the
     * collection of jobs to process. Once the region has a position available, and a driver is available,
     * the booking should commence automatically.
     *
     * If the region has been told to shutdown, this function should return null, and log a message to the
     * console that the booking was rejected.
     *
     * @param waitingPassenger
     * @return a Future that will provide the final BookingResult object from the completed booking
     */
    public Future<BookingResult> bookPassenger(Passenger waitingPassenger)
    {
        if(executor.isShutdown()){
            System.out.println("线程池已经关闭了，不要提交了");
            return  null;
        }
        Booking booking = new Booking(dispatch, waitingPassenger);
        pendingBookings.add(booking); // 添加预定到待处理队列
        return executor.submit(booking::call);
    }

    /**
     * Called by dispatch to tell the region to complete its existing bookings and stop accepting any new bookings
     */
    public void shutdown()
    {
        executor.shutdown();
    }

    public int getPendingBookingsCount() {
        return pendingBookings.size();
    }

}
