/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

import csulb.cecs323.model.*;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.stream.IntStream;

import static org.eclipse.persistence.sessions.server.ConnectionPolicy.ExclusiveMode.Transactional;

/**
 * This program intends to create a database that keeps track of the NHL for sports journalist.
 * This program was written by Clayton, Mariana and Jorge.
 * This program may not work and may not look elegant but there are countless hours, crying session, and lots of love
 * poured into it.
 */
public class Homework4Application {
   private EntityManager entityManager;

   private static final Logger LOGGER = Logger.getLogger(Homework4Application.class.getName());

    private static final Scanner KEYBOARD_INPUT = new Scanner(System.in);

   public Homework4Application(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {

      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("homework4_PU");
      EntityManager manager = factory.createEntityManager();
      Homework4Application hw4application = new Homework4Application(manager);


      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();

      IntStream.range(0, INITIAL_GAMES.length).forEach(i -> {
         Game game = INITIAL_GAMES[i];
         game.setHomeTeam(INITIAL_TEAMS[HOME_TEAMS[i]]);
         INITIAL_TEAMS[HOME_TEAMS[i]].addHomeGame(game);
         game.setAwayTeam(INITIAL_TEAMS[AWAY_TEAMS[i]]);
         INITIAL_TEAMS[AWAY_TEAMS[i]].addAwayGame(game);
         game.setStadium(INITIAL_STADIUM[HOME_STADIUM[i]]);
         INITIAL_STADIUM[HOME_STADIUM[i]].addSingleGame(game);
      });

       IntStream.range(0, INITIAL_TEAMPLAYERS.length).forEach(i -> {
           TeamPlayers teamPlayers = INITIAL_TEAMPLAYERS[i];
           teamPlayers.setTeam(INITIAL_TEAMS[TEAMS[i]]);
           INITIAL_TEAMS[TEAMS[i]].addTeamPlayer(teamPlayers);
           teamPlayers.setPlayer(INITIAL_PLAYERS[PLAYERS[i]]);
           INITIAL_PLAYERS[PLAYERS[i]].addTeam(teamPlayers);
       });

       IntStream.range(0, INITIAL_SPONSORAMOUNT.length).forEach(i -> {
           SponsorAmount sponsorAmounts = INITIAL_SPONSORAMOUNT[i];
           sponsorAmounts.setTeam(INITIAL_TEAMS[TEAM_AMOUNT[i]]);
           INITIAL_TEAMS[TEAM_AMOUNT[i]].addSponsorAmount(sponsorAmounts);
           sponsorAmounts.setSponsor(INITIAL_SPONSORS[SPONSOR_AMOUNT[i]]);
           INITIAL_SPONSORS[SPONSOR_AMOUNT[i]].setPayments(sponsorAmounts);
       });

       IntStream.range(0, INITIAL_GAMESTAT.length).forEach(i -> {
           GameStat gameStat = INITIAL_GAMESTAT[i];
           gameStat.setPlayer(INITIAL_PLAYERS[PLAYERS_PLAY[i]]);
           INITIAL_PLAYERS[PLAYERS_PLAY[i]].addGame(gameStat);
           gameStat.setGame(INITIAL_GAMES[GAMES_PLAYED[i]]);
           INITIAL_GAMES[GAMES_PLAYED[i]].addGameStat(gameStat);
       });


      for(Team team: INITIAL_TEAMS)
        manager.persist(team);

      for(Player player: INITIAL_PLAYERS)
        manager.persist(player);

      for(Stadium stadium: INITIAL_STADIUM)
        manager.persist(stadium);

      for(Sponsor sponsor: INITIAL_SPONSORS)
        manager.persist(sponsor);

      tx.commit();


      LOGGER.fine("End of Transaction");

       printMessage();
       displayMenu();
       int menuChoice = getChoice(6);

       manager.close();
       EntityManagerFactory factory2 = Persistence.createEntityManagerFactory("homework4_PU");
       EntityManager manager2 = factory2.createEntityManager();
       hw4application = new Homework4Application(manager2);
       //tx = manager2.getTransaction();  //working
       //tx.begin();  //working

       while (menuChoice != 6) {
           tx = manager2.getTransaction();
           tx.begin();
           if (menuChoice == 1) {
               Query queryOne = hw4application.entityManager.createQuery(
                       "SELECT p.firstName AS player_first_name, p.lastName AS player_last_name,t.name AS team, SUM(gs.goals) AS total_goals\n" +
                               "FROM Teams t INNER JOIN TeamPlayers tp ON t.teamID = tp.team.teamID\n" +
                               "             INNER JOIN Players p ON tp.player.playerID = p.playerID\n" +
                               "             INNER JOIN GameStats gs ON p.playerID = gs.player.playerID\n" +
                               "GROUP BY p.playerID, p.firstName, p.lastName, t.name\n" +
                               "HAVING SUM(gs.goals) >= 100");
               List list1 = queryOne.getResultList();
               // DO OTHER STUFF
           } else if (menuChoice == 2) {
               Query queryTwo = hw4application.entityManager.createQuery(
                       "SELECT t.teamID, t.name \n" +
                               "FROM Teams t LEFT OUTER JOIN Games g ON t.teamID = g.homeTeam.teamID\n" +
                               "WHERE t.teamID NOT IN(SELECT t2.teamID\n" +
                               "                       FROM Teams t2 LEFT OUTER JOIN Games g2 ON t2.teamID = g2.homeTeam.teamID\n" +
                               "                       WHERE g2.homeScore > g2.awayScore)");
               List list2 = queryTwo.getResultList();
               // DO OTHER STUFF
           } else if (menuChoice == 3) {
               Query queryThree = hw4application.entityManager.createQuery(
                       "SELECT t.name AS Team_Name, COUNT(ga.gameID)\n" +
                               "FROM Teams t LEFT OUTER JOIN Games ga ON t.teamID = ga.awayTeam.teamID\n" +
                               "            LEFT OUTER JOIN Games gh ON t.teamID = gh.homeTeam.teamID\n" +
                               "WHERE ga.season = (SELECT MAX(season) FROM Games)\n" +
                               "GROUP BY t.teamID, t.name");
               List list3 = queryThree.getResultList();
               // DO OTHER STUFF
           } else if (menuChoice == 4) {
               displayEntityMenu();
               int insertChoice = getChoice(5);
               if (insertChoice == 1) { // Player chosen
                   hw4application.createPlayerEntity();
                   System.out.println("insert player");
               } else if (insertChoice == 2) { // Team chosen
                   hw4application.createTeamEntity();
                   System.out.println("insert team");
               } else if (insertChoice == 3) { // Stadium chosen
                   hw4application.createStadiumEntity();
                   System.out.println("insert stadium");
               } else if (insertChoice == 4){ // Sponsor chosen
                   hw4application.createSponsorEntity();
                   System.out.println("insert sponsor");
               } else {  // Game chosen
                   hw4application.createGameEntity();
                   System.out.println("insert game");
               }
           } else if (menuChoice == 5) {
               displayEntityMenu();
               int removeChoice  = getChoice(5);
               if (removeChoice == 1) { // Player chosen
                   System.out.println("Enter the first  name of the player you would like to remove.");
                   String temp1 = KEYBOARD_INPUT.nextLine();
                   System.out.println("last name");
                   String temp2 = KEYBOARD_INPUT.nextLine();
                   int[] dateComponents = getDateComponents("Enter the birth-date: yyyy-mm-dd: ");
                   manager.remove(new Player(temp1, temp2,new GregorianCalendar(dateComponents[0], dateComponents[1], dateComponents[2])));
                   System.out.println("Remove player");
               } else if (removeChoice == 2) { // Team chosen
                   System.out.println("Remove team");
               } else if (removeChoice == 3) { // Stadium chosen
                   System.out.println("Remove stadium");
               } else if (removeChoice == 4){ // Sponsor chosen
                   System.out.println("Remove sponsor");
               } else { // Game Chosen
                   System.out.println("Remove game");
               }
           }

           tx.commit();

           displayMenu();
           menuChoice = getChoice(6);


       }
       KEYBOARD_INPUT.close();
      // tx.commit();  //working
   }


    /**
     *  Prints the opening basis instructions to the user.
     */
    public static void printMessage() {
        try {
            File file = new File("src/Instructions.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException f) {
            System.out.println(f.getMessage());
        }
    }

    /**
     * Displays the 6 choices the user will be able to do
     * in this application.
     */
    public static void displayMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("  1) Query #1: Players who have over 100 goals in their career and what team they play for.");
        System.out.println("  2) Query #2: Teams that have not won a home game.");
        System.out.println("  3) Query #3: Amount of games each team has had in a certain season.");
        System.out.println("  4) Insert a player, team, stadium, sponsor, or game.");
        System.out.println("  5) Remove a player, team, stadium, sponsor, or game.");
        System.out.println("  6) Quit");
    }

    /**
     * Displays the 5 entities the user will be able to insert
     * ot remove.
     */
    public static void displayEntityMenu() {
        System.out.println("\nWhich entity do you choose?");
        System.out.println("  1) Player");
        System.out.println("  2) Team");
        System.out.println("  3) Stadium");
        System.out.println("  4) Sponsor");
        System.out.println("  5) Game");
    }

    /**
     * Asks the user to enter data for the player entity.
     */
    public void createPlayerEntity() {
        Player player = new Player();

        System.out.print("Enter the player's first name: ");
        player.setFirstName(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the player's last name: ");
        player.setLastName(KEYBOARD_INPUT.nextLine().trim());
        System.out.println("Enter the player's position");
        player.setPosition(playerPosition());

        String height = "";
        while(!height.matches("[0-9]+")) {
            System.out.print("Enter the player's height in inches: ");
            height = KEYBOARD_INPUT.nextLine().trim();
        }
        player.setHeight(Integer.parseInt(height));

        String weight = "";
        while (!weight.matches("[0-9]+")) {
            System.out.print("Enter the player's wight in pounds: ");
            weight = KEYBOARD_INPUT.nextLine().trim();
        }
        player.setWeight(Integer.parseInt(weight));

        String salary = "";
        while (!salary.matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")) {
            System.out.print("Enter the player's annual salary: ");
            salary = KEYBOARD_INPUT.nextLine().trim();
        }
        player.setSalary(Double.parseDouble(salary));

        int[] dateComponents = getDateComponents("Enter the birth-date of the player in the form of yyyy-mm-dd: ");
        player.setBirthDate(new GregorianCalendar(dateComponents[0], dateComponents[1], dateComponents[2]));

        this.entityManager.persist(player);
    }

    /**
     * Asks the user to enter a character to represent a player's position.
     * @return a position from the enumerated class Position
     */
    private static Position playerPosition() {
        boolean isPositionValid;
        Position position = null;
        do {
            isPositionValid = true;
            System.out.print("Enter G for Goaltender, D for Defenseman, C for Center, " +
                    "L for Left_Wingman, or R for Right_Wingman: ");
            char character = KEYBOARD_INPUT.next().toUpperCase().charAt(0);
            if (character == 'G')
                position = Position.Goaltender;
            else if (character == 'D')
                position = Position.Defenseman;
            else if (character == 'C')
                position = Position.Center;
            else if (character == 'L')
                position = Position.Left_Wingman;
            else if (character == 'R')
                position = Position.Right_Wingman;
            else
                isPositionValid = false;
        } while (!isPositionValid);
        return position;
    }

    /**
     * Asks the user to enter data for a team entity.
     */
    public void createTeamEntity() {
        Team team = new Team();

        System.out.print("Enter the team's name: ");
        team.setName(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the team's home city: ");
        team.setCity(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the team's home state: ");
        team.setState(KEYBOARD_INPUT.nextLine().trim());
        System.out.println("Enter the team's conference");
        team.setConference(teamConference());

        this.entityManager.persist(team);
    }

    /**
     * Asks the user to enter a character to represent the conference a team
     * participates in.
     * @return the conference a players participates in.
     */
    private static Conference teamConference() {
        boolean isConferenceValid;
        Conference conference = null;
        do {
            isConferenceValid = true;
            System.out.print("Enter E for EASTERN, W for WESTERN: ");
            char character = KEYBOARD_INPUT.next().toUpperCase().charAt(0);
            if (character == 'E')
                conference = Conference.EASTERN;
            else if (character == 'W')
                conference = Conference.WESTERN;
            else
                isConferenceValid = false;
        } while (!isConferenceValid);
        return conference;
    }

    /**
     * Asks the user to enter data for a stadium entity.
     */
    public void createStadiumEntity() {
        Stadium stadium = new Stadium();

        System.out.print("Enter the stadium's name: ");
        stadium.setName(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the stadium's home city: ");
        stadium.setCity(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the stadium's home state: ");
        stadium.setState(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the stadium's number of seats: ");

        String strSeats = "";
        while(!strSeats.matches("[0-9]+")) {
            System.out.print("Enter the stadium's number of seats:: ");
            strSeats = KEYBOARD_INPUT.nextLine().trim();
        }
        stadium.setNumSeats(Integer.parseInt(strSeats));

        this.entityManager.persist(stadium);
    }

    /**
     * Asks the user to enter data for a sponsor entity.
     */

    public void createSponsorEntity() {
        Sponsor sponsor = new Sponsor();

        System.out.print("Enter the sponsor's name: ");
        sponsor.setName(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter a description about the sponsor: ");
        sponsor.setDescription(KEYBOARD_INPUT.nextLine().trim());
        System.out.print("Enter the sponsor's website: ");
        sponsor.setWebsite(KEYBOARD_INPUT.nextLine().trim());

        this.entityManager.persist(sponsor);
    }

    /**
     * Asks the user to enter data for a game entity.
     */
    public void createGameEntity() {
        Game game = new Game();

        String strSeason = "";
        while (!strSeason.matches("[0-9]+")) {
            System.out.print("Enter the game's season: ");
            strSeason = KEYBOARD_INPUT.nextLine().trim();
        }
        game.setSeason(Integer.parseInt(strSeason));

        int[] dateComponents = getDateComponents("Enter the date of when the game was played in the form of yyyy-mm-dd: ");
        game.setDate(new GregorianCalendar(dateComponents[0], dateComponents[1], dateComponents[2]));

        System.out.println("Enter the type of game");
        game.setGameType(gameType());

        String strHomeScore = "";
        while (!strHomeScore.matches("[0-9]+")) {
            System.out.print("Enter the home team's final score: ");
            strHomeScore = KEYBOARD_INPUT.nextLine().trim();
        }
        game.setHomeScore(Integer.parseInt(strHomeScore));

        String strAwayScore = "";
        while (!strAwayScore.matches("[0-9]+")) {
            System.out.print("Enter the away team's final score: ");
            strAwayScore = KEYBOARD_INPUT.nextLine().trim();
        }
        game.setAwayScore(Integer.parseInt(strAwayScore));

        String strTotalGameTime = "";
        while (!strTotalGameTime.matches("[0-9]+")) {
            System.out.print("Enter the total game time in minutes: ");
            strTotalGameTime = KEYBOARD_INPUT.nextLine().trim();
        }
        game.setTotalGameTime(Integer.parseInt(strTotalGameTime));

        this.entityManager.persist(game);
    }

    /**
     * Asks the user to enter a character to state what kind of game is played
     * @return a GameType
     */
    private static GameType gameType() {
        boolean isGameTypeValid;
        GameType type = null;
        do {
            isGameTypeValid = true;
            System.out.print("Enter C for Conference, P for Preseason, O for PlayOffs, or S for th Stanley Cup: ");
            char character = KEYBOARD_INPUT.next().toUpperCase().charAt(0);
            if (character == 'C')
                type = GameType.CONFERENCE;
            else if (character == 'P')
                type = GameType.PRESEASON;
            else if (character == 'O')
                type = GameType.PLAYOFF;
            else if (character == 'S')
                type = GameType.STANELY_CUP;
            else
                isGameTypeValid = false;
        } while (!isGameTypeValid);
        return type;
    }

    /**
     * Lets the user enter a number between the values of 1 and n.
     * If any other value/character not in this interval, the user
     * will be asked to enter the correct value.
     * @param n the ending interval
     * @return the choice the user made
     */
    public static int getChoice(int n){
        int val = 0;
        while (val < 1 || val > n) {
            System.out.printf("Enter a choice between [1 & %d]: ", n);
            String strValue = KEYBOARD_INPUT.nextLine().trim();
            while(!strValue.matches("[0-9]+")) {
                System.out.printf("Enter a choice between [1 & %d]: ", n);
                strValue = KEYBOARD_INPUT.nextLine().trim();
            }
            val = Integer.parseInt(strValue);
        }
        return val;
    }

    /**
     * Determines if the user entered a valid date
     * @param strDate a string representing a date in the form year-month-day
     * @return true if string is a valid date; otherwise false
     */
    public static boolean isValidDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false);
        try {
            formatter.parse(strDate);
        } catch (ParseException p) {
            return false;
        }
        return true;
    }

    /**
     * Here, the user will be asked to enter a proper date which will then
     * be separated into its year, month, and day components and stored into
     * an array for further use.
     * @param message message to display to the user
     * @return int array of the dates 3 components
     */
    public static int[] getDateComponents(String message) {
        String strDate = "";
        while (!isValidDate(strDate)) {
            System.out.print(message);
            strDate = KEYBOARD_INPUT.nextLine().trim();
        }
        StringTokenizer tokenizer = new StringTokenizer(strDate, "-");
        int[] items = new int[tokenizer.countTokens()];
        for (int i = 0; i < items.length; i++) {
            items[i] = Integer.parseInt(tokenizer.nextToken());
        }
        return items;
    }
    /**
     * Here we are creating the initial teams for the database
     */
    private static final Team[] INITIAL_TEAMS = new Team[] {
            new Team("Los Angeles Kings", "Los Angeles", "California", Conference.WESTERN),
            new Team("San Jose Sharks", "San Jose", "California", Conference.WESTERN),
            new Team("Boston Bruins", "Boston", "Massachusetts", Conference.EASTERN),
            new Team("Ottawa Senators", "Ottawa", "Ontario", Conference.EASTERN)
    };

    /**
     * Creating the initial players for the database
     */
    private static final Player[] INITIAL_PLAYERS = new Player[] {
            new Player("Michael", "Amadio", Position.Center, 73, 204, 742500, new GregorianCalendar(1996, 5, 13)), // Kings
            new Player("Logan", "Couture", Position.Center, 73, 200, 6000000, new GregorianCalendar(1989, 3, 28)), // Sharks
            new Player("Kevin", "Labanc", Position.Right_Wingman, 71, 185, 742500, new GregorianCalendar(1995, 12, 12)), // Sharks
            new Player("Brad", "Marchand", Position.Left_Wingman, 69, 181, 8000000, new GregorianCalendar(1988, 5, 11)), // Bruins
            new Player("Mike","McKenna",Position.Goaltender, 74, 185, 595000, new GregorianCalendar(1983,4,11))
    };

    /**
     * Creating the initial stadiums
     */
    private static final Stadium[] INITIAL_STADIUM = new Stadium[] {
            new Stadium("Staples Center", "Los Angeles", "California", 18340, INITIAL_TEAMS[0]), // Kings
            new Stadium("SAP Center", "San Jose", "California", 17562, INITIAL_TEAMS[1]), // Sharks
            new Stadium("TD Garden", "Boston", "Massachusetts", 19600,INITIAL_TEAMS[2]), // Bruins
            new Stadium("Canadian Tire Centre", "Ottawa", "Ontaria", 18652, INITIAL_TEAMS[3])//Senators
    };

    /**
     * creating the initial sponosors
     */
    private static final Sponsor[] INITIAL_SPONSORS = new Sponsor[] {
            new Sponsor("JMG Security Systems", "Top Security", "JMG.com"),
            new Sponsor("Coca-Cola", "Beverage", "https://us.coca-cola.com/"),
    };

    /**
     * this creates the initial games
     */
    private static final Game[] INITIAL_GAMES = new Game[]{
            new Game(101,new GregorianCalendar(2018,10,5), GameType.CONFERENCE, 2,
                    3, 6253) , //sharks away, kings home
            new Game(98,new GregorianCalendar(2016,4,18), GameType.PLAYOFF, 1,
                    2, 6347) , //kings away, sharks home
            new Game(101,new GregorianCalendar(2018,12,9), GameType.CONFERENCE, 1,
                    2, 6307) // boston away, ottawa home
    };

    /**
     * the two methods below will create arrays that will help correspond to what teams are
     * the home and away teams at each game
     */
    private static final int[] HOME_TEAMS = new int[]{ 0, 1, 3};

    private static final int[] AWAY_TEAMS = new int[]{ 1, 0, 2 };

    private static final int[] HOME_STADIUM = new int[]{0, 1, 3};

    /**Team Player initialization*/
    private static final TeamPlayers[] INITIAL_TEAMPLAYERS = new TeamPlayers[]{
            new TeamPlayers(101),
            new TeamPlayers(101),
            new TeamPlayers(101),
            new TeamPlayers(101),
            new TeamPlayers(101),

    };

    /**Teams*/
    private static final int[] TEAMS = new int[]{0,1,1,2,3};
    /**Players*/
    private static final int[] PLAYERS = new int[]{0,1,2,3,1};

    /**Sponsor Amount Initializer*/
    private static final SponsorAmount[] INITIAL_SPONSORAMOUNT = new SponsorAmount[]{
            new SponsorAmount(2000000),
            new SponsorAmount(5009000),
            new SponsorAmount(73819000)
    };
    private static final int[] TEAM_AMOUNT = new int[]{0, 2 ,3};
    private static final int[] SPONSOR_AMOUNT = new int[]{0, 1, 0};

    /**
     * GAME STAT INITIALIZATION
     */
    public static final GameStat[] INITIAL_GAMESTAT = new GameStat[]{
            new GameStat(0, 1, 1218, 0, 1), //leblanc game 1
            new GameStat(0, 0, 2032, 0, 2), // couture game 1
            new GameStat(0, 0, 2122, 0, 2), // couture game 2
            new GameStat(0, 0, 716, 0, 0), //game 1 amadio
            new GameStat(0, 1,2339,2,9), // game 3 marchand
            new GameStat(42,0,6307,0,0)// 3 mckenna
    };

    /**
     * Player and game initialization for gamestat
     */
    private static final int[] PLAYERS_PLAY = new int[]{2,1,1,0,3,4};
    private static final int[] GAMES_PLAYED = new int[]{0,0,1,0,2,2};

}



