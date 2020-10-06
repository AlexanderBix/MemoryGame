import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

public class MemoryGame {

    static int points = 0; // Number of correct guesses made
    static int guesses = 0; // Number of incorrect guesses made
    static int counter = 0; // Number of actively flipped up cards
    final static int[] timer = {1}; // Current Time

    // List of all images


    // Creates a Grid Jpanel with images
    public static JPanel createButtons(JFrame frame, JLabel pointDisplay) {

         String[] imageList = {
                "images/cat1.jpg", "images/cat2.jpg", "images/cat3.jpg", "images/cat4.jpg", "images/cat5.jpg", "images/cat6.jpg",
                "images/cat7.jpg", "images/cat8.jpg", "images/cat9.jpg", "images/cat10.jpg", "images/cat11.jpg", "images/cat12.jpg",
                "images/cat1.jpg", "images/cat2.jpg", "images/cat3.jpg", "images/cat4.jpg", "images/cat5.jpg", "images/cat6.jpg",
                "images/cat7.jpg", "images/cat8.jpg", "images/cat9.jpg", "images/cat10.jpg", "images/cat11.jpg", "images/cat12.jpg",
                "images/dog.jpg" // dog.jpg is the 'smiley face'
        };

        // Array that stores properties of cards
        CardProperties[] cardIndex = {};

        // Grid layout dimensions
        int horizontal = 5; // Rows
        int vertical = 5; // Columns

        int index = imageList.length;
        int arrayIndex = 0;

        // Configuring JPanel with Grid Layout
        JPanel cards = new JPanel();
        GridLayout grid = new GridLayout(horizontal,vertical,15,15);
        cards.setLayout(grid);

        // Configuring JButton Array
        JButton[][] buttons = new JButton[horizontal][vertical];

        // For each row
        for(int i = 0; i < horizontal; i++) {
            // For each column
            for(int j = 0; j < vertical; j++) {
                // Pick a random picture from the image list
                int randomPic = (int)(imageList.length * Math.random());

                // Displaying default black square
                 ImageIcon icon = new ImageIcon("images/blackSquare.jpg");
                 buttons[i][j] = new JButton(icon);
                 buttons[i][j].setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
                 buttons[i][j].setContentAreaFilled(false);
                 cards.add(buttons[i][j]);

                // Creating Card Properties
                CardProperties entry = new CardProperties();
                entry.path = imageList[randomPic];
                entry.index = arrayIndex;
                entry.isActive = false;
                entry.isMatched = false;

                // Adding entry to entry index
                cardIndex = CardProperties.addEntry(cardIndex,entry);

                // Removing image from the image list
                imageList = arrayHelper.removeArray(imageList,randomPic);
                arrayIndex++;
            }
        }

        // Creating button listeners
        int k = 0;
        for(int i = 0; i < horizontal; i++) {
            for(int j = 0; j < vertical; j++) {
                buttonLogic(horizontal,vertical,i,j,k,cardIndex,buttons,cards,frame,pointDisplay);
                k++;

            }
         }
        return cards;
    }

    // Logic When Button is pressed
    public static void buttonLogic(int xBound,int yBound,int vertical, int horizontal, int index, CardProperties[] cardIndex, JButton[][] buttons, JPanel cards, JFrame frame, JLabel pointDisplay) {
        buttons[vertical][horizontal].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int cIndex = CardProperties.findEntry(index, cardIndex);
                ImageIcon icon = null;

                // If happy smile is picked
                if(cardIndex[cIndex].path.equals("images/dog.jpg")) {
                    icon = new ImageIcon(cardIndex[cIndex].path);
                    buttons[vertical][horizontal].setIcon(icon);
                    cards.remove(index);
                    cards.add(buttons[vertical][horizontal],index);
                }

                // If the card hasn't been verified already
                else if(!cardIndex[cIndex].isMatched) {

                    // If card isn't active
                    if(!cardIndex[cIndex].isActive) {
                        icon = new ImageIcon(cardIndex[cIndex].path);
                        cardIndex[cIndex].isActive = true;
                        buttons[vertical][horizontal].setIcon(icon);
                        cards.remove(index);
                        cards.add(buttons[vertical][horizontal],index);
                    }

                    // Calculating how many cards are actively opened
                    counter = 0;
                    for(int i = 0 ; i < cardIndex.length; i++) {
                        if(cardIndex[i].isActive && !cardIndex[i].path.equals("images/dog.jpg")) {
                            counter++;
                        }
                    }

                    // If two cards are opened
                    final int[] indexRemoved = {0};
                    if(counter%2 == 0) {

                        // If the two cards are the same
                        if(CardProperties.findIfMatched(cardIndex[cIndex],cardIndex)) {
                            cardIndex[CardProperties.findIfMatchedValue(cardIndex[cIndex],cardIndex)].isMatched = true;
                            cardIndex[cIndex].isMatched = true;

                            // Displaying points and guesses
                            points++;
                            pointDisplay.setText("<html>" + "Guesses:" +  String.valueOf(guesses) +"<br>" +"Points: "+ String.valueOf(points) +"</html>");
                            frame.add(pointDisplay, BorderLayout.EAST);
                            //frame.revalidate();
                        }

                        // If the two cards are different
                        else {
                            // Displaying points and guesses
                            guesses++;
                            pointDisplay.setText("<html>" + "Guesses:" + String.valueOf(guesses) + "<br>" + "Points: " + String.valueOf(points) + "</html>");
                            frame.add(pointDisplay, BorderLayout.EAST);
                            //frame.revalidate();

                            // Reset all currently active buttons
                            for (int i = 0; i < xBound; i++) {
                                for (int j = 0; j < yBound; j++) {
                                    if (!cardIndex[indexRemoved[0]].isMatched && !cardIndex[indexRemoved[0]].path.equals("images/dog.jpg")) {
                                        icon = new ImageIcon("images/blackSquare.jpg");
                                       if (indexRemoved[0] == cardIndex[cIndex].index && cardIndex[cIndex].isActive) {
                                            icon = new ImageIcon(cardIndex[cIndex].path);
                                       } else {
                                           cardIndex[indexRemoved[0]].isActive = false;
                                       }
                                        buttons[i][j].setIcon(icon);
                                        cards.remove(indexRemoved[0]);
                                        cards.add(buttons[i][j], indexRemoved[0]);

                                    }
                                    indexRemoved[0]++;
                                }
                            }

                            // Timer to flip over cards
                            ActionListener action = new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    indexRemoved[0] = 0;
                                    for(int i = 0; i < xBound; i++) {
                                        for(int j = 0; j < yBound; j++) {
                                            ImageIcon icon = new ImageIcon("images/blackSquare.jpg");
                                            if(cardIndex[indexRemoved[0]].isActive && !cardIndex[indexRemoved[0]].isMatched && !cardIndex[indexRemoved[0]].path.equals("images/dog.jpg")) {
                                                cardIndex[indexRemoved[0]].isActive = false;
                                                buttons[i][j].setIcon(icon);
                                                cards.remove(indexRemoved[0]);
                                                cards.add(buttons[i][j],indexRemoved[0]);
                                            }
                                            indexRemoved[0]++;
                                        }
                                    }
                                }
                            };

                            // Starting timer
                            Timer resetFlippedFirst = new Timer(300,action);
                            resetFlippedFirst.setRepeats(false);
                            resetFlippedFirst.start();

                        }

                    }
                }

            }
        });
    }

    // Start up JFrame
    public static void frameStart(JFrame frame, JLabel time) {
        points = 0;
        guesses = 0;
        counter = 0;

        // JFrame setup
        time.setText("Time: 0");
        frame.add(time, BorderLayout.NORTH);
        JButton reset = new JButton("Reset");
        frame.setSize(800,600);
       // time.setText("Time: 0");

        // Guesses and misses
        JLabel pointDisplay = new JLabel();
        pointDisplay.setText("<html>Guesses: 0 <br> Points: 0 </html>");
        frame.add(pointDisplay, BorderLayout.EAST);
        frame.add(createButtons(frame,pointDisplay), BorderLayout.CENTER);
        frame.add(reset, BorderLayout.SOUTH);
        frame.setVisible(true);

        ActionListener incrementTime =  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time.setText("Time: " + String.valueOf(timer[0]));
                frame.add(time,BorderLayout.NORTH);
                timer[0]++;

                if(points == 12) {
                    frame.getContentPane().removeAll();
                    frame.repaint();
                    timer[0] = 0;
                    frameStart(frame,time);
                }

            }
        };

        Timer timeCounter = new Timer(1000,incrementTime);
        timeCounter.start();

        // Reset button logic
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.repaint();
                timer[0] = 0;
                frameStart(frame,time);
            }
        });

    }

    public static void main(String[] args) {
        // Creating JFrame and starting rendering
        JFrame frame = new JFrame();

        // Timer
        JLabel time = new JLabel();

        frameStart(frame,time);

    }

}
