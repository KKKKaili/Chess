package chess_kaili_local;

import java.util.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import chess_kaili_local.pieces.King;
import chess_kaili_local.pieces.Piece;

public abstract class Player {

	protected final CBoard cBoard;
	protected final King pieceKing;
	protected final Collection<PieceMove> playerLegalMoves;
	private final boolean isInCheck;
	
	Player(final CBoard cBoard,final Collection<PieceMove> playerLegalMoves,final Collection<PieceMove> rivalMoves){
		this.cBoard = cBoard;
		this.pieceKing = existKing();
		this.playerLegalMoves = ImmutableList.copyOf(Iterables.concat(playerLegalMoves,calculateKingCastles(playerLegalMoves,rivalMoves)));
		this.isInCheck = !Player.getAttacks(this.pieceKing.getPieceLocation(),rivalMoves).isEmpty();
	}
	
	public King getPieceKing(){
		return this.pieceKing;
	}
	
	public Collection<PieceMove> getPlayerLegalMoves(){
		return this.playerLegalMoves;
	}

	protected static Collection<PieceMove> getAttacks(int pieceLocation, Collection<PieceMove> rivalMoves) {
		final List<PieceMove> attackMoves = new ArrayList<>();
		for(final PieceMove pieceMove : rivalMoves){
			if(pieceLocation == pieceMove.getDestinationCoords()){
				attackMoves.add(pieceMove);
			}
		}
		return ImmutableList.copyOf(attackMoves);
	}

	private King existKing() {
		for(final Piece piece: getExistPieces()){
			if(piece.getPieceKind().isKing()){
				return (King) piece;
			}
		}
		throw new RuntimeException("IT IS NOT A VALID CHESS BOARD!");
	}
	
	private boolean isLegalMove(final PieceMove pieceMove){
		return this.playerLegalMoves.contains(pieceMove);
	}
	
	public boolean isInCheck(){
		return this.isInCheck;
	}
	
	public boolean isInCheckMate(){
		return this.isInCheck && !hasEscapeMoves();
	}
	
	protected boolean hasEscapeMoves() {
		for(final PieceMove pieceMove : this.playerLegalMoves ){
			final MoveForward moveForward = makeMove(pieceMove);
			if(moveForward.getMoveState().isFinished()){
				return true;
			}
		}
		return false;
	}

	public boolean isInStaleMate(){
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled(){
		return false;
	}
	
	public MoveForward makeMove(final PieceMove pieceMove){
		//if the pieceMove is not illegal, it will not be forwarded and return the same board as current board
		if(!isLegalMove(pieceMove)){
			return new MoveForward(this.cBoard,pieceMove,MoveState.IllegalMove);
		}
		
		final CBoard forwardBoard = pieceMove.execute();
		
		//if white coming in, the nowPlayer will be black
		//get the attacks collection for whitePlayer's King
		final Collection<PieceMove> kingAttacks = Player.getAttacks(forwardBoard.nowPlayer().getRival().getPieceKing().getPieceLocation(),
																	forwardBoard.nowPlayer().getPlayerLegalMoves());
		//if the attacks collection for the whitePlayer's king is not empty,return same board as now and the move state of PlayerInCheck
		if(!kingAttacks.isEmpty()){
			return new MoveForward(this.cBoard,pieceMove,MoveState.PlayerInCheck);
		}
		//otherwise return a new forward board and make the move
		return new MoveForward(forwardBoard,pieceMove,MoveState.Finished);
	}

	public abstract Collection<Piece> getExistPieces();
	public abstract PieceUnion getPieceUnion();
	public abstract Player getRival();
	public abstract Collection<PieceMove> calculateKingCastles(Collection<PieceMove> playerLegalMoves,
																Collection<PieceMove> rivalLegalMoves);
}
