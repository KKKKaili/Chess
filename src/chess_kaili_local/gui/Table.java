package chess_kaili_local.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.google.common.collect.Lists;

import chess_kaili_local.CBoard;
import chess_kaili_local.CBoardUtil;
import chess_kaili_local.Cell;
import chess_kaili_local.MoveForward;
import chess_kaili_local.PieceMove;
import chess_kaili_local.pieces.Piece;

public class Table {
	

	private final JFrame chessFrame;
	private final BoardPanel boardPanel;
	private final MoveLog moveLog;
	private CBoard chessBoard;
	
	private final GameHistoryPanel gameHistoryPanel;
	private final CapturedPiecesPanel capturedPiecesPanel;
	
	private Cell sourceCell;
	private Cell destinationCell;
	private Piece humanPiece;
	private PlayerSwitch boardDirection;
	private boolean routeLegalMoves;
	
	private final static Dimension OutFrameDimension = new Dimension(600,600);
	private final static Dimension BoardPanelDimension = new Dimension(400,350);
	private final static Dimension CellPanelDimension = new Dimension(10,10);
	private static String PieceImgPath = "pieceImgs/";
	
	
	
	public Table(){
		this.chessFrame = new JFrame("Chess");	
		this.chessFrame.setLayout(new BorderLayout());
		final JMenuBar menuBar = generateMenuBar();
		this.chessFrame.setJMenuBar(menuBar);
		this.chessFrame.setSize(OutFrameDimension);	
		this.chessBoard = CBoard.generateInitialBoard();
		this.gameHistoryPanel = new GameHistoryPanel();
		this.capturedPiecesPanel = new CapturedPiecesPanel();
		this.boardPanel = new BoardPanel();
		this.moveLog = new MoveLog();
		this.boardDirection = PlayerSwitch.NORMAL;
		this.routeLegalMoves = false;
		this.chessFrame.add(this.capturedPiecesPanel, BorderLayout.WEST);	
		this.chessFrame.add(this.gameHistoryPanel,BorderLayout.EAST);
		this.chessFrame.add(this.boardPanel,BorderLayout.CENTER);
		this.chessFrame.setVisible(true);
		
	}
	
	private JMenuBar generateMenuBar(){
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(generateMenuFile());
		menuBar.add(generateMenuPreference());
		return menuBar;
	}

	private JMenu generateMenuFile() {
		final JMenu menuFile = new JMenu("FILE");
		final JMenuItem openPGNFile = new JMenuItem("OPEN PGN FILE");
		openPGNFile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open pgn file");
			}
			
		});
		menuFile.add(openPGNFile);
		
		final JMenuItem exit = new JMenuItem("EXIT");
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}	
		});
		menuFile.add(exit);
		return menuFile;
	}
	
	private JMenu generateMenuPreference(){
		final JMenu menuPreference = new JMenu("PREFERENCE");
		final JMenuItem flipBoard = new JMenuItem("FLIP BOARD");
		flipBoard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
		});
		menuPreference.add(flipBoard);
		menuPreference.addSeparator();
		final JCheckBoxMenuItem legalMoveRoute = new JCheckBoxMenuItem("ROUTE LEGAL MOVES",false);
		legalMoveRoute.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				routeLegalMoves = legalMoveRoute.isSelected();
			}
			
		});
		menuPreference.add(legalMoveRoute);
		return menuPreference;
	}
	
	public enum PlayerSwitch{
		
		NORMAL{
			@Override
			List<CellPanel> traverse(final List<CellPanel> boardCells) {
				return boardCells;
			}

			@Override
			PlayerSwitch opposite() {
				return FLIPPED;
			}
		},
		FLIPPED{
			@Override
			List<CellPanel> traverse(List<CellPanel> boardCells) {
				return Lists.reverse(boardCells);
			}

			@Override
			PlayerSwitch opposite() {
				return NORMAL;
			}
		};
		abstract List<CellPanel> traverse(final List<CellPanel> boardCells);
		abstract PlayerSwitch opposite();
	}
	
	private class BoardPanel extends JPanel{
		final List<CellPanel> boardCells;
		
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardCells = new ArrayList<>();
			for(int i=0;i<CBoardUtil.CellNums;i++){
				final CellPanel cellPanel = new CellPanel(this,i);
				this.boardCells.add(cellPanel);
				add(cellPanel);
			}
			setPreferredSize(BoardPanelDimension);
			validate();			
		}
		public void drawBoard(final CBoard cBoard){
			removeAll();
			for(final CellPanel cellPanel : boardDirection.traverse(boardCells)){
				cellPanel.drawCell(cBoard);
				add(cellPanel);
			}
			validate();
			repaint();
		}
	}
	
	public static class MoveLog{
		
		private final List<PieceMove> pieceMoves;
		MoveLog(){
			this.pieceMoves = new ArrayList<>();
		}
		
		public List<PieceMove> getPieceMoves(){
			return this.pieceMoves;
		}
			
		public void addPieceMove(final PieceMove pieceMove){
			this.pieceMoves.add(pieceMove);
		}
		
		public boolean removeMove(final PieceMove pieceMove){
			return this.pieceMoves.remove(pieceMove);
		}
		
		public PieceMove removeMove(int i){
			return this.pieceMoves.remove(i);
		}
		
		public int size(){
			return this.pieceMoves.size();
		}
		
		public void clear(){
			this.pieceMoves.clear();
		}
	}
	
	private class CellPanel extends JPanel{
		
		private final int cellID;
		
		CellPanel(final BoardPanel boardPanel,final int cellID){
			super(new GridBagLayout());
			this.cellID = cellID;
			setPreferredSize(CellPanelDimension);
			setCellColor();
			setPieceImg(chessBoard);
			
			addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(final MouseEvent e) {
					//right click to undo selection
					if(SwingUtilities.isRightMouseButton(e)){
						sourceCell = null;
						destinationCell = null;
						humanPiece = null;	
					}else if(SwingUtilities.isLeftMouseButton(e)){
						//first click without sourceCell and assign the sourceCell 
						if(sourceCell == null){
							sourceCell = chessBoard.getCell(cellID);
							humanPiece = sourceCell.getPiece();
							//the cell that the player is clicking is empty
							if(humanPiece == null){
								sourceCell = null;
							}
						}else{
							destinationCell = chessBoard.getCell(cellID);
							final PieceMove pieceMove = PieceMove.MoveFactory.generateMove(chessBoard, sourceCell.getCellCoords(),
																						 destinationCell.getCellCoords());
							final MoveForward forward = chessBoard.nowPlayer().makeMove(pieceMove);
							if(forward.getMoveState().isFinished()){
								chessBoard = forward.getForwardBoard();
								moveLog.addPieceMove(pieceMove);
							}
							sourceCell = null;
							destinationCell = null;
							humanPiece = null;					
						}
						SwingUtilities.invokeLater(new Runnable(){

							@Override
							public void run() {
								gameHistoryPanel.redo(chessBoard, moveLog);
								capturedPiecesPanel.redo(moveLog);
								boardPanel.drawBoard(chessBoard);						
							}
							
						});
					}
					
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					
					
				}

				@Override
				public void mouseExited(MouseEvent e) {

					
				}
				
			});
			
			validate();
		}
		
		public void drawCell(final CBoard cBoard){
			setCellColor();
			setPieceImg(cBoard);
			routeLegalMoves(cBoard);
			validate();
			repaint();
		}
		
		private void setPieceImg(final CBoard cBoard){
			this.removeAll();
			if(cBoard.getCell(this.cellID).notEmptyCell()){
	
				try {
					final BufferedImage img = 
							ImageIO.read(new File(PieceImgPath + cBoard.getCell(this.cellID).getPiece().getPieceUnion().toString().substring(0,1) +
									     cBoard.getCell(this.cellID).getPiece().toString() + ".png"));
					//System.out.print("img:" +img);
					add(new JLabel(new ImageIcon(img)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void routeLegalMoves(final CBoard cBoard){
			if(routeLegalMoves){
				for(final PieceMove pieceMove : pieceLegalMoves(cBoard)){
					if(pieceMove.getDestinationCoords() == this.cellID){
						try{
							add(new JLabel(new ImageIcon(ImageIO.read(new File("pics/routeDot.png")))));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private Collection<PieceMove> pieceLegalMoves(final CBoard cBoard){
			if(humanPiece != null && humanPiece.getPieceUnion() == cBoard.nowPlayer().getPieceUnion()){
				return humanPiece.getLegalMoves(cBoard);
			}
			return Collections.emptyList();
		}

		private void setCellColor() {
			final Color lightYello = new Color(255,235,205);
			final Color deepBrown = new Color(205,133,63);
			if(CBoardUtil.is8thRow[this.cellID] || CBoardUtil.is6thRow[this.cellID] || CBoardUtil.is4thRow[this.cellID] || 
			   CBoardUtil.is2ndRow[this.cellID]){
				setBackground(this.cellID % 2 == 0? lightYello : deepBrown);
			}else if(CBoardUtil.is7thRow[this.cellID] || CBoardUtil.is5thRow[this.cellID]||CBoardUtil.is3thRow[this.cellID] || 
					 CBoardUtil.is1stRow[this.cellID]){
				setBackground(this.cellID % 2 != 0? lightYello : deepBrown);
			}
			
		}
	}
	
}
