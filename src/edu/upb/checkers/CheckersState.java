package edu.upb.checkers;

import java.util.ArrayList;
import java.util.List;

public class CheckersState extends CheckersBoard {


    private int i, j, i2, j2;


    public CheckersState() {
        init();
    }

    public CheckersState( char turn, char[][] board ) {
        this.currentPlayer = turn;
        this.board = board;
    }


    public CheckersState( char turn, char[][] board, int i, int j, int i2, int j2 ) {
        this.currentPlayer = turn;
        this.board = board;
        this.i = i;
        this.j = j;
        this.i2 = i2;
        this.j2 = j2;
    }

    List< CheckersState > children;

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getI2() {
        return i2;
    }

    public int getJ2() {
        return j2;
    }

    public void expandChildren() {
        children = new ArrayList<>();
        int direction = getDirection();
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( belongsToCurrentPlayer( board[ i ][ j ] ) ) {
                    if ( isCapturePossible() ) {
                        if ( isCapturePossible( i, j, i + ( 2 * direction ), j - 2 ) ) {
                            int i2Child = i + ( 2 * direction );
                            int j2Child = j - 2;
                            CheckersState child = new CheckersState( currentPlayer, cloneBoard(), i, j, i2Child, j2Child );
                            child.capture( i, j, i2Child, j2Child );
                            child.crown();
                            children.add( child );
                        }
                        if ( isCapturePossible( i, j, i + ( 2 * direction ), j + 2 ) ) {
                            int i2Child = i + ( 2 * direction );
                            int j2Child = j + 2;
                            CheckersState child = new CheckersState( currentPlayer, cloneBoard(), i, j, i2Child, j2Child );
                            child.capture( i, j, i2Child, j2Child );
                            child.crown();
                            children.add( child );
                        }
                        if ( isCapturePossible( i, j, i - ( 2 * direction ), j - 2 ) ) {
                            int i2Child = i - ( 2 * direction );
                            int j2Child = j - 2;
                            CheckersState child = new CheckersState( currentPlayer, cloneBoard(), i, j, i2Child, j2Child );
                            child.capture( i, j, i2Child, j2Child );
                            child.crown();
                            children.add( child );
                        }
                        if ( isCapturePossible( i, j, i - ( 2 * direction ), j + 2 ) ) {
                            int i2Child = i - ( 2 * direction );
                            int j2Child = j + 2;
                            CheckersState child = new CheckersState( currentPlayer, cloneBoard(), i, j, i2Child, j2Child );
                            child.capture( i, j, i2Child, j2Child );
                            child.crown();
                            children.add( child );
                        }

                    } else {
                        generateForwardMoves( direction, i, j );
                        generateBackwardMoves( direction, i, j );
                    }

                }
            }
        }
    }

    public CheckersState findBestChild( int level ) {

        int result = Integer.MIN_VALUE;
        if ( level == 0 ) {
            return this;
        }
        expandChildren();
        CheckersState bestChild = null;
        for ( CheckersState child : children ) {
            int childScore = child.findChildScore( level - 1, currentPlayer );

            if ( childScore > result ) {
                result = childScore;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public int findChildScore( int level, char player ) {
        int result;
        if ( level == 0 ) {
            return score();
        }
        expandChildren();
        if (children == null || children.isEmpty()) {
            return score();
        }
        if ( currentPlayer == player ) {
            result = Integer.MIN_VALUE;

            for ( CheckersState child : children ) {
                int childScore = child.findChildScore( level - 1, player );

                if ( childScore > result ) {
                    result = childScore;
                }
            }
        } else {
            result = Integer.MAX_VALUE;

            for ( CheckersState child : children ) {
                int childScore = child.findChildScore( level - 1, player );

                if ( childScore < result ) {
                    result = childScore;
                }
            }
        }
        return result;
    }


    private int score() {
        int result = 0;
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( board[ i ][ j ] == currentPlayer ) {
                    result += 1;
                } else if ( board[ i ][ j ] == Character.toUpperCase( currentPlayer ) ) {
                    result += 2;
                } else if ( board[ i ][ j ] == otherPlayer() ) {
                    result -= 1;
                } else if ( board[ i ][ j ] == Character.toUpperCase( otherPlayer() ) ) {
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
                children.add( new CheckersState( otherPlayer(), newBoard, i, j, i-direction,j-1 ) );
            }
            if ( isInBoard( i - direction, j + 1 )
                    && board[ i - direction ][ j + 1 ] == ' ' ) {
                char[][] newBoard = cloneBoard();
                newBoard[ i - direction ][ j + 1 ] = newBoard[ i ][ j ];
                newBoard[ i ][ j ] = ' ';
                children.add( new CheckersState( otherPlayer(), newBoard, i, j, i-direction,j+1  ) );
            }
        }
    }

    private void generateForwardMoves( int direction, int i, int j ) {
        if ( isInBoard( i + direction, j - 1 )
                && board[ i + direction ][ j - 1 ] == ' ' ) {
            char[][] newBoard = cloneBoard();
            newBoard[ i + direction ][ j - 1 ] = newBoard[ i ][ j ];
            newBoard[ i ][ j ] = ' ';
            CheckersState child =  new CheckersState( otherPlayer(), newBoard, i, j, i+direction, j-1) ;
            child.crown();
            children.add(child);
        }
        if ( isInBoard( i + direction, j + 1 )
                && board[ i + direction ][ j + 1 ] == ' ' ) {
            char[][] newBoard = cloneBoard();
            newBoard[ i + direction ][ j + 1 ] = newBoard[ i ][ j ];
            newBoard[ i ][ j ] = ' ';
            CheckersState child =  new CheckersState( otherPlayer(), newBoard, i, j, i+direction, j+1 ) ;
            child.crown();
            children.add(child);
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
