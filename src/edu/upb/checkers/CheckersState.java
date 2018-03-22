package edu.upb.checkers;

import java.util.List;

public class CheckersState {

    char[][] board = new char[ 8 ][ 8 ];
    char currentPlayer;

    // X bloqueado
    // ' ' vacio
    // 'b' blancas
    // 'n' negras
    // 'B' coronada
    // 'N' coronada

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

    private char otherPlayer() {
        if (currentPlayer=='b') {
            return 'n';
        }
        return 'b';
    }

    public void expandChildren() {
        int i_inc = currentPlayer == 'b' ? 1 : -1;
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( belongsToCurrentPlayer( board[ i ][ j ] ) ) {
                    // TODO Primero verificar si puedo comer una pieza
                    generateForwardMoves( i_inc, i, j );
                    generateBackwardMoves( i_inc, i, j );
                }
            }
        }
    }

    private void generateBackwardMoves( int i_inc, int i, int j ) {
        if ( isCrowned( board[ i ][ j ] ) ) {
            if ( isInBoard( i - i_inc, j - 1 )
                    && board[ i - i_inc ][ j - 1 ] == ' ' ) {
                char[][] newBoard = cloneBoard();
                newBoard[ i - i_inc ][ j - 1 ] = newBoard[ i ][ j ];
                newBoard[ i ][ j ] = ' ';
                children.add(new CheckersState( otherPlayer(), newBoard ))
            }
            if ( isInBoard( i - i_inc, j + 1 )
                    && board[ i - i_inc ][ j + 1 ] == ' ' ) {
                char[][] newBoard = cloneBoard();
                newBoard[ i - i_inc ][ j + 1 ] = newBoard[ i ][ j ];
                newBoard[ i ][ j ] = ' ';
                children.add(new CheckersState( otherPlayer(), newBoard ))
            }
        }
    }

    private void generateForwardMoves( int i_inc, int i, int j ) {
        if ( isInBoard( i + i_inc, j - 1 )
                && board[ i + i_inc ][ j - 1 ] == ' ' ) {
            char[][] newBoard = cloneBoard();
            newBoard[ i + i_inc ][ j - 1 ] = newBoard[ i ][ j ];
            newBoard[ i ][ j ] = ' ';
            children.add(new CheckersState( otherPlayer(), newBoard ))
        }
        if ( isInBoard( i + i_inc, j + 1 )
                && board[ i + i_inc ][ j + 1 ] == ' ' ) {
            char[][] newBoard = cloneBoard();
            newBoard[ i + i_inc ][ j + 1 ] = newBoard[ i ][ j ];
            newBoard[ i ][ j ] = ' ';
            children.add(new CheckersState( otherPlayer(), newBoard ))
        }
    }

    private boolean isCrowned( char ch ) {
        return Character.isUpperCase( ch );
    }

    private boolean belongsToCurrentPlayer( char ch ) {
        return Character.toLowerCase( ch ) == currentPlayer;
    }

    private boolean isInBoard( int i, int j ) {
        return j >= 0 && j < 8 && i < 8 && i >= 0;
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

    void printBoard() {
        for ( int i = 0; i < 8; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                System.out.print( board[ i ][ j ] );
                System.out.print( " " );
            }
            System.out.println();
        }
        System.out.println();
    }
}
