package edu.upb.checkers;

import java.util.Scanner;

public class CheckersGame extends CheckersBoard {

    public static void main( String[] args ) {
        CheckersGame onePiece = new CheckersGame();
        onePiece.init();

        Scanner scan = new Scanner( System.in );
        do {
            int i, j, i2, j2;
            onePiece.printBoard();

            if (onePiece.currentPlayer == 'b' ) {
                System.out.println( "ingrese las coordenadas de la ficha que quiere mover" );
                i = scan.nextInt();
                j = scan.nextInt();
                System.out.println( "ingrese las coordenadas del destino " );
                i2 = scan.nextInt();
                j2 = scan.nextInt();
            } else {
                CheckersState checkersState = new CheckersState( onePiece.currentPlayer, onePiece.cloneBoard() );
                CheckersState bestChild = checkersState.findBestChild( 3 );
                i = bestChild.getI();
                i2 = bestChild.getI2();
                j = bestChild.getJ();
                j2 = bestChild.getJ2();
                System.err.println( "i: " + i + " j: " + j + " i2: " + i2 + " j2: " + j2 );
                System.err.flush();
            }
            if ( Math.abs( i2 - i ) == 1 ) {
                if (!onePiece.moveWithCapturedCheck( i, j, i2, j2 )) {
                    System.err.println("MOVIMIENTO INVALIDO!!!");
                }
            } else {
                if (onePiece.capture( i, j, i2, j2 )) {
                    System.err.println("CAPTURA INVALIDA!!!");
                }
            }
            onePiece.crown();
        }
        while ( onePiece.checkVictory() == ' ' );
        System.out.println("GAME OVER");
        onePiece.printBoard();
        scan.close();
    }
}
