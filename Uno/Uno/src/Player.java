import java.util.Scanner;
import java.io.Serializable;

public class Player implements Serializable
{
	private Card[] playercards;
	private int numCards;
	private String name; //nome do jogador

    /**
     * cria um array list que armazena as cartas do jogador
     * atribui nome ao jogador.
     * Esse objeto é criado na classe UNO.
     */
	public Player(String nome) {
		playercards = new Card[108];
		int numCards = 0;
		this.name = nome;
	}

    /**
     * retorna o numero de cartas que o jogador possui na mão
     */
	public int getNumCards() {
		return numCards;
	}

    /**
     * adiciona uma carta na mão do jogador
     * @param c carta a ser adicionada
     * @return true se for possivel, false caso contrário
     */
	public boolean add (Card c)
    {
        if (numCards < 0 || c == null) return false;

        playercards[numCards] = c;
        numCards++;
        return true;
    }

    /**
     * remove uma carta da mão do jogador
     * @param i a posição da carta a ser removida
     * @return true se for possível, false caso contrário
     */
    public Card remove (int i)
    {
        if (numCards < 0 || i < 0 || i >= numCards) return null;

        Card c = playercards[i];
        playercards[i] = null;

        for (int k = i; k < numCards - 1; k++)
        {
            playercards[k] = playercards[k+1];
        }

        numCards--;
        playercards[numCards] = null;

        return c;
    }

    /**
     * jogador diz Uno quando tiver apenas uma carta na mão
     */
	public void sayUno() {

		Scanner s = new Scanner(System.in);
		
		if (numCards==1)
		{
			System.out.println("Uno");
			System.out.println("Pressione Enter...");
			s.nextLine();
		}
	}

	/**
	  * Isso é uma representação gráfica de uma carta
	  * apenas para fazer as cartas ficarem um pouco mais parecidas com cartas hahaha
	  * usado no método showboard() na classe Uno
	  **/
	public void showCards() {
        StringBuilder sb = new StringBuilder();

        for (int line = 0; line < (numCards - 1) / 8; line++) {
            for (int i = line * 8; i < (line + 1) * 8; i++) {
                sb.append(" -----  ");
            }
            sb.append("\n");

            for (int i = line * 8; i < (line + 1) * 8; i++) {
                sb.append("| ");
                sb.append(playercards[i].getColorString());
                sb.append(" | ");
            }
            sb.append("\n");

            for (int i = line * 8; i < (line + 1) * 8; i++) {
                sb.append("| ").append(playercards[i].getValueString()).append(" | ");
            }
            sb.append("\n");

            for (int i = line * 8; i < (line + 1) * 8; i++) {
                sb.append(" -----  ");
            }
            sb.append("\n");
        }

        for (int i = 8 * ((numCards - 1) / 8); i < numCards; i++) {
            sb.append(" -----  ");
        }
        sb.append("\n");

        for (int i = 8 * ((numCards - 1) / 8); i < numCards; i++) {
            sb.append("| ");
            sb.append(playercards[i].getColorString());
            sb.append(" | ");
        }
        sb.append("\n");

        for (int i = 8 * ((numCards - 1) / 8); i < numCards; i++)
        {
            sb.append("| ").append(playercards[i].getValueString()).append(" | ");
        }
		sb.append("\n");

		for (int i = 8 * ((numCards-1)/8); i < numCards; i++)
		{
            sb.append(" -----  ");
        }
		sb.append("\n");

		System.out.println(sb.toString());
	}

	/**
	  * esconde cartas do jogador
	  * usado no método showboard() na classe Uno
	  **/
	public void hideCards() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < numCards; i++)
		{
            sb.append("[] ");
        }

		System.out.println(sb.toString());
	}


    /**
     * verifica se o jogador ganhou o jogo
     * @return true caso tiver ganhado
     */
	public boolean hasWon() {
		return numCards==0;
	}

	/**
	 * Representação do jogador em texto
     * @return String com o nome
	 */
	public String getName() {
		return this.name;
	}

    /**
     * acessa a carta da posição
     * @param i a posição a ser acessada
     * @return a carta da posição, ou null se a posicao for inválida
     */
	public Card cardAt(int i)
    {
        if (i < 0 || i >= numCards) return null;

        return playercards[i];
    }

    /**
     * verifica se o jogador possue carta válida para jogar na mesa
     * @param current a carta do topo da mesa
     * @return true se ele pode jogar alguma carta, false se ele não pode
     */
    public boolean hasCardToPutAbove (Card current)
    {
        for (int i = 0; i < getNumCards(); i++)
        {
            if (playercards[i].canPutThisAbove(current)) return true;
        }
        return false;
    }
}
