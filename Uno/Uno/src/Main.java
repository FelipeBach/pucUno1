import java.io.IOException;
import java.io.Serializable;


public class Main implements Serializable
{
    /**
     * Main apenas roda o jogo chamando o método game.
     * Metodo game esta na classe Uno.
     */
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException
    {
        int escolha = Uno.menuInicial();
        Uno game;
        String filename;

        if (escolha == 0)
        {
            filename = Uno.askFilename();
            game = new Uno(filename);
        }
        else
            game = new Uno (escolha);

		game.game();
	}
}
