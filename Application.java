import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Font;

public class Application {

	private JFrame frmPlayingCards;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frmPlayingCards.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Application() {
		for(int i = 1; i < 52; i++) list.add(i);
		initialize();
	}
	
	int panelW;
	int panelH;
	int cardTopOffset;
	int cardBottomOffset;
	int cardWidthOffset;
	
	boolean playerTurn = false;
	
	int playerCardCount = 0;
	int botCardCount = 0;
	
	int tableCardID = 0;
	
	ArrayList<Integer> list = new ArrayList<Integer>();
	ArrayList<JLabel> playerCardsObj = new ArrayList<JLabel>();
	ArrayList<JLabel> botCardsObj = new ArrayList<JLabel>();
	ArrayList<Integer> playerCards = new ArrayList<Integer>();
	ArrayList<Integer> botCards = new ArrayList<Integer>();
	
	JLabel infoText;
	JLabel deck;
	JLabel startCard;
	
	int animationTime = 400;
	int animationSpeed = 8;
	JLabel animatedCard = null;
	int animatedCardID, animatedCardIndex;
	int deltaX, deltaY;
	int targetX;
	
	Boolean IsAValidCard(int cardID) {
		if(list.contains(cardID)) return true;
		return false;
	}
	
	int SimplifyCardID(int cardID) {
		if(cardID > 13) cardID %= 13;
		return cardID;
	}
	
	String ConvertCardIDToPath(int cardID) {
		if(cardID == 1) {
			return "/res/" + "HeartA.png";
		}
		else if(cardID >= 2 && cardID <= 10) {
			return "/res/" + "Heart" + cardID + ".png";
		}
		else if(cardID == 11) {
			return "/res/" + "HeartJ.png";
		}
		else if(cardID == 12) {
			return "/res/" + "HeartQ.png";
		}
		else if(cardID == 13) {
			return "/res/" + "HeartK.png";
		}
		else if(cardID == 14) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "DiamondA.png";
		}
		else if(cardID >= 15 && cardID <= 23) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "Diamond" + cardID + ".png";
		}
		else if(cardID == 24) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "DiamondJ.png";
		}
		else if(cardID == 25) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "DiamondQ.png";
		}
		else if(cardID == 26) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "DiamondK.png";
		}
		else if(cardID == 27) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "SpadeA.png";
		}
		else if(cardID >= 28 && cardID <= 36) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "Spade" + cardID + ".png";
		}
		else if(cardID == 37) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "SpadeJ.png";
		}
		else if(cardID == 38) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "SpadeQ.png";
		}
		else if(cardID == 39) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "SpadeK.png";
		}
		else if(cardID == 40) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "ClubA.png";
		}
		else if(cardID >= 41 && cardID <= 49) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "Club" + cardID + ".png";
		}
		else if(cardID == 50) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "ClubJ.png";
		}
		else if(cardID == 51) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "ClubQ.png";
		}
		else if(cardID == 52) {
			cardID = SimplifyCardID(cardID);
			return "/res/" + "ClubK.png";
		}
		else return "";
	}
	
	Card GenerateARandomCard() {
		Random rand = new Random();
		int cardID;
		do {
			cardID = rand.nextInt(1, 53);
		}
		while(!IsAValidCard(cardID));
		
		list.remove((Object)cardID);
		
		return new Card(cardID, ConvertCardIDToPath(cardID));
	}
	
	int GetCardColor(int cardID) // 0 - black, 1 - red 
	{
		if(cardID >= 1 && cardID <= 26) return 1;
		else return 0;
	}
	
	int GetCardSymbol(int cardID) // 0 - heart, 1 - diamond, 2 - spade, 3 - club
	{
		if(cardID >= 1 && cardID <= 13)
			return 0;
		else if(cardID >= 14 && cardID <= 26)
			return 1;
		else if(cardID >= 27 && cardID <= 39)
			return 2;
		else
			return 3;
	}
	
	int GetCardNumber(int cardID) {
		return cardID % 13;
	}

	int GetLowestCardX(int panelWidth, int cardCount, int cardWidth, int cardWidthOffset) {
		return (panelWidth - (cardCount * (cardWidth + cardWidthOffset) - cardWidthOffset)) / 2; 
	}
	
	void repaintPlayerCards(int cardCount, int cardWidth, int cardHeight) {
		int startX = GetLowestCardX(panelW, cardCount, cardWidth, cardWidthOffset);
		for(int i = 0; i < cardCount; i++) {
			playerCardsObj.get(i).setBounds(startX + i * (cardWidth + cardWidthOffset), panelH - cardHeight - cardBottomOffset, cardWidth, cardHeight);
		}
		frmPlayingCards.repaint();
	}
	
	void repaintBotCards(int cardCount, int cardWidth, int cardHeight) {
		int startX = GetLowestCardX(panelW, cardCount, cardWidth, cardWidthOffset);
		for(int i = 0; i < cardCount; i++) {
			botCardsObj.get(i).setBounds(startX + i * (cardWidth + cardWidthOffset), cardTopOffset, cardWidth, cardHeight);
		}
		frmPlayingCards.repaint();
	}
	
	void CheckIfCardIsPlayable(int cardID) {
		if(GetCardSymbol(cardID) == GetCardSymbol(tableCardID) || GetCardNumber(cardID) == GetCardNumber(tableCardID)) {
			animatedCard = playerCardsObj.get(playerCards.indexOf(cardID));
			animatedCardID = cardID;
			deltaX = startCard.getBounds().x - animatedCard.getBounds().x;
			deltaY = startCard.getBounds().y - animatedCard.getBounds().y;
			
			playerTurn = false;
			
			if(playerCardCount == 1)
				infoText.setText("You won!");
			else {
				if(list.size() > 0) {
					Timer timer = new Timer(1200, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg) {
							StartBotTurn();
						}
					});
					timer.setRepeats(false);
					timer.start();
					
					infoText.setText("It's bot's turn now");
				}
				else {
					if(!CheckIfIsDraw(false)) {
						Timer timer = new Timer(1200, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg) {
								StartBotTurn();
							}
						});
						timer.setRepeats(false);
						timer.start();
						infoText.setText("It's bot's turn now");
					}
				}
			}
		}
		else {
			infoText.setText("You can't use that card.");
		}
	}
	
	boolean CheckIfNoCardIsPlayable(ArrayList<Integer> cards) {
		for(int i = 0; i < cards.size(); i++) {
			if(GetCardSymbol(cards.get(i)) == GetCardSymbol(tableCardID) || GetCardNumber(cards.get(i)) == GetCardNumber(tableCardID))
				return false;
		}
		return true;
	}
	
	void DrawCardToPlayer() {
		JLabel card = new JLabel("");
		Card cardClass = GenerateARandomCard();
		
		try {
			card.setIcon(new ImageIcon(Application.class.getResource(cardClass.getPath())));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	    card.addMouseListener(new MouseAdapter() {
	    	@Override
	        public void mouseClicked(MouseEvent e) {
	    		if(playerTurn)
	    			CheckIfCardIsPlayable(cardClass.getCardID());
	    		else
	    			if(playerCardCount > 0 && botCardCount > 0)
	    				infoText.setText("It's not your turn.");
	        }
	    });
	    
		card.setVisible(true);
		playerCardsObj.add(card);
		playerCards.add(cardClass.getCardID());
		frmPlayingCards.getContentPane().add(card);
		playerCardCount++;
		repaintPlayerCards(playerCardCount, 77, 128);
	}
	
	void DrawCardToBot() {
		JLabel card = new JLabel("");
		Card cardClass = GenerateARandomCard();
		
		try {
			if(GetCardColor(cardClass.getCardID()) == 0)
				card.setIcon(new ImageIcon(Application.class.getResource("/res/BlackCard.png")));
			else
				card.setIcon(new ImageIcon(Application.class.getResource("/res/RedCard.png")));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		card.setVisible(true);
		botCardsObj.add(card);
		botCards.add(cardClass.getCardID());
		frmPlayingCards.getContentPane().add(card);
		botCardCount++;
		repaintBotCards(botCardCount, 77, 128);
	}
	
	void DrawStartCard() {
		startCard = new JLabel("");
		Card cardClass = GenerateARandomCard();
		
		tableCardID = cardClass.getCardID();
		
		try {
			startCard.setIcon(new ImageIcon(Application.class.getResource(cardClass.getPath())));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		startCard.setBounds(panelW / 2 - 77 / 2, panelH / 2 - 128 / 2, 77, 128);
		startCard.setVisible(true);
		frmPlayingCards.getContentPane().add(startCard);
		frmPlayingCards.repaint();
	}
	
	void UpdateStartCard() {
		startCard.setIcon(new ImageIcon(Application.class.getResource(ConvertCardIDToPath(tableCardID))));
	}
	
	void UpdateDeck() {
		if(list.size() >= 30)
			deck.setIcon(new ImageIcon(Application.class.getResource("/res/FullDeck.png")));
		else if(list.size() >= 20 && list.size() < 30)
			deck.setIcon(new ImageIcon(Application.class.getResource("/res/MidDeck.png")));
		else if(list.size() >= 10 && list.size() < 20)
			deck.setIcon(new ImageIcon(Application.class.getResource("/res/SmallDeck.png")));
		else if(list.size() <= 10 && list.size() >= 1)
			deck.setIcon(new ImageIcon(Application.class.getResource("/res/EmptyDeck.png")));
		else if(list.size() == 0)
			deck.setIcon(null);
	}
	
	boolean CheckIfIsDraw(boolean pTurn) {
		if(pTurn) {
			if(playerCardCount > 1 && CheckIfNoCardIsPlayable(playerCards)) {
				infoText.setText("Draw!");
				playerTurn = false;
				return true;
			}
			else if(playerCardCount == 1) {
				if(GetCardSymbol(playerCards.get(0)) == GetCardSymbol(tableCardID) || GetCardNumber(playerCards.get(0)) == GetCardNumber(tableCardID))
					return false;
				else {
					infoText.setText("Draw!");
					playerTurn = false;
					return true;
				}
			}
		}
		else {
			if(botCardCount > 1 && CheckIfNoCardIsPlayable(botCards)) {
				infoText.setText("Draw!");
				return true;
			}
			else if(botCardCount == 1) {
				if(GetCardSymbol(botCards.get(0)) == GetCardSymbol(tableCardID) || GetCardNumber(botCards.get(0)) == GetCardNumber(tableCardID))
					return false;
				else {
					infoText.setText("Draw!");
					return true;
				}
			}
		}
		return false;
	}
	
	void StartBotTurn() {
		int cardIndex = -1;
		for(int i = 0; i < botCardCount; i++) {
			if(GetCardSymbol(botCards.get(i)) == GetCardSymbol(tableCardID) || GetCardNumber(botCards.get(i)) == GetCardNumber(tableCardID)) {
				cardIndex = i;
				break;
			}
		}
		if(cardIndex == -1) {
			if(list.size() > 0) {
				DrawCardToBot();
				UpdateDeck();
				playerTurn = true;
				infoText.setText("It's your turn");
			}
		}
		else {
			playerTurn = true;
			animatedCard = botCardsObj.get(cardIndex);
			animatedCard.setIcon(new ImageIcon(Application.class.getResource(ConvertCardIDToPath(botCards.get(cardIndex)))));
			animatedCardID = botCards.get(cardIndex);
			animatedCardIndex = cardIndex;
			deltaX = startCard.getBounds().x - animatedCard.getBounds().x;
			deltaY = startCard.getBounds().y - animatedCard.getBounds().y;
		}
	}
	
	void move() {
		if(animatedCard != null) {
			animatedCard.setBounds(animatedCard.getBounds().x + deltaX / (animationTime / animationSpeed), animatedCard.getBounds().y + deltaY / (animationTime / animationSpeed), animatedCard.getBounds().width, animatedCard.getBounds().height);
			if((deltaX > 0 && animatedCard.getBounds().x >= startCard.getBounds().x) || (!playerTurn && deltaY < 0 && animatedCard.getBounds().y <= startCard.getBounds().y) || (playerTurn && deltaY > 0 && animatedCard.getBounds().y >= startCard.getBounds().y)) {
				if(!playerTurn) {
					System.out.println(1);
					frmPlayingCards.remove(playerCardsObj.get(playerCards.indexOf(animatedCardID)));
					playerCardsObj.remove(playerCards.indexOf(animatedCardID));
					playerCards.remove(playerCards.indexOf(animatedCardID));
					tableCardID = animatedCardID;
					UpdateDeck();
					UpdateStartCard();
					playerCardCount--;
					repaintPlayerCards(playerCardCount, 77, 128);
				}
				else {
					System.out.println(2);
					tableCardID = botCards.get(animatedCardIndex);
					UpdateStartCard();
					
					frmPlayingCards.remove(botCardsObj.get(animatedCardIndex));
					botCardsObj.remove(animatedCardIndex);
					botCards.remove(animatedCardIndex);
					botCardCount--;
					repaintBotCards(botCardCount, 77, 128);
					
					if(botCardCount == 0) {
						infoText.setText("You lost!");
						playerTurn = false;
					}
					else {
						if(list.size() > 0) {
							playerTurn = true;
							infoText.setText("It's your turn now");
						}
						else {
							if(!CheckIfIsDraw(true)) {
								playerTurn = true;
								infoText.setText("It's your turn now");
							}
						}
					}
				}
				
				animatedCard = null;
			}
			else if((deltaX <= 0 && animatedCard.getBounds().x <= startCard.getBounds().x) || (!playerTurn && deltaY < 0 && animatedCard.getBounds().y <= startCard.getBounds().y) || (playerTurn && deltaY > 0 && animatedCard.getBounds().y >= startCard.getBounds().y)) {
				if(!playerTurn) {
					System.out.println(3);
					frmPlayingCards.remove(playerCardsObj.get(playerCards.indexOf(animatedCardID)));
					playerCardsObj.remove(playerCards.indexOf(animatedCardID));
					playerCards.remove(playerCards.indexOf(animatedCardID));
					tableCardID = animatedCardID;
					UpdateDeck();
					UpdateStartCard();
					playerCardCount--;
					repaintPlayerCards(playerCardCount, 77, 128);
				}
				else {
					System.out.println(4);
					tableCardID = botCards.get(animatedCardIndex);
					UpdateStartCard();
					
					frmPlayingCards.remove(botCardsObj.get(animatedCardIndex));
					botCardsObj.remove(animatedCardIndex);
					botCards.remove(animatedCardIndex);
					botCardCount--;
					repaintBotCards(botCardCount, 77, 128);
					
					if(botCardCount == 0) {
						infoText.setText("You lost!");
						playerTurn = false;
					}
					else {
						if(list.size() > 0) {
							playerTurn = true;
							infoText.setText("It's your turn now");
						}
						else {
							if(!CheckIfIsDraw(true)) {
								playerTurn = true;
								infoText.setText("It's your turn now");
							}
						}
					}
				}
				
				animatedCard = null;
			}
		}
	}
	
	public void startAnimation() {
        SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                while (true) {
                    move();
                    Thread.sleep(animationSpeed);
                }
            }
        };

        sw.execute();
    }
	
	private void initialize() {
		frmPlayingCards = new JFrame();
		frmPlayingCards.getContentPane().setBackground(Color.WHITE);
		frmPlayingCards.setMinimumSize(new Dimension(1200, 1000));
		frmPlayingCards.setTitle("Playing Cards");
		frmPlayingCards.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPlayingCards.getContentPane().setLayout(null);
		
		panelW = frmPlayingCards.getWidth();
		panelH = frmPlayingCards.getHeight();
		int buttonW = 180;
		int buttonH = 70;
		cardTopOffset = 8;
		cardBottomOffset = 64;
		cardWidthOffset = 33;
		
		infoText = new JLabel("");
		infoText.setHorizontalAlignment(SwingConstants.CENTER);
		infoText.setFont(new Font("Arial", Font.BOLD, 20));
		infoText.setBounds(panelW / 2 - 500 / 2, panelH / 2 - 300, 500, 100);
		infoText.setVisible(true);
		frmPlayingCards.getContentPane().add(infoText);
		
		deck = new JLabel("");
		deck.setBounds(panelW / 2 - 300, panelH / 2 - 128 / 2, 77, 128);
		deck.setVisible(false);
		deck.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(playerTurn) {
					if(CheckIfNoCardIsPlayable(playerCards)) {
						if(list.size() > 0) {
							DrawCardToPlayer();
							repaintPlayerCards(playerCardCount, 77, 128);
							UpdateDeck();
					
							playerTurn = false;
							Timer timer = new Timer(1200, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg) {
									StartBotTurn();
								}
							});
							timer.setRepeats(false);
							timer.start();
							infoText.setText("It's bot's turn now");
						}
					}
					else {
						infoText.setText("You have cards to play.");
					}
				}
			}
		});
		frmPlayingCards.getContentPane().add(deck);
		
		JButton btnNewButton = new JButton("Start Game");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setVisible(false);
				for(int i = 0; i < 5; i++) { 
					DrawCardToPlayer();
					DrawCardToBot();
				}
				DrawStartCard();
				infoText.setText("It's your turn now");
				playerTurn = true;
				UpdateDeck();
				deck.setVisible(true);
			}
		});
		btnNewButton.setBounds(panelW / 2 - buttonW / 2, panelH / 2 - buttonH, buttonW, buttonH);
		frmPlayingCards.getContentPane().add(btnNewButton);
		
		startAnimation();
	}
}
