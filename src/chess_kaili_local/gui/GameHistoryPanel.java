package chess_kaili_local.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import chess_kaili_local.CBoard;
import chess_kaili_local.PieceMove;
import chess_kaili_local.gui.Table.MoveLog;

public class GameHistoryPanel extends JPanel{

	private final DataModel model;
	private final JScrollPane scrollPane;
	private static final Dimension HistoryPanelDimension = new Dimension(100,400);
	
	GameHistoryPanel(){
		this.setLayout(new BorderLayout());
		this.model = new DataModel();
		final JTable table = new JTable(model);
		table.setRowHeight(15);
		this.scrollPane = new JScrollPane(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setPreferredSize(HistoryPanelDimension);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	void redo(final CBoard cBoard, final MoveLog moveLog){
		int nowRow = 0;
		this.model.clear();
		for(final PieceMove move : moveLog.getPieceMoves()){
			final String moveText = move.toString();
			if(move.getMovedPiece().getPieceUnion().isWhiteUnion()){
				this.model.setValueAt(moveText, nowRow, 0);
			}else if(move.getMovedPiece().getPieceUnion().isBlackUnion()){
				this.model.setValueAt(moveText, nowRow, 1);
				nowRow++;
			}
		}
		
		if(moveLog.getPieceMoves().size() > 0){
			final PieceMove lastMove = moveLog.getPieceMoves().get(moveLog.size() - 1);
			final String moveText = lastMove.toString();
			
			if(lastMove.getMovedPiece().getPieceUnion().isWhiteUnion()){
				this.model.setValueAt(moveText + figureCheckAndCheckMateHash(cBoard), nowRow, 0);
			}else if(lastMove.getMovedPiece().getPieceUnion().isBlackUnion()){
				this.model.setValueAt(moveText + figureCheckAndCheckMateHash(cBoard), nowRow - 1, 1);
			}
		}
		
		final JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private String figureCheckAndCheckMateHash(final CBoard cBoard) {
		if(cBoard.nowPlayer().isInCheckMate()){
			return "#";
		}else if(cBoard.nowPlayer().isInCheck()){
			return "+";
		}
		return "";
			
	}

	private static class DataModel extends DefaultTableModel{
		private final List<Row> values;
		private static final String[] Names = {"WHITE","BLACK"};
		
		DataModel(){
			this.values = new ArrayList<>();
		}
		
		public void clear(){
			this.values.clear();
			setRowCount(0);
		}
		
		@Override
		public int getRowCount(){
			if(this.values == null){
				return 0;
			}
			return this.values.size();
		}
		
		@Override
		public int getColumnCount(){
			return Names.length;
		}
		
		@Override
		public Object getValueAt(final int row, final int column){
			final Row nowRow = this.values.get(row);
			if(column == 0){
				return nowRow.getWhiteMove();
			}else if(column == 1){
				return nowRow.getBlackMove();
			}
			return null;
		}
		
		@Override
		public void setValueAt(final Object pValue,final int row,final int column){
			final Row nowRow;
			if(this.values.size() <= row){
				nowRow = new Row();
				this.values.add(nowRow);
			}else{
				nowRow = this.values.get(row);
			}
			if(column == 0){
				nowRow.setWhiteMove((String)pValue);
				fireTableRowsInserted(row,row);
			}else if(column == 1){
				nowRow.setBlackMove((String)pValue);
				fireTableCellUpdated(row,column);
			}
		}
		
		@Override
		public Class<?> getColumnClass(final int column){
			return PieceMove.class;
		}
		
		@Override
		public String getColumnName(final int column){
			return Names[column];
		}
	}
	
	private static class Row{
		private String whiteMove;
		private String blackMove;
		Row(){		
		}
		
		public String getWhiteMove(){
			return this.whiteMove;
		}
		
		public String getBlackMove(){
			return this.blackMove;
		}
		
		public void setWhiteMove(final String move){
			this.whiteMove = move;
		}
		
		public void setBlackMove(final String move){
			this.blackMove = move;
		}
	}
}
