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
            LinkedList<String> externalMemory = new LinkedList<>();
            int ramSize = Integer.parseInt(br.readLine().split(" ")[2]);
            int frameSize = Integer.parseInt(br.readLine().split(" ")[2]);
            int processNumber = Integer.parseInt(br.readLine().split(" ")[2]);
            br.readLine();
            int baseAddress = 0;
            for (int i = 0; i < processNumber; i++) {
                String[] processInfo = br.readLine().split(" ");
                Process process = new Process(processInfo[0], 0, Integer.parseInt(processInfo[4]),
                        Integer.parseInt(processInfo[1]), Integer.parseInt(processInfo[3]), Integer.parseInt(processInfo[2]), baseAddress);
                baseAddress += process.getSize();
                readyQueue.add(process);
                externalMemory.add(process.getProcessName());
            }

            int pageNumber = ramSize/frameSize;
            LinkedList<String> ram = new LinkedList<>();
            int emptyPages = pageNumber;

            for(int i = 0; i < pageNumber; i++) {
                ram.add("empty");
            }

            //cpu
            int quantumSize = 3;
            int[][] pageTable = new int[pageNumber][2]; //pageTable[i][0] = memBase and pageTable[i][0] = pageNumber
            while (readyQueue.size() > 0){
                int remainTime = quantumSize;
                Process process;
                //ram check
                if(ram.contains(readyQueue.peek().getProcessName()))
                    process = readyQueue.poll();
                else{
                    externalMemory.remove(readyQueue.peek().getProcessName());
                    int count = 0;
                    int needPages;
                    if(readyQueue.peek().getSize() % frameSize == 0)
                        needPages = Math.round(readyQueue.peek().getSize() / frameSize);
                    else
                        needPages = Math.round(readyQueue.peek().getSize() / frameSize) + 1;
                    if(emptyPages >= needPages){
                        process = readyQueue.poll();
                        externalMemory.remove(process.getProcessName());
                        for(int i = 0; i < pageNumber ; i++) {
                            if(ram.get(i).equals("empty") && count < needPages){
                                pageTable[i][0] = process.getMemBase() + count * frameSize;
                                pageTable[i][1] = process.getMemBase() + count * frameSize;
                                ram.set(i, process.getProcessName());
                                count++;
                                emptyPages--;
                            }
                        }
                    }
                    else{
                        process = readyQueue.poll();
                        externalMemory.add(ram.get(0));
                        externalMemory.remove(process.getProcessName());
                        while (emptyPages < needPages){
                            int index = 0;
                            for (index = 0; index < pageNumber ; index++) {
                                if(!ram.get(index).equals("empty"))
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
                            if(ram.get(i).equals( "empty") && count < needPages){
                                ram.set(i, process.getProcessName());
                                pageTable[i][0] = process.getMemBase() + count * frameSize;
                                pageTable[i][1] = process.getMemBase() + count * frameSize;
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
                    System.out.println("page table is:");
                    System.out.println("memory base   page offset");
                    for (int i = 0; i < pageNumber; i++) {
                        System.out.println(pageTable[i][0] + "   "  + pageTable[i][1]);
                    }
                    //memory
                    System.out.println("Processes that are in Memory: ");
                    externalMemory.forEach(s -> System.out.print(s + ", "));
                    System.out.println("\n*****");
                }
                //end or stop quantum
                process.setTurnTime(process.getInterval());
                if(process.getRemainTime() > 0)
                    readyQueue.add(process);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTurnTimes(Queue<Process> processes){
        processes.forEach(process -> process.setTurnTime(Math.max(process.getTurnTime() - 1, 0)));
    }
}