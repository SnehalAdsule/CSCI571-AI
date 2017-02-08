/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

///------------
// Introduction to Programming Using Java: An Object-Oriented Approach
//	Arnow/Weiss
//------------

//------------
// Chapter 12 / Section 12.4 / Page 589
//	The Game of Mancala
//------------ 

//------------
// Notes
//------------

import  java.io.*;
class  MancalaGame  {
	MancalaGame(String  name0,  String  name1)  {
        board  =  new MancalaBoard();
        board.setUpForPlay();
        players  =  new Player[2];
        players[0]  =  new Player(name0,  0);
        players[1]  =  new Player(name1,  1);
        currentPlayer  =  0;
    }

    public  void play()  throws  IOException  {
        displayBoard();
        while  (!board.gameOver())  {
           int  pitNum  =  players[currentPlayer].      
                       selectAMove(board);  // The player chooses 
                                            //    the move.
		    boolean  goAgain  =  
                    board.doTheMove(currentPlayer, pitNum);
                                         //    which is then carried 
                                         //     out by the board.
			System.out.println("Player  "  +  currentPlayer  +
                           " moved  from  "  +  pitNum);
			displayBoard();
			if  (!goAgain)         // If the current player does not go again,
			if  (currentPlayer  ==  0)     // switch to the other player.
				currentPlayer  =  1;
			else
				currentPlayer  =  0;
			else
			System.out.println("Player  "  +  
                         currentPlayer  + "  goes  again");
		}
		board.emptyStonesIntoMancalas();    // Game is over                                        //      board empty stones,
		displayBoard();                     //      display final board,
		if  (board.stonesInMancala(0)  > 
				board.stonesInMancala(1))	//      and announce winner.
			System.out.println(players[0].getName()+"  wins");
		else if (board.stonesInMancala(0)  <  
                                 board.stonesInMancala(1))
			System.out.println(players[1].getName()+"  wins");
		else
			System.out.println("Tie");
	}

    private  void  displayBoard()  {
        String  mancalaLineFiller  =  "";    // Used to properly 
                                             //      space the 
                                             //      mancala line
        System.out.println("-----------------------");
                                             // Top border
        // Player 1's pit line
        System.out.print("      ");   // space  past  mancala  entry
        for  (int  i  =  1;  i  <=  
                                board.playingPits;  i++)  {
           System.out.print(board.stonesInPit(1,  i)  +  
                      "    ");    // Print pit's contents and pit spacing.
           mancalaLineFiller  +=  "     ";   // Build mancala 
                                             //      spacing string.
        }
        displayPlayer(1);    // Player 1 info
        // Mancala line
        System.out.print(board.stonesInMancala(1)  +  
                   "    ");    // Print player 1's manacala and spacing.
        System.out.print(mancalaLineFiller);   // Space past pit 
                                               //      entries.
        System.out.println(board.stonesInMancala(0));
                                       // Print player 0's mancala.
        // Player 0's pit line
        System.out.print("      ");
        for  (int  i  =  board.playingPits;  
             i  >=  1;  i--)
           System.out.print(board.stonesInPit(1,  i)  +  
                       "    ");    // Print pit's contents and spacing.
        displayPlayer(0);    // Player  0  info
        System.out.println("-----------------------"); 
                                               // Bottom border
    }

    private  void  displayPlayer(int  playerNum)  {
        // Turn indicator
        if  (currentPlayer  ==  playerNum)			// If it this player's turn,
		    System.out.print("            -->");	//      display turn 
													//      indicator,
        else
			System.out.print("                  "); //      or display equal number of spaces otherwise.

		// player info
        System.out.println("Player  "  +  playerNum  +  
				"(  "  +
				players[playerNum].getName()  +  ")");
    }

    public  static  void  main(String  []  args)  throws  
                               IOException  {
		MancalaGame  game  =  new  MancalaGame("Snehal",  null);
		game.play();
    }

    int  currentPlayer  =  0;
    MancalaBoard  board;
    Player  []  players;
}

class  MancalaBoard  {
    MancalaBoard()  {
        pits  =  new  Pit[totalPits];
        for  (int  pitNum  =  0;  pitNum  <  totalPits;  
              pitNum++)
			pits[pitNum]  =  new Pit();
    }

    public  void  setUpForPlay()  {
        for  (int  pitNum  =  0;  pitNum  <  totalPits;  
              pitNum++)
               if  (!isAMancala(pitNum))
                   pits[pitNum].addStones(4);
    }

    public  int  stonesInMancala(int  playerNum)  {
		return  pits[getMancala(playerNum)].getStones();
    }

    public  int  stonesInPit(int  playerNum, int pitNum)  {
        return  pits[getPitNum(playerNum, pitNum)].getStones();
    }

    private  int  getPitNum(int  playerNum, int  pitNum) {
        return  playerNum  *  (playingPits+1)  +  pitNum;
    }

    private  int  getMancala(int  playerNum)  {
        return  playerNum  *  (playingPits+1);
    }

    private  boolean  isAMancala(int  pitNum)  {
        return  pitNum  %  (playingPits+1)  ==  0;
    }

    public  MancalaBoard  makeACopy()  {
        MancalaBoard  newBoard  =  new  MancalaBoard();
        for  (int  pitNum  =  0;  pitNum  <  totalPits;  
              pitNum++)
           newBoard.pits[pitNum].addStones(this. 
                                 pits[pitNum].getStones());
        return  newBoard;
    }

    public  boolean  doTheMove(int  currentPlayerNum,  
                               int  chosenPitNum)  {
		int  pitNum  =  getPitNum(currentPlayerNum, chosenPitNum);
        int  stones  =  pits[pitNum].removeStones();
        while  (stones  !=  0)  {
           pitNum--;
           if  (pitNum  <  0)
                   pitNum  =  totalPits  -  1;
           if  (pitNum  !=  
           getMancala(otherPlayerNum(currentPlayerNum)))  {
               pits[pitNum].addStones(1);
               stones--;
               }
        }
        if  (pitNum  ==  getMancala(currentPlayerNum))
           return  true;
        if  (ownerOf(pitNum)  ==  currentPlayerNum  &&
               pits[pitNum].getStones()  ==  1)  {
			stones  =  pits[oppositePitNum(pitNum)].removeStones();
			pits[getMancala(currentPlayerNum)].addStones(stones);
        }
	    return false;
	}

	private  int  ownerOf(int  pitNum)  {
        return  pitNum  /  (playingPits+1);
	}

	private  int  oppositePitNum(int  pitNum)  {
		return  totalPits  -  pitNum;
	}

	private  int  otherPlayerNum(int  playerNum)  {
		if  (playerNum  ==  0)
			return  1;
		else
			return  0;
	}

	public  boolean  gameOver()  {
		for  (int  player  =  0;  player  <  2;  player++)  {
	        int  stones  =  0;
		    for  (int  pitNum  =  1;  pitNum  <=  playingPits; pitNum++)
				stones  +=  pits[getPitNum(player, pitNum)].getStones();
			if  (stones  ==  0)
               return  true;
        }
        return  false;
    }

    public  void  emptyStonesIntoMancalas()  {
        for  (int  player  =  0;  player  <  2;  player++)
           for  (int  pitNum  =  0;  pitNum  <=  playingPits;  pitNum++)  {
               int  stones  =  pits[getPitNum(player,  
                                 pitNum)].removeStones(); 
                                 pits[getMancala(player)]. 
                                 addStones(stones);
           }
    }

    Pit  []  pits;
    static  final  int  playingPits=6,
                          totalPits  =  2*(playingPits+1);
}

class  Pit  {
    Pit()  {this.stones  =  0;}
    public  int  getStones()  {return  stones;}
    public  void  addStones(int  stones) {this.stones += stones;}
    public  boolean  isEmpty() {return stones == 0;}
    public  int  removeStones() {
        int  stones  =  this.stones;
        this.stones  =  0;
        return  stones;
    }
    int  stones;
}

class  Player  {
    Player(String  name,  int  playerNum)  {
        this.name  =  name;
        this.playerNum  =  playerNum;
    }

    public  String  getName()  {
        if  (name  !=  null)
           return  name;
        else
           return  "Computer";
    }

    public  int  getPlayerNum()  {
        return  this.playerNum;
    }

    public  int  selectAMove(MancalaBoard board)   throws  
                             IOException  {
        if  (name  !=  null)  {    // Real  player  -  not  the  computer
			BufferedReader  br  =
				new  BufferedReader(
						new InputStreamReader(System.in));
        System.out.print("Enter  a  pit  to  move from:  ");          // Prompt,
        System.out.flush();
        int  pitNum  =  Integer.parseInt(br.readLine()); //  get the move,
        return  pitNum;									//     and return it.
    }

    // Computer player - need to determine best move
    int  bestMove  =  -1;        // No best move initially
    int  repeatMove  =  -1;      // Or go again.
    int  maxNewStones  =  -1;    // Mo move has added stones to the 
                                 //      mancala.
    // Trying the possible moves
    for  (int  pitNum  =  1;  pitNum  <=  
          board.playingPits;  pitNum++)  {
        if  (board.stonesInPit(playerNum, pitNum)  !=  0)  {	// Only nonempty pits may be 
																//      moved from
			MancalaBoard  testBoard  =  board.makeACopy();		// Make a copy of the board
			boolean  goAgain  = 
				testBoard.doTheMove(playerNum, pitNum);			// Try the move on the board copy.
			if  (goAgain)										// If move allows us to go again,
				repeatMove  =  pitNum;							//      remember the move.
			int  newStones  =  
                 testBoard.stonesInMancala(playerNum)  -  
                 board.stonesInMancala(playerNum);				// See how many stones this move added
																//      to our mancala.
			if  (newStones  >  maxNewStones) {		// More stones 
													//     than so far?
                   maxNewStones = newStones;		// Remember 
                   bestMove  =  pitNum;				//     how many 
													//     and the move.
               }
           }
        }

        // Tried all possibilities, return the best one
        if  (maxNewStones  >  1)  // maxNewStones > 1 means a 
                                  //      multistone capture occurred.
			return  bestMove;
        else  if  (repeatMove  !=  -1) // Barring that, use a "go 
                                       //      again".
			return  repeatMove;
        else
			return  bestMove;    // 1 or possibly 0 stones added; oh well!
    }

    String  name;
    int  playerNum;
}
