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
            LinkedList<String> memory = new LinkedList<>();
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
                memory.add(process.getProcessName());
            }

            int pageNumber = ramSize/frameSize;
            LinkedList<String> ram = new LinkedList<>();
            int emptyPages = pageNumber;
            LinkedList<LinkedList<String>>  

            for(int i = 0; i < pageNumber; i++) {
                ram.add("empty");
            }

            //cpu
            boolean turn = false;
            int quantumSize = 3;
            while (readyQueue.size() > 0){
                int remainTime = quantumSize;
                Process process = null;
                //ram check
                if(ram.contains(readyQueue.peek().getProcessName()))
                    process = readyQueue.poll();
                else{
                    memory.remove(readyQueue.peek().getProcessName());
                    int count = 0;
                    int needPages;
                    if(readyQueue.peek().getSize() % frameSize == 0)
                        needPages = Math.round(readyQueue.peek().getSize() / frameSize);
                    else
                        needPages = Math.round(readyQueue.peek().getSize() / frameSize) + 1;
                    if(emptyPages >= needPages){
                        process = readyQueue.poll();
                        memory.remove(process.getProcessName());
                        for(int i = 0; i < pageNumber ; i++) {
                            if(ram.get(i) == "empty" && count < needPages){
                                ram.set(i, process.getProcessName());
                                count++;
                                emptyPages--;
                            }
                        }
                    }
                    else{
                        process = readyQueue.poll();
                        memory.add(ram.get(0));
                        memory.remove(process.getProcessName());
                        while (emptyPages < needPages){
                            int index = 0;
                            for (index = 0; index < pageNumber ; index++) {
                                if(ram.get(index) != "empty")
                                    break;
                            }
                            for (int i = index + 1; i < pageNumber; i++) {
                                if (ram.get(i) == ram.get(index)) {
                                    ram.set(i, "empty");
                                    emptyPages++;
                                }
                            }
                            ram.set(index, "empty");
                            emptyPages++;
                        }
                        for(int i = 0; i < pageNumber ; i++) {
                            if(ram.get(i) == "empty" && count < needPages){
                                ram.set(i, process.getProcessName());
                                count++;
                                emptyPages--;
                            }
                        }
                    }

                }
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

                    //show result
                    System.out.println("current time: " + currentTime);
                    System.out.println("Name of the Process on CPU is: " + process.getProcessName());
                    //ram
                    System.out.println("ram pages are: ");
                    ram.forEach(s -> System.out.print(s + ", "));
                    //page table
                    System.out.println();
                    //memory
                    System.out.println("Processes that are in Memory: ");
                    memory.forEach(s -> System.out.print(s + ", "));
                    System.out.println("\n*****");
                }
                //end or stop quantum
                process.setTurnTime(process.getInterval());
                if(process.getRemainTime() > 0)
                    readyQueue.add(process);;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateTurnTimes(Queue<Process> processes){
        processes.forEach(process -> process.setTurnTime(Math.max(process.getTurnTime() - 1, 0)));
    }
}