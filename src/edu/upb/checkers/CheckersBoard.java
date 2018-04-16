package edu.upb.checkers;

/**
 * Created by daniel on 3/22/18.
 */
public class CheckersBoard {

    char[][] board = new char[ 8 ][ 8 ];
    char currentPlayer;

    // X bloqueado
    // ' ' vacio
    // 'b' blancas
    // 'n' negras
    // 'B' coronada
    // 'N' coronada

    public CheckersBoard() {

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

    protected int getDirection() {
        return currentPlayer == 'b' ? 1 : -1;
    }

    protected boolean isCrowned( char ch ) {
        return Character.isUpperCase( ch );
    }

    protected boolean belongsToCurrentPlayer( char ch ) {
        return Character.toLowerCase( ch ) == currentPlayer;
    }

    protected boolean isInBoard( int i, int j ) {
        return j >= 0 && j < 8 && i < 8 && i >= 0;
    }

    protected char[][] cloneBoard() {
        char[][] result = new char[ 8 ][ 8 ];
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                result[ i ][ j ] = board[ i ][ j ];
            }
        }
        return result;
    }

    public boolean isCapturePossible() {
        int direction = getDirection();
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {

                if ( isInBoard( i + ( direction * 2 ), j - 2 )
                        && currentPlayer == Character.toLowerCase( board[ i ][ j ] )
                        && Character.toLowerCase( board[ i + direction ][ j - 1 ] ) == otherPlayer()
                        && board[ i + ( direction * 2 ) ][ j - 2 ] == ' ' ) {

                    return true;

                }
                if ( isInBoard( i + ( direction * 2 ), j + 2 )
                        && currentPlayer == Character.toLowerCase( board[ i ][ j ] )
                        && Character.toLowerCase( board[ i + direction ][ j + 1 ] ) == otherPlayer()
                        && board[ i + ( direction * 2 ) ][ j + 2 ] == ' ' ) {

                    return true;

                }
                if ( isCrowned( board[ i ][ j ] ) ) {
                    if ( isInBoard( i - ( direction * 2 ), j - 2 )
                            && currentPlayer == Character.toLowerCase( board[ i ][ j ] )
                            && Character.toLowerCase( board[ i - direction ][ j - 1 ] ) == otherPlayer()
                            && board[ i - ( direction * 2 ) ][ j - 2 ] == ' ' ) {

                        return true;

                    }
                    if ( isInBoard( i - ( direction * 2 ), j + 2 )
                            && currentPlayer == Character.toLowerCase( board[ i ][ j ] )
                            && Character.toLowerCase( board[ i - direction ][ j + 1 ] ) == otherPlayer()
                            && board[ i - ( direction * 2 ) ][ j + 2 ] == ' ' ) {

                        return true;

                    }
                }

            }

        }
        return false;
    }

    protected boolean isCapturePossible( int i, int j, int i2, int j2 ) {
        int direction = getDirection();

        if ( !isInBoard( i, j ) || !isInBoard( i2, j2 ) ) {
            return false;
        }
        if ( !belongsToCurrentPlayer( board[ i ][ j ] ) ) {
            return false;
        }

        if ( Math.abs( i2 - i ) != 2 || Math.abs( j2 - j ) != 2 ) {
            return false;
        }

        int jMedio = ( j + j2 ) / 2;
        int iMedio = ( i + i2 ) / 2;

        if ( board[ i2 ][ j2 ] != ' ' ) {
            return false;
        }
        if ( Character.toLowerCase( board[ iMedio ][ jMedio ] ) != otherPlayer() ) {
            return false;

        }
        if ( !isCrowned( board[ i ][ j ] ) && i2 - i != 2 * direction ) {
            return false;
        }

        return true;
    }

    protected boolean move( int i, int j, int i2, int j2 ) {
        if ( !isInBoard( i, j ) || !isInBoard( i2, j2 ) ) {
            return false;
        }
        if ( Math.abs( i2 - i ) != 1 || Math.abs( j2 - j ) != 1 ) {
            return false;
        }

        if ( !isCrowned( board[ i ][ j ] ) && i2 - i != getDirection() ) {
            return false;
        }

        if ( !belongsToCurrentPlayer( board[ i ][ j ] ) ) {
            return false;
        }
        if ( board[ i2 ][ j2 ] != ' ' ) {
            return false;
        }
        board[ i ][ j ] = ' ';
        board[ i2 ][ j2 ] = currentPlayer;
        currentPlayer = otherPlayer();
        return true;
    }

    protected boolean capture( int i, int j, int i2, int j2 ) {

        if ( !isCapturePossible( i, j, i2, j2 ) ) {
            return false;
        }
        int jMedio = ( j + j2 ) / 2;
        int iMedio = ( i + i2 ) / 2;

        board[ i2 ][ j2 ] = board[ i ][ j ];
        board[ i ][ j ] = ' ';
        board[ iMedio ][ jMedio ] = ' ';
        if ( !isCapturePossibleForOnePiece( i2, j2 ) ) {
            currentPlayer = otherPlayer();
        }
        return true;
    }

    protected boolean isCapturePossibleForOnePiece( int i, int j ) {

        return isCapturePossible( i, j, i - 2, j - 2 ) || isCapturePossible( i, j, i - 2, j + 2 )
                || isCapturePossible( i, j, i + 2, j - 2 ) || isCapturePossible( i, j, i + 2, j + 2 );
    }

    protected boolean moveWithCapturedCheck( int i, int j, int i2, int j2 ) {
        if ( isCapturePossible() ) {
            return false;
        }
        return move( i, j, i2, j2 );
    }

    public void crown() {
        for ( int j = 0; j < 8; j++ ) {
            if ( board[ 0 ][ j ] == 'n' ) {
                board[ 0 ][ j ] = 'N';
            }
            if ( board[ 7 ][ j ] == 'b' ) {
                board[ 7 ][ j ] = 'B';
            }
        }
    }

    protected char otherPlayer() {
        if ( currentPlayer == 'b' ) {
            return 'n';
        }
        return 'b';
    }

    protected int countPieces( char player ) {
        int counter = 0;
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( Character.toLowerCase( board[ i ][ j ] ) == player ) {
                    counter++;
                }
            }
        }
        return counter;
    }

    protected char checkVictory() {
        if ( countPieces( currentPlayer ) == 0 ) {
            return otherPlayer();
        }
        if ( countPieces( otherPlayer() ) == 0 ) {
            return currentPlayer;
        }
        return ' ';
    }

    void printBoard() {
        System.out.println( "Turno actual : " + currentPlayer );
        System.out.println( "  0  1  2  3  4  5  6  7" );
        for ( int i = 0; i < 8; i++ ) {
            System.out.print( i + " " );
            for ( int j = 0; j < 8; j++ ) {
                if ( board[ i ][ j ] == 'X' ) {
                    System.out.print( "x  " );
                } else {
//                    System.out.print( "  " );
                    /*
                    switch ( board[ i ][ j ] ) {
                        case 'n':
                            System.out.print( '\u26AB' );
                            break;
                        case 'b':
                            System.out.print( '\u26AA' );
                            break;
                        case 'N':
                            System.out.print( '\u265B' );
                            break;
                        case 'B':
                            System.out.print( '\u2655' );
                            break;
                    }*/
                    System.out.print( board[ i ][ j ] );
                    System.out.print( "  " );
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
