package chess_kaili_local;

public enum MoveState {

	Finished {
		@Override
		public
		boolean isFinished() {
			return true;
		}
	},
	IllegalMove {
		@Override
		public
		boolean isFinished() {
			return false;
		}
	},
	PlayerInCheck {
		@Override
		public
		boolean isFinished() {
			return false;
		}
	};
	
	public abstract boolean isFinished(); 
}
