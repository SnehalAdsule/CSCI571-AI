
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sneh
 */
public class oldMancala {

    int Task;
    int Player;
    int CutOffDepth;
    int BoardPlayer2[];
    int BoardPlayer1[];
    int Mancala2;
    int Mancala1;
    int evalFunction[];
    int BoardSize;
    int INFINITY = Integer.MAX_VALUE;
    public static oldMancala mancala;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        readInputFile("C:\\Users\\Snehal\\Documents\\NetBeansProjects\\mancala\\src\\input.txt");
        if (mancala.Task == 1) {
            mancala.Greedy();
        }
    }

    public int Greedy() {
        try {
            int positionMAX = mancala.GreedyEVAL();
            int side = mancala.Player;

            while (positionMAX >= 0) {
                System.out.println("positionMAX" + positionMAX);
                int length = mancala.BoardSize;
                int i = positionMAX;
                
                    int player = mancala.Player;
                    int NoOfSeeds = ((player == 1) ? mancala.BoardPlayer1[i] : mancala.BoardPlayer2[i]);
                    System.out.print("No of Seeds " + NoOfSeeds);
                    System.out.println("=======================================");
                    if (player == 1) {
                        mancala.BoardPlayer1[i] = 0;
                    } else {
                        mancala.BoardPlayer2[i] = 0;
                    }
                    int tempCap1 = mancala.Mancala1;
                    int tempCap2 = mancala.Mancala2;
                    int next = (i + 1);
                    side = mancala.Player;
                    int lastIndex = -1;
                    if(NoOfSeeds==0)
                        break;

                    while (NoOfSeeds > 0) {
                        if (side == 1) {
                            for (int j = next; j < length && NoOfSeeds > 0; j++) {
                                System.out.println((j < length) + "i" + i + "j" + j + "No of seeds " + NoOfSeeds);
                                mancala.BoardPlayer1[j]++;
                                NoOfSeeds--;
                                lastIndex = j;
                                System.out.println(" i" + next + "j" + j + "No of seeds " + NoOfSeeds);
                            }
                            if (NoOfSeeds > 0) {
                                tempCap1++;
                                NoOfSeeds--;
                                side = 2;
                                next = length - 1;
                            }
                            System.out.println("i" + i + "No of seeds " + NoOfSeeds + " Side" + side + "next" + next);
                        } else {
                            for (int k = next; k >= 0 && k<length && NoOfSeeds > 0; k--) {
                                // System.out.println("i" + i + "No of seeds " + NoOfSeeds + " Side" + side +"next"+next);
                                mancala.BoardPlayer2[k]++;
                                NoOfSeeds--;
                                lastIndex = k;
                                System.out.println("i" + next + "k" + k + "No of seeds " + NoOfSeeds);
                            }
                            if (NoOfSeeds > 0) {
                                side = 1;
                                tempCap2++;
                                NoOfSeeds--;
                                next = 0;
                            }
                        }
                        //NoOfSeeds--;
                    }
                    if (mancala.Player == 1) {
                        if (mancala.Player == side && lastIndex > -1) {
                            if (mancala.BoardPlayer1[lastIndex] == 1) {
                                tempCap1 = tempCap1 + mancala.BoardPlayer2[lastIndex] + 1;
                                mancala.BoardPlayer2[lastIndex] = 0;
                                mancala.BoardPlayer1[lastIndex] = 0;
                            }
                        }

                    } else {
                        if (mancala.Player == side && lastIndex > -1) {
                            if (mancala.BoardPlayer2[lastIndex] == 1) {
                                tempCap2 = tempCap2 + mancala.BoardPlayer1[lastIndex] + 1;
                                mancala.BoardPlayer1[lastIndex] = 0;
                                mancala.BoardPlayer2[lastIndex] = 0;
                            }
                        }

                    }

                    mancala.Mancala1 = tempCap1;
                    mancala.Mancala2 = tempCap2;
                    System.out.println("Last :: "+lastIndex);
                    print();
                
                if(((lastIndex==3&& next==3)||(lastIndex==0&& next==0)))
                positionMAX = mancala.GreedyEVAL();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void print() {
        System.out.print("Task : " + mancala.Task
                + "\t Player : " + mancala.Player + "\t Cut off : " + mancala.CutOffDepth + "\n");
        for (int seeds = 0; seeds < mancala.BoardSize; seeds++) {
            System.out.print(mancala.BoardPlayer2[seeds] + " ");
        }
        System.out.print("\n");
        for (int seeds : mancala.BoardPlayer1) {
            System.out.print(seeds + " ");
        }
        System.out.println("\n" + mancala.Mancala2);
        System.out.println(mancala.Mancala1);
    }

    public static boolean readInputFile(String inputFile) {
        try {
            mancala = new oldMancala();
            Path path = Paths.get(inputFile);
            String line = null;
            BufferedReader reader = Files.newBufferedReader(path);
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;

                if (lineCount == 1) {
                    mancala.Task = Integer.parseInt(line);
                } else if (lineCount == 2) {
                    mancala.Player = Integer.parseInt(line);
                } else if (lineCount == 3) {
                    mancala.CutOffDepth = Integer.parseInt(line);
                } else if (lineCount == 4) {
                    String temp[] = line.split(" ");
                    mancala.BoardSize = temp.length;
                    mancala.BoardPlayer2 = new int[temp.length];
                    for (int i = 0; i < temp.length; i++) {
                        mancala.BoardPlayer2[i] = Integer.parseInt(temp[i]);
                    }

                } else if (lineCount == 5) {
                    String temp[] = line.split(" ");
                    mancala.BoardPlayer1 = new int[temp.length];
                    for (int i = 0; i < temp.length; i++) {
                        mancala.BoardPlayer1[i] = Integer.parseInt(temp[i]);
                    }
                } else if (lineCount == 6) {
                    mancala.Mancala2 = Integer.parseInt(line);
                } else if (lineCount == 7) {
                    mancala.Mancala1 = Integer.parseInt(line);
                }

            }
            print();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int GreedyEVAL() {
        int side = mancala.Player;
        int positionMAX = -1;
        int evalMAX = 0;
        int length = mancala.BoardSize;
        int array1[] = new int[length], array2[] = new int[length];
        mancala.evalFunction = new int[length];
        for (int i = 0; i < length; i++) {
            side = mancala.Player;
            for (int j = 0; j < length; j++) {
                array1[j] = mancala.BoardPlayer1[j];
                array2[j] = mancala.BoardPlayer2[j];
            }
            int NoOfSeeds = (side == 1) ? array1[i] : array2[i];
            // System.out.println("=======================================");
            if (side == 1) {
                array1[i] = 0;
            } else {
                array2[i] = 0;
            }
            // System.out.println("Side" + side + " i::" + i + "Seeds " + NoOfSeeds);
            int tempMod = length;
            int tempCap1 = mancala.Mancala1;
            int tempCap2 = mancala.Mancala2;
            int tempEval = tempCap1 - tempCap2;
            int next = i + 1;
            int lastIndex = -1;
            while (NoOfSeeds > 0) {
                if (side == 1) {
                    for (int j = next; j < length && NoOfSeeds > 0; j++) {
                        //System.out.println("i" + i + "j" + j + "No of seeds " + NoOfSeeds);
                        array1[j]++;
                        NoOfSeeds--;
                        lastIndex = j;
                        //System.out.println("i" + next + "j" + j + "No of seeds " + NoOfSeeds);
                    }

                    if (NoOfSeeds > 0) {
                        tempCap1++;
                        NoOfSeeds--;
                        side = 2;
                        next = length - 1;
                    }
                    // System.out.println("i" + i + "No of seeds " + NoOfSeeds + " Side" + side);
                } else {
                    for (int k = next; k >= 0 && k<length && NoOfSeeds > 0; k--) {
                        array2[k]++;
                        NoOfSeeds--;
                        lastIndex = k;
                        //System.out.println("i" + next + "k" + k + "No of seeds " + NoOfSeeds);
                    }
                    if (NoOfSeeds > 0) {
                        side = 1;
                        tempCap2++;
                        NoOfSeeds--;
                        next = 0;
                    }
                }
                //NoOfSeeds--;
            }
            System.out.println(" Temp2  " + tempCap2 + " Temp1  " + tempCap1 + " lastIndex" + side + " " + lastIndex);
            if (mancala.Player == 1) {
                if (mancala.Player == side && lastIndex > -1) {
                    if (array1[lastIndex] == 1) {
                        tempCap1 = tempCap1 + array2[lastIndex] + 1;
                    }
                }
                mancala.evalFunction[i] = tempCap1 - tempCap2 - tempEval;

            } else {
                if (mancala.Player == side && lastIndex > -1) {
                    if (array2[lastIndex] == 1) {
                        tempCap2 = tempCap2 + array1[lastIndex] + 1;
                    }
                }
                mancala.evalFunction[i] = -tempCap1 + tempCap2 - tempEval;
            }
            System.out.println("Eval Value" + mancala.evalFunction[i]);
            for (int seed2 : array2) {
                System.out.print(seed2);
            }
            System.out.println();
            for (int seed1 : array1) {
                System.out.print(seed1);
            }
            System.out.println();

            if (evalMAX < mancala.evalFunction[i]) {
                evalMAX = mancala.evalFunction[i];
                positionMAX = i;
                System.out.println("PositionMAX" + i);
            }
        }
        if (evalMAX == 0) {
            positionMAX = -1;
        }
        return positionMAX;
    }

    public void Rules() {

    }

}
