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

public class Rook extends Piece{

	private final static int[] LegalMoveOffset = {-8,-1,1,8};
	public Rook(final PieceUnion pieceUnion,final int pieceLocation) {
		super(PieceKind.ROOK,pieceLocation, pieceUnion,true);
	}
	public Rook(final PieceUnion pieceUnion,final int pieceLocation,final boolean is1stStep){
		super(PieceKind.ROOK,pieceLocation, pieceUnion,is1stStep);
	}

	@Override
	public Collection<PieceMove> getLegalMoves(final CBoard cBoard) {
		
		final List<PieceMove> legalMoves = new ArrayList<>();
		for(final int offset: LegalMoveOffset){
			int destinationCoords = this.pieceLocation;
			while(CBoardUtil.judgeValidCoords(destinationCoords)){
				
				if(except1stColumn(destinationCoords,offset) || except8thColumn(destinationCoords,offset)){
					break;
				}
				destinationCoords += offset;
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
						break;
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public Rook movePiece(final PieceMove pieceMove) {	
		return new Rook(pieceMove.getMovedPiece().getPieceUnion(),pieceMove.getDestinationCoords());
	}
	
	@Override
	public String toString(){
		return PieceKind.ROOK.toString();
	}
	
	private static boolean except1stColumn(final int nowLocation, final int offset){
		return CBoardUtil.is1stColumn[nowLocation] && ( offset == -1);
	}
	
	private static boolean except8thColumn(final int nowLocation, final int offset){
		return CBoardUtil.is8thColumn[nowLocation] && ( offset == 1);
	}

}
