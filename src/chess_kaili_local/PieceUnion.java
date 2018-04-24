package chess_kaili_local;

public enum PieceUnion {
	WHITE{
		@Override
		public
		int directedByUnion() {
			return -1;
		}

		@Override
		public boolean isBlackUnion() {
			return false;
		}

		@Override
		public boolean isWhiteUnion() {
			return true;
		}

		@Override
		public Player selectPlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
			return whitePlayer;
		}
	},
 	BLACK{
		@Override
		public
		int directedByUnion() {
			return 1;
		}

		@Override
		public boolean isBlackUnion() {
			return true;
		}

		@Override
		public boolean isWhiteUnion() {
			return false;
		}

		@Override
		public Player selectPlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
			return blackPlayer;
		}
	};
	
	public abstract int directedByUnion();
	public abstract boolean isBlackUnion();
	public abstract boolean isWhiteUnion();
	
	public abstract Player selectPlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer);
}
