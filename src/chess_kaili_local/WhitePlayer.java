package chess_kaili_local;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import java.util.*;

import chess_kaili_local.pieces.Piece;
import chess_kaili_local.pieces.Rook;

public class WhitePlayer extends Player{

	public WhitePlayer(final CBoard cBoard,final Collection<PieceMove> whiteLegalMoves,final Collection<PieceMove> blackLegalMoves) {
		super(cBoard, whiteLegalMoves,blackLegalMoves);
	}

	@Override
	public Collection<Piece> getExistPieces() {
		return this.cBoard.getWhitePieces();
	}

	@Override
	public PieceUnion getPieceUnion() {
		return PieceUnion.WHITE;
	}

	@Override
	public Player getRival() {
		return this.cBoard.blackPlayer();
	}

	@Override
	public Collection<PieceMove> calculateKingCastles(final Collection<PieceMove> playerLegalMoves,
													  final Collection<PieceMove> rivalLegalMoves) {
		
		final List<PieceMove> kingCastles = new ArrayList<>();
		if(this.pieceKing.is1stStep() && !this.isInCheck()){
			//short castles for white pieces
			if(!this.cBoard.getCell(61).notEmptyCell() && !this.cBoard.getCell(62).notEmptyCell()){
				final Cell rookCell = this.cBoard.getCell(63);
				if(rookCell.notEmptyCell() && rookCell.getPiece().is1stStep()){
					if(Player.getAttacks(61, rivalLegalMoves).isEmpty() && Player.getAttacks(62, rivalLegalMoves).isEmpty() &&
						rookCell.getPiece().getPieceKind().isRook()){
						kingCastles.add(new PieceMove.shortCastleMove(this.cBoard, this.pieceKing, 62, 
								                                      (Rook)rookCell.getPiece(), rookCell.getCellCoords(), 61));
					}
				}
			}
			//long castles 
			if(!this.cBoard.getCell(57).notEmptyCell() && !this.cBoard.getCell(58).notEmptyCell() && 
			   !this.cBoard.getCell(59).notEmptyCell()){
				final Cell rookCell = this.cBoard.getCell(56);
				if(rookCell.notEmptyCell() && rookCell.getPiece().is1stStep() && Player.getAttacks(58, rivalLegalMoves).isEmpty() &&
				   Player.getAttacks(59, rivalLegalMoves).isEmpty() && rookCell.getPiece().getPieceKind().isRook()){
					
					kingCastles.add(new PieceMove.longCastleMove(this.cBoard, this.pieceKing, 58,
																 (Rook)rookCell.getPiece(), rookCell.getCellCoords(), 59));
				}
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}

}
