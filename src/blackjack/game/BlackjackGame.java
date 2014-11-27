
package blackjack.game;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Edark
 */
public class BlackjackGame {

    public static void main(String[] args) {
        // TODO code application logic here
        int saldo;
        int[] resultados;
        int[] saldos;
        int numero, i;
        Mano manoJugador[];
        boolean primera = true;
        boolean bancarrota[];
// Presentacion
        Locale loc = new Locale("es","ES");
        ResourceBundle resb1 = ResourceBundle.getBundle("bundle",loc);
        JOptionPane.showMessageDialog(null, resb1.getString("BIENVENIDO AL JUEGO DE BLACKJACK."));



       // JOptionPane.showMessageDialog(null, resb1.getString("                AUTOR:  EDARK"));


// Preguntamos por número de jugadores y por la cantidad con la que
//empiezan

        numero = Integer.parseInt(JOptionPane.showInputDialog(resb1.getString("NUMERO DE JUGADORES:")));

        saldo = Integer.parseInt(JOptionPane.showInputDialog(resb1.getString("CON QUE CANTIDAD EMPEZARA CADA JUGADOR (EUROS)?")));
// Inicializamos los arrays que vamos a utilizar
        saldos = new int[numero];
        manoJugador = new Mano[numero];
        resultados = new int[numero + 1];
        bancarrota = new boolean[numero];
        for (i = 0; i < numero; i++) {
            saldos[i] = saldo;
            bancarrota[i] = false;
        }
// Comienza el bucle...
        while (true) {
// Le preguntamos a cada jugador cuanto dinero desea apostar
            for (int j = 0; j < numero; j++) {
// Creamos la mano
                manoJugador[j] = new Mano();
                if (saldos[j] <= 0) {
                    bancarrota[j] = true;
                }

                JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(resb1.getString("JUGADOR NUMERO {0}{1}. TIENE {2}"), new Object[] {"",j+1,saldos[j]}));
                if (!bancarrota[j]) {
                    do {
                        manoJugador[j].apuesta = Integer.parseInt(JOptionPane.showInputDialog(resb1.getString("CUANTOS EUROS QUIERE APOSTAR? (0 PARA SALIR)")));

                        if (manoJugador[j].apuesta < 0 || manoJugador[j].apuesta
                                > saldos[j]) {
                            JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(resb1.getString("SU APUESTA DEBE ESTAR ENTRE 0 Y {0}"), new Object[] {saldos[j]})                                    + '.');
                        }
// En caso de que el jugador no desee jugar más
//utilizamos el mismo array
// de booleanos que usamos cuando un jugador se
//queda sin dinero, para
// que la siguiente ronda no se le pregunte
                        if (manoJugador[j].apuesta == 0) {

                            JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(resb1.getString("ADIOS JUGADOR {0}{1}"), new Object[] {"",j}));
                            bancarrota[j] = true;
                        }
                    } while (manoJugador[j].apuesta < 0
                            || manoJugador[j].apuesta > saldos[j]);
                }
            }
// En caso de que no se hayan arruinado todos los jugadores
//entramos al metodo jugar
            if (!queda_alguno(bancarrota, numero)) {
                resultados =
                        jugar(numero, manoJugador, bancarrota,resb1);
            } else {

                JOptionPane.showMessageDialog(null, resb1.getString("TODOS LOS JUGADORES SE HAN QUEDADO SIN DINERO O NO HAY MAS JUGADORES"));
                JOptionPane.showMessageDialog(null, resb1.getString("ADIOOOOS"));
                System.exit(-1);
            }
// Al salir del metodo jugar comprobamos las puntuaciones ya
//actualizamos los saldos
            for (int j = 0; j < numero; j++) {
                if ((resultados[0] == 2) || (resultados[0] == 1)) {
                    saldos[j] = saldos[j]
                            - manoJugador[j].apuesta;
                } else {
                    switch (resultados[j + 1]) {
                        case -1:
                            saldos[j] = saldos[j]
                                    - manoJugador[j].apuesta;
                            break;
                        case 1:
                            saldos[j] = saldos[j]
                                    + manoJugador[j].apuesta;
                            break;
                        case 2:
                            saldos[j] = saldos[j]
                                    + 2 * manoJugador[j].apuesta;
                            break;
                        default:
                            saldos[j] = saldos[j] - manoJugador[j].apuesta;
                    }
                }
            }
        }
    }
// Metodo jugar que lleva el desarrollo principal de la partida
    static int[] jugar(int jugadores, Mano[] manoJugador, boolean[] bancarrota,ResourceBundle resb1) {
        Baraja baraja;
        Mano manoBanca;
        boolean fin = false;
        int i, j;
// En resultados almacenamos lo que ha hecho cada jugador.
// 0 en un principio, -1 si se pasa y 1 si gana.
// La posicion 0 sera la de la banca y la posicion i la del jugador i
// Pondremos un 2 en caso de blackjack.
        int resultados[];
        resultados = new int[jugadores + 1];
        for (int m = 0; m <= jugadores; m++) {
            resultados[m] = 0;
        }
        baraja = new Baraja();
// Barajamos y repartimos
        baraja.barajar();
// La banca roba sus cartas
        manoBanca = new Mano();
        manoBanca.cogerCarta(baraja.robar());
        manoBanca.cogerCarta(baraja.robar());

        for (i = 0; i < jugadores; i++) {
// Los jugadores van robando
            manoJugador[i].cogerCarta(baraja.robar());
            manoJugador[i].cogerCarta(baraja.robar());
// En caso de que el usuario consiga Blackjack (21 a la primera)
//gana el doble de la apuesta
// y se acaba la ronda
            if ((manoJugador[i].getBlackjackValor() == 21) && (!bancarrota[i])) {
                JOptionPane.showMessageDialog(null,resb1.getString("LA BANCA TIENE ")
                        + manoBanca.obtenerCarta(0)
                        + java.text.MessageFormat.format(resb1.getString(" Y {0}."), new Object[] {manoBanca.obtenerCarta(1)}));
                JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("EL JUGADOR {0}{1} TIENE "), new Object[] {i, 1})
                        + manoJugador[i].obtenerCarta(0)
                        + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString(" Y {0}."), new Object[] {manoJugador[i].obtenerCarta(1)}));
                JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("EL JUGADOR {0}{1} TIENE BLACKJACK Y GANA"), new Object[] {i, 1}));
                resultados[i + 1] = 2;
                fin = true;
            }
        }
// Si la banca tiene BJ gana y se acaba la partida
        if (manoBanca.getBlackjackValor() == 21) {
            JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("LA BANCA TIENE {0}"), new Object[] {manoBanca.obtenerCarta(0)})                    + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString(" Y {0}."), new Object[] {manoBanca.obtenerCarta(1)}));
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("LA BANCA TIENE BLACKJACK Y GANA."));
            resultados[0] = 2;
            fin = true;
        }
        if (fin) {
            return resultados;
        }
// Si ninguno de los 2 tiene BJ seguimos con el juego
// Comienza la iteracion para cada uno de los jugadores
        for (j = 0; j < jugadores; j++) {
// Mostramos las cartas de los jugadores y una de las cartas de la
//banca
            JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(resb1.getString("--------- JUGADOR {0}{1} ---------"), new Object[] {"",j+1}));

            fin = false;
            while (!bancarrota[j]) {

                JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(resb1.getString("JUGADOR {0}{1}.SUS CARTAS SON:"), new Object[] {"",j+1}));
                for (i = 0; i < manoJugador[j].contar(); i++) {
                    JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(resb1.getString(" {0}"), new Object[] {manoJugador[j].obtenerCarta(i)}));
                }
                JOptionPane.showMessageDialog(null, resb1.getString("Y SUMAN UN TOTAL DE ")+ manoJugador[j].getBlackjackValor() + resb1.getString(" PUNTOS."));
                JOptionPane.showMessageDialog(null, resb1.getString("LA BANCA MUESTRA ")+ manoBanca.obtenerCarta(0));

// Carta o se planta?

                int accion;
                do {
                    accion = Integer.parseInt(JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("CARTA (1) O SE PLANTA (2)? ")));
                    if (accion != 1 && accion != 2) {
                        accion = Integer.parseInt(JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("POR FAVOR RESPONDA C O P: ")));
                    }
                } while (accion != 1 && accion != 2);
// Si se planta salimos del bucle
                if (accion == 2) {

                    JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("EL JUGADOR {0}{1} SE PLANTA."), new Object[] {j, 1}));

                    break;
                } // Si no se planta seguimos con una nueva carta
                else {
                    Carta newCarta = baraja.robar();
                    manoJugador[j].cogerCarta(newCarta);

                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("USTED ROBA CARTA."));
                    JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("SU CARTA ES {0}"), new Object[] {newCarta}));
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("Y USTED TIENE ")
                            + manoJugador[j].getBlackjackValor() + java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString(" PUNTOS"));
// Si se pasa de 21 puntos pierde y pone su resultado a -1
                    if (manoJugador[j].getBlackjackValor() > 21) {
                        JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("EL JUGADOR {0}{1} SE HA PASADO DE 21. HA PERDIDO"), new Object[] {j, 1}));

                        resultados[j + 1] = -1;
                        fin = true;
                    }
                }
                if (fin) {
                    break;
                }
            }
        }
// Ahora le toca jugar a la banca
        JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("LAS CARTAS DE LA BANCA SON "));
        JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString(" {0}"), new Object[] {manoBanca.obtenerCarta(0)}));
        JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString(" {0}"), new Object[] {manoBanca.obtenerCarta(1)}));
        while (true) {
            float beneficio = 0;
            float beneficio_hip = 0;
            float beneficio_aux = 0;
            float probabilidad = 0;
// Primero comprobamos el beneficio que obtendriamos en la
//situacion actual
            for (i = 0; i < jugadores; i++) {
                if (manoBanca.getBlackjackValor() >= manoJugador[i].getBlackjackValor()) {
                    beneficio = beneficio + manoJugador[i].apuesta;
                } else if (resultados[i + 1] != -1) {
                    beneficio = beneficio + manoJugador[i].apuesta;
                }
            }

// Sabiendo las cartas que hay en la mesa calcularemos la
//esperanza de sacar cada una de las
// posibles cartas y en funcion de eso el beneficio hipotetico que
//obtendriamos.
            for (j = 1; j < 14; j++) {
                Carta aux = new Carta(j, 0);
                manoBanca.cogerCarta(aux);
                probabilidad = baraja.vistas[j - 1] / baraja.restantes();
                for (i = 0; i < jugadores; i++) {
                    if (manoBanca.getBlackjackValor() >= manoJugador[i].getBlackjackValor()) {
                        beneficio_aux = beneficio_aux + manoJugador[i].apuesta;
                    } else if (resultados[i + 1] != -1) {
                        beneficio_aux = beneficio_aux - manoJugador[i].apuesta;
                    }
                }
                beneficio_hip = beneficio_hip + beneficio_aux * probabilidad;
                beneficio_aux = 0;
                manoBanca.dejarCarta(manoBanca.contar() - 1);
            }
// Si el beneficio hipotetico es mayor que el actual robamos carta
            if (beneficio_hip > beneficio) {
                Carta newCarta = baraja.robar();
                JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("LA BANCA ROBA {0}"), new Object[] {newCarta}));
                manoBanca.cogerCarta(newCarta);
                if (manoBanca.getBlackjackValor() > 21) {
                    resultados[0] = -1;
                    break;
                }
            } else {
                break;
            }
        }
// Repetimos esto hasta que nos pasemos o decidamos no coger nueva
//carta
        JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("EL TOTAL DE LA BANCA ES DE ")
                + manoBanca.getBlackjackValor() + java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString(" PUNTOS"));
// Vamos a comparar los puntos de cada uno para ver quien ha ganado
//a quien y lo reflejamos
// en el array de resultados
        for (i = 0; i < jugadores; i++) {
            if ((resultados[i + 1] == -1) || (bancarrota[i])) {
                continue;
            }
            if (resultados[0] == -1) {
                resultados[i + 1] = 1;
                continue;
            }
            if (manoJugador[i].getBlackjackValor() > manoBanca.getBlackjackValor()) {
                resultados[i + 1] = 1;
            } else {
                resultados[i + 1] = -1;
            }
        }

// Imprimimos por pantalla quien ha ganado a quien
        for (i = 1; i <= jugadores; i++) {
            if (!bancarrota[i - 1]) {
                if (resultados[i] > resultados[0]) {
                    JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("JUGADOR {0} GANA A LA BANCA."), new Object[] {i}));
                } else {
                    JOptionPane.showMessageDialog(null, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("blackjack/game/bundle_es_ES").getString("LA BANCA GANA AL JUGADOR {0}."), new Object[] {i}));
                }
            }
        }
        return resultados;
    }
// Este metodo comprueba si todos los jugadores se han quedado sin
//dinero
    static boolean queda_alguno(boolean bancarrota[], int numero) {
        boolean bancarrota_total = true;
        for (int p = 0; (p < numero) && bancarrota_total; p++) {
            if (!bancarrota[p]) {
                bancarrota_total = false;
                break;
            }
        }
        return bancarrota_total;


    }
}
