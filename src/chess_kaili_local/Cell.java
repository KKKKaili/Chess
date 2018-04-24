package chess_kaili_local;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import chess_kaili_local.pieces.Piece;

public abstract class Cell{
	
	protected final int cellCoords;
	
	private static final Map<Integer,EmptyCell> EmptyCellsMap = getAllEmptyCells();
	
	private static Map<Integer, EmptyCell> getAllEmptyCells() {
		final Map<Integer,EmptyCell> emptyCellMap = new HashMap<>();
		for(int i=0;i<CBoardUtil.CellNums;i++){
			emptyCellMap.put(i, new EmptyCell(i));
		}
		return ImmutableMap.copyOf(emptyCellMap);
	}
	
	private Cell(final int cellCoords){
		this.cellCoords = cellCoords;
	}
	
	static Cell newCell(final int cellCoords,final Piece piece){
		return piece != null ? new OccupiedCell(cellCoords,piece) : EmptyCellsMap.get(cellCoords);
	}

	public abstract boolean notEmptyCell();
	
	public abstract Piece getPiece();
	
	public int getCellCoords(){
		return this.cellCoords;
	}
	
	public static final class EmptyCell extends Cell{
		EmptyCell(final int coords){
			super(coords);
		}
		
		@Override
		public String toString(){
			return "-";
		}
		
		@Override
		public boolean notEmptyCell() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}		
	}
	public static final class OccupiedCell extends Cell{
		private final Piece pieceOnCell;
		OccupiedCell(int cellCoords,final Piece pieceOnCell){
			super(cellCoords);
			this.pieceOnCell = pieceOnCell;
		}
		
		@Override
		public String toString(){
			return getPiece().getPieceUnion().isBlackUnion() ? getPiece().toString().toLowerCase() :
				   getPiece().toString();
		}
		
		@Override
		public boolean notEmptyCell() {
			return true;
		}
		@Override
		public Piece getPiece() {
			return this.pieceOnCell;
		}
	}
}
