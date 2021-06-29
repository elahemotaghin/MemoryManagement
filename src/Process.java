public class Process implements Comparable<Process> {
    private String processName;
    private int size = 0;
    private int time = 0;
    private int interval = 0;
    private int deadline = 0;
    private int remainTime = 0;
    private int turnTime = 0;
    private boolean first;

    public Process(String processName, int turnTime, int size, int time, int interval, int deadline){
        this.processName = processName;
        this.size = size;
        this.time = time;
        this.interval = interval;
        this.deadline = deadline;
        this.remainTime = time;
        this.turnTime = turnTime;
        this.first = true;
    }

    public void printProcess(){
        System.out.println("process name: " + processName);
        System.out.println("size: " + size);
        System.out.println("time: " + time);
        System.out.println("interval: " + interval);
        System.out.println("deadline: " + deadline);
        System.out.println("remain time: " + Math.max(remainTime, 0));
        System.out.println("wait time: " + turnTime);
        System.out.println();
    }

    public void updateRemainTime(){
        setRemainTime(remainTime - 1);
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getProcessName() {
        return processName;
    }

    public int getSize() {
        return size;
    }

    public int getTime() {
        return time;
    }

    public int getInterval() {
        return interval;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public int getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(int turnTime) {
        this.turnTime = turnTime;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    public int compareTo(Process o) {
        if(this.getTurnTime() == 0 && o.getTurnTime() == 0)
            return this.getDeadline() - o.getDeadline();
        else if(this.getTurnTime() != 0 && o.getTurnTime() != 0)
            return this.getTurnTime() - o.getTurnTime();
        else if(this.getTurnTime() != 0)
            return 1;
        else
            return -1;
    }
}
