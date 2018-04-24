package chess_kaili_local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess_kaili_local.pieces.Piece;
import chess_kaili_local.pieces.Rook;

public class BlackPlayer extends Player {

	public BlackPlayer(final CBoard cBoard,final Collection<PieceMove> whiteLegalMoves,final Collection<PieceMove> blackLegalMoves) {
		super(cBoard,blackLegalMoves,whiteLegalMoves);
	}

	@Override
	public Collection<Piece> getExistPieces() {
		return this.cBoard.getBlackPieces();
	}

	@Override
	public PieceUnion getPieceUnion() {
		return PieceUnion.BLACK;
	}

	@Override
	public Player getRival() {
		return this.cBoard.whitePlayer();
	}

	@Override
	public Collection<PieceMove> calculateKingCastles(final Collection<PieceMove> playerLegalMoves,
													  final Collection<PieceMove> rivalLegalMoves) {
		final List<PieceMove> kingCastles = new ArrayList<>();
		if(this.pieceKing.is1stStep() && !this.isInCheck()){
			//short castles for black pieces
			if(!this.cBoard.getCell(5).notEmptyCell() && !this.cBoard.getCell(6).notEmptyCell()){
				final Cell rookCell = this.cBoard.getCell(7);
				if(rookCell.notEmptyCell() && rookCell.getPiece().is1stStep()){
					if(Player.getAttacks(5, rivalLegalMoves).isEmpty() && Player.getAttacks(6, rivalLegalMoves).isEmpty() &&
							rookCell.getPiece().getPieceKind().isRook()){
						kingCastles.add(new PieceMove.shortCastleMove(this.cBoard, this.pieceKing, 6, 
                                									  (Rook)rookCell.getPiece(), rookCell.getCellCoords(), 5));
					}
				}
			}
			//long castles
			if(!this.cBoard.getCell(1).notEmptyCell() && !this.cBoard.getCell(2).notEmptyCell() && 
			   !this.cBoard.getCell(3).notEmptyCell()){
				final Cell rookCell = this.cBoard.getCell(0);
				if(rookCell.notEmptyCell() && rookCell.getPiece().is1stStep() && Player.getAttacks(2, rivalLegalMoves).isEmpty() &&
				   Player.getAttacks(3, rivalLegalMoves).isEmpty() && rookCell.getPiece().getPieceKind().isRook()){
					
					kingCastles.add(new PieceMove.shortCastleMove(this.cBoard, this.pieceKing, 2, 
																  (Rook)rookCell.getPiece(), rookCell.getCellCoords(), 3));
				}
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}

}
