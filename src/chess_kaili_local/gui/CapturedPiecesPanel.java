package chess_kaili_local.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.google.common.primitives.Ints;

import chess_kaili_local.PieceMove;
import chess_kaili_local.gui.Table.MoveLog;
import chess_kaili_local.pieces.Piece;

public class CapturedPiecesPanel extends JPanel{
	
	private final JPanel topPanel;
	private final JPanel bottomPanel;

	private static final Color PanelColor = Color.decode("0xFDFE6");
	private static final Dimension CapturedPiecesDimension = new Dimension(40,80);
	private static final EtchedBorder PanelBorder = new EtchedBorder(EtchedBorder.RAISED);
	
	
	public CapturedPiecesPanel(){
		super(new BorderLayout());
		this.setBackground(PanelColor);
		this.setBorder(PanelBorder);
		this.bottomPanel = new JPanel(new GridLayout(8,2));
		this.topPanel = new JPanel(new GridLayout(8,2));
		this.bottomPanel.setBackground(PanelColor);
		this.topPanel.setBackground(PanelColor);
		this.add(this.topPanel,BorderLayout.NORTH);
		this.add(this.bottomPanel,BorderLayout.SOUTH);
		setPreferredSize(CapturedPiecesDimension);
	}
	
	public void redo(final MoveLog moveLog){
		this.bottomPanel.removeAll();
		this.topPanel.removeAll();
		
		final List<Piece> whiteCapturedPieces = new ArrayList<>();
		final List<Piece> blackCapturedPieces = new ArrayList<>();
		
		for(final PieceMove move: moveLog.getPieceMoves()){
			if(move.isAttack()){
				final Piece capturedPiece = move.getCapturedPiece();
				if(capturedPiece.getPieceUnion().isWhiteUnion()){
					whiteCapturedPieces.add(capturedPiece);
				}else if(capturedPiece.getPieceUnion().isBlackUnion()){
					blackCapturedPieces.add(capturedPiece);
				}else{
					throw new RuntimeException("ERROR!");
				}
			}
		}
		
		Collections.sort(whiteCapturedPieces,new Comparator<Piece>(){
			@Override
			public int compare(Piece p1, Piece p2){
				return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
			}
		});
		
		Collections.sort(blackCapturedPieces,new Comparator<Piece>(){
			@Override
			public int compare(Piece p1, Piece p2){
				return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
			}
		});
		
		for(final Piece capturedPiece : whiteCapturedPieces){
			try{
				final BufferedImage img = ImageIO.read(new File("pieceImgs/"+capturedPiece.getPieceUnion().toString().substring(0,1)
														+ "" + capturedPiece.toString()));
				final ImageIcon icon = new ImageIcon(img);
				final JLabel imgLabel = new JLabel();
				this.bottomPanel.add(imgLabel);
			}catch(final IOException e){
				e.printStackTrace();
			}
		}
		
		for(final Piece capturedPiece : blackCapturedPieces){
			try{
				final BufferedImage img = ImageIO.read(new File("pieceImgs/"+capturedPiece.getPieceUnion().toString().substring(0,1)
														+ "" + capturedPiece.toString()));
				final ImageIcon icon = new ImageIcon(img);
				final JLabel imgLabel = new JLabel();
				this.bottomPanel.add(imgLabel);
			}catch(final IOException e){
				e.printStackTrace();
			}
		}
		validate();
	}
}
