package chess_kaili_local.pieces;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import java.util.*;

import chess_kaili_local.CBoard;
import chess_kaili_local.CBoardUtil;
import chess_kaili_local.Cell;
import chess_kaili_local.PieceMove;
import chess_kaili_local.PieceUnion;
import chess_kaili_local.pieces.Piece.PieceKind;

public class King extends Piece {

	private final static int[] LegalMoveOffset = {-9,-8,-7,-1,1,7,8,9};
	public King(final PieceUnion pieceUnion,final int pieceLocation) {
		super(PieceKind.KING,pieceLocation, pieceUnion,true);
	}
	public King(final PieceUnion pieceUnion,final int pieceLocation,final boolean is1stStep) {
		super(PieceKind.KING,pieceLocation, pieceUnion,is1stStep);
	}

	@Override
	public Collection<PieceMove> getLegalMoves(CBoard cBoard) {
		
		final List<PieceMove> legalMoves = new ArrayList<>();
		for(final int offset : LegalMoveOffset){
			final int destinationCoords = this.pieceLocation + offset;		
			if(except1stColumn(this.pieceLocation,offset) || except8thColumn(this.pieceLocation,offset)){
				continue;
			}
			if(CBoardUtil.judgeValidCoords(destinationCoords)){
				final Cell destinationCell = cBoard.getCell(destinationCoords);
				if(!destinationCell.notEmptyCell()){
					legalMoves.add(new PieceMove.CommonMove(cBoard,this,destinationCoords));
				}else{
					final Piece destinationPiece = destinationCell.getPiece();
					final PieceUnion pieceUnion = destinationPiece.getPieceUnion();
					if(this.pieceUnion != pieceUnion){
						legalMoves.add(new PieceMove.CaptureMove(cBoard,this,destinationCoords,destinationPiece));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public King movePiece(final PieceMove pieceMove) {	
		return new King(pieceMove.getMovedPiece().getPieceUnion(),pieceMove.getDestinationCoords());
	}
	
	@Override
	public String toString(){
		return PieceKind.KING.toString();
	}
	
	private static boolean except1stColumn(final int nowLocation, final int offset){
		return CBoardUtil.is1stColumn[nowLocation] && (offset == -9 || offset == -1 ||
				offset == 7);
	}
	private static boolean except8thColumn(final int nowLocation, final int offset){
		return CBoardUtil.is8thColumn[nowLocation] && (offset == -7 || offset == -1 ||
				offset == 9);
	}

}
