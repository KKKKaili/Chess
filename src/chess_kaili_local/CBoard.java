package chess_kaili_local;

import java.util.ArrayList;
import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import chess_kaili_local.pieces.Bishop;
import chess_kaili_local.pieces.King;
import chess_kaili_local.pieces.Knight;
import chess_kaili_local.pieces.Pawn;
import chess_kaili_local.pieces.Piece;
import chess_kaili_local.pieces.Queen;
import chess_kaili_local.pieces.Rook;

public final class CBoard{

	private final List<Cell> chessBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player nowPlayer;
	
	private CBoard(final Builder builder){
		this.chessBoard = newChessBoard(builder);
		this.whitePieces = getMovingPieces(this.chessBoard,PieceUnion.WHITE);
		this.blackPieces = getMovingPieces(this.chessBoard,PieceUnion.BLACK);
				
		final Collection<PieceMove> whiteLegalMoves = getUnionLegalMoves(this.whitePieces);
		final Collection<PieceMove> blackLegalMoves = getUnionLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this,whiteLegalMoves, blackLegalMoves);
		this.blackPlayer = new BlackPlayer(this,whiteLegalMoves, blackLegalMoves);
		this.nowPlayer = builder.nextPerson.selectPlayer(this.whitePlayer,this.blackPlayer);
	}
	
	@Override
	public String toString(){
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i< CBoardUtil.CellNums; i++){
			final String cellStr = this.chessBoard.get(i).toString();
			builder.append(String.format("%3s", cellStr));
			if((i+1) % CBoardUtil.CellNumsEachRow == 0){
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	public Player whitePlayer(){
		return this.whitePlayer;
	}
	public Player blackPlayer(){
		return this.blackPlayer;
	}
	public Player nowPlayer(){
		return this.nowPlayer;
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}
	

	private Collection<PieceMove> getUnionLegalMoves(final Collection<Piece> pieces) {
		final List<PieceMove> legalMoves = new ArrayList<>();
		for(final Piece piece:pieces){
			legalMoves.addAll(piece.getLegalMoves(this));
		}
		return ImmutableList.copyOf(legalMoves);
	}

	//track white moving pieces and black moving pieces on the board
	private static Collection<Piece> getMovingPieces(final List<Cell> chessBoard,final PieceUnion pieceUnion) {
		final List<Piece> movingPieces = new ArrayList<>();
		for(final Cell cell:chessBoard){
			if(cell.notEmptyCell()){
				final Piece piece = cell.getPiece();
				if(piece.getPieceUnion() == pieceUnion){
					movingPieces.add(piece);
				}
			}
		}
		return ImmutableList.copyOf(movingPieces);
	}

	public Cell getCell(final int cellCoords){
		return chessBoard.get(cellCoords);
	}
	
	private static List<Cell> newChessBoard(final Builder builder){
		final Cell[] cells = new Cell[CBoardUtil.CellNums];
		for(int i = 0; i < CBoardUtil.CellNums; i++){
			cells[i] = Cell.newCell(i,builder.boardSetting.get(i));
		}
		return ImmutableList.copyOf(cells);
	}
	
	public static CBoard generateInitialBoard(){
		final Builder builder = new Builder();
		//black pieces
		builder.setPiece(new Rook(PieceUnion.BLACK,0));
		builder.setPiece(new Knight(PieceUnion.BLACK,1));
		builder.setPiece(new Bishop(PieceUnion.BLACK,2));
		builder.setPiece(new Queen(PieceUnion.BLACK,3));
		builder.setPiece(new King(PieceUnion.BLACK,4));
		builder.setPiece(new Bishop(PieceUnion.BLACK,5));
		builder.setPiece(new Knight(PieceUnion.BLACK,6));
		builder.setPiece(new Rook(PieceUnion.BLACK,7));
		builder.setPiece(new Pawn(PieceUnion.BLACK,8));
		builder.setPiece(new Pawn(PieceUnion.BLACK,9));
		builder.setPiece(new Pawn(PieceUnion.BLACK,10));
		builder.setPiece(new Pawn(PieceUnion.BLACK,11));
		builder.setPiece(new Pawn(PieceUnion.BLACK,12));
		builder.setPiece(new Pawn(PieceUnion.BLACK,13));
		builder.setPiece(new Pawn(PieceUnion.BLACK,14));
		builder.setPiece(new Pawn(PieceUnion.BLACK,15));
		//white pieces		
		builder.setPiece(new Pawn(PieceUnion.WHITE,48));
		builder.setPiece(new Pawn(PieceUnion.WHITE,49));
		builder.setPiece(new Pawn(PieceUnion.WHITE,50));
		builder.setPiece(new Pawn(PieceUnion.WHITE,51));
		builder.setPiece(new Pawn(PieceUnion.WHITE,52));
		builder.setPiece(new Pawn(PieceUnion.WHITE,53));
		builder.setPiece(new Pawn(PieceUnion.WHITE,54));
		builder.setPiece(new Pawn(PieceUnion.WHITE,55));
		builder.setPiece(new Rook(PieceUnion.WHITE,56));
		builder.setPiece(new Knight(PieceUnion.WHITE,57));
		builder.setPiece(new Bishop(PieceUnion.WHITE,58));
		builder.setPiece(new Queen(PieceUnion.WHITE,59));
		builder.setPiece(new King(PieceUnion.WHITE,60));
		builder.setPiece(new Bishop(PieceUnion.WHITE,61));
		builder.setPiece(new Knight(PieceUnion.WHITE,62));
		builder.setPiece(new Rook(PieceUnion.WHITE,63));
		
		builder.setNextPerson(PieceUnion.WHITE);
		
		return builder.build();
	}
	
	public Iterable<PieceMove> getAllLegalMoves(){
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getPlayerLegalMoves(),this.blackPlayer.getPlayerLegalMoves()));
	}
	
	public static class Builder{
	
		Map<Integer,Piece> boardSetting;
		PieceUnion nextPerson;
		Pawn enpassantPawn;
		
		public Builder(){
			this.boardSetting = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece){
			//System.out.println("Builder error "+piece);
			this.boardSetting.put(piece.getPieceLocation(), piece);
			return this;
		}
		
		public Builder setNextPerson(final PieceUnion nextPerson){
			this.nextPerson = nextPerson;
			return this;
		}
		
		public CBoard build(){
			return new CBoard(this);
		}
		 public void setEnpassantPawn(Pawn enpassantPawn){
			 this.enpassantPawn = enpassantPawn;
		 }
	}
}
