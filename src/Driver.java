/**
 * Driver类
 */
public class Driver extends Person {
    private Passenger currentPassenger; //存储当前乘客

    // 初始化司机的名称 以及最大睡眠时间
    public Driver(String driverName, int maxSleep) {
        super(driverName, maxSleep);
    }


    public void pickUpPassenger(Passenger newPassenger) throws InterruptedException {
        // 将乘客可添加到currentPassenger
        this.currentPassenger = newPassenger;
        // 线程休眠一段时间
        Thread.sleep((long)(Math.random() * maxSleep));
    }
    // 根据当前乘客的旅行时间睡眠想等待相应时间
    public void driveToDestination() throws InterruptedException {
        if(currentPassenger != null) {
            Thread.sleep(currentPassenger.getTravelTime());
        }
    }
}