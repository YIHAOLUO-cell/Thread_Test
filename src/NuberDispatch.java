import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;
public class NuberDispatch {

    private final int MAX_DRIVERS = 999;
    private boolean logEvents;
    private ConcurrentHashMap<String, NuberRegion> regions;
    private ConcurrentLinkedQueue<Driver> drivers;
    private volatile boolean isShuttingDown = false;

    public NuberDispatch(HashMap<String, Integer> regionInfo, boolean logEvents) {
        this.logEvents = logEvents;
        this.regions = new ConcurrentHashMap<>();
        this.drivers = new ConcurrentLinkedQueue<>();
        for (Map.Entry<String, Integer> entry : regionInfo.entrySet()) {
            regions.put(entry.getKey(), new NuberRegion(this, entry.getKey(), entry.getValue()));
        }
    }

    public boolean addDriver(Driver newDriver) {
        if (drivers.size() >= MAX_DRIVERS) {
            return false;
        }
        drivers.add(newDriver);
        return true;
    }

    public Driver getDriver() {
        return drivers.poll();
    }

    // Other methods...
    public void logEvent(Booking booking, String message) {

        if (!logEvents) return;

        System.out.println(booking + ": " + message);

    }
    public Future<BookingResult> bookPassenger(Passenger passenger, String regionName) {
//        if(isShuttingDown) { // 检查是否正在关闭
//            CompletableFuture<BookingResult> future = new CompletableFuture<>();
//            future.completeExceptionally(new IllegalStateException("Cannot book a passenger because the dispatch is shutting down."));
//            return future;
//        }
//        NuberRegion region = regions.get(regionName);
//        if (region == null) {
//            CompletableFuture<BookingResult> future = new CompletableFuture<>();
//            future.completeExceptionally(new IllegalArgumentException("Invalid region name: " + regionName));
//            return future;
//        }
//        Future<BookingResult> result = region.bookPassenger(passenger);
//        if(result == null) {
//            CompletableFuture<BookingResult> future = new CompletableFuture<>();
//            future.completeExceptionally(new IllegalStateException("Booking could not be made because the region is shutting down."));
//            return future;
//        }
//        return result;
        if(isShuttingDown) { // 检查是否正在关闭
            return null;
        }
        NuberRegion region = regions.get(regionName);
        if (region == null) {
            CompletableFuture<BookingResult> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalArgumentException("Invalid region name: " + regionName));
            return future;
        }
        Future<BookingResult> result = region.bookPassenger(passenger);
        if(result == null) {
            return null;
        }
        return result;
    }

    public void shutdown() {
        isShuttingDown = true; // 设置为正在关闭
        for (NuberRegion region : regions.values()) {
            region.shutdown();
        }
    }
    public int getBookingsAwaitingDriver() {
        int total = 0;
        for (NuberRegion region : regions.values()) {
            // getPendingBookingsCount() 等待司机的预定数量
            total += region.getPendingBookingsCount();
        }
        return total;
    }

}