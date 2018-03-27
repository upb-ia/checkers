package edu.upb.checkers;

import java.util.List;

public class CheckersState  extends  CheckersBoard{


    public CheckersState() {
        init();
    }

    public CheckersState( char turn, char[][] board ) {
        this.currentPlayer = turn;
        this.board = board;
    }

    List< CheckersState > children;

    private char[][] cloneBoard() {
        char[][] result = new char[ 8 ][ 8 ];
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                result[ i ][ j ] = board[ i ][ j ];
            }
        }
        return result;
    }



    public void expandChildren() {
        int direction = getDirection();
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( belongsToCurrentPlayer( board[ i ][ j ] ) ) {
                    if ( isCapturePossibleForOnePiece( i,j ) ) {
                        if(isCapturePossible( i,j,i + (2*direction), j - 2 )){
                          CheckersState child =  new CheckersState(currentPlayer,cloneBoard());
                            child.capture(i,j,i + (2*direction), j - 2  );
                            children.add( child );
                        }
                        if(isCapturePossible( i,j,i + (2*direction), j + 2 )){
                            CheckersState child =  new CheckersState(currentPlayer,cloneBoard());
                            child.capture(i,j,i + (2*direction), j + 2  );
                            children.add( child );
                        }
                        if(isCapturePossible( i,j,i - (2*direction), j - 2 )){
                            CheckersState child =  new CheckersState(currentPlayer,cloneBoard());
                            child.capture(i,j,i - (2*direction), j - 2  );
                            children.add( child );
                        }
                        if(isCapturePossible( i,j,i - (2*direction), j + 2 )){
                            CheckersState child =  new CheckersState(currentPlayer,cloneBoard());
                            child.capture(i,j,i - (2*direction), j + 2  );
                            children.add( child );
                        }

                    }else{
                        generateForwardMoves( direction, i, j );
                        generateBackwardMoves( direction, i, j );
                    }

                }
            }
        }
    }

    public int findBestChildScore(int level) {
        int result = 0;
        if(level == 0) {
            return score();
        }
        expandChildren();
        for ( CheckersState child: children ) {
            int childScore = child.findBestChildScore( level - 1 );
            if( childScore > result) {
                result = childScore;
            }

        }
        return result;
    }

    private int score() {
        int result = 0;
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if(board[i][j] == currentPlayer) {
                    result += 1;
                }
                else if(board[i][j] == Character.toUpperCase( currentPlayer )) {
                    result += 2;
                }
                else if(board[i][j] == otherPlayer()) {
                    result -= 1;
                }
                else if(board[i][j] == Character.toUpperCase( otherPlayer() )){
                    result -= 2;
                }
            }
        }
        return result;
    }


    private void generateBackwardMoves( int direction, int i, int j ) {
        if ( isCrowned( board[ i ][ j ] ) ) {
            if ( isInBoard( i - direction, j - 1 )
                    && board[ i - direction ][ j - 1 ] == ' ' ) {
                char[][] newBoard = cloneBoard();
                newBoard[ i - direction ][ j - 1 ] = newBoard[ i ][ j ];
                newBoard[ i ][ j ] = ' ';
                children.add(new CheckersState( otherPlayer(), newBoard ));
            }
            if ( isInBoard( i - direction, j + 1 )
                    && board[ i - direction ][ j + 1 ] == ' ' ) {
                char[][] newBoard = cloneBoard();
                newBoard[ i - direction ][ j + 1 ] = newBoard[ i ][ j ];
                newBoard[ i ][ j ] = ' ';
                children.add(new CheckersState( otherPlayer(), newBoard ));
            }
        }
    }

    private void generateForwardMoves( int direction, int i, int j ) {
        if ( isInBoard( i + direction, j - 1 )
                && board[ i + direction ][ j - 1 ] == ' ' ) {
            char[][] newBoard = cloneBoard();
            newBoard[ i + direction ][ j - 1 ] = newBoard[ i ][ j ];
            newBoard[ i ][ j ] = ' ';
            children.add(new CheckersState( otherPlayer(), newBoard ));
        }
        if ( isInBoard( i + direction, j + 1 )
                && board[ i + direction ][ j + 1 ] == ' ' ) {
            char[][] newBoard = cloneBoard();
            newBoard[ i + direction ][ j + 1 ] = newBoard[ i ][ j ];
            newBoard[ i ][ j ] = ' ';
            children.add(new CheckersState( otherPlayer(), newBoard ));
        }
    }

    public void init() {
        currentPlayer = 'b';
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( ( i + j ) % 2 == 0 ) {
                    board[ i ][ j ] = 'X';
                } else if ( i < 3 ) {
                    board[ i ][ j ] = 'b';
                } else if ( i > 4 ) {
                    board[ i ][ j ] = 'n';
                } else {
                    board[ i ][ j ] = ' ';
                }
            }
        }
    }


}
