import java.io.Serializable;

/**
 * Essa classe implementa uma carta de UNO.
 */

public class Card implements Serializable
{
    private int cor;
	private int valor;

	/*
	    Cores:
	        0 - No color (specials)
	        1 - Red
	        2 - Green
	        3 - Blue
	        4 - Yellow

	    Valor especial:
	        10 - Pula
	        11 - Retorno
	        12 - Compra 2
	        13 - Coringa (sem cor a priori)
	        14 - Compra 4 (sem cor a priori)

	 */

    /**
     * Construtor padrão de carta.
     * @param valor valor (inteiro) da carta
     * @param cor   a cor da carta (inteiro).
     */
	public Card(int valor, int cor)
    {
		setCor(cor);
		setValor(valor);
	}

    /**
     * Muda cor da carta
     * @param cor
     */
    public void setCor(int cor)
    {
        if(cor >= 0 && cor <= 4)
            this.cor = cor;
    }

    /**
     * Muda o valor da carta
     * @param valor
     */
    private void setValor(int valor)
    {
        if(valor >= 0 && valor <=14) this.valor = valor;
    }

    /** Retorna a cor da carta*/
    public int getColor(){
	    return this.cor;
	}

    /**
     * Devolve cor do objeto
     * @return Cor do objeto
     */
	public String getColorString()
    {
        switch(this.cor)
        {
            case 1: return "red";
            case 2: return "gre";
            case 3: return "blu";
            case 4: return "yel";
            default: return "   ";
        }
    }

    /**
     * Retorna o valor da carta
     * @return Valor da carta
     */
	public int getValue()
    {
        return this.valor;
	}

    /**
     * Retorna o valor da carta(inclusive se for uma ação)
     * @return
     */
	public String getValueString()
    {
        switch(valor)
        {
            case 10: return "pul";
            case 11: return "inv";
            case 12: return "+2 ";
            case 13: return "cur";
            case 14: return "+4 ";
            default: return " " + valor + " ";
        }
    }

    /**
     * Checa se a carta é especial.
     * @return True se for, False se não for
     * */
    public boolean isSpecial() { //Como se fosse getSpecial()
        return valor >= 10;
    }

    /**
     * Checa se a carta é do tipo que pula a vez
     * @return False se não for, True se for
     */
    public boolean isPula()
    {
        return valor == 10;
    }

    /**
     *  Checa se a carta é do tipo que troca o sentido
     * @return True se for, False se não for
     */
    public boolean isTrocaSentido()
    {
        return valor == 11;
    }

    /**
     * Checa se a carta é do tipo coringa
     * @return True se for, False se não for
     */
    public boolean isCoringa()
    {
        return valor >= 13;
    }

    /**
     * Escreve na tela a cor e o valor da carta, dentro de um quadrado
     * @return quadrado com valor e cor da carta.
     */
    public String toString()
    {
        return " ----- \n| " + this.getColorString() + " |\n| " +
                this.getValueString() + " | \n ----- \n";
	}

    /**
     * Checa se a carta do jogador pode ser combinada com a carta atual
     * @param below carta do jogador
     * @return True se puder, False caso contrário.
     */
	public boolean canPutThisAbove (Card below)
    {
        if (below == null) return false;
        if (this.isCoringa()) return true;
        return below.getColor() == this.getColor() || this.getValue() == below.getValue();
    }

    /**
     * Checa se a carta é +2 ou +4
     * @return O número de cartas que o jogador deve comprar
     */
    public int getPenalty ()
    {
        if (valor == 12 || valor == 14) return valor - 10;
        return 0;
    }
}
