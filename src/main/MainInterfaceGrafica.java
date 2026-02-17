package main;

import Entities.CasaBotao;
import Utils.LogicTabuleiro;
import Utils.PaintTabuleiro;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Douglas
 */
public final class MainInterfaceGrafica extends JFrame {

    private final int TAMANHO = 6;
    private final Tabuleiro tabuleiroLogico;
    private final CasaBotao[][]  tabuleiroInterface = new CasaBotao[TAMANHO][TAMANHO];
    private final LogicTabuleiro logic;        // Objeto que verifica toda a logica de movimetos do jogo
    private static PaintTabuleiro paint;        // Objeto que pinta as jogadas de um jogador tem com a sua peca

    private int linhaOrigem = -1, colOrigem = -1;

    public MainInterfaceGrafica() {
        
        /*
            TABULEIRO DO JOGO
        */
        tabuleiroLogico = new Tabuleiro();
        logic = new LogicTabuleiro(tabuleiroLogico);
        paint = new PaintTabuleiro(tabuleiroLogico, tabuleiroInterface);

        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(800, 800);
        setLayout(new GridLayout(TAMANHO, TAMANHO));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
        sincronizarInterface(); 

        setVisible(true);
    }

    private void inicializarComponentes() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiroInterface[i][j] = new CasaBotao();

                paint.colorirCasa(i, j);

                final int linha = i;
                final int coluna = j;
                tabuleiroInterface[i][j].addActionListener(e -> tratarClique(linha, coluna));
                add(tabuleiroInterface[i][j]);
            }
        }
    }

    private void tratarClique(int linha, int col) {
        // Caso 1: Nenhuma peça selecionada ainda
        if (linhaOrigem == -1) {
            
            // Verifica se a casa clicada contém QUALQUER peça (1, 2, 3 ou 4)
            if (tabuleiroLogico.getMatriz()[linha][col] != 0) {
                linhaOrigem = linha;
                colOrigem = col;
                tabuleiroInterface[linha][col].setBackground(Color.YELLOW); // Destaque do clique
                paint.mostrarPossiveisJogadas(linha, col, tabuleiroLogico.getMatriz()[linha][col]);
            }
        }
        // Caso 2: Já existe uma peça selecionada, tentando mover
        else {

            // Se clicar na mesma peça, cancela a seleção
            if (linhaOrigem == linha && colOrigem == col) {
                paint.cancelarPossiveisJogadas(tabuleiroLogico.getMatriz()[linhaOrigem][colOrigem], linhaOrigem, colOrigem);
                cancelarSelecao();
                return;
            }


            boolean sucesso = logic.moverPecaLogica(linhaOrigem, colOrigem, linha, col);


            if (sucesso) {
                paint.cancelarPossiveisJogadas(tabuleiroLogico.getMatriz()[linha][col], linhaOrigem, colOrigem);
                cancelarSelecao();
                sincronizarInterface();

                /*
                    VERIFICAÇÃO DE QUEM É A VEZ DE JOGAR E IMPLEMENTAÇÃO DA JOGADA DA IA
                */
            } else {
                // Se o movimento for inválido (ex: clicar em cima de outra peça)
                paint.cancelarPossiveisJogadas(tabuleiroLogico.getMatriz()[linhaOrigem][colOrigem], linhaOrigem, colOrigem);
                cancelarSelecao();
            }
        }
    }

    // Quebrar essa funcao em logic e em paint
    private void cancelarSelecao() {
        if (linhaOrigem != -1) {
            // Restaura a cor original
            if ((linhaOrigem + colOrigem) % 2 != 0)
                tabuleiroInterface[linhaOrigem][colOrigem].setBackground(new Color(119, 149, 86));
            else
                tabuleiroInterface[linhaOrigem][colOrigem].setBackground(new Color(235, 235, 208));
        }
        linhaOrigem = -1;
        colOrigem = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainInterfaceGrafica::new);
    }
    
    /*
     * Atualiza a interface gráfica com base na matriz lógica do Tabuleiro. Este
     * método será chamado após cada jogada da IA.
     */
    public void sincronizarInterface() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                int peca = tabuleiroLogico.getMatriz()[i][j];
                tabuleiroInterface[i][j].setTipoPeca(peca);
            }
        }
    }
}
