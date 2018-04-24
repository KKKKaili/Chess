package chess_kaili_local.pieces;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import java.util.*;

import chess_kaili_local.CBoard;
import chess_kaili_local.CBoardUtil;
import chess_kaili_local.PieceMove;
import chess_kaili_local.PieceUnion;
import chess_kaili_local.pieces.Piece.PieceKind;

public class Pawn extends Piece {

	private final static int[] LegalMoveOffset = {7,8,9,16};
	public Pawn(final PieceUnion pieceUnion,final int pieceLocation) {
		super(PieceKind.PAWN,pieceLocation, pieceUnion,true);
	}
	public Pawn(final PieceUnion pieceUnion,final int pieceLocation,final boolean is1stStep) {
		super(PieceKind.PAWN,pieceLocation, pieceUnion,is1stStep);
	}


	@Override
	public Collection<PieceMove> getLegalMoves(CBoard cBoard) {
		
		final List<PieceMove> legalMoves = new ArrayList<>();
		for(final int offset:LegalMoveOffset){
			final int destinationCoords = this.pieceLocation + (this.getPieceUnion().directedByUnion() * offset);
			if(!CBoardUtil.judgeValidCoords(destinationCoords)){
				continue;
			}
			if(offset == 7 && !(( CBoardUtil.is1stColumn[this.pieceLocation] && this.pieceUnion.isWhiteUnion() || 
					  ( CBoardUtil.is8thColumn[this.pieceLocation] && this.pieceUnion.isBlackUnion())))){
				if(cBoard.getCell(destinationCoords).notEmptyCell()){
					final Piece destinationPiece = cBoard.getCell(destinationCoords).getPiece();
					if(this.pieceUnion != destinationPiece.getPieceUnion()){
						legalMoves.add(new PieceMove.PawnCaptureMove(cBoard,this,destinationCoords,destinationPiece));
					}
				}
			}
			else if(offset == 8 && !cBoard.getCell(destinationCoords).notEmptyCell() ){
				legalMoves.add(new PieceMove.CommonMove(cBoard,this,destinationCoords));
				
			}
			else if(offset == 9 && !(( CBoardUtil.is8thColumn[this.pieceLocation] && this.pieceUnion.isWhiteUnion() || 
					  ( CBoardUtil.is1stColumn[this.pieceLocation] && this.pieceUnion.isBlackUnion())))){
						if(cBoard.getCell(destinationCoords).notEmptyCell()){
							final Piece destinationPiece = cBoard.getCell(destinationCoords).getPiece();
							if(this.pieceUnion != destinationPiece.getPieceUnion()){
								legalMoves.add(new PieceMove.PawnCaptureMove(cBoard,this,destinationCoords,destinationPiece));
							}
						}
			}
			else if(offset == 16 && this.is1stStep() && (( CBoardUtil.is7thRow[this.pieceLocation] && this.getPieceUnion().isBlackUnion()) 
					|| ( CBoardUtil.is2ndRow[this.pieceLocation] && this.getPieceUnion().isWhiteUnion()))){
				final int forwardDestinationCoords = this.pieceLocation + (this.pieceUnion.directedByUnion() * 8);
				if(!cBoard.getCell(forwardDestinationCoords).notEmptyCell() && !cBoard.getCell(destinationCoords).notEmptyCell()){
					legalMoves.add(new PieceMove.PawnJump(cBoard,this,destinationCoords));
				}
			}		
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public Pawn movePiece(final PieceMove pieceMove) {	
		return new Pawn(pieceMove.getMovedPiece().getPieceUnion(),pieceMove.getDestinationCoords());
	}
	
	@Override
	public String toString(){
		return PieceKind.PAWN.toString();
	}

}
