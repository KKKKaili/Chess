package chess_kaili_local.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess_kaili_local.CBoard;
import chess_kaili_local.CBoardUtil;
import chess_kaili_local.Cell;
import chess_kaili_local.PieceMove;
import chess_kaili_local.PieceUnion;
import chess_kaili_local.pieces.Piece.PieceKind;


public class Knight extends Piece{

	private final static int[] LegalMoveOffset = {-17,-15,-10,-6,6,10,15,17};
	
	public Knight(final PieceUnion pieceUnion,final int pieceLocation){
		super(PieceKind.KNIGHT,pieceLocation,pieceUnion,true);
	}
	public Knight(final PieceUnion pieceUnion,final int pieceLocation,final boolean is1stStep){
		super(PieceKind.KNIGHT,pieceLocation,pieceUnion,is1stStep);
	}
	

	@Override
	public Collection<PieceMove> getLegalMoves(final CBoard cBoard) {
		
		final List<PieceMove> legalMoves = new ArrayList<>();
		for(final int offset : LegalMoveOffset){
			 final int destinationCoords = this.pieceLocation + offset;
			if(CBoardUtil.judgeValidCoords(destinationCoords)){
				if(except1stColumn(this.pieceLocation,offset) || except2ndColumn(this.pieceLocation,offset) ||
				   except7thColumn(this.pieceLocation,offset) || except8thColumn(this.pieceLocation,offset)){
					continue;
				}
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
	public Knight movePiece(final PieceMove pieceMove) {	
		return new Knight(pieceMove.getMovedPiece().getPieceUnion(),pieceMove.getDestinationCoords());
	}

	@Override
	public String toString(){
		return PieceKind.KNIGHT.toString();
	}
	
	private static boolean except1stColumn(final int nowLocation, final int offset){
		return CBoardUtil.is1stColumn[nowLocation] && (offset == -17 || offset == -10 ||
				offset == 6 || offset == 15);
	}
	
	private static boolean except2ndColumn(final int nowLocation, final int offset){
		return CBoardUtil.is2ndColumn[nowLocation] && ( offset == -10 || offset == 6);
	}
	
	private static boolean except7thColumn(final int nowLocation, final int offset){
		return CBoardUtil.is7thColumn[nowLocation] && ( offset == -6 || offset == 10);
	}
	
	private static boolean except8thColumn(final int nowLocation, final int offset){
		return CBoardUtil.is8thColumn[nowLocation] && (offset == -15 || offset == -6 ||
				offset == 10 || offset == 17);
	}
	
}
