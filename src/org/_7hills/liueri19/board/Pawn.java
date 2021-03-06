package org._7hills.liueri19.board;

/**
 * Represents a Pawn. This class overrides certain methods in Piece.
 * @author liueri19
 *
 */
public class Pawn extends Piece {

	public Pawn(Board board, boolean color, int x, int y) {
		super(board, color, x, y);
	}

	public Pawn(Board board, boolean color, int[] coordinate) {
		super(board, color, coordinate);
	}

	@Override
	public String toString() {
		String result;
		if (this.getColor())
			result = "WP@";
		else
			result = "BP@";
		result += getFile();
		result += getRank();
		return result;
	}

	@Override
	public String toBriefString() {
		if (getColor())
			return "WP";
		return "BP";
	}

	@Override
	void updatePiece(boolean threatsOnly) {
		if (!threatsOnly)
			clearLegalMoves();
		clearThreats();
		int[] square = getSquare();
		Piece target;
		if (getColor()) {
			boolean canPromote = square[1] == 7;
			if (getBoard().getPieceAt(square[0], square[1] + 1) == null) {	//white pawn, moving up, new int[] {square[0], square[1] + 1}), threatsOnly);
				if (!canPromote) {
					checkMove(new Move(this, square, new int[]{square[0], square[1] + 1}), threatsOnly);
					if (square[1] == 2 && getBoard().getPieceAt(square[0], square[1] + 2) == null)    //on the second rank
						checkMove(new Move(this, square, new int[]{square[0], square[1] + 2}), threatsOnly);
				}
				else
					checkMove(new Promotion(this, new int[] {square[0], square[1] + 1}, null), threatsOnly);
			}
			if ((target = getBoard().getPieceAt(square[0] - 1, square[1] + 1)) != null &&
					target.getColor() != getColor()) {    //take and promote
				if (!canPromote)
					checkMove(new Move(this, target, square, new int[]{square[0] - 1, square[1] + 1}), threatsOnly);
				else {
					checkMove(
							new Promotion(this, target, new int[]{square[0] - 1, square[1] + 1}, null),
							threatsOnly);
				}
			}
			if ((target = getBoard().getPieceAt(square[0] + 1, square[1] + 1)) != null &&
					target.getColor() != getColor()) {
				if (!canPromote)
					checkMove(new Move(this, target, square, new int[]{square[0] + 1, square[1] + 1}), threatsOnly);
				else {
					checkMove(
							new Promotion(this, target, new int[]{square[0] + 1, square[1] + 1}, null),
							threatsOnly);
				}
			}
			/*
			 * if (last move == file left || file right && is pawn move)
			 *   add en passant;
			 */
			if (getRank() == 5) {
				Move lastMove = getBoard().getMove(getBoard().getCurrentMoveNum() - 1);
				if (lastMove != null) {
					int[] origin = lastMove.getOrigin();
					int[] des = lastMove.getDestination();
					if (lastMove.getInit() instanceof Pawn && origin[1] - des[1] == 2) {
						if (origin[0] == getFile() - 1 || origin[0] == getFile() + 1) {
							checkMove(new Move(this, lastMove.getInit(), getSquare(),
									new int[] {origin[0], des[1] + 1}), threatsOnly);
						}
					}
				}
			}
		}
		else {
			boolean canPromote = square[1] == 2;
			if (getBoard().getPieceAt(square[0], square[1] - 1) == null) {	//black pawn, moving down
				if (!canPromote) {
					checkMove(new Move(this, square, new int[] {square[0], square[1] - 1}), threatsOnly);
					if (square[1] == 7 && getBoard().getPieceAt(square[0], square[1] - 2) == null)	//on the second rank
						checkMove(new Move(this, square, new int[] {square[0], square[1] - 2}), threatsOnly);
				}
				else
					checkMove(new Promotion(this, new int[]{square[0], square[1] - 1}, null), threatsOnly);
			}
			if ((target = getBoard().getPieceAt(square[0] - 1, square[1] - 1)) != null &&
					target.getColor() != getColor()) {
				if (!canPromote)
					checkMove(new Move(this, target, square, new int[]{square[0] - 1, square[1] - 1}), threatsOnly);
				else {
					checkMove(
							new Promotion(this, target, new int[]{square[0] - 1, square[1] - 1}, null),
							threatsOnly);
				}
			}
			if ((target = getBoard().getPieceAt(square[0] + 1, square[1] - 1)) != null &&
					target.getColor() != getColor()) {
				if (!canPromote)
					checkMove(new Move(this, target, square, new int[]{square[0] + 1, square[1] - 1}), threatsOnly);
				else {
					checkMove(
							new Promotion(this, target, new int[]{square[0] + 1, square[1] - 1}, null),
							threatsOnly);
				}
			}
			//en passant
			if (getRank() == 4) {
				Move lastMove = getBoard().getMove(getBoard().getCurrentMoveNum() - 1);
				if (lastMove != null) {
					int[] origin = lastMove.getOrigin();
					int[] des = lastMove.getDestination();
					if (lastMove.getInit() instanceof Pawn && des[1] - origin[1] == 2) {
						int lastMoveFile = origin[0];
						if (lastMoveFile == getFile() - 1 || lastMoveFile == getFile() + 1) {
							checkMove(new Move(this, lastMove.getInit(), getSquare(),
									new int[] {lastMoveFile, des[1] - 1}), threatsOnly);
						}
					}
				}
			}
		}
	}

}
