package chess_kaili_local;

public class MoveForward {

	private final CBoard forwardBoard;
	private final PieceMove pieceMove;
	private final MoveState moveState;
	
	public MoveForward(final CBoard forwardBoard,final PieceMove pieceMove,final MoveState moveState){
		this.forwardBoard = forwardBoard;
		this.pieceMove = pieceMove;
		this.moveState = moveState;
	}
	
	public MoveState getMoveState(){
		return this.moveState;
	}
	
	public CBoard getForwardBoard(){
		return this.forwardBoard;
	}
	
}
