
public class Card {
	
	private int cardID;
	private String path;
	
	public Card(int cardID, String path) {
		this.setCardID(cardID);
		this.setPath(path);
	}

	public int getCardID() {
		return cardID;
	}

	public void setCardID(int cardID) {
		this.cardID = cardID;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
