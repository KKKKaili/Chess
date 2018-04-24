package chess_kaili_local.pieces;

import java.util.Collection;

import chess_kaili_local.CBoard;
import chess_kaili_local.PieceMove;
import chess_kaili_local.PieceUnion;

public abstract class Piece {
	
	protected final PieceKind pieceKind;
	protected final int pieceLocation;
	protected final PieceUnion pieceUnion;
	protected final boolean is1stStep;
	private final int reHashCode;
	
	protected Piece(final PieceKind pieceKind,final int pieceLocation,final PieceUnion pieceUnion,final boolean is1stStep){
		this.pieceKind = pieceKind;
		this.pieceLocation = pieceLocation;
		this.pieceUnion = pieceUnion;
		this.is1stStep = is1stStep;
		this.reHashCode = getHashCode();
		
	}
	
	private int getHashCode() {
		int result = pieceKind.hashCode();
		result = 31 * result + pieceUnion.hashCode();
		result = 31 * result + pieceLocation;
		result = 31 * result + (is1stStep ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(final Object other){
		if(this == other){
			return true;
		}
		if(!(other instanceof Piece)){
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return pieceLocation == otherPiece.getPieceLocation() && pieceKind == otherPiece.getPieceKind() &&
				pieceUnion == otherPiece.getPieceUnion() && is1stStep == otherPiece.is1stStep();
	}
	
	@Override
	public int hashCode(){
		return this.reHashCode;
	}
	
	public int getPieceLocation(){
		//System.out.println("Piece error: "+this.pieceLocation);
		//return this.pieceLocation;
		return this.pieceLocation;
	}
	
	public PieceUnion getPieceUnion(){
		return this.pieceUnion;
	}
	
	public boolean is1stStep(){
		return this.is1stStep;
	}
	
	public PieceKind getPieceKind(){
		return this.pieceKind;
	}
	
	public int getPieceValue(){
		return this.pieceKind.getPieceValue();
	}
	
	public abstract Collection<PieceMove> getLegalMoves(final CBoard cBoard); 
	
	public abstract Piece movePiece(PieceMove pieceMove);
	
	public enum PieceKind{
		
		PAWN(100,"P") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK(500,"R") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		KNIGHT(300,"N") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP(300,"B") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		QUEEN(900,"Q") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING(10000,"K") {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		PieceKind(final int pieceValue,final String pieceName){
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		@Override
		public String toString(){
			return this.pieceName;
		}
	
		public int getPieceValue(){
			return this.pieceValue;
		}
		
		public abstract boolean isKing();
		public abstract boolean isRook();
	}

}
