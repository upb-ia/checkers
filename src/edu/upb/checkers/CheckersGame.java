package edu.upb.checkers;

import java.util.Scanner;

public class CheckersGame extends CheckersBoard {

    public static void main( String[] args ) {
        CheckersGame onePiece = new CheckersGame();
        onePiece.init();
        Scanner scan = new Scanner( System.in );
        do {
            onePiece.printBoard();
            System.out.println( "ingrese las coordenadas de la ficha que quiere mover" );
            int i = scan.nextInt();
            int j = scan.nextInt();
            System.out.println( "ingrese las coordenadas del destino " );
            int i2 = scan.nextInt();
            int j2 = scan.nextInt();
            if ( Math.abs( i2 - i ) == 1 ) {
                onePiece.moveWithCapturedCheck( i, j, i2, j2 );
            } else {
                onePiece.capture( i, j, i2, j2 );
            }
            onePiece.crown();
        }
        while ( onePiece.checkVictory() == ' ' );
        scan.close();
    }
}
