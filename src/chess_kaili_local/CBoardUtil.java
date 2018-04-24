package chess_kaili_local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class CBoardUtil {
	
	public static final int CellNums = 64;
	public static final int CellNumsEachRow = 8;
	
	public static final boolean[] is8thRow = initialiseRow(0);
	public static final boolean[] is7thRow = initialiseRow(8);
	public static final boolean[] is6thRow = initialiseRow(16);
	public static final boolean[] is5thRow = initialiseRow(24);
	public static final boolean[] is4thRow = initialiseRow(32);
	public static final boolean[] is3thRow = initialiseRow(40);
	public static final boolean[] is2ndRow = initialiseRow(48);
	public static final boolean[] is1stRow = initialiseRow(56);
		
	public static final boolean[] is1stColumn = initialiseColumn(0);
	public static final boolean[] is2ndColumn = initialiseColumn(1);
	public static final boolean[] is7thColumn = initialiseColumn(6);
	public static final boolean[] is8thColumn = initialiseColumn(7);
	
	
	public static final List<String> AlgebraicNotation = intialiseAlgNotation();
	public static final Map<String,Integer> LocationToCoords = intialiseLocaToCoordsMap();
	
	public static final int StartCellIndex = 0; 
	

	private CBoardUtil(){
		throw new RuntimeException("ERROR!");
	}
	
	private static Map<String, Integer> intialiseLocaToCoordsMap() {
		final Map<String,Integer> locationToCoords = new HashMap<>();
		for(int i = StartCellIndex; i < CellNums; i++){
			locationToCoords.put(AlgebraicNotation.get(i), i);
		}
		return ImmutableMap.copyOf(locationToCoords);
	}

	private static List<String> intialiseAlgNotation() {
		return ImmutableList.copyOf(new String[]{
			"a8","b8","c8","d8","e8","f8","g8","h8",
			"a7","b7","c7","d7","e7","f7","g7","h7",	
			"a6","b6","c6","d6","e6","f6","g6","h6",	
			"a5","b5","c5","d5","e5","f5","g5","h5",	
			"a4","b4","c4","d4","e4","f4","g4","h4",	
			"a3","b3","c3","d3","e3","f3","g3","h3",	
			"a2","b2","c2","d2","e2","f2","g2","h2",	
			"a1","b1","c1","d1","e1","f1","g1","h1",	
		});		
	}

	private static boolean[] initialiseColumn(int columnNo) {
		final boolean[] column = new boolean[CellNums];
		do{
			column[columnNo] = true;
			columnNo = columnNo + CellNumsEachRow;
		}while(columnNo < CellNums);
		return column;
	}
	
	private static boolean[] initialiseRow(int rowNo){
		final boolean[] row = new boolean[CellNums];
		do{
			row[rowNo] = true;
			rowNo++;
		}while(rowNo % CellNumsEachRow != 0);
		return row;
	}

	public static boolean judgeValidCoords(final int coords) {
		return coords >= 0 && coords < CellNums;
	}
		
	public static int getDestinationCoords(final String location){
		return LocationToCoords.get(location);
	}
	
	public static String getDestinationLocation(final int coords){
		return AlgebraicNotation.get(coords);
	}

}
