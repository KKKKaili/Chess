package chess_kaili_local;

import chess_kaili_local.CBoard.Builder;
import chess_kaili_local.pieces.Pawn;
import chess_kaili_local.pieces.Piece;
import chess_kaili_local.pieces.Rook;

public abstract class PieceMove {

	public static final PieceMove InvalidMove = new InvalidMove();
		
	public final CBoard cBoard;
	public final Piece movedPiece;
	public final int destinationCoords;
	public final boolean is1stStep;

	//for invalid move
	private PieceMove(final CBoard cBoard,final int destinationCoords){
		this.cBoard = cBoard;
		this.destinationCoords = destinationCoords;
		this.movedPiece = null;
		this.is1stStep = false;
	}
	
	private PieceMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords){
		this.cBoard = cBoard;
		this.movedPiece = movedPiece;
		this.destinationCoords = destinationCoords;
		this.is1stStep = movedPiece.is1stStep();
	}

	@Override
	public int hashCode(){
		final int primeNum = 31;
		int num = 1;
		num = primeNum * num + this.destinationCoords;
		num = primeNum * num + this.movedPiece.hashCode();
		num = primeNum * num + this.movedPiece.getPieceLocation();
		return num;
	}
	
	@Override
	public boolean equals(final Object object){
		if(this == object){
			return true;
		}
		if(!(object instanceof PieceMove)){
			return false;
		}
		final PieceMove otherMove = (PieceMove) object;
		
		//System.out.println(otherMove.getNowCoords());
		//System.out.println(getNowCoords());
		
		return   getNowCoords() == otherMove.getNowCoords() &&
				getDestinationCoords() == otherMove.getDestinationCoords()  && getMovedPiece().equals(otherMove.getMovedPiece());
	}

	
	public int getNowCoords(){
		
		//System.out.println(this.movedPiece.getPieceLocation());
		
		return this.movedPiece.getPieceLocation();
		
		
	}
	
	public int getDestinationCoords(){
		return this.destinationCoords;
	}
	
	public Piece getMovedPiece(){
		return this.movedPiece;
	}
	
	public boolean isAttack(){
		return false;
	}
	
	public boolean isCastlingMove(){
		return false;
	}
	
	public Piece getCapturedPiece(){
		return null;
	}
	
	
	
	public CBoard execute() {
		final Builder builder = new Builder();
		//go through and set all of the pieces on the new outbound board that are not the current move piece for nowPlayer 
		for(final Piece piece : this.cBoard.nowPlayer().getExistPieces()){
			if(!this.movedPiece.equals(piece)){
				builder.setPiece(piece);
			}
		}
		//do the same thing for the rival's pieces
		//It's not the rival turn to move,so it will not have the moved piece, not need the if check 
		for(final Piece piece:this.cBoard.nowPlayer().getRival().getExistPieces()){
			builder.setPiece(piece);
		}
		//move the moved piece
		builder.setPiece(this.movedPiece.movePiece(this));
		//if the incoming person was white, then this call would set the person for the outgoing board to black
		builder.setNextPerson(this.cBoard.nowPlayer().getRival().getPieceUnion());
		return builder.build();
	}

	
	
	public static final class CommonMove extends PieceMove{		
		public CommonMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords){
			super(cBoard,movedPiece,destinationCoords);
		}
				
		@Override
		public String toString(){
			return movedPiece.getPieceKind().toString() + CBoardUtil.getDestinationLocation(this.destinationCoords);
		}
		@Override
		public boolean equals(final Object object){
			return this == object || object instanceof CommonMove && super.equals(object);
		}
		
	}
	
	public static class CaptureMove extends PieceMove{		
		final Piece capturedPiece;
		public CaptureMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords,
				    final Piece capturedPiece){
			super(cBoard,movedPiece,destinationCoords);
			this.capturedPiece = capturedPiece;
		}
		
		@Override
		public int hashCode(){
			return this.capturedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other){
			if(this == other){
				return true;
			}
			if(!(other instanceof CaptureMove)){
				return false;
			}
			final CaptureMove otherCaptureMove = (CaptureMove) other;
			return super.equals(otherCaptureMove) && getCapturedPiece().equals(otherCaptureMove.getCapturedPiece());
		}
		@Override
		public CBoard execute() {
			return null;
		}
		@Override
		public boolean isAttack(){
			return true;
		}
		@Override
		public Piece getCapturedPiece(){
			return this.capturedPiece;
		}
		
	}
	
	
	
	public static final class PawnMove extends PieceMove{	
		public PawnMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords){
			super(cBoard,movedPiece,destinationCoords);
		}
	}
	
	public static class PawnCaptureMove extends CaptureMove{	
		public PawnCaptureMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords,final Piece capturedPiece){
			super(cBoard,movedPiece,destinationCoords,capturedPiece);
		}
	}
	
	public static final class PawnEnpassantMove extends PawnCaptureMove{		
		public PawnEnpassantMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords,final Piece capturedPiece){
			super(cBoard,movedPiece,destinationCoords,capturedPiece);
		}
	}
	
	public static final class PawnJump extends PieceMove{	
		public PawnJump(final CBoard cBoard,final Piece movedPiece,final int destinationCoords){
			super(cBoard,movedPiece,destinationCoords);
		}
		
		@Override
		public CBoard execute(){
			//System.out.println("Jump Start");
			final Builder builder = new Builder();
			for(final Piece piece : this.cBoard.nowPlayer().getExistPieces()){
				if(!this.movedPiece.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece : this.cBoard.nowPlayer().getRival().getExistPieces()){
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnpassantPawn(movedPawn);
			builder.setNextPerson(this.cBoard.nowPlayer().getRival().getPieceUnion());
			return builder.build();
		}
		
		@Override
		public String toString(){
			return "p";
		}
	}
	
	
	
	static abstract class CastleMove extends PieceMove{	
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookEnd;
		
		public CastleMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords,
							final Rook castleRook,final int castleRookStart,final int castleRookEnd){
			super(cBoard,movedPiece,destinationCoords);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookEnd = castleRookEnd;
		}
		public Rook getCastleRook(){
			return this.castleRook;
		}
		@Override
		public boolean isCastlingMove(){
			return true;
		}
		@Override
		public CBoard execute(){
			final Builder builder = new Builder();
			for(final Piece piece : this.cBoard.nowPlayer().getExistPieces()){
				if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece : this.cBoard.nowPlayer().getRival().getExistPieces()){
				builder.setPiece(piece);
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			//TODO look into the first move on normal pieces
			builder.setPiece(new Rook(this.castleRook.getPieceUnion(),this.castleRookEnd));
			builder.setNextPerson(this.cBoard.nowPlayer().getRival().getPieceUnion());
			return builder.build();
		}
	}
	
	public static final class shortCastleMove extends CastleMove{	
		public shortCastleMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords,
								final Rook castleRook,final int castleRookStart,final int castleRookEnd){
			super(cBoard,movedPiece,destinationCoords,castleRook,castleRookStart,castleRookEnd);
		}
		@Override
		public String toString(){
			return "O-O";
		}
	}
	
	public static final class longCastleMove extends CastleMove{		
		public longCastleMove(final CBoard cBoard,final Piece movedPiece,final int destinationCoords,
								final Rook castleRook,final int castleRookStart,final int castleRookEnd){
			super(cBoard,movedPiece,destinationCoords,castleRook,castleRookStart,castleRookEnd);
		}
		@Override
		public String toString(){
			return "O-O-O";
		}
	}
	
	public static final class InvalidMove extends PieceMove{		
		public InvalidMove(){
			super(null,-1);
		}	
		@Override
		public CBoard execute(){
			throw new RuntimeException("CAN'T EXECUTE THE INVALID MOVE");
		}
	}
	
	public static class MoveFactory{
		private MoveFactory(){
			throw new RuntimeException("ERROR!");
		}
		public static PieceMove generateMove(final CBoard cBoard,final int nowCoords,final int destinationCoords){
			for(final PieceMove pieceMove : cBoard.getAllLegalMoves()){
				if(pieceMove.getNowCoords() == nowCoords && pieceMove.getDestinationCoords() == destinationCoords){
					return pieceMove;
				}
			}
			return InvalidMove;
		}
		
	}
	
}
