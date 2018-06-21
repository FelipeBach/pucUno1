import java.util.Scanner;
import java.io.*;

public class Uno implements Serializable
{
	private Deck deck; // o deck do jogo
	private Deck cardpile; //quando os jogadores jogam uma carta, ela é empilhada aqui, também, cria um novo deck quando o deck atual estiver vazio
	private int penalty; // quando cartas especiais são acumuladas  a penalidade acumula, se um jogador não tiver como counterar a carta especial atual, o jogador é penalizado
	private Player[] player; //player 1 and 2
    private boolean sentido;
    private int numPlayers;
    private int currentPlayer;

    /**
     * cria um jogo Uno, com o número específico de jogadores
     * cria um baralho de 108 cartas, embaralha-o e distribui as cartas
     * @param numJogadores o número de jogadores
     */
	public Uno(int numJogadores)
    {
        deck = new Deck();
        deck.inicializa();

        penalty = 0;
        cardpile = new Deck();
        cardpile.addToDeck(getStartingCard());

        player = new Player[numJogadores];
        for (int i = 0; i < numJogadores; i++)
        {
            player[i] = new Player("Jogador " + (i+1));
        }

        sentido = true;
        numPlayers = numJogadores;
        currentPlayer = 0;
        distributecards();
	}

    /**
     * cria um jogo Uno do arquivo-texto
     * @param filename o nome do arqiuivo
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
	public Uno (String filename) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        recuperateOlderGame(filename);
    }

    /**
     * recupera as informações de um jogo antigo do arquivo texto
     * @param namefile nome do aquivo texto
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void recuperateOlderGame(String namefile) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fi = new FileInputStream(new File(namefile));
        ObjectInputStream oi = new ObjectInputStream(fi);

        deck = (Deck) oi.readObject();
        penalty = oi.readInt();
        cardpile = (Deck) oi.readObject();
        sentido = oi.readBoolean();
        numPlayers = oi.readInt();
        currentPlayer = oi.readInt();
        player = (Player[]) oi.readObject();

        oi.close();
        fi.close();
    }

    /**
     * salva o jogo no arquivo
     * @param namefile o nome do arquivo
     * @return true se for possível salvar, false caso contrário
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private boolean savegame(String namefile) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileOutputStream f = new FileOutputStream(new File(namefile));
        ObjectOutputStream o = new ObjectOutputStream(f);

        // Write objects to file
        o.writeObject(deck);
        o.writeInt(penalty);
        o.writeObject(cardpile);
        o.writeBoolean(sentido);
        o.writeInt(numPlayers);
        o.writeInt(currentPlayer);
        o.writeObject(player);

        o.close();
        f.close();

        return true;
    }

    /**
     * exibe menu inicial, com opções de jogo, lendo a opção escolhida
     * @return zero caso jogador queira pegar um jogo antigo, ou o número de jogadores, caso queira criar um jogo novo
     * @throws InterruptedException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
	public static int menuInicial () throws InterruptedException, FileNotFoundException, IOException, ClassNotFoundException
    {
        Scanner in = new Scanner(System.in);
        int numJogadores, opcao;

        cls();
        decorate();
        System.out.println("\n\t\t\t\tUNO\n");
        decorate();
        System.out.println("\n\n\n");

        System.out.println("Menu\n\t1: novo jogo\n\t2: carregar jogo antigo\n\nEscolha uma opção: ");
        opcao = in.nextInt();

        while (opcao != 1 && opcao != 2)
        {
            System.out.print("Opção inválida. Digite novamente: ");
            opcao = in.nextInt();
        }

        int length = findSavedGamesOnFolder().length;

        if (opcao == 2 && length > 0) return 0;
        else
        {
            if (opcao == 2 && length == 0) System.out.println("Não há jogos salvos.");
            System.out.println("Digite o número de jogadores para uma nova partida: ");
            numJogadores = in.nextInt();
            while (numJogadores < 2 || numJogadores >5)
            {
                System.out.println("Escolha inválida. Digite novamente: ");
                numJogadores = in.nextInt();
            }
            return numJogadores;
        }
    }

    /**
     * pergunta qual jogo salvo o jogador quer esolher
     * @return o nome do arquivo escolhido
     */
	public static String askFilename()
    {
        Scanner in = new Scanner (System.in);
        File[] fileList = findSavedGamesOnFolder();

        System.out.println("Jogos salvos: ");
        displaySavedGames(fileList);

        System.out.println("\n\nEscolha um jogo: ");
        int k = in.nextInt();

        while (k < 1 || k > fileList.length){
            System.out.println("Opção inválida. Tente novamente: ");
            k = in.nextInt();
        }

        return fileList[k-1].getName();
    }

    /**
     * exibe na tela uma lista de jogos salvos
     * @param fileList a lista de arquivos
     */
    private static void displaySavedGames(File[] fileList)
    {
        String s;

        for (int i = 1; i <= fileList.length; i++)
        {
            s = fileList[i-1].getName();
            System.out.println("\t" + i + ": " + s.substring(0, s.length() - 4));
        }
    }

    /**
     * procura na pasta atual arquivos com extensão .uno
     * @return a lista com arquivos
     */
	private static File[] findSavedGamesOnFolder()
    {
        String workingDirectory = System.getProperty("user.dir");

        File dir = new File(workingDirectory);
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.endsWith(".uno"); }
        });
    }

	/**
	 *  esse método simula turnos entre os jogadores.
     */
	public void game() throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException
    {
		while(!gameOver())
        {
            cls();
            System.out.println("Proximo jogador: \n\n\n\n\n\n" + player[currentPlayer].getName());
            pause();

            playGame();
            setNextPlayer();
		}
	}

    /**
     * distrubui as sete cartas entre os jogadores
     */
	private void distributecards()
    {
		for(int i=0; i<7 ;i++)
		{
		    for (int j=0; j < numPlayers; j++)
            {
                player[j].add(deck.getTopCard());
            }
		}
	}


    /**
     *  este metodo pega o jogador atual como parametro
     *  esse método contem o processo do jogo
     */
	public void playGame() throws IOException, InterruptedException, ClassNotFoundException
    {
        Scanner choice = new Scanner(System.in);
        Card current = cardpile.peek();
        Player p = player[currentPlayer];

        showBoard();
		int pickchoice;

		if(penalty > 0)
		{
		    System.out.println("Penalização: " + penalty);
		    enterParaContinuar();
		    compra(p, penalty);
		    penalty = 0;
		    showBoard();
		    enterParaContinuar();
		    return;
		}

        if (!p.hasCardToPutAbove(current))
            System.out.println("Você não possui nenhuma carta valida para jogar, então tera de comprar cartas.");
        System.out.println("Por favor, escolha uma carta (digite zero para comprar): ");

        pickchoice = choice.nextInt()-1;
        while(!isValidChoice(p,pickchoice))
        {
            System.out.println("Escolha invalida! Por favor, tente novamente: ");
            pickchoice = choice.nextInt()-1;
        }

        if (pickchoice == -1)
        {
            compra(p, 1);
            showBoard();
            enterParaContinuar();
        }
        else
        {
            Card play = p.remove(pickchoice);
            p.sayUno();
            cardpile.addToDeck(play);
            penalty += play.getPenalty();
            if (play.isCoringa())
            {
                System.out.println("Carta jogada é um curinga. Escolha uma cor (1 - red, 2 - green, 3 - blue, 4 - yellow): ");
                int cor = choice.nextInt();

                if (cor < 1 || cor > 4)
                {
                    System.out.println("Opcao invalida! Tente novamente.");
                    cor = choice.nextInt();
                }

                play.setCor(cor);
            }
            else if (play.isTrocaSentido()) trocaSentido();
            else if (play.isPula()) setNextPlayer();
        }
	}

	private void setNextPlayer()
    {
        if (sentido) currentPlayer = (currentPlayer + 1) % numPlayers;
        else
        {
            if (currentPlayer == 0) currentPlayer = numPlayers;
            currentPlayer--;
        }
    }

	private void trocaSentido()
    {
        sentido = !sentido;
    }

    /**
     * Verifica se a escolha do jogador foi valida ou não
     * para ser uma escolha valida: o jogador deve possuir a carta, a carta deve possuir o mesmo valor ou cor que a carta atual.
     */
	private boolean isValidChoice(Player p,int choice)
    {
        if (choice == -1) return true;
        if (choice < 0 || choice >= p.getNumCards()) return false;
        return p.cardAt(choice).canPutThisAbove(cardpile.peek());
	}

    /**
     * Cria uma pausa
     * pergunta ao usuario se deseja continuar
     * feito apenas para aumentar a interatividade com o usuário
     */
	private void pause() throws FileNotFoundException, IOException, ClassNotFoundException
    {
	    Scanner in = new Scanner(System.in);
	    String opcao;

		System.out.println("Digite s para salvar o jogo ou enter para continuar......");
		opcao = in.nextLine();

		if (opcao.equals("s"))
        {
            System.out.println("Digite um nome para esse save: ");
            opcao = in.nextLine();
            savegame(opcao + ".uno");
        }
	}

    /**
     * compra o número de cartas específico, do jogador
     * @param p o jogador
     * @param penalty número de cartas
     * @return true, caso for possível
     */
	private boolean compra (Player p, int penalty)
    {
        Card pick;
        for(int i=0;i<penalty;i++)
        {
            if(deck.isEmpty()) {
                if (cardpile.isEmpty()) return false;
                deck = cardpile;
                deck.shuffle();
                cardpile = new Deck();
            }
            pick = deck.getTopCard();
            p.add(pick);
        }

        return true;
    }

    /**
     * desenha linha de asteriscos.
     */
	private static void decorate() {
		System.out.println("*********************************************************" +
                "******");
	}

    /**
     * pega a primeira carta não especial
     * @return a carta
     */
	private Card getStartingCard() {
		Card temp = deck.peek();
		while(temp.isSpecial()) {
			deck.shuffle();
			temp = deck.peek();
		}
		
		temp = deck.getTopCard();
		return temp;
	}

    /**
     * verifica se algum jogador ganhou o jogo, imprimindo mensagem na tela
     * @return true se alguem tiver ganhado, false caso contrário
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean gameOver() throws IOException, InterruptedException
    {
	    for (int i = 0; i < numPlayers; i++)
        {
            if (player[i].hasWon())
            {
                cls();
                decorate();
                System.out.println(player[i].getName() + " venceu");
                decorate();

                return true;
            }
        }

        return false;
    }

    /**
     * exibe a tela do jogador da vez
     * @throws IOException
     * @throws InterruptedException
     */
    public void showBoard() throws IOException, InterruptedException
    {
        cls();
        decorate();
        System.out.println(player[currentPlayer].getName() + ", É seu turno\nA carta atual na mesa é:\n"+cardpile.peek());
        decorate();
        showCards();
	    decorate();
    }

    /**
     * exibe as cartas do jogador atual e esconde as cartas do jogador da vez passada
     * @throws IOException
     * @throws InterruptedException
     */
    private void showCards () throws IOException, InterruptedException
    {
        for (int i = 0; i < numPlayers; i++)
        {
            if (i != currentPlayer)
            {
                System.out.print(player[i].getName() + ": ");
                player[i].hideCards();
                System.out.println();
            }
        }

        System.out.println("Seu baralho:");
        player[currentPlayer].showCards();
    }

    /**
     * limpa a tela
     * funciona tanto para windows quanto para mac
     * @throws IOException
     * @throws InterruptedException
     */
    public final static void cls() throws IOException, InterruptedException
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            }
            else
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch (final Exception e){}
    }

    /**
     * espera o enter do usuario para continuar
     */
    private void enterParaContinuar()
    {
        Scanner in = new Scanner(System.in);

        System.out.println("Digite enter para continuar......");
        in.nextLine();
    }
}
