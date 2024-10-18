import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.Timer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * DataAnalyzerGUI class that reads data regarding presidential candidates, and displays and analyzes
 * the data in a graphical interface with 6 buttons. Utilizes GUI components and JavaFX for media playback
 *
 * @author (124116)
 * @version (5/21/2024)
 */
public class DataAnalyzerGUI extends JFrame implements ActionListener
{
    private HashMap<String, Nominee> nomineeMap;
    private ArrayList<String> counties;
    private Canvas canvas;
    private Random rand;

    private JButton[] buttons;
    private JTextArea textArea;//textArea
    private JPanel panel1, panel2;
    private JScrollPane scroll;

    private int currentIndex;

    /**
     * Constructor for objects of class GUI
     */
    public DataAnalyzerGUI()
    {
        nomineeMap = new HashMap<String, Nominee>();
        counties = new ArrayList<>();
        Random rand = new Random();
        
        setSize(800,700);
        setResizable(false);
        setTitle("Statistics of U.S. Presidential Election in Illinois");
        
        buttons = new JButton[6];
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(600,400));
    
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(200,800));
        add(scroll);

        panel2 = new JPanel(new GridLayout(3,2,10,10));
        String[] labels = {"Load Data", "Display Data", "Analyze Data", "Graph Data", "Slideshow", "WoW"};
        textArea.setBackground(Color.white);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        textArea.setText("Click the buttons below!");
        
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(labels[i]);
            buttons[i].addActionListener(this);
            buttons[i].setPreferredSize(new Dimension(150,120));
            buttons[i].setFont(new Font("Times New Roman", Font.BOLD, 20)); 
            buttons[i].setBackground(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))); 
            buttons[i].setOpaque(true);
            panel2.add(buttons[i]);
        }

        add(panel2,BorderLayout.SOUTH);
        setVisible(true);

    } 

    
    /**
     * Responds to action events triggered by button presses in the GUI.
     * It routes to different methods based on the command associated with the action event, using switch case.
     *
     * @param ActionEvent e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String event = e.getActionCommand();
        switch(event)
        {
            case "Load Data":
                loadData("voteData.txt");
                break;
            case "Display Data":
                displayData();
                break;
            case "Analyze Data":
                analyzeData();
                break;
            case "Graph Data":
                graphData();
                break;
            case "Slideshow":
                startSlideshow();
                break;
            case "WoW":
                video("trump.mp4");
                break;
        }
    }

    /**
     * Loads data from file into HashMap
     * 
     * @param file 
     * @return void - none
     */
    private void loadData(String file)
    {
        try{
            Scanner scan = new Scanner(new File(file));
            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                String[] parts = line.split(": ");
                String name = parts[0];
                String[] counties = parts[1].split(", ");

                Nominee n = new Nominee(name);
                for (String s: counties)
                {
                    String[] pairings = s.split(" ");
                    n.mapCountyVotes(pairings[0] + " " + pairings[1], Integer.parseInt(pairings[2]));
                }
                nomineeMap.put(name, n);    
            }
            scan.close();
        } catch(Exception e){}
        textArea.setText("Data loaded in the map.");
    }

    /**
     * Analyzes loaded data to compute total votes and determine the winner.
     * The method calculates total votes, percentage of votes for each nominee, and identifies the nominee with the maximum votes.
     * 
     * @param none
     * @return none
     */
    private void analyzeData()
    {
        if (nomineeMap.isEmpty())
        {
            textArea.setText("Data not loaded in the map; cannot display.");
        }

        else{
            textArea.setText(" ");
            int totalVotes = 0;
            int maxVotes = Integer.MIN_VALUE;
            String winner = "";

            for (Nominee n: nomineeMap.values()){
                int nomineeVotes = n.getTotalVotes();
                totalVotes += nomineeVotes;
                if(nomineeVotes> maxVotes){
                    maxVotes = nomineeVotes;
                    winner = n.getName();
                }
            }

            textArea.append("Analysis Report: \n");
            textArea.append("Total Votes: " + totalVotes + "\n\n");

            for(Nominee n: nomineeMap.values()){
                int tVotes = n.getTotalVotes();
                double percentage = (double) tVotes/totalVotes * 100;
                textArea.append("Nominee name: " + n.getName() + "\n");
                textArea.append("Total votes: " + tVotes + "\n");
                textArea.append("Total Votes Percentage: " + String.format("%.2f%%", percentage) + "\n\n");
            }

            textArea.append("Winner of the election is: " + winner);
        }
    }

    /**
     * Displays the loaded data on the GUI.
     * It checks if data is loaded and displays each nominee's data in the text area.
     * 
     * @param none
     * @return none
     */
    private void displayData()
    {
        if (nomineeMap.isEmpty())
        {
            textArea.setText("Data not loaded in the map; cannot display.");
        }

        else{
            textArea.setText("");

            for (Nominee n: nomineeMap.values())
            {
                textArea.append(n+"\n");  
            }
        }
    }

    /**
     * Visualizes the data as a graph.
     * This method plots a basic graph showing votes of each nominee if data is loaded.
     * 
     * @param none
     * @return none
     */
    private void graphData()
    {
        if (nomineeMap.isEmpty())
        {
            textArea.setText("Data not loaded in the map; cannot display.");
        }

        else{
            Canvas canvas = new Canvas("Voter Data Graph", 800, 600);
            canvas.setVisible(true);
            canvas.drawLine(100,100,100,500);//y-axis
            canvas.drawLine(100,500,700,500);//x-axis
            canvas.drawString("Votes(in 10,000s) Of Each Presidential Candidate", 240, 50);
            canvas.drawString("Votes", 20, 300);
            canvas.drawString("Candidates", 375, 580);
            
            for(int i = 0; i<=8; i++)
            {
                canvas.drawLine(100, 500 - (i * 50), 95, 500 - (i *50));
                canvas.drawString(Integer.toString(i*50), 60, 500 - (i * 50));
            }
            
            int x = 200;
            int y = 500;
            int width = 50;
            int height = 50;
            for(Nominee n: nomineeMap.values())
            {
                canvas.drawString(n.getName(),x,y+50);
                canvas.fillRectangle(x,y-n.getTotalVotes()/10000,width, n.getTotalVotes()/10000);
                x += width+125; 
            }
        }

    }

    /**
     * Starts a slideshow of images in a dedicated canvas.
     * The slideshow displays each image for a set interval and cycles through a predefined list of images.
     * @param none
     * @return none
     */
    public void startSlideshow() {
        String[] files = {"img1.png", "img2.png", "img3.png", "img4.png", "img5.png"};
        currentIndex = 0;

        Canvas slideShow = new Canvas("Slideshow", 800, 600, Color.white);
        Timer timer = new Timer(1000, new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        if (currentIndex < files.length) {
                            try 
                            {
                                BufferedImage image = ImageIO.read(new File(files[currentIndex]));
                                Image newImage = image.getScaledInstance(800, 600, Image.SCALE_DEFAULT);
                                slideShow.erase();
                                slideShow.drawImage(image, 0, 0);
                                currentIndex++;
                            } catch (Exception ex) {

                            }
                        } 
                        else 
                        {
                            ((Timer) e.getSource()).stop();
                            textArea.setText("Slideshow has ended.");
                            slideShow.setVisible(false);
                        }
                    }
                });
        timer.start();
    }

    
    /**
     * Plays a video file in a dedicated window using JavaFX's media capabilities. The method sets up a JFrame containing a JFXPanel,
     * initializes a MediaPlayer with the provided video file, and plays the video. The window automatically closes once the video playback completes.
     *
     * @param mp4
     * @return void none
     */
    private void video(String mp4)
    {   
        JFrame video= new JFrame("Trump Video");
        video.setSize(800, 600); 
        video.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        video.setLocationRelativeTo(null); 
    
        final JFXPanel fxPanel = new JFXPanel();
        video.add(fxPanel);
        video.setVisible(true);
        
        Platform.runLater(() -> {
            Media media = new Media(new File(mp4).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
    
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play(); 
            });
    
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop(); 
                video.dispose(); 
            });
    
            Scene scene = new Scene(new javafx.scene.Group(mediaView));
            fxPanel.setScene(scene);
        });
    }
           
    
    private static void main(String[] args)
    {
        new DataAnalyzerGUI();
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */

}