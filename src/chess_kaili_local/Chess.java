package chess_kaili_local;

import java.io.IOException;

import chess_kaili_local.gui.Table;

public class Chess {
	
	public static void main(String[] args) throws IOException{
		CBoard cBoard = CBoard.generateInitialBoard();
		System.out.println(cBoard);
		Table table = new Table();
	}
}
