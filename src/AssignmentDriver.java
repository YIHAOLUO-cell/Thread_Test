import java.util.HashMap;

public class AssignmentDriver {


    public static void main(String[] args) throws Exception {

        //turn this or off to enable/disable output from the dispatch's logEvent function
        //use the logEvent function to print out debug output when required.
        boolean logEvents = true;

        HashMap<String, Integer> testRegions = new HashMap<String, Integer>();
        testRegions.put("Test Region", 50);



        /**
         * This driver has a number of different sections that you can uncomment as you progress through the assignment
         * Once you have completed all parts, you should be able to run this entire function uncommented successfully
         */

        // 创建 乘客
        Passenger testPassenger = new Passenger("Alex", 100);
        // 创建 司机
        Driver testDriver = new Driver("Barbara", 100);

        System.out.println(testPassenger);
        System.out.println(testDriver);


        // 司机接送乘客到达目的地
        testDriver.pickUpPassenger(testPassenger);
        System.out.println("ok,司机接到乘客");

        // 司机离开
        testDriver.driveToDestination();
        System.out.println("ok，司机走了");


        // 创建 NuberDispatch 对象
        NuberDispatch dispatch = new NuberDispatch(testRegions, logEvents);

        // 创建预定
        Booking b1 = new Booking(dispatch, testPassenger);
        Booking b2 = new Booking(dispatch, testPassenger);
        System.out.println(b1+ " " +b2);
        // 在区域中添加司机和预订乘客
        NuberRegion region = new NuberRegion(dispatch, "Test Region", 10);
        System.out.println("region是 " + region);

        //test adding a driver to dispatch
        dispatch.addDriver(testDriver);
        System.out.println("ok 添加司机");

        // test booking a single passenger
        dispatch.bookPassenger(testPassenger, "Test Region");

        // 关闭NuberDispatch
        dispatch.shutdown();





        //create NuberDispatch for given regions and max simultaneous jobs per region
        //once you have the above running, you should be able to uncomment the Simulations below to start to put everything together

        HashMap<String, Integer> regions = new HashMap<String, Integer>();
        regions.put("North", 50);
        regions.put("South", 50);


        try {
            new Simulation(regions, 1, 10, 1000, logEvents);
        } catch (Exception e) {
            e.printStackTrace();
        }



//        new Simulation(regions, 5, 10, 1000, logEvents);
//        new Simulation(regions, 10, 10, 1000, logEvents);
//        new Simulation(regions, 10, 100, 1000, logEvents);
//        new Simulation(regions, 1, 50, 1000, logEvents);
    }

}
