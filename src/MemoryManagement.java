import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class MemoryManagement {
    public static void main(String[] args){
        try{
            File f1=new File("sample\\input.txt");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            int currentTime = 0;
            Queue<Process> readyQueue = new PriorityQueue<>();
            int ramSize = Integer.parseInt(br.readLine().split(" ")[2]);
            int frameSize = Integer.parseInt(br.readLine().split(" ")[2]);
            int processNumber = Integer.parseInt(br.readLine().split(" ")[2]);
            int[] deadlines = new int[processNumber];
            for (int i = 0; i < processNumber; i++) {
                br.readLine();
                String name = br.readLine().split(" ")[1];
                int size = Integer.parseInt(br.readLine().split(" ")[1]);
                int time = Integer.parseInt(br.readLine().split(" ")[1]);
                int period = Integer.parseInt(br.readLine().split(" ")[1]);
                deadlines[i] = Integer.parseInt(br.readLine().split(" ")[1]);
                Process process = new Process(name, 0, size, time, period, deadlines[i]);
                readyQueue.add(process);
            }

            boolean[] ram = new boolean[ramSize/frameSize];
            for (int i = 0; i < ram.length; i++) {
                ram[i] = false;
            }

            //cpu
            boolean turn = false;
            int quantumSize = 3;
            while (readyQueue.size() > 0){
                int remainTime = quantumSize;
                Process process = readyQueue.poll();
                //start quantum
                while (((readyQueue.size() > 0 && (readyQueue.peek().getTurnTime() != 0 || readyQueue.peek().isFirst())) && remainTime > 0) ||
                        (readyQueue.size() == 0 && remainTime > 0)) {
                    currentTime++;
                    remainTime--;
                    process.updateRemainTime();
                    updateTurnTimes(readyQueue);
                    process.setFirst(false);
                    if(readyQueue.size() > 0){
                        readyQueue.add(readyQueue.poll());
                    }
                }
                //end quantum
                System.out.println("current time: " + currentTime);
                process.setTurnTime(process.getInterval());
                process.printProcess();
                if(process.getRemainTime() > 0)
                    readyQueue.add(process);;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ramUpdate(boolean[] ram, Process process){

    }
    public static void updateTurnTimes(Queue<Process> processes){
        processes.forEach(process -> process.setTurnTime(Math.max(process.getTurnTime() - 1, 0)));
    }
}
