

public class Passenger extends Person
{
    // 初始化乘客的姓名以及最大睡眠时间
    public Passenger(String name, int maxSleep) {
        super(name, maxSleep);
    }
    // 获取乘客的旅行时间
    public int getTravelTime()
    {
        return (int)(Math.random() * maxSleep);
    }

}
